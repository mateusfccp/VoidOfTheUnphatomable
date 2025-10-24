package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.core.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Canvas;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Component;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Constraints;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Paint;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

/**
 * A component that displays text content.
 * <p>
 * The component automatically handles line wrapping based on the available width. If a word exceeds the maximum width,
 * it will be truncated and an ellipsis ("…") will be added at the end of the line.
 * <p>
 * The text is drawn using the specified {@link Paint} style.
 */
public class Text extends Component {
    private final String content;
    private final Paint paint;
    private final ArrayList<String> lines = new ArrayList<>();

    /**
     * Constructs a {@link Text} component with the given content.
     *
     * @param content The text content to display.
     */
    public Text(String content) {
        this.content = content;
        this.paint = new Paint();
    }

    /**
     * Constructs a {@link Text} component with the given content and paint style.
     *
     * @param content The text content to display.
     * @param paint   The paint style to use for rendering the text.
     */
    public Text(String content, Paint paint) {
        this.content = content;
        this.paint = paint;
    }

    @Override
    public void layout(Constraints constraints) {
        final Size maximumSize = constraints.biggest();
        computeLines(content, maximumSize.width());
        final int width = Math.min(content.length(), maximumSize.width());
        size = new Size(width, Math.min(lines.size(), maximumSize.height()));
    }

    private void computeLines(String content, int maxWidth) {
        lines.clear();

        if (content.isEmpty() || maxWidth <= 0) return;

        final StringBuilder currentWord = new StringBuilder();
        final StringBuilder currentLine = new StringBuilder();
        final StringCharacterIterator iterator = new StringCharacterIterator(content);

        while (true) {
            final char c = iterator.current();

            if (c == ' ' || c == '\n' || c == CharacterIterator.DONE) {
                // We reached the end of a word
                if (currentLine.length() + currentWord.length() <= maxWidth) {
                    // The word fits in the current line, so we add it
                    currentLine.append(currentWord);
                } else if (currentWord.length() > maxWidth) {
                    // The word is too long to fit in a single line, so we use a line for the entire word and truncate it.
                    // Maybe we could hyphenate it instead?
                    addLine(currentLine);
                    currentLine.append(currentWord, 0, maxWidth - 1).append('…');

                    addLine(currentLine);
                } else {
                    // Move to the next line
                    addLine(currentLine);
                    currentLine.append(currentWord);
                }

                currentWord.setLength(0);

                if (c == ' ' && currentLine.length() < maxWidth) {
                    currentLine.append(c);
                } else if (c == '\n') {
                    lines.add(currentLine.toString());
                    currentLine.setLength(0);
                }
                if (c == CharacterIterator.DONE) {
                    break;
                }
            } else {
                // We are still in the middle of a word
                currentWord.append(c);

                if (currentLine.length() + currentWord.length() > maxWidth) {
                    // Move current word to next line
                    addLine(currentLine);
                }
            }

            iterator.next();
        }

        lines.add(currentLine.toString());
    }

    void addLine(StringBuilder currentLine) {
        if (!currentLine.toString().trim().isEmpty()) {
            lines.add(currentLine.toString());
            currentLine.setLength(0);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (int y = 0; y < lines.size(); y++) {
            final String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                canvas.draw(line.charAt(x), x, y, paint);
            }
        }
    }
}
