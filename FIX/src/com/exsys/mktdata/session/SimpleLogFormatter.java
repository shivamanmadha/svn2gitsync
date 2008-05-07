package com.exsys.mktdata.session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created on Feb 18, 2005
 */
public class SimpleLogFormatter extends Formatter {
    private final String separator = System.getProperty("line.separator");
    private SimpleDateFormat dformatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    private Date now = new Date();

    /**
     *
     */
    public SimpleLogFormatter() {
        super();
    }

    /**
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public synchronized String format(LogRecord record) {
        now.setTime(System.currentTimeMillis());
        return new StringBuffer().append(dformatter.format(now)).append(" ").append(record.getLevel().getName())
                .append(" ").append(record.getMessage()).append(separator).toString();
    }
}
