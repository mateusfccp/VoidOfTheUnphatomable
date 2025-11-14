package org.pintoschneider.void_of_the_unfathomable.frontend;

import com.jediterm.core.util.TermSize;
import com.jediterm.terminal.TtyConnector;
import org.jetbrains.annotations.NotNull;
import org.jline.terminal.Size;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * A TtyConnector that connects JediTerm to a pair of I/O streams.
 * This allows us to pipe the I/O of a process (in our case, JLine) into JediTerm.
 */
public final class JLineTtyConnector implements TtyConnector {
    private final Reader reader;
    private final OutputStream outputStream;
    private org.jline.terminal.Terminal jlineTerminal; // Reference to the JLine Main

    /**
     * Creates a JLineTtyConnector with the given input and output streams.
     *
     * @param inputStream  The input stream to read from (JLine's output).
     * @param outputStream The output stream to write to (JLine's input).
     */
    public JLineTtyConnector(InputStream inputStream, OutputStream outputStream) {
        this.reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        this.outputStream = outputStream;
    }

    /**
     * Sets the JLine Main instance.
     *
     * @param jlineTerminal The JLine Main to be associated with this connector.
     */
    public void setJLineTerminal(org.jline.terminal.Terminal jlineTerminal) {
        this.jlineTerminal = jlineTerminal;
    }

    @Override
    public void close() {
        try {
            reader.close();
            outputStream.close();
        } catch (IOException e) {
            // ignore
        }
    }

    @Override
    public String getName() {
        return "JLinePty";
    }

    @Override
    public int read(char[] chars, int i, int i1) throws IOException {
        return reader.read(chars, i, i1);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
        outputStream.flush();
    }

    @Override
    public boolean isConnected() {
        return true; // Simplified
    }

    @Override
    public void write(@NotNull String string) throws IOException {
        write(string.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public int waitFor() throws InterruptedException {
        return 0; // Simplified
    }

    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }

    @Override
    public void resize(@NotNull TermSize termSize) {
        if (jlineTerminal != null) {
            jlineTerminal.setSize(
                new Size(
                    termSize.getColumns(),
                    termSize.getRows()
                )
            );
        }
    }
}
