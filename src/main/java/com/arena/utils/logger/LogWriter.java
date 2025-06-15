package com.arena.utils.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class LogWriter {
    private static final Semaphore LOG_SEMAPHORE = new Semaphore(1);

    private static final String LOG_FILE_PATH = Paths.get("..", "server_log.txt").toString();

    static void processLogQueue(ConcurrentLinkedQueue<String> logQueue) {
        try {
            LOG_SEMAPHORE.acquire();

            File logFile = new File(LOG_FILE_PATH);
            File parentDir = logFile.getParentFile();
            if (!parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    Logger.info("Log directory created.");
                } else {
                    System.err.println("[Logger][WriteError] Failed to create log directory.");
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8))) {
                while (!logQueue.isEmpty()) {
                    String logEntry = logQueue.poll();
                    if (logEntry != null) {
                        writer.write(logEntry);
                        writer.newLine();
                    }
                }
                writer.flush();
            } catch (IOException e) {
                System.err.println("[Logger][WriteError] " + e.getMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            LOG_SEMAPHORE.release();
        }
    }
}
