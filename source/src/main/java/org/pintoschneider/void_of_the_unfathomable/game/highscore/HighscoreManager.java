package org.pintoschneider.void_of_the_unfathomable.game.highscore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * A manager for handling high-scores.
 * Handles main methods and save file management.
 */
public class HighscoreManager implements AutoCloseable {
    private final File file = new File("highscores.schneidersave");
    private final FileWriter fileWriter = new FileWriter(file, true);
    private final Scanner scanner = new Scanner(file);
    private final List<HighscoreEntry> entries = new ArrayList<>();

    public HighscoreManager() throws IOException {
        readFromFile();
    }

    /**
     * Gets the list of high-score entries.
     *
     * @return The sorted list of high-score entries.
     */
    public List<HighscoreEntry> entries() {
        return entries;
    }

    /**
     * Adds a high-score entry to the list of high-score entries.
     * Appends the entry into a file using the CSV format.
     *
     * @param entry The high-score entry.
     */
    public void addHighscore(HighscoreEntry entry) {
        try {
            entries.add(entry);
            quickSort(entries, 0, entries.size() - 1);
            fileWriter.write("%s,%s,%s%n".formatted(entry.status(), entry.percentage(), entry.turnCount()));
            fileWriter.flush();
        } catch (IOException exception) {
            System.err.println("Error when writing to file.");
            System.err.println(exception.getMessage());
            System.err.println(Arrays.toString(exception.getStackTrace()));
        }
    }

    /**
     * Reads all high-score entries stored in a file.
     */
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

        quickSort(entries, 0, entries.size() - 1);
    }

    /**
     * Sorts a list using QuickSort with an efficiency of (n log n).
     *
     * @param list The list of high-score entries to be sorted.
     * @param low  The start of the list.
     * @param high The end of the list.
     */
    private void quickSort(List<HighscoreEntry> list, int low, int high) {
        if (low < high) {
            int pivot = partition(list, low, high);
            quickSort(list, low, pivot - 1);
            quickSort(list, pivot + 1, high);
        }
    }

    /**
     * QuickSort helper method that handles the partitioning and comparisons of the QuickSort.
     * <p>
     * The list is ordered first by RunStatus priority, then percentage completed and then least amount of turns.
     *
     * @param list The list of high-score entries to be sorted.
     * @param low  The start of the list.
     * @param high The end of the list.
     */
    private int partition(List<HighscoreEntry> list, int low, int high) {
        HighscoreEntry aux;
        HighscoreEntry pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).status().priority() < pivot.status().priority()) {
                i = i + 1;
                aux = list.get(i);
                list.set(i, list.get(j));
                list.set(j, aux);
            }
            if (list.get(j).status().priority() == pivot.status().priority() && (list.get(j).percentage() > pivot.percentage())) {
                i = i + 1;
                aux = list.get(i);
                list.set(i, list.get(j));
                list.set(j, aux);

                if (list.get(j).percentage() == pivot.percentage() && (list.get(j).turnCount() < pivot.turnCount())) {
                    i = i + 1;
                    aux = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, aux);

                }
            }
        }
        aux = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, aux);
        return i + 1;
    }


    @Override
    public void close() throws IOException {
        fileWriter.close();
        scanner.close();
    }
}
