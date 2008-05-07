package com.exsys.service;

import java.io.*;
import java.util.*;


public class MetricsCollector {
	private static HashMap stats = new HashMap();

	private static int statLevel = 0;
	private static int checkpoint = 0;
	private static String checkpointMode = "summary";
	private static int checkpointCount = 0;
	private static String outputFile = null;
	private static int maxDepth = 10;
	private static int maxWidth = 134;

	private static Metrics checkpointStats = null;

	public static final int LEVEL3 = 3;
	public static final int LEVEL2 = 2;
	public static final int LEVEL1 = 1;

	static {
		loadConfigValues();
	}
/**
 * Takes and array of statistic objects and adds them together.
 * Entries that match have their underlying values added and
 * entries that are different are added to the tree.
 */
public static final Metrics aggregate(Metrics[] stats) {
	if (stats == null || stats.length == 0) {
		return null;
	}
	Metrics base = new Metrics(false, maxDepth, maxWidth);
	for (int i = 0; i < stats.length; i++) {
		base.add(stats[i]);
	}
	return base;
}
/**
 * Converts and array of Strings containing statistics into
 * Metrics objects and then aggregates them.
 */
public static final Metrics aggregate(String[] stats) {
	if (stats == null || stats.length == 0) {
		return null;
	}
	Metrics[] oStats = new Metrics[stats.length];
	for (int i = 0; i < stats.length; i++) {
		oStats[i] = new Metrics(false, maxDepth, maxWidth, stats[i]);
	}
	return aggregate(oStats);
}
/**
 * @deprecated The use of an object is no longer supported
 */
public static final long beginTimer(Object obj, String description) {
	return beginTimerImpl(description, LEVEL1, 0);
}
public static final long beginTimer(String description) {
	return beginTimerImpl(description, LEVEL1, 0);
}
public static final long beginTimer(String description, int level) {
	return beginTimerImpl(description, level, 0);
}
public static final long beginTimer(String description, int level, long time) {
	return beginTimerImpl(description, level, time);
}
public static final long beginTimer(String description, long time) {
	return beginTimerImpl(description, LEVEL1, time);
}
/**
 * Insert the method's description here.
 * Creation date: (6/24/02 7:04:40 PM)
 * @return long
 * @param o java.lang.Object
 * @param description java.lang.String
 * @param level int
 * @param time long
 */
private static long beginTimerImpl(String description, int level, long time) {
	if (level <= statLevel) {
		if (time == 0) {
			time = System.currentTimeMillis();
		}
		if (checkpointStats != null) {
			checkpointStats.startTimer(description, time);
		}
		getStatistics().startTimer(description, time);
		return time;
	} else {
		return 0;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (9/18/02 1:11:24 PM)
 */
public static final void checkpoint() {
	if (statLevel >= LEVEL1 && checkpoint > 0) {
		checkpointCount++;
		if (checkpointCount == checkpoint) {
			getStatistics().checkpoint(new java.util.Date());
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (9/18/02 1:11:24 PM)
 */
public static final void checkpoint(java.io.PrintStream stream) {
	checkpointCount++;
	if (checkpointCount == checkpoint && checkpointMode != null) {
		checkpointCount = 0;
		if (checkpointMode.toLowerCase().indexOf("summary") > -1) {
			getStatistics().printStats(stream);
		}
		if (checkpointMode.toLowerCase().indexOf("xml") > -1) {
			getStatistics().checkpoint(new java.util.Date());
		}
		if (checkpointMode.toLowerCase().indexOf("point") > -1) {
			if (checkpointStats == null) {
				checkpointStats = getStatistics();
			}
			checkpointStats.printStats(stream);
			checkpointStats = new Metrics(false, maxDepth, maxWidth);
		}
	}
}
/**
 * @deprecated The use of an object is no longer supported
 */
public static final long endTimer(Object obj, String description) {
	return endTimerImpl(description, null, LEVEL1, 0);
}
public static final long endTimer(String description) {
	return endTimerImpl(description, null, LEVEL1, 0);
}
public static final long endTimer(String description, int level) {
	return endTimerImpl(description, null, level, 0);
}
public static final long endTimer(String description, int level, long time) {
	return endTimerImpl(description, null, level, time);
}
public static final long endTimer(String description, long time) {
	return endTimerImpl(description, null, LEVEL1, time);
}
public static final long endTimer(String description, String flag) {
	return endTimerImpl(description, flag, LEVEL1, 0);
}
public static final long endTimer(String description, String flag, int level) {
	return endTimerImpl(description, flag, level, 0);
}
public static final long endTimer(String description, String flag, int level, long time) {
	return endTimerImpl(description, flag, level, time);
}
public static final long endTimer(String description, String flag, long time) {
	return endTimerImpl(description, flag, LEVEL1, time);
}
public static final long endTimer(String description, boolean flag) {
	return endTimerImpl(description, String.valueOf(flag), LEVEL1, 0);
}
public static final long endTimer(String description, boolean flag, int level) {
	return endTimerImpl(description, String.valueOf(flag), level, 0);
}
public static final long endTimer(String description, boolean flag, int level, long time) {
	return endTimerImpl(description, String.valueOf(flag), level, time);
}
public static final long endTimer(String description, boolean flag, long time) {
	return endTimerImpl(description, String.valueOf(flag), LEVEL1, time);
}
private static final long endTimerImpl(String description, String flag, int level, long time) {
	if (level <= statLevel) {
		if (time == 0) {
			time = System.currentTimeMillis();
		}
		if (flag != null) {
			if (checkpointStats != null) {
				checkpointStats.endTimer(description, flag, time);
			}
			getStatistics().endTimer(description, flag, time);
		} else {
			if (checkpointStats != null) {
				checkpointStats.endTimer(description, time);
			}
			getStatistics().endTimer(description, time);
		}
		return time;
	} else {
		return 0;
	}
}
public static final void flush() {
	stats = new HashMap();
	checkpointStats = null;
}
private static final Metrics getStatistics() {
	Thread thread = Thread.currentThread();
	Metrics stat = (Metrics)stats.get(thread);
	if (stat == null) {

		stat = new Metrics(true, maxDepth, maxWidth);
		stats.put(thread, stat);
	}
	return stat;
}
public static final int incrementCounter(String description) {
	return incrementCounterImpl(description, 1, LEVEL1);
}
public static final int incrementCounter(String description, int level) {
	return incrementCounterImpl(description, 1, level);
}
private static final int incrementCounterImpl(String description, int value, int level) {
	if (level <= statLevel) {
		if (checkpointStats != null) {
			checkpointStats.getCounter(description).increment(value);
		}
		return getStatistics().getCounter(description).increment(value);
	} else {
		return 0;
	}
}
/**
 * Gets configuration values based on the latest active instance of the configuration.
 */
public final static void loadConfigValues() {
	try {
		statLevel = Integer.parseInt(ConfigurationService.getValue("statistics"));
	} catch(Exception ex) {
	}
	try {
		checkpoint = Integer.parseInt(ConfigurationService.getValue("statistics.checkpoint"));
	} catch(Exception ex) {
	}
	try {
		// This can be one of three values: summary, point, xml
		checkpointMode = ConfigurationService.getValue("statistics.mode");
		if (checkpointMode == null) {
			checkpointMode = "summary";
		}
	} catch(Exception ex) {
	}
	try {
		// This can be one of three values: summary, point, xml
		outputFile = ConfigurationService.getValue("statistics.file");
	} catch(Exception ex) {
	}
	try {
		maxDepth = Integer.parseInt(ConfigurationService.getValue("statistics.depth"));
	} catch(Exception ex) {
	}
	try {
		maxWidth = Integer.parseInt(ConfigurationService.getValue("statistics.width"));
	} catch(Exception ex) {
	}
}
/**
 * Insert the method's description here.
 * Creation date: (8/12/02 11:15:35 AM)
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	MetricsCollector.beginTimer("Main1");
	MetricsCollector.beginTimer("Sub1");
	MetricsCollector.beginTimer("Sub2Sub2Sub2");

	MetricsCollector.beginTimer("Sub3");
	MetricsCollector.endTimer("Sub3", true);
	MetricsCollector.beginTimer("Sub3");
	MetricsCollector.endTimer("Sub3", false);
	MetricsCollector.beginTimer("Sub3");
	MetricsCollector.endTimer("Sub3", false);
	MetricsCollector.beginTimer("Sub3");
	MetricsCollector.endTimer("Sub3", true);

	MetricsCollector.beginTimer("Sub4");
	MetricsCollector.beginTimer("Sub5");
	MetricsCollector.endTimer("Sub5");
	MetricsCollector.endTimer("Sub4", false);
	MetricsCollector.beginTimer("Sub4");
	MetricsCollector.beginTimer("Sub5");
	MetricsCollector.endTimer("Sub5");
	MetricsCollector.endTimer("Sub4", false);
	MetricsCollector.beginTimer("Sub4");
	MetricsCollector.beginTimer("Sub5");
	MetricsCollector.endTimer("Sub5");
	MetricsCollector.endTimer("Sub4", true);
	MetricsCollector.beginTimer("Sub4");
	MetricsCollector.endTimer("Sub4", true);
	MetricsCollector.beginTimer("Sub4");
	MetricsCollector.beginTimer("Sub5");
	MetricsCollector.endTimer("Sub5");
	MetricsCollector.endTimer("Sub4", false);

	MetricsCollector.endTimer("Sub2Sub2Sub2");
	MetricsCollector.endTimer("Sub1");
	MetricsCollector.endTimer("Main1");


	MetricsCollector.beginTimer("Main2");
	MetricsCollector.beginTimer("Sub1");
	MetricsCollector.beginTimer("Sub2Sub2Sub2");
	MetricsCollector.endTimer("Sub1");
	MetricsCollector.endTimer("Sub2Sub2Sub2");
	MetricsCollector.endTimer("Main2");
	MetricsCollector.printStats(System.out);
}
/**
 * Insert the method's description here.
 * Creation date: (7/9/01 11:47:38 PM)
 * @param obj java.lang.Object
 * @param writer java.io.PrintWriter
 */
public static final void printStats(PrintStream stream) {
	printStats(new PrintWriter(stream, true));
}
/**
 * Insert the method's description here.
 * Creation date: (7/9/01 11:47:38 PM)
 * @param obj java.lang.Object
 * @param writer java.io.PrintWriter
 */
public static final void printStats(PrintWriter writer) {
	if (statLevel > 0) {
		Iterator statIter = stats.values().iterator();
		while (statIter.hasNext()) {
			Metrics stat = (Metrics)statIter.next();
			stat.printStats(writer);

			if (checkpointMode != null && checkpointMode.toLowerCase().indexOf("xml") > -1) {
				if (outputFile != null) {
					try {
						File output = new File(ConfigurationService.getValue("workingdirectory") + "/" + outputFile);
						FileWriter fwriter = new FileWriter(output);
						PrintWriter pWriter = new PrintWriter(fwriter);
						stat.toXML(pWriter);
						fwriter.close();
					} catch (Exception ex) {
						stat.toXML(writer);
					}
				} else {
					stat.toXML(writer);
				}
			}
		}
	}
}
/**
 * @deprecated The use of an object is no longer supported
 */
public static final void printStats(Object obj, PrintWriter stream) {
	printStats(stream);
}
/**
 * @deprecated This just isn't supported anymore
 */
public static final void setDefaultObject(Object object) {
}
/**
 * Insert the method's description here.
 * Creation date: (2/18/03 4:04:10 PM)
 * @param writer java.io.PrintWriter
 */
public final static void toXML(PrintWriter writer) {
	Iterator statIter = stats.values().iterator();
	while (statIter.hasNext()) {
		Metrics stat = (Metrics)statIter.next();
		stat.toXML(writer);
	}
}
}
