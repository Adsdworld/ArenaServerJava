package com.arena.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;


public class Logger {

    private static final String LOG_FILE_PATH = Paths.get("..", "server_log.txt").toString();

    private static final ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<>();
    private static final Semaphore logSemaphore = new Semaphore(1);

    // Thread dédié pour l'écriture
    private static volatile boolean isWriting = false;

    public static void info(String message) {
        enqueueLog("info", message);
    }

    public static void warn(String message) {
        enqueueLog("warning", message);
    }

    public static void failure(String message) {
        enqueueLog("failure", message);
    }

    public static void error(String message) {
        enqueueLog("error", message);
    }

    private static void enqueueLog(String level, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());

        // Récupérer le nom de la méthode appelante (optionnel)
        String callerInfo = getCallerInfo();

        String formatted = String.format("[%s][%s][%s] %s", timestamp, callerInfo, level, message);
        System.out.println(formatted); // Affichage console

        logQueue.add(formatted);

        if (!isWriting) {
            isWriting = true;
            new Thread(Logger::processLogQueue).start();
        }
    }

    private static void processLogQueue() {
        while (!logQueue.isEmpty()) {
            String logEntry = logQueue.poll();
            if (logEntry != null) {
                try {
                    logSemaphore.acquire();

                    File logFile = new File(LOG_FILE_PATH);
                    File parentDir = logFile.getParentFile();
                    if (!parentDir.exists()) {
                        if (parentDir.mkdirs()) {
                            info("Log directory created.");
                        } else {
                            System.err.println("[Logger][WriteError] Failed to create log directory.");
                        }
                    }

                    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8))) {
                        writer.write(logEntry);
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("[Logger][WriteError] " + e.getMessage());
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    logSemaphore.release();
                }
            }
        }
        isWriting = false;
    }

    private static String getCallerInfo() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        // index 0: getStackTrace, 1: getCallerInfo, 2: enqueueLog, 3: info/warn/failure, 4: caller
        if (stack.length > 4) {
            StackTraceElement caller = stack[4];
            String fileName = caller.getFileName() != null ? caller.getFileName().replace(".java", "") : "UnknownFile";
            String methodName = caller.getMethodName();
            return fileName + "][" + methodName+"]";
        }
        return "UnknownCaller";
    }
}
