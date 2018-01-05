package edu.odu.cs480.adamhill;

import java.util.ArrayList;
import java.util.List;

public class Sudoku {
    private int[][] puzzle;
    private static final int MAX_SIZE = 9;

    public Sudoku() {
        puzzle = new int[MAX_SIZE][MAX_SIZE];
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public void setPosition(int i, int j, int number) {
        puzzle[i][j] = number;
    }

    /**
     * A naive backtracking algorithm to solve a Sudoku puzzle
     * @param row The current row
     * @param col The current column
     * @return True if a solution has been found. False otherwise
     */
    public boolean solve(int row, int col) {
        // If we've gone through all the positions in the puzzle, start backtracking
        if (row == 9) {
            return true;
        }

        // Check the current position to see if it has been set
        if (puzzle[row][col] != 0) {
            if (col == 8) {
                return solve(row + 1, (col + 1) % 9);
            } else {
                return solve(row, (col + 1) % 9);
            }
        }

        // Get a list of valid numbers from this position
        List<Integer> validNumbers = findValidNumbers(row, col);

        // plug in each valid number one at a time and then advance to the next open position
        for (int n : validNumbers) {
            puzzle[row][col] = n;
            if (col == 8) {
                if (solve(row + 1, (col + 1) % 9)) {
                    return true;
                }
            } else {
                if (solve(row, (col + 1) % 9)) {
                    return true;
                }
            }
        }

        // Reset on backtrack
        puzzle[row][col] = 0;

        return false;
    }

    /**
     * A minimum remaining value backtracking algorithm
     * @param row The current row
     * @param col The current column
     * @return True if a solution has been found. False otherwise
     */
    public boolean solveMRV(int row, int col) {
        if (!zeroSpaces() && row == -1 && col == -1) {
            //found a solution
            return true;
        }

        if (row == -1 && col == -1) {
            return false; // no solution was found
        }

        // Get a list of valid numbers from this position
        List<Integer> validNumbers = findValidNumbers(row, col);
        if (validNumbers.size() == 0) {
            return false; // no valid numbers. start backtracking
        } else {
            // plug in each valid number one at a time and check to see if we have a solution
            for (int num : validNumbers) {
                puzzle[row][col] = num;
                Coordinate minimum = findMRV(); // find the next position with the minimum remaining values
                if (solveMRV(minimum.getRow(), minimum.getCol())) {
                    return true;
                }
            }
        }

        // reset on backtrack
        puzzle[row][col] = 0;
        return false;
    }

    /**
     * Check to see if any remaining spaces are zeroes.
     * @return True if there is at least one zero. False otherwise.
     */
    private boolean zeroSpaces() {
        for (int i = 0; i < MAX_SIZE; i++) {
            for (int j = 0; j < MAX_SIZE; j++) {
                if (puzzle[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds all the numbers that can be inserted into a given row and column.
     * Checks if numbers are present in the row, column, and 3x3 grid.
     * @param startRow The row to check
     * @param startCol The column to check
     * @return A List of integers representing the valid values.
     */
    private List<Integer> findValidNumbers(int startRow, int startCol) {
        List<Integer> validNumbers = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            if (!containedInRow(startRow, i) && !containedInCol(startCol, i) && !containedInGrid(startRow, startCol, i)) {
                validNumbers.add(i);
            }
        }

        return validNumbers;
    }

    /**
     * Checks to see if a number is in a row.
     * @param row The current row
     * @param number the value to be checked
     * @return True if the number is in the row; false otherwise
     */
    private boolean containedInRow(int row, int number) {
        for (int j = 0; j < MAX_SIZE; j++) {
            if (puzzle[row][j] == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if a number is in a column.
     * @param col The current column
     * @param number The value to be checked
     * @return True if the number is in the row; false otherwise
     */
    private boolean containedInCol(int col, int number) {
        for (int i = 0; i < MAX_SIZE; i++) {
            if (puzzle[i][col] == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if a number is in its 3x3 grid
     * @param row The current row
     * @param col The current column
     * @param number The value to be checked
     * @return True if the number is in the box; false otherwise
     */
    private boolean containedInGrid(int row, int col, int number) {
        int startingRow = (row / 3) * 3;
        int startingCol = (col / 3) * 3;

        for (int i = startingRow; i < startingRow + 3; i++) {
            for (int j = startingCol; j < startingCol + 3; j++) {
                if (puzzle[i][j] == number) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns a row/col pair representing the space which has the fewest legal moves remaining
     * @return A row and a column which has the fewest remaining values. (-1, -1) if there are no choices left
     */
    public Coordinate findMRV() {
        Coordinate coord = new Coordinate(0, 0);
        int minMoves = Integer.MAX_VALUE;

        for (int i = 0; i < MAX_SIZE; i++) {
            for (int j = 0; j < MAX_SIZE; j++) {
                if (puzzle[i][j] == 0) {
                    List<Integer> numbers = findValidNumbers(i, j);
                    if (numbers.size() < minMoves && numbers.size() > 0) {
                        //System.out.println("Found fewer moves at " + i + ", " + j + ": " + numbers.size());
                        coord.setCoordinate(i, j);
                        minMoves = numbers.size();
                    }
                }
            }
        }
        //System.out.println("Minimum pos: " + coord.getRow() + ", " + coord.getCol() + ": " + minMoves);
        // If we found a spot with the fewest moves, return it
        if (minMoves != Integer.MAX_VALUE) {
            return coord;
        }
        // Otherwise, return (-1, -1)
        return new Coordinate(-1, -1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean hasAppendedLine = false;
        sb.append("|-----------------------|\n");
        for (int i = 0; i < MAX_SIZE; i++) {
            for (int j = 0; j < MAX_SIZE; j++) {
                if ((i == 3 || i == 6) && !hasAppendedLine) {
                    sb.append("|-----------------------|\n");
                    hasAppendedLine = true;
                }
                if (i == 4) {
                    hasAppendedLine = false; // reset the flag to allow the line to be drawn again on i = 6
                }
                if (j == 0) {
                    sb.append('|');
                } else if (j % 3 == 0) {
                    sb.append(" |");
                }
                sb.append(' ').append(puzzle[i][j]);
                if (j == MAX_SIZE - 1) {
                    sb.append(" |");
                }
            }
            if (i < MAX_SIZE - 1) {
                sb.append('\n');
            }
        }
        sb.append("\n|-----------------------|");
        return sb.toString();
    }
}
