package com.exsys.service;

import java.util.*;
import java.io.*;

/**
 * This base object is used for maintaining execution statistics.
 */
public class Metrics {
	private HashMap statHash = new HashMap();
	private ArrayList stats = new ArrayList();
	private Timer activeTimer = null;
	private int maxDepth;
	private int maxWidth;
	private boolean strict;
	private int warningMessageCount = 0;
	private static final int MAX_WARNING_MESSAGES = 100;

	public static final String TIME_FORMAT = "HH:mm:ss.SSS";
	public static final java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(TIME_FORMAT);
	static {
		formatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
	}

private class XMLTokenizer {
	private StringTokenizer st;
	private boolean hasNext = false;
	public XMLTokenizer(String xml) {
		st = new StringTokenizer(xml, "<>", true);
		preAddress();
	}
	public XMLTag nextTag() {
		if (hasNext) {
			String value = st.nextToken();
			preAddress();
			return new XMLTag(value.trim());
		} else {
			return null;
		}
	}
	public boolean hasNext() {
		return hasNext;
	}
	private void preAddress() {
		hasNext = false;
		// Skip to the first opening tag
		while (st.hasMoreTokens()) {
			if (st.nextToken().equals("<")) {
				hasNext = true;
				break;
			}
		}
	}
}
private class XMLTag {
	public boolean close = false;
	public boolean selfClose = false;
	public String name;
	public HashMap attributes;
	public XMLTag(String contents) {
		int closeIndex = contents.indexOf("/");
		if (closeIndex == 0) {
			close = true;
			name = contents.substring(1).trim();
		} else {
			if (closeIndex == (contents.length() - 1)) {
				selfClose = true;
				contents = contents.substring(0, closeIndex).trim();
			}
			int firstSpace = contents.indexOf(" ");
			if (firstSpace == -1) {
				name = contents;
			} else {
				name = contents.substring(0, firstSpace);
				contents = contents.substring(firstSpace + 1).trim();
				attributes = new HashMap();
				int eqIndex = -1;
				while ((eqIndex = contents.indexOf("=")) != -1) {
					String name = contents.substring(0, eqIndex).trim();
					int q1start = contents.indexOf("\"", eqIndex);
					int q2start = contents.indexOf("\"", q1start + 1);
					attributes.put(name, contents.substring(q1start + 1, q2start).trim());
					contents = contents.substring(q2start + 1);
				}
			}
		}
	}
}

// scope of class Timer has been changed to protected while migrating to JDK 1.3
private class Timer extends HashMap {
	private long accessTime = 0;
	private long intermediateStartTime = 0;
	private int counter = 0;
	private String description;
	private Timer parent;
	private ArrayList childStats = new ArrayList();
	private ArrayList checkpoints = null;
	private Point lastCheckpoint = null;
	private int depth = 0;
	private int rep = 0;

	public Timer(Timer parentTimer, String desc) {
		description = desc;
		parent = parentTimer;
		if (parentTimer != null) {
			depth = parentTimer.depth + 1;
		}
	}
	public Timer(Timer parentTimer, XMLTag tag, XMLTokenizer tk) {
		parent = parentTimer;
		if (tag.attributes != null) {
			description = (String)tag.attributes.get("name");
			accessTime = Long.parseLong((String)tag.attributes.get("duration"));
			counter = Integer.parseInt((String)tag.attributes.get("count"));
		}
		while (!tag.selfClose && tk.hasNext()) {
			XMLTag t2 = tk.nextTag();
			if (t2.name.equals("point")) {
				// ignore
			} else if (t2.name.equals("timer")) {
				if (t2.close) {
					break;
				} else {
					Timer t = new Timer(this, t2, tk);
					put(t.description, t);
				}
			}
		}
	}
	public synchronized void start(long time) {
		intermediateStartTime += time;
	}
	public synchronized void stop(long time) {
		accessTime += (time - intermediateStartTime);
		intermediateStartTime = 0;
	}
	public void incrementCounter() {
		counter++;
	}
	public String getDescription() {
		return description;
	}
	public Date getTime() {
		return new Date(accessTime);
	}
	public Date getAverageTime() {
		if (counter > 0) {
			return new Date(accessTime/counter);
		} else {
			return new Date(0);
		}
	}
	public int getCount() {
		return counter;
	}
	public boolean equals(Object obj) {
	    if (obj == this) {
		    return true;
	    }
	    if (obj instanceof String) {
			return description.equals(obj);
		}
	    return false;
	}
	public void add(Timer timer) {
		accessTime += timer.accessTime;
		counter += timer.counter;

		Iterator iter = timer.childStats.iterator();
		while(iter.hasNext()) {
			Timer child = (Timer)iter.next();
			Timer matchingChild = (Timer)get(child.description);
			if (matchingChild != null) {
				matchingChild.add(child);
			} else {
				put(child.description, child);
			}
		}
	}
	public Object put(Object key, Object description) {
		childStats.add(description);
		return super.put(key, description);
	}
	public Object remove(Object key) {
		Object rem = super.remove(key);
		childStats.remove(rem);
		return rem;
	}
	public int getMaxDescription() {
		int max = description.length();

		Iterator iter = childStats.iterator();
		while(iter.hasNext()) {
			int childMax = ((Timer)iter.next()).getMaxDescription() + 2;
			if (childMax > max) {
				max = childMax;
			}
		}
		return max;
	}
	public void checkpoint(Date currentTime) {
		if (checkpoints == null) {
			checkpoints = new ArrayList();
		}
		Point p = null;
		if (lastCheckpoint == null) {
			p = new Point(accessTime, counter, currentTime);
			lastCheckpoint = p;
		} else {
			p = new Point(accessTime - lastCheckpoint.duration, counter - lastCheckpoint.count, currentTime);
			lastCheckpoint = new Point(accessTime, counter, currentTime);
		}
		checkpoints.add(p);
		Iterator children = childStats.iterator();
		while (children.hasNext()) {
			((Timer)children.next()).checkpoint(currentTime);
		}
	}
	public void toXML(PrintWriter writer, String lead) {
		writer.println(lead + "<timer name=\"" + description + "\" duration=\"" + accessTime + "\" count=\"" + counter + "\">");
		if (checkpoints != null) {
			Iterator points = checkpoints.iterator();
			while (points.hasNext()) {
				Point p = (Point)points.next();
				writer.println(lead + "  <point duration=\"" + accessTime + "\" count=\"" + p.count + "\" time=\"" + p.time + "\"/>");
			}
		}
		Iterator children = childStats.iterator();
		while (children.hasNext()) {
			Timer child = (Timer)children.next();
			child.toXML(writer, "  " + lead);
		}
		writer.println(lead + "</timer>");
	}
}

private class Point {
	public long duration;
	public Date time;
	public int count;

	public Point(long nDuration, int nCount, Date nTime) {
		duration = nDuration;
		time = nTime;
		count = nCount;
	}
}

// scope of class Timer has been changed to protected while migrating to JDK 1.3
protected class Counter {
	private int counter = 0;
	private String description;

	public Counter(String description) {
		this.description = description;
	}
	public Counter(XMLTag tag, XMLTokenizer tk) {
		if (tag.attributes != null) {
			description = (String)tag.attributes.get("name");
			counter = Integer.parseInt((String)tag.attributes.get("count"));
		}
		while (!tag.selfClose && tk.hasNext()) {
			XMLTag t2 = tk.nextTag();
			if (t2.name.equals("counter") && t2.close) {
				break;
			}
		}
	}
	public int increment() {
		counter++;
		return counter;
	}
	public int increment(int value) {
		counter += value;
		return counter;
	}
	public String getDescription() {
		return description;
	}
	public int getCount() {
		return counter;
	}
	public boolean equals(Object obj) {
	    if (obj == this) {
		    return true;
	    }
	    if (obj instanceof String) {
			return description.equals(obj);
		}
	    return false;
	}
	public void add(Counter ctr) {
		counter += ctr.counter;
	}
	public void toXML(PrintWriter writer, String lead) {
		writer.println(lead + "<counter name=\"" + description + "\" count=\"" + counter + "\"/>");
	}
}
public Metrics(boolean newStrict, int newMaxDepth, int newMaxWidth) {
	strict = newStrict;
	maxDepth = newMaxDepth;
	maxWidth = newMaxWidth;
}
public Metrics(boolean newStrict, int newMaxDepth, int newMaxWidth, String xmlData) {
	strict = newStrict;
	maxDepth = newMaxDepth;
	maxWidth = newMaxWidth;
	buildFromXML(xmlData);
}
/**
 */
public void add(Metrics newStats) {
	Iterator iter = newStats.stats.iterator();
	while (iter.hasNext()) {
		Object stat = iter.next();
		if (stat instanceof Timer) {
			Timer newTimer = (Timer)stat;
			Timer myTimer = (Timer)statHash.get(newTimer.description);
			if (myTimer != null) {
				myTimer.add(newTimer);
			} else {
				stats.add(newTimer);
				statHash.put(newTimer.description, newTimer);
			}
		} else if (stat instanceof Counter) {
			Counter newCounter = (Counter)stat;
			Counter myCounter = (Counter)statHash.get(newCounter.description);
			if (myCounter != null) {
				myCounter.add(newCounter);
			} else {
				stats.add(newCounter);
				statHash.put(newCounter.description, newCounter);
			}
		}
	}
}
/**
 * This method should generate the contents of this object from
 * the xml data passed in.  Because we don't want to include
 * XML parsing libraries in the install just for a debug utility,
 * the XML data is parsed using a rudimentary tokenizing methodology.
 */
private void buildFromXML(String xmlData) {
	XMLTokenizer tokenizer = new XMLTokenizer(xmlData);
	while (tokenizer.hasNext()) {
		XMLTag tag = tokenizer.nextTag();
		if (tag.name.equals("execution")) {
			// Ignore the data
			if (tag.close) {
				break;
			}
		} else if (tag.name.equals("timer") && !tag.close) {
			Timer timer = new Timer(null, tag, tokenizer);
			stats.add(timer);
			statHash.put(timer.description, timer);
		} else if (tag.name.equals("counter") && !tag.close) {
			Counter counter = new Counter(tag, tokenizer);
			stats.add(counter);
			statHash.put(counter.description, counter);
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (9/22/02 12:49:00 PM)
 * @param currentTime long
 */
public void checkpoint(Date currentTime) {
	Date time = new Date(currentTime.getTime());
	Iterator statIter = stats.iterator();
	while (statIter.hasNext()) {
		Object stat = statIter.next();
		if (stat instanceof Timer) {
			((Timer)stat).checkpoint(time);
		} else if (stat instanceof Counter) {
			Counter counter = (Counter)stat;
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (8/12/02 10:09:37 AM)
 * @param description java.lang.String
 */
public void endTimer(String desc, long time) {
	String description = new String(desc);

	try {
		if (activeTimer != null) {
			if (activeTimer.description.equals(description)) {
				activeTimer.stop(time);
				activeTimer.incrementCounter();
				if (activeTimer.rep > 0) {
					activeTimer.rep--;
				} else {
					activeTimer = activeTimer.parent;
				}
			} else {
				if (strict) {
					writeWarning("Timer mismatch, end for inactive timer: " + description);
					writeWarning("Auto closing: " + activeTimer.description);
				}
				activeTimer.stop(time);
				activeTimer = activeTimer.parent;
				endTimer(description, time);
			}
		} else if (strict) {
			writeWarning("Timer mismatch, end for inactive timer: " + description);
			Timer timer;
			if (!statHash.containsKey(description)) {
				timer = new Timer(null, description);
				stats.add(timer);
				statHash.put(description, timer);
			} else {
				timer = (Timer)statHash.get(description);
			}
			timer.stop(time);
			timer.incrementCounter();
		}
	} catch (Throwable ex) {
		ex.printStackTrace();
	}
}
/**
 * End the timer with a flag
 */
public void endTimer(String desc, String flagValue, long time) {
	String description = new String(desc);
	String flag = new String(flagValue);
	try {
		// Build up the description
		String flaggedDescription = description + " - " + flag;

		// If there is an active timer
		if (activeTimer != null) {

			// If this is the end for an active timer that doesn't already contain a flag
			if (activeTimer.description.equals(description)) {
				// Stop the timer as you normally would
				activeTimer.stop(time);
				activeTimer.incrementCounter();
				Timer parent = activeTimer.parent;
				activeTimer = parent;

				// Remove the unflagged timer and add the new one in
				if (parent == null) {
					Timer tempTimer = (Timer)statHash.remove(description);
					stats.remove(tempTimer);

					if (statHash.containsKey(flaggedDescription)) {
						Timer timer = (Timer)statHash.get(flaggedDescription);
						timer.add(tempTimer);
					} else {
						tempTimer.description = flaggedDescription;
						stats.add(tempTimer);
						statHash.put(flaggedDescription, tempTimer);
					}
				} else {
					Timer tempTimer = (Timer)parent.remove(description);

					if (parent.containsKey(flaggedDescription)) {
						Timer timer = (Timer)parent.get(flaggedDescription);
						timer.add(tempTimer);
					} else {
						tempTimer.description = flaggedDescription;
						parent.put(flaggedDescription, tempTimer);
					}
				}

			} else {
				if (strict) {
					writeWarning("Timer mismatch, end for inactive timer: " + description);
					writeWarning("Auto closing: " + activeTimer.description);
				}
				activeTimer.stop(time);
				activeTimer = activeTimer.parent;
				endTimer(description, time);
			}
		} else if (strict) {
			writeWarning("Timer mismatch, end for inactive timer: " + description);
			Timer timer;
			if (!statHash.containsKey(flaggedDescription)) {
				timer = new Timer(null, flaggedDescription);
				stats.add(timer);
				statHash.put(flaggedDescription, timer);
			} else {
				timer = (Timer)statHash.get(flaggedDescription);
			}
			timer.stop(time);
			timer.incrementCounter();
		}
	} catch (Throwable ex) {
		ex.printStackTrace();
	}
}
public Counter getCounter(String desc) {
	String description = new String(desc);
	Counter counter;
	if (!statHash.containsKey(description)) {
		counter = new Counter(description);
		stats.add(counter);
		statHash.put(description, counter);
	} else {
		counter = (Counter)statHash.get(description);
	}
	return counter;
}
/**
 * Insert the method's description here.
 * Creation date: (8/12/02 11:01:53 AM)
 * @return int
 * @param startPoint int
 */
private int getMaxDescription(int startPoint) {
	int max = startPoint;
	Iterator iter = stats.iterator();
	while(iter.hasNext()) {
		Object stat = iter.next();
		if (stat instanceof Timer) {
			int tLength = ((Timer)stat).getMaxDescription();
			if (tLength > max) {
				max = tLength;
			}
		} else if (stat instanceof Counter) {
			int cLength = ((Counter)stat).description.length();
			if (cLength > max) {
				max = cLength;
			}
		}
	}
	return max;
}
/**
 * Insert the method's description here.
 * Creation date: (7/9/01 11:43:43 PM)
 * @param writer java.io.PrintWriter
 * @param count int
 */
private void padSpaces(PrintWriter writer, int count) {
	for (int i = 0; i < count; i++) {
		writer.print(" ");
	}
}
/**
 * Insert the method's description here.
 * Creation date: (7/9/01 11:29:43 PM)
 * @param writer java.io.PrintWriter
 */
public void printStats(PrintStream stream) {
	PrintWriter writer = new PrintWriter(stream, true);
	printStats(writer);
}
/**
 * Insert the method's description here.
 * Creation date: (7/9/01 11:29:43 PM)
 * @param writer java.io.PrintWriter
 */
public void printStats(PrintWriter writer) {
	try {
		// Setup the header
		String statHeader1 = "Metrics";
		String statHeader2 = "Total Time     Average        Access Count";
		int longDescription = getMaxDescription(statHeader1.length());
		int maxDescription = maxWidth - statHeader2.length();
		if (longDescription > maxDescription) {
			longDescription = maxDescription;
		}
		writer.print(statHeader1);
		padSpaces(writer, longDescription - statHeader1.length() + 1);
		writer.println(statHeader2);
		for (int i = 0; i < longDescription + 1 + statHeader2.length(); i++) {
			writer.print("-");
		}
		writer.println("-");

		Iterator iter = stats.iterator();
		while(iter.hasNext()) {
			Object stat = iter.next();
			if (stat instanceof Timer) {
				Timer timer = (Timer)stat;
				printTimer(writer, timer, "", longDescription);
			} else if (stat instanceof Counter) {
				Counter counter = (Counter)stat;
				writer.print(counter.getDescription());
				padSpaces(writer, longDescription - counter.getDescription().length() + 1);
				padSpaces(writer, TIME_FORMAT.length());
				writer.print("   ");
				padSpaces(writer, TIME_FORMAT.length());
				writer.print("   ");
				writer.println(counter.getCount());
			}
		}
	} catch (Throwable ex) {
		ex.printStackTrace();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (8/12/02 10:46:45 AM)
 * @param writer java.io.PrintWriter
 * @param timer java.lang.Object
 * @param lead java.lang.String
 * @param length int
 */
private void printTimer(PrintWriter writer, Timer timer, String lead, int length) {
	if (timer.depth <= maxDepth) {
		String description = lead + timer.getDescription();
		if (description.length() > length) {
			writer.print(description.substring(0, length+1));
		} else {
			writer.print(description);
			padSpaces(writer, length - description.length() + 1);
		}
		writer.print(formatter.format(timer.getTime()));
		writer.print("   ");
		writer.print(formatter.format(timer.getAverageTime()));
		writer.print("   ");
		writer.println(timer.getCount());

		Iterator children = timer.childStats.iterator();
		while (children.hasNext()) {
			Timer child = (Timer)children.next();
			printTimer(writer, child, "    " + lead, length);
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (8/12/02 10:09:37 AM)
 * @param description java.lang.String
 */
public void startTimer(String desc, long time) {
	String description = new String(desc);
	try {
		Timer timer;
		if (activeTimer == null) {
			if (!statHash.containsKey(description)) {
				timer = new Timer(null, description);
				stats.add(timer);
				statHash.put(description, timer);
			} else {
				timer = (Timer)statHash.get(description);
			}

		} else if (activeTimer.description.equals(description)) {
			timer = activeTimer;
			timer.rep++;
		} else {
			if (!activeTimer.containsKey(description)) {
				timer = new Timer(activeTimer, description);
				activeTimer.put(description, timer);
			} else {
				timer = (Timer)activeTimer.get(description);
			}
		}
		timer.start(time);
		activeTimer = timer;
	} catch (Throwable ex) {
		ex.printStackTrace();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (9/22/02 1:08:02 PM)
 * @param stream java.io.PrintStream
 */
public void toXML(PrintStream stream) {
	PrintWriter writer = new PrintWriter(stream, true);
	toXML(writer);
}
/**
 * Insert the method's description here.
 * Creation date: (9/22/02 1:08:02 PM)
 * @param stream java.io.PrintStream
 */
public void toXML(PrintWriter writer) {
	writer.println("<execution date=\"" + new java.util.Date() + "\">");
	Iterator statIter = stats.iterator();
	while (statIter.hasNext()) {
		Object stat = statIter.next();
		if (stat instanceof Timer) {
			((Timer)stat).toXML(writer, "  ");
		} else if (stat instanceof Counter) {
			((Counter)stat).toXML(writer, "  ");
		}
	}
	writer.println("</execution>");
}
/**
 * Insert the method's description here.
 * Creation date: (2/22/03 3:42:02 PM)
 * @param message java.lang.String
 */
private void writeWarning(String message) {
	if (warningMessageCount < MAX_WARNING_MESSAGES) {
		System.err.println("STATISTICS WARNING: " + message);
		warningMessageCount++;
		if (warningMessageCount >= MAX_WARNING_MESSAGES) {
			System.err.println("STATISTICS WARNING: Exceeded space for warning messages");
		}
	}
}
}
