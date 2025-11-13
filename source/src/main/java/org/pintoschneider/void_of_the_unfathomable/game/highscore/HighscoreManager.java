package org.pintoschneider.void_of_the_unfathomable.game.highscore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HighscoreManager implements AutoCloseable {
    final File file = new File("highscores.schneidersave");
    final FileWriter fileWriter = new FileWriter(file, true);
    final Scanner scanner = new Scanner(file);
    final List<HighscoreEntry> entries = new ArrayList<>();

    public HighscoreManager() throws IOException {
        readFromFile();
    }

    List<HighscoreEntry> entries() {
        return entries;
    }

    public void addHighscore(HighscoreEntry entry) {
        try {
            entries.add(entry);
            fileWriter.write("%s,%s,%s%n".formatted(entry.status(), entry.percentage(), entry.turnCount()));
            fileWriter.flush();
        } catch (IOException exception) {
            System.err.println("Error when writing to file.");
            System.err.println(exception.getMessage());
            System.err.println(Arrays.toString(exception.getStackTrace()));
        }
    }

    private void readFromFile() {
        try {
            Files.createFile(file.toPath());
        } catch (IOException _) {}

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");

            entries.add(
                new HighscoreEntry(
                    RunStatus.valueOf(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])
                )
            );
        }
    }

    @Override
    public void close() throws IOException {
        fileWriter.close();
        scanner.close();
    }
}
