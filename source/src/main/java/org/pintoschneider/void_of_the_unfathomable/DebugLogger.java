package org.pintoschneider.void_of_the_unfathomable;

import java.io.*;
import java.time.Instant;

/**
 * A debug logger that redirects standard output and error streams to a log file.
 * <p>
 * When instantiated, it creates a log file named "debug-<timestamp>.log" and redirects {@code System.out} and
 * {@code System.err} to this file. Upon closing, it restores the original output streams and closes the log file.
 */
public final class DebugLogger implements AutoCloseable {
    final private PrintStream originalOut = System.out;
    final private PrintStream originalErr = System.err;
    final private PrintStream printStream;
    final private FileOutputStream fileOutputStream;

    /**
     * Constructs a DebugLogger that redirects output to a timestamped log file.
     *
     * @throws FileNotFoundException if the log file cannot be created
     */
    public DebugLogger() throws FileNotFoundException {
        final Instant now = Instant.now();
        final String directoryPath = "logs";
        final String fileName = "debug-%s.log".formatted(now.toString().replace(":", "-"));
        final String fullPath = directoryPath + File.separator + fileName;

        final File directory = new File(directoryPath);
        if (!directory.exists()) {
            final boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create logs directory.");
            }
        }

        this.fileOutputStream = new FileOutputStream(fullPath);
        this.printStream = new PrintStream(fileOutputStream, true);

        System.setOut(printStream);
        System.setErr(printStream);
    }

    @Override
    public void close() throws IOException {
        System.setOut(originalOut);
        System.setErr(originalErr);
        printStream.close();
        fileOutputStream.close();
    }
}
