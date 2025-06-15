package com.arena.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
    /**
     * Return the caller's file name and method name.
     *
     * @return a string containing the file name and method name of the caller in the format "FileName][MethodName".
     * @implNote This method retrieves the stack trace of the current thread and extracts the file name and method name of the caller.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static String getCallerInfo() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        if (stack.length > 4) {
            StackTraceElement caller = stack[4];
            String fileName = caller.getFileName() != null ? caller.getFileName().replace(".java", "") : "UnknownFile";
            String methodName = caller.getMethodName();
            return fileName + "][" + methodName;
        }
        return "UnknownCaller";
    }

    /**
     * Returns the current UTC timestamp in the format "yyyy-MM-dd HH:mm:ss.SSS".
     *
     * @return a {@link String}  representing the current UTC date and time.
     * @implNote This method uses `SimpleDateFormat` to format the current date and time in UTC. It sets the time zone to UTC and formats the current date into a string.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static String getUTCTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
}
