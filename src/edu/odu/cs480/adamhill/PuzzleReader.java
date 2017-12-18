package edu.odu.cs480.adamhill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PuzzleReader {
    private String filename;

    public PuzzleReader(String filename) {
        this.filename = filename;
    }

    public Sudoku readPuzzle() {
        Sudoku sudoku = new Sudoku();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filename));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                // This next line is here because I'm not sure about the formatting of the file
                // I'm not sure if Sudoku 01 is supposed to a part of the test file or not,
                // so if the program encounters a line with alphabetic characters in it, it just skips it
                String regex = "\\d+"; // match only digits
                if (line.matches(regex)) {
                    for (int j = 0; j < line.length(); j++) {
                        sudoku.setPosition(i, j, Character.getNumericValue(line.charAt(j)));
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sudoku;
    }
}
