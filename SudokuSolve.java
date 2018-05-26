import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Produces a 9x9 Sudoku game board that a user can solve
 * 
 * @author Anthony Panisales
 *
 */
public class Sudoku {
	
	protected static String[][] board = new String[9][9];
	protected static String[][] unsolvedBoard = new String[9][9];
	protected static String[][] xBoard = new String [9][9];
	protected static int[] sudokuNums = {1, 2, 3, 4, 5, 6, 7, 8, 9};

	/**
	 * Fills the Sudoku board with numbers.
	 * 
	 * @return true if filling the board was successful, false otherwise
	 */
	public boolean fillBoard() {
		return fillBoard(0,0);
	}
	
	public boolean fillBoard(int row, int column) {
		// Base Case
		if (row == 9) {
			return true;
		} else { // Step Case
			/* Each spot in the first row has a random number
			from 1 to 9 */
			Random randomNumber = new Random();
			if (row == 0) {
				String num = Integer.toString(randomNumber.nextInt(9) + 1);
				while (!safeToPlace(row, column, num))
					num = Integer.toString(randomNumber.nextInt(9) + 1);
				board[row][column] = num;
				if (column != 8) {
					if (fillBoard(row, column+1))
						return true;
				} else {
					if (fillBoard(row+1, 0))
						return true;
				}
				return false;
			} else { // Each row after the first reacts to the rows above it
				for (int s = 0; s < sudokuNums.length; s++) {
					/* If a number is not safe to place then the number that follows
					 it in the array sudokuNums will be checked */
					if (safeToPlace(row, column, Integer.toString(sudokuNums[s]))) {
						board[row][column] = Integer.toString(sudokuNums[s]);
						if (column != 8) {
							if (fillBoard(row, column+1))
								return true;
						} else {
							if (fillBoard(row+1, 0))
								return true;
						}
					}
				}
				return false;
			}
		}
	}
	
	/**
	 * Makes a copy of the original board.
	 */
	protected void copyBoard() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++)
				unsolvedBoard[i][j] = board[i][j];
		}
	}
	
	/**
	 * Hides several numbers on the copy of the original board.
	 */
	protected void hideNums() { 
		// 17 is the minimum number of visible spots on the board
		int limit = 17, count = 81;
		Random randomNumber = new Random();
		for (int row = 0; row < 9; row++) {
			for (int column = 0; column < 9; column++) {
				int num = randomNumber.nextInt(2);
				if (num == 1 && count > limit) {
					unsolvedBoard[row][column] = "x";
					xBoard[row][column] = "x";
					count--;
				} else {
					xBoard[row][column] = "0";
				}
			}
		}
	}
	
	/** 
	 * Checks if a specific number is safe to be placed at a specific 
	 * spot on the board.
	 * 
	 * @param row
	 *          row of the specific position to check
	 * @param column
	 *          column of the specifc position to check
	 * @param num
	 *          number to check
	 *          
	 * @return true if safe to place, false otherwise
	 */
	protected boolean safeToPlace(int row, int column, String num) {
		int startRow = row - (row % 3);
		int startCol = column - (column % 3);
		
		// Check up
		for (int i = 1; i <= row; i++) {
			if (board[row-i][column].equals(num))
				return false;
		}
		
		// Check left
		for (int i = 1; i <= column; i++) {
			if (board[row][column-i].equals(num))
				return false;
		}
		
		// Check subgrid
		for (int i = startRow; i < row; i++) {
			for (int j = startCol; j < startCol+3; j++) {
				if (board[i][j].equals(num))
					return false;
			}
		}
		
		return true;
	}

	/**
	 * Prints a sudoku game board.
	 * 
	 * @param specificBoard
	 *                 Sudoku game board to print
	 */
	public void printBoard(String[][] specficBoard) {
		System.out.print(" -----------------------" + "\n" + "| ");
		for (int row = 0; row < specficBoard.length; row++) {
			for (int column = 0; column < specficBoard[0].length; column++) {
				if (column != 0 && column % 8 == 0) {
					if (row % 3 == 2) {
						System.out.print(specficBoard[row][column] + " |" + "\n");
						System.out.print(" -----------------------" + "\n" + "| ");
					} else if (row == 8) {
						System.out.print(specficBoard[row][column] + " |" + "\n" + " -----------------------");
					} else {
						System.out.print(specficBoard[row][column] + " |" + "\n" + "| ");
					}
				} else {
					System.out.print(specficBoard[row][column] + " ");
					if (column % 3 == 2)
						System.out.print("| ");
				}
			}
		}
	}
	
	/**
	 * Checks if an integer is in an integer array.
	 * 
	 * @param num
	 *            target integer to be searched for
	 * @param array
	 *            array of integers to be searched through
	 *            
	 * @return true if array contains target, false otherwise
	 */
	protected static boolean contains(int num, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (num == array[i])
				return true;
		}
		return false;
	}
	
	/**
	 * Adds a number of the user's choosing to the copy of the original board.
	 * 
	 * @param row
	 *           row to insert number to
	 * @param column
	 *           column to insert number to
	 * @param scan
	 *           scanner to obtain a number from the user
	 */
	protected void solve(String row, String column, Scanner scan) {
		int num = 0;
		while (!contains(num, sudokuNums)) {
			try {
				System.out.print("Enter a number from 1-9 in this spot: ");
				num = scan.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Error: Invalid Input");
				scan.next();
			}
		}
		unsolvedBoard[Integer.parseInt(row)-1][Integer.parseInt(column)-1] = Integer.toString(num);
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		SudokuSolve sudoku = new SudokuSolve();
		String play = "y";
		while (play.equals("y")) {
			String response1 = "";
			String response2 = "";
			sudoku.fillBoard();
			sudoku.copyBoard();
			sudoku.hideNums();
			System.out.println("Let's play sudoku!");
			while(!response1.equals("answer")) {
				try {
					sudoku.printBoard(unsolvedBoard);
					System.out.println("\n" + "Enter the row and column you want to change or "
							+ "type 'answer' to see the answer");
					System.out.println("The rows and the columns are from 1-9");
					System.out.print("Row: ");
					response1 = scan.next().toLowerCase();
					if (response1.equals("answer"))
						break;
					System.out.print("Column: ");
					response2 = scan.next().toLowerCase();
					if (response2.equals("answer"))
						break;
					if (contains(Integer.parseInt(response1), sudokuNums) && contains(Integer.parseInt(response2), sudokuNums)) {
						if (!xBoard[Integer.parseInt(response1)-1][Integer.parseInt(response2)-1].equals("x"))
							System.out.println("That spot has a fixed number");
						else
							sudoku.solve(response1, response2, scan);
					} else {
						System.out.println("No such row and/or column exists");
					}
				} catch (NumberFormatException e) {
					System.out.println("Error: Invalid input");
					continue;
				}
			}
			sudoku.printBoard(board);
			System.out.print("\n" + "Do you want to play again?(y/n) ");
			play = scan.next();
			System.out.println();
		}
		scan.close();
		System.out.println("Thanks for playing!");
	}
}
