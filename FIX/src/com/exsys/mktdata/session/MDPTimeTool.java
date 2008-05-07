package com.exsys.mktdata.session;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.exsys.mktdata.cfg.Configuration;
import com.exsys.mktdata.cfg.ConfigurationBuilder;
import com.exsys.mktdata.cfg.ConfigurationBuilderFactory;
import com.exsys.mktdata.cfg.ConfigurationException;

/**
 * This is the main class of this tool. It reads through the config file and initializes listeners for each
 * channel, for both feeds if needed
 */


public class MDPTimeTool {
	private Configuration[] channels; //Configuration instances for each channel, with their configuration detail
	private List<MDPChannelListener[]> mdpListeners; //List of listeners initialized for each channel
	private int threadPoolSize; //Thread pool size
	private ExecutorService threadPool;
	static int negativeLatencyThreshold; //Threshold for -ve latency. Latencies lesser than this are not reported.
	static long diffInTime; //Time zone difference in minutes
	static boolean logLatentMessage; //Boolean indicating if the latent messages should be uncompressed and logged
	static boolean logReceivedMessages; //Boolean to indicate if the received messages should be logged

	/**
	 * Get the config file, initialize the static parameters and the thread pool.
	 * @param configFile
	 * @throws ConfigurationException
	 */
	public MDPTimeTool(String configFile) throws ConfigurationException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configFile);
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open configuration file: " + configFile);
			System.exit(0);
		}
		ConfigurationBuilder builder = ConfigurationBuilderFactory.createConfigurationBuilder();
		Configuration cfg = builder.build(fis);
		threadPoolSize = cfg.getAttributeAsInteger("thread-pool-size", 4);
		negativeLatencyThreshold = cfg.getAttributeAsInteger("negative-latency-threshold",2);
		MDPTimeTool.logLatentMessage = cfg.getAttributeAsBoolean("log-latent-message",false);
		logReceivedMessages = cfg.getAttributeAsBoolean("log-recvd-messages",true);
		MDPTimeTool.diffInTime = 60*1000*cfg.getAttributeAsInteger("time-diff-in-minutes",0);
		threadPool = Executors.newFixedThreadPool(threadPoolSize);
		channels = cfg.getChildren("mdp-channel"); //Get all the MDP listeners configured by Channel
	}

	/**
	 * Find the channels configured and start a listener for each of them (and each of the feeds, if configred).
	 * @throws ConfigurationException
	 * @throws IOException
	 */
	public void init() throws ConfigurationException, IOException {
		mdpListeners = new ArrayList<MDPChannelListener[]>(channels.length);
		MDPChannelListener listener;
		MDPChannelListener[] AandBChannels;
		int index = 0;
		//For each channel configured
		for (Configuration c : channels) {
			//Get the Listeners by Feed
			Configuration[] listeners = c.getChildren("mdp-channel-listener");
			// there should only be two, but there can one or more
			AandBChannels = new MDPChannelListener[listeners.length];
			index = 0;
			//For each feed configured
			for (Configuration listenerCfg : listeners) {
				listener = new MDPChannelListener(); //Instantiate a listener
				listener.configure(listenerCfg); //Configure the listener by passing it its parameters
				listener.initialize(); //Initialize it
				AandBChannels[index] = listener; //Add a reference of this listener to the A and B channels array
				index++;
			}
			mdpListeners.add(AandBChannels);
		}
	}

	public void start() {
		for (MDPChannelListener[] listenerArray : mdpListeners) {
			//For each listener
			for (MDPChannelListener listener : listenerArray) {
				// pass the listener a reference to both A and B channels
				listener.setAandBChannels(listenerArray);
				listener.start(); //start the listener
				threadPool.execute(listener); //add it to the threadpool
				threadPool.execute(listener.new MDPMsgProcessor());
			}
		}
	}

	/**
	 * Perform the clean up by stopping and destroying all the listeners
	 */
	public void stopListeners() {
		for (MDPChannelListener[] listeners : mdpListeners) {
			for (MDPChannelListener listener : listeners) {
				listener.stop();
				listener.destroy();
			}
		}
		threadPool.shutdownNow();
	}

	/**
	 * Main function of the tool. Creates an instance of time tool, initializes it and starts it.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java " + MDPTimeTool.class.getName() + " <configuration file>");
			System.exit(0);
		}
		MDPTimeTool t = null;
		try {
			t = new MDPTimeTool(args[0]);
			t.init();
			t.start();
			//Add the instance of ShutdownHook to Runtime for clean up operations on JVM shutdown.
			Runtime.getRuntime().addShutdownHook(t.new ShutdownHook());
		} catch (ConfigurationException ce) {
			System.out.println("There was a problem processing the configuration file: " + args[0]);
			ce.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("There was an error setting up the multicast socket: " + ioe.getMessage());
			ioe.printStackTrace();
		}
	}


	/**
	 * This class will be executed while JVM is shutting down
	 */
	class ShutdownHook extends Thread {
		public void run() {
			stopListeners();
		}
	}

}