package org.pintoschneider.void_of_the_unfathomable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
        final String filePath = "debug-%s.log".formatted(now.toString().replace(":", "-"));
        this.fileOutputStream = new FileOutputStream(filePath);
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
