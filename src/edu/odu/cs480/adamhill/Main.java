package edu.odu.cs480.adamhill;

public class Main {

    public static void main(String[] args) {
	    Sudoku sudoku = new Sudoku();
	    PuzzleReader pr = new PuzzleReader("evil3.txt");
	    sudoku = pr.readPuzzle();
	    System.out.println(sudoku);

	    System.out.println("\n");

	    long startTime = System.nanoTime();
		sudoku.solve(0, 0);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000; //divide by 1000000 to get milliseconds
		System.out.println("Backtracking solution found in " + duration + " ms.");
		System.out.println(sudoku);

		System.out.println("\n");

		startTime = System.nanoTime();
		sudoku = pr.readPuzzle();
		Coordinate min = sudoku.findMRV();
		sudoku.solveMRV(min.getRow(), min.getCol());
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000; //divide by 1000000 to get milliseconds
		System.out.println("MRV solution found in " + duration + " ms.");
		System.out.println(sudoku);
    }
}
