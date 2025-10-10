package org.pintoschneider.void_of_the_unphatomable.ui.components;

import org.pintoschneider.void_of_the_unphatomable.ui.Size;
import org.pintoschneider.void_of_the_unphatomable.ui.core.Drawable;

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
        if (content.isEmpty()) return;

        final StringBuilder currentWord = new StringBuilder();
        final StringBuilder currentLineContent = new StringBuilder();
        final StringCharacterIterator iterator = new StringCharacterIterator(content);
        int currentLine = 0;

        while (iterator.current() != CharacterIterator.DONE) {
            final char c = iterator.current();

            if (c == ' ' || c == '\n') {
                // We don't have any word, so we just add the space or newline
                if (currentWord.isEmpty()) {
                    if (c == '\n') {
                        // Move to the next line on newline
                        lines.add(currentLineContent.toString());
                        currentLine++;
                        currentLineContent.setLength(0);
                    } else if (currentLineContent.length() + 1 <= maxWidth) {
                        // We add the space if it fits in the current line
                        currentLineContent.append(c);
                    }
                    iterator.next();
                    continue;
                }

                // We reached the end of a word
                if (currentLineContent.length() + currentWord.length() <= maxWidth) {
                    // The word fits in the current line, so we add it
                    currentLineContent.append(currentWord);

                    if (currentLineContent.length() + 1 <= maxWidth) {
                        // We add the space if it fits in the current line
                        currentLineContent.append(' ');
                    }
//                    } else if (c == '\n') {
//                        // Move to the next line on newline
//                        lines.add(currentLineContent.toString());
//                        currentLine++;
//                        currentLineContent.setLength(0);
//                    }
                } else {
                    // The word doesn't fit in the current line
                    if (currentWord.length() > maxWidth) {
                        // The word is too long to fit in a single line, so we use a line for the entire word and truncate it.
                        // Maybe we could hyphenate it instead?
                        lines.add(currentLineContent.toString());
                        currentLineContent.setLength(0);
                        currentLineContent.append(currentWord, 0, maxWidth - 1).append('…');
                        lines.add(currentLineContent.toString());
                        currentLineContent.setLength(0);
                        currentLine = currentLine + 2;
                    } else {
                        // Move to the next line
                        lines.add(currentLineContent.toString());
                        currentLineContent.setLength(0);
                        currentLine++;
                        currentLineContent.append(currentWord);
                    }
                }

                currentWord.setLength(0);
            } else {
                // We are still in the middle of a word
                currentWord.append(c);
            }

            iterator.next();
        }

        if (!currentLineContent.isEmpty()) {
            assert currentLineContent.length() <= maxWidth;
            lines.add(currentLineContent.toString());
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
