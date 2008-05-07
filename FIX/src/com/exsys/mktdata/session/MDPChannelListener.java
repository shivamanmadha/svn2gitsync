package com.exsys.mktdata.session;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import com.exsys.mktdata.cfg.Configuration;
import com.exsys.mktdata.cfg.ConfigurationException;
import com.exsys.mktdata.message.*;
import com.exsys.common.exceptions.*;


/**
 * For each listener configured, an instance of this class will be created. This class listens to the
 * multicast, keeps track of sequence numbers and reports missed sequence numbers. Also it calculates the
 * latency between the time CME published the message and the time client receives the message. Incase if
 * the calculated latency is greater than the configured threshold, then a message will be logged to the log.
 *
 * This class has two threads, producer and consumer. The producer thread will read the messages from the socket
 * and add it to a buffer, which will be then picked up by the consumer to process and check the sequence number
 * and latencies. This class is designed so that listener to the feeds A and B will have reference to each other,
 * so that incase if one of the feed say A, misses a message, it can always check with the listener for feed B,
 * which again is an instance of this class and does the same function of checking sequenc numbers and latencies.
 *
 * Important Note: Since we want this tool to be able to listen and report latent messages and missed
 * sequence numbers from both feeds, we are here completely processing messages from both feeds to get
 * the sequence number and latency information. But clients, in their applications to arbitrate between
 * the two feeds, should choose to implement a slightly different arbitration logic where they would process
 * packets from secondary feed only when they miss some packets on the primary and until then buffer packets
 * from secondary feed temporarily in a bounded queue. Care should be taken that this temporary buffer doesn't
 * store stale packets for a long time to prevent memory leak.
 *
 */
public class MDPChannelListener implements Runnable {

	private String channelName; //Name of the channel this listener will listen on
	private int mcastPort; //The port on the host, on which this application will listen
	private String mcastGrp; //The IP address of the Multicast group this listener will listen on
	private String mcastInterface; //Name of the Interface this listener would use to listen
	private int socketSOTimeout = 100; //Socket timeout parameter for the Multicast socket
	private MulticastSocket msocket; //Instance of a Mulitcast socket to be used to read multicast packets
	private InetAddress group; //
	private Logger logger; //An instance of JDK logger for logging
	private long latencyThreshold = 0; //The threshold to define a packet as being latent

	private String publishPrefix = null;

	private String msglogfile; //File to be used to log messages
	private PrintWriter pWriter; //Writer instance to log messages to log file
	private MDPChannelListener[] AandBChannels; //Listener of this channel for the A and B feed.Listening to 2
											   //feeds can help arbitrate, when one of the feed misses a packet.
	// lifecycle booleans
	private boolean configured = false;  //Indicates whether if this instance has been configured
	private boolean initialized = false; //Indicates whether if this instance has been initialized
	private boolean started = false; //Indicates whether if this instance has been started
	private boolean destroyed = false; //Indicates whether if this instance has been destroyed

	private int receiveBufferSize = 1024 * 1024 * 4; // Buffer size for the Multicast socket. Default to 4mb
	private BoundedQueue<Integer> observedSeqNbrs; //Used to keep track of received sequence numbers.
	private int sequenceQueueSize = 200; //Size of observedSeqNbrs. Limits the number of sequence numbers cached.

	private static int MAX_COMPRESSED_BUFFER_SIZE; //Maximum size expeceted for a compressed message
	private static int MAX_UNCOMPRESSED_BUFFER_SIZE; //Maximum size expected for a uncompressed message

	//This class has two threads, one that reads socket and adds the packets received to a queue and another one
	//that will process the messages from this queue.This queue will hold the packets, received from socket.
	private ConcurrentLinkedQueue<byte[]> msgQueue = new ConcurrentLinkedQueue<byte[]>();

	final Lock lock = new ReentrantLock(); //Lock to synchronize between two threads
	final Condition notEmpty = lock.newCondition(); //Condition to be used in conjunction with above lock


	/**
	 * This function will set the A and B feed listeners for this channel.
	 * @param aandBChannels - An array, consisting of MDPChannelListener instances for both feeds
	 */
	public void setAandBChannels(MDPChannelListener[] aandBChannels) {
		AandBChannels = aandBChannels;
	}

	/**
	 * This function will accept an instance of Configuration, which will have all the configuration
	 * parameters for this channel, and will configure the different fields with the configured value
	 * @param cfg - Configuration instance
	 * @throws ConfigurationException
	 */
	public void configure(Configuration cfg) throws ConfigurationException {
		channelName = cfg.getAttribute("channel-name"); //Set the channel name
		logger = Logger.getLogger(channelName);
		Handler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleLogFormatter());
		logger.addHandler(handler);
		logger.setUseParentHandlers(false);

		mcastPort = cfg.getChild("mcast-port").getValueAsInteger(); //Set the multicast port
		mcastGrp = cfg.getChild("mcast-group").getValue(); //Set the multicast group
		mcastInterface = cfg.getChild("mcast-interface").getValue(); //Set the multicast interface
		latencyThreshold = cfg.getChild("latency-threshold").getValueAsLong(500L); //Try to get the latency
						//threshold from config file and if nothing found then set a default value of 500ms
		//Try to get socket time out from config file and if nothing found then set a default value of 100ms
		if (cfg.hasChild("socket-so-timeout"))
			socketSOTimeout = cfg.getChild("socket-so-timeout").getValueAsInteger(100);
		//Try to get sequence queue size from config file and if nothing found then set a default value of 200
		if (cfg.hasChild("sequence-queue-size")) {
			sequenceQueueSize = cfg.getChild("sequence-queue-size").getValueAsInteger(200);
		}
		//Try to get receive buffer size from config file and if nothing found then set a default value of 4MB
		if (cfg.hasChild("receive-buffer-size")) {
			receiveBufferSize = cfg.getChild("receive-buffer-size").getValueAsInteger(1024 * 1024 * 4);
		}
	    publishPrefix = cfg.getChild("mcast-publish-prefix").getValue();
		MAX_COMPRESSED_BUFFER_SIZE = cfg.getAttributeAsInteger("max-compressed-buf-size", 1024);
		MAX_UNCOMPRESSED_BUFFER_SIZE = cfg.getAttributeAsInteger("max-uncompressed-buf-size", 2048);
		msglogfile = cfg.getChild("msg-logfile").getValue(); //Set the message log file
		logger.info("MDPChannelListener <" + channelName
				+ "> successfully configured with the following parameters Port: " + mcastPort + " Group: " + mcastGrp
				+ " Interface: " + mcastInterface);
		configured = true;

	}

	/**
	 * This function will initialize the multicast socket and will also join to the group.
	 * Also it initializes the writer to the Message log file.
	 * @throws IOException
	 */
	public void initialize() throws IOException {
		if (!configured) {
			throw new RuntimeException("initialize() called on a non-configured MDPChannelListener.");
		}
		msocket = new MulticastSocket(mcastPort);
		group = InetAddress.getByName(mcastGrp);
		msocket.setNetworkInterface(NetworkInterface.getByName(mcastInterface));
		msocket.joinGroup(group);
		msocket.setSoTimeout(socketSOTimeout);
		msocket.setReceiveBufferSize(receiveBufferSize);
		logger.info("<" + channelName + "> multicast socket with Port: " + mcastPort + " Group: " + mcastGrp
				+ " Interface: " + mcastInterface + " joined multicast group successfully");
		// keeping a bounded queue of 200 of the latest observed sequence numbers
		observedSeqNbrs = new BoundedQueue<Integer>(sequenceQueueSize);
		pWriter = new PrintWriter(new BufferedWriter(new FileWriter(msglogfile, true)));
		initialized = true;
	}

	public void start() {
		started = true;
	}

	public void stop() {
		started = false;
	}

	/**
	 * This function will stop the listener and will do all the clean up operations
	*/
	public void destroy() {
		destroyed = true;
		if (!initialized) {
			logger.info("Destroy called on an uninitialized MDPChannelListener.  Ingoring invocation.");
			return;
		}
		try {
			msocket.leaveGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("There was an error leaving group " + mcastGrp);
		}
		msocket.disconnect();
		msocket.close();
		pWriter.flush();
		pWriter.close();
	}

	/**
	 * This function will return the channel name
	 * @return - channel's name
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * This function will return true if the sequence number passed in is found in this listener's observedSeqNbr
	 * queue.
	 * Warning: This thread will only search x number of the most recently observed sequence numbers
	 */
	public boolean receivedSequenceNumber(int expectedSeqNbr) {
		return observedSeqNbrs.contains(expectedSeqNbr);
	}

	/**
	 * This function will log a messgae to the message-log file
	 * @param msg
	 * @param args
	 */
	private void writeMsg(String msg, Object... args) {
		Calendar cal = Calendar.getInstance();
		pWriter.printf("%tY/%tm/%td %tH:%tM:%tS.%tL [%s] ", cal, cal, cal, cal, cal, cal, cal, channelName);
		pWriter.printf(msg + "%n", args);
		pWriter.flush();
	}


	/**
	 * This function handles cases when a sequence number higher than expected is received
	 * @param seqNbr
	 * @param expectedSeqNbr
	 */
	private void handleSeqNbrTooHigh(int seqNbr, int expectedSeqNbr) {
		boolean found = false;
		String receivingChannel = null;
		//Try to see if the missed messages are received by the listener on the other feed
		for(int currentSeqNum = expectedSeqNbr;currentSeqNum < seqNbr;currentSeqNum++){
			for (MDPChannelListener listener : AandBChannels) {
				if (listener == this)
					continue;
				else if (listener.receivedSequenceNumber(currentSeqNum)) {
					found = true;
					receivingChannel = listener.getChannelName();
					break;
				}
			}
			//Log a message, accordingly if the missed message was received on the other channel or not
			if (found) {
				writeMsg(
						"A message with seq no. %d was missed on channel %s but was received on channel %s.",
						currentSeqNum, channelName, receivingChannel);
			} else {
				writeMsg("Missed message with seq no. %d on channel %s", currentSeqNum, channelName);
			}
		}
	}

	/**
	 * This function handles reception of a low sequence number than expected by logging a corresponding message
	 * @param seqNbr
	 * @param expectedSeqNbr
	 */
	private void handleSeqNbrTooLow(int seqNbr, int expectedSeqNbr) {
		writeMsg("Received messages out of sequence on "+channelName+".  expected seq nbr: %d received seq nbr %d", expectedSeqNbr, seqNbr);
	}

	/*
	 * TODO: refactor this into transports and channels with a message callback
	 * on a channel
	 *
	 */

	/*
	* This function receives packets from socket, adds a
	*/
	public void run() {
		// assert
		if (destroyed || !configured || !initialized || !started) {
			throw new RuntimeException(
					"run() called on MDPChannelListener thread that has been destroyed or has not been configured, initialized or started.");
		}
		logger.info("MDPChannelListener <" + channelName + "> thread started listening for datagram packets");
		byte[] buffer = new byte[receiveBufferSize];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		ByteBuffer longBuf = ByteBuffer.allocate(8);
		byte[] longBufArray;
		while (started) {
			try {
				try {
					msocket.receive(packet);
				} catch (SocketTimeoutException se) {
					continue;
				}
				// create a receipt timestamp and add it to the byte[]
				longBuf.putLong(System.currentTimeMillis());
				longBufArray = longBuf.array();
				//copy the timestamp and bytes read from socket to a new byte array and add it to msgQueue to be
				//processed latter.
				int length = packet.getLength();
				byte[] copy = new byte[length + longBufArray.length];
				System.arraycopy(longBufArray, 0, copy, 0, longBufArray.length);
				System.arraycopy(buffer, 0, copy, longBufArray.length, length);

				lock.lock(); //Obtain the lock
				try{
					msgQueue.add(copy); //Add the received message to the message queue
					this.notEmpty.signal(); //Signal the reception to threads waiting on same condition
				} finally {
					lock.unlock(); //Unlock the lock under any instance
				}

				longBuf.clear();
			} catch (IOException e) {
				e.printStackTrace();
				logger.warning("An error occurred reading from the multicast socket: " + e.getMessage()
						+ " MDPChannelListener thread is exiting...");
				return;
			}
		}
		logger.info("MDPChannelListener <" + channelName + "> thread terminating . . . ");
	}

	/**
	 * fixed-capacity (bounded) queue A BoundedQueue will hold the most recent
	 * <code>size</code> elements added.
	 */
	public class BoundedQueue<E> {
		private ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();//Structure to store data
		private int capacity; //size of this bounded queue

		public BoundedQueue(int capacity) {
			this.capacity = capacity;
		}

		public boolean add(E elem) {
			if (queue.size() >= capacity) {
				queue.poll(); // drop the oldest element
			}
			return queue.add(elem);
		}

		public boolean contains(E elem) {
			return queue.contains(elem);
		}

	}

	/**
	 * This class will process the messages from the msgQueue. It does the sequence number check and latency
	 * check on all received messages.
	 */
	public class MDPMsgProcessor implements Runnable {
		private long startOfTradingMS = 0; //Indicates the milli seconds elapsed since 1970 Jan 1 12 AM and 12 AM
										   //of the recent past Sunday
		private Inflater decompressor = new Inflater(); //To decompress the message, if needed

		private RLCMarketDataSessionManager sessionMgr = null;
		public MDPMsgProcessor() {
			// initialize a millis counter with start of trading offset.
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			startOfTradingMS = calendar.getTimeInMillis();

			sessionMgr = new RLCMarketDataSessionManager();
			sessionMgr.startMarketDataSession();

		}

	public void processRLCMessage(String msg)
	{

		logger.info("FixSession - dequeuedMessage - Message Received is "+ msg );
		RLCMessage rlcMsg = null;
		try
		{
			rlcMsg = RLCMessageFactory.createRLCMessage(
					msg.getBytes());
			handleRLCMessage(rlcMsg);
		}
		catch(RLCProtocolError e)
		{
			String fmsg = "MDPMsgProcessor - processRLCMessage - RLC Protocol Error - " +e.getMessage();
			System.out.println("RLC Msg Error - " + fmsg);
			//Logger.error("Fix Msg Received - " + message);
			//Logger.error(fmsg);
			//Logger.error(e.getExternalMessage());
			//error(fmsg + "\n" + e.getExternalMessage()+"\n"+message);
			return;
		}


	}

	public void handleRLCMessage(RLCMessage rlcMsg)
	{
		if(sessionMgr != null)
		{
			String msgType = rlcMsg.getMessageType();
			String topic = publishPrefix + msgType;
			sessionMgr.sendMarketDataMessage(rlcMsg,topic);
		}
		else
		{
			System.out.println("ERROR. NO HANDLER FOR MKT DATA");
		}


	}
		public void run() {
			ByteBuffer byteBuffer = ByteBuffer.allocate(receiveBufferSize);
			byte[] compressedData = new byte[MAX_COMPRESSED_BUFFER_SIZE];
			byte[] uncompressedData = new byte[MAX_UNCOMPRESSED_BUFFER_SIZE];
			int batchSize = 0; //keeps track of the number of messages batched in one packet
			int totalLength = 0; //Length of the packet from msgQueue
			int lastSeqNbr = -1; //Last seq number received
			int seqNbr = 0; // current sequence number
			int expectedSeqNbr = 0; //next expeceted seq number
			int timestamp = 0; //time stamp on the message
			short msgLength = 0; //lenght of message
			long latency = 0L; //calculated latency for current message
			long receivedTime = 0L; //socket time stamp
			int uncompressedLen = 0;
			String data = null;
			byte[] buffer = null;
			logger.info("MDPMsgProcessor <" + channelName + "> thread started");
			while (started) {
				lock.lock();
				try{
					//Using a 'while loop' instead of 'if' to make sure that the signal received on the condition
					//is because a new message is added to the queue
					while(msgQueue.size() <= 0) { //While there are no messages in the message queue
						try {
							logger.fine("Waiting for a message");
							MDPChannelListener.this.notEmpty.await(); //Wait on the condition until a message is received
						} catch (InterruptedException e) {
							MDPChannelListener.this.writeMsg("Thread interrupted while waiting on buffer for messages");
						}
					}
					buffer = msgQueue.poll(); //Get the currently available message from head
		          }  finally {
		              lock.unlock(); //Unlock the lock, under any circumstances
		          }
				//Copy the read bytes to a local buffer
				totalLength = buffer.length;
				byteBuffer.put(buffer);
				byteBuffer.rewind();
				batchSize = 0;
				// the first long in the byte array is a receiving timestamp
				receivedTime = byteBuffer.getLong();
				while (byteBuffer.position() < totalLength) {
					batchSize++;
					seqNbr = byteBuffer.getInt(); //parse the seq number of current message

					observedSeqNbrs.add(seqNbr); //add it to the observed seq nbrs
					timestamp = byteBuffer.getInt(); //get the time stamp on the message
					msgLength = byteBuffer.getShort(); //get the message length

					//check if the sequence number of the current message is expected, if not handle it
					if (lastSeqNbr == -1) {
						lastSeqNbr = seqNbr;
						expectedSeqNbr = seqNbr;
					}
					if (seqNbr > expectedSeqNbr) {
						handleSeqNbrTooHigh(seqNbr, expectedSeqNbr);
					} else if (seqNbr < expectedSeqNbr) {
						handleSeqNbrTooLow(seqNbr, expectedSeqNbr);
					}
					byteBuffer.get(compressedData, 0, msgLength); //get the compressed data
					//calculate the latency, accounting for the time zone difference
					latency = (receivedTime - startOfTradingMS) - timestamp - MDPTimeTool.diffInTime;
					/*
					//if latency is greater than threshold, then log a message to the msg-log file
					if (latency >= latencyThreshold) {
						//if it is configured to log the original msg, decompress the original message and log it
						if(MDPTimeTool.logLatentMessage){
							try {
								decompressor.reset();
								decompressor.setInput(compressedData);
								Arrays.fill(uncompressedData, (byte) 0);
								uncompressedLen = decompressor.inflate(uncompressedData);
								data = new String(uncompressedData, 0, uncompressedLen);
							} catch (DataFormatException ex) {
								data = new String(compressedData, 0, msgLength);
							}
							writeMsg("sequence number: %d, latency: %d, message timestamp: %d (ms since Sunday 12AM), system timestamp: %d (ms from 1/1/1970 12AM UTC), data: [%s]", seqNbr, latency, timestamp, receivedTime, data);
						} else {
							writeMsg("sequence number: %d, latency: %d, message timestamp: %d (ms since Sunday 12AM), system timestamp: %d (ms from 1/1/1970 12AM UTC)", seqNbr, latency, timestamp, receivedTime);
						}

					} else if (latency < 0 && (Math.abs(latency)> MDPTimeTool.negativeLatencyThreshold)) {
						//If it is a -ve latency log a message indicating that the system time may be offset
						writeMsg("Time difference calculation on this machine resulted in a negative number.  The system clock on this machine maybe incorrect.  Please check your system's clock. (message timestamp: %d ms since Sunday 12AM | system timestamp: %d ms from 1/1/1970 12AM UTC | Latency = %d)", timestamp, receivedTime,latency);
					} else {
						if(MDPTimeTool.logReceivedMessages){
							try {
								decompressor.reset();
								decompressor.setInput(compressedData);
								Arrays.fill(uncompressedData, (byte) 0);
								uncompressedLen = decompressor.inflate(uncompressedData);
								data = new String(uncompressedData, 0, uncompressedLen);
								processRLCMessage(data);
							} catch (DataFormatException ex) {
								data = new String(compressedData, 0, msgLength);
							}
							logger.info("Received message with sequence number: "+seqNbr+
																" on channel "+channelName+
																". Data: "+data);
						}
					}
					*/

					// UNCOMMENT THE ABOVE AND COMMENT BELOW, if latency checks are needed
							try {
								decompressor.reset();
								decompressor.setInput(compressedData);
								Arrays.fill(uncompressedData, (byte) 0);
								uncompressedLen = decompressor.inflate(uncompressedData);
								data = new String(uncompressedData, 0, uncompressedLen);
								processRLCMessage(data);
							} catch (DataFormatException ex) {
								data = new String(compressedData, 0, msgLength);
							}
							writeMsg("sequence number: %d, latency: %d, message timestamp: %d (ms since Sunday 12AM), system timestamp: %d (ms from 1/1/1970 12AM UTC), data: [%s]", seqNbr, latency, timestamp, receivedTime, data);
							logger.info("Received message with sequence number: "+seqNbr+
																" on channel "+channelName+
																". Data: "+data);




					expectedSeqNbr = seqNbr + 1;
				}
				if (batchSize > 1) {
					writeMsg("Last packet processed contained %d batched messages.", batchSize);
				}
				byteBuffer.clear();
			}
			logger.info("MDPChannelListener <" + channelName + "> MDPMsgProcessor thread terminating . . . ");
		}

	}
}