package com.arena.utils.logger;

import com.arena.game.GameNameEnum;
import com.arena.utils.TimeUtil;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Logger {
    private static final ConcurrentLinkedQueue<String> LOG_QUEUE = new ConcurrentLinkedQueue<>();

    private static final int MAX_BUFFER_SIZE = 100;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::flush));
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(Logger::flush, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * For logging {@code System} , {@link com.arena.network.JavaWebSocket} , {@link com.arena.server.Server}  and {@link com.arena.player.Player}  events.
     * example: "Player joined the game", "Game created", "Websocket connection established".
     *
     * @param message the message to log.
     * @implNote This method enqueues a log message with the level "info".
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void info(String message) {
        enqueueLog("info", message);
    }

    /**
     * For logging {@link com.arena.server.Server}  actions that have been processed successfully.
     * Must not be used if info, warn, failure or error can be used instead.
     * example: "Started", "Stopped", "Registering player <uuid> successfully".
     *
     * @param message the message to log.
     * @implNote This method enqueues a log message with the level "info server" and a custom prefix.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void server(String message) {
        enqueueLog("info server", message, "...___---{([||| SERVER |||]})---<___... >>>");
    }

    /**
     * For logging {@link com.arena.game.Game}  actions that have been processed successfully.
     *
     * @param message the message to log.
     * @implNote This method enqueues a log message with the level "info game" and a custom prefix.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static  void game(String message) {
        enqueueLog("info game", message, "...___---{([||| GAME |||]})---<___... >>>");
    }

    /**
     * For logging {@link com.arena.game.Game}  actions that have been processed successfully with a specific {@link GameNameEnum} .
     *
     * @param message the message to log.
     * @param gameName the name of the game as an enum.
     * @implNote This method enqueues a log message with the level "info game" and a custom prefix that includes the game name.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static  void game(String message, GameNameEnum gameName) {
        enqueueLog("info game", message, "...___---{([|| " + gameName.getGameName() + " ||]})---<___... >>>");
    }

    /**
     * For logging warnings, which indicate potential issues that may lead to future failures in code execution.
     * A warning make the code continue to run, but it is a sign that something might not be right.
     * example: "Player tried to join a game that is full", "Data transmitted ignored or unrecognized enum: Foo = 'bar'".
     *
     * @param message the message to log.
     * @implNote This method enqueues a log message with the level "warning".
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void warn(String message) {
        enqueueLog("warning", message);
    }

    /**
     * For logging failures, which indicate that something went wrong but the code can still continue to run.
     * Used for game logic failures.
     * example: "Player tried to join a game that does not exist", "Game creation failed due to invalid parameters".
     *
     * @param message the message to log.
     * @implNote This method enqueues a log message with the level "failure".
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void failure(String message) {
        enqueueLog("failure", message);
    }

    /**
     * For logging errors, an error is raised by Java Runtime or a library exception.
     *
     * @param message the message to log.
     * @implNote This method enqueues a log message with the level "error".
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void error(String message) {enqueueLog("error", message);}

    /**
     * Logs a test message with the level "test".
     * This method is used for debugging purposes and should not be used in production code.
     * exemple: "Test message: Client connected".
     *
     * @param message the message to log.
     * @implNote This method enqueues a log message with the level "test" and a custom prefix.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void test(String message) {
        enqueueLog("@TEST", message);
    }

    /**
     * Enqueues a log message with a custom prefix before the message.
     * This method is used to format the log message with a timestamp, caller information, and a custom prefix.
     *
     * @param level the log level (e.g., "info", "warning", "error").
     * @param message the message to log.
     * @param customBefore the custom prefix to include before the message.
     * @implNote This method formats the log message and adds it to the log queue for processing.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    private static void enqueueLog(String level, String message, String customBefore) {
        String timestamp = TimeUtil.getUTCTimestamp();

        String callerInfo = TimeUtil.getCallerInfo();

        String formatted = String.format("[%s][%s][%s] %s %s", timestamp, callerInfo, level, customBefore, message);
        System.out.println(formatted);

        LOG_QUEUE.add(formatted);

        if (LOG_QUEUE.size() >= MAX_BUFFER_SIZE) {
            new Thread(() -> LogWriter.processLogQueue(LOG_QUEUE)).start();
        }
    }
    /**
     * Enqueues a log message with a custom prefix before the message.
     * This method is used to format the log message with a timestamp, caller information.
     *
     * @param level the log level (e.g., "info", "warning", "error").
     * @param message the message to log.
     * @implNote This method formats the log message and adds it to the log queue for processing.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    private static void enqueueLog(String level, String message) {
        String timestamp = TimeUtil.getUTCTimestamp();

        String callerInfo = TimeUtil.getCallerInfo();

        String formatted = String.format("[%s][%s][%s] %s", timestamp, callerInfo, level, message);
        System.out.println(formatted);

        LOG_QUEUE.add(formatted);
    }

    /**
     * Flushes the log queue buffer to write all pending log messages to the log file.
     *
     * @implNote This method checks if the log queue is not empty and call the LogWriter to process the queue.
     * @author A.SALLIER
     * @date 2025-06-15
     */
    public static void flush() {
        if (!LOG_QUEUE.isEmpty()) {
            new Thread(() -> LogWriter.processLogQueue(LOG_QUEUE)).start();
        }
    }
}
