/**
 * Package {@code com.arena.utils.logger} provides utility classes for logging within the Arena project.
 * <p>
 * It includes:
 * <ul>
 *   <li>{@link com.arena.utils.logger.Logger} - a public-facing logger interface for enqueuing
 *       log messages with various levels (info, warn, error, etc.) and formatting them.</li>
 *   <li>{@link com.arena.utils.logger.LogWriter} - a utility class responsible for safely writing
 *       the queued log messages to a file asynchronously with concurrency control.</li>
 * </ul>
 * <p>
 * The logging system uses a thread-safe queue to buffer log entries and periodically flushes
 * them to disk, ensuring minimal performance impact on the main application.
 * <p>
 * Logging messages include timestamps, caller information, and customizable prefixes.
 * <p>
 * Usage of this package facilitates consistent, thread-safe logging across different
 * components such as {@code Server}, {@code Game}, {@code Player}, and networking modules.
 * <p>
 * Author: A.SALLIER
 * Date: 2025-06-15
 */
package com.arena.utils.logger;
