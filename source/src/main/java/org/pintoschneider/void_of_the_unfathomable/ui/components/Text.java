package org.pintoschneider.void_of_the_unfathomable.ui.components;

import org.pintoschneider.void_of_the_unfathomable.ui.Size;
import org.pintoschneider.void_of_the_unfathomable.ui.core.Drawable;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

public class Text extends Drawable {
    private final String content;
    ArrayList<String> lines = new ArrayList<>();

    public Text(String content) {
        this.content = content;
    }

    @Override
    public void layout(Size maximumSize) {
        computeLines(content, maximumSize.getWidth());
        final int width = Math.max(content.length(), maximumSize.getWidth());
        size = new Size(width, Math.min(lines.size(), maximumSize.getHeight()));
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
    public Character draw(int x, int y) {
        if (y >= lines.size()) return null;
        final String line = lines.get(y);
        if (x >= line.length()) return null;
        return line.charAt(x);
    }
}
