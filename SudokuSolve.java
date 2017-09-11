//Author: Anthony Panisales

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

//Class Purpose: Produces a 9x9 Sudoku game board
public class SudokuSolve {
	
	protected static String[][] board = new String[9][9];
	protected static String[][] unsolvedBoard = new String[9][9];
	protected static String[][] xBoard = new String [9][9];
	Random randomNumber = new Random();
	protected static int[] sudokuNums = {1, 2, 3, 4, 5, 6, 7, 8, 9};

	//Function Purpose: Fills the Sudoku board with numbers
	protected boolean fillBoard(int row, int column) {
		//Base Case
		if (row == 9) {
			return true;
		} else { //Step Case
			/*Each spot in the first row has a random number
			 * from 1 to 9
			 */
			if (row == 0) {
				String num = Integer.toString(randomNumber.nextInt(9) + 1);
				while (!safeToPlace(row, column, num)) {
					num = Integer.toString(randomNumber.nextInt(9) + 1);
				}
				if (column != 8) {
					board[row][column] = num;
					if (fillBoard(row, column+1)) {
						return true;
					}
				} else {
					board[row][column] = num;
					if (fillBoard(row+1, 0)) {
						return true;
					} 
				}
				return false;
			} else { //Each row after the first reacts to the rows above it
				for (int s = 0; s < sudokuNums.length; s++) {
					/*If a number is not safe to place then the number that follows
					 * it in the array sudokuNums will be checked
					 */
					if (safeToPlace(row, column, Integer.toString(sudokuNums[s]))) {
						board[row][column] = Integer.toString(sudokuNums[s]);
						if (column != 8) {
							if (fillBoard(row, column+1)) {
								return true;
							}
						} else {
							if (fillBoard(row+1, 0)) {
								return true;
							} 
						}
					}
				}
				return false;
			}
		}
	}
	
	/*Function Purpose: Makes a copy of the original board*/
	protected void copyBoard() {
		for (int row = 0; row < unsolvedBoard.length; row++) {
			for (int column = 0; column < unsolvedBoard.length; column++) {
				unsolvedBoard[row][column] = board[row][column];
			}
		}
	}
	
	/*Function Purpose: Hides several numbers on the copy of the
	 * original board
	 */
	protected void hideNums() { 
		for (int row = 0; row < unsolvedBoard.length; row++) {
			for (int column = 0; column < unsolvedBoard.length; column++) {
				int num = randomNumber.nextInt(2);
				if (num == 0) {
					unsolvedBoard[row][column] = "x";
					xBoard[row][column] = "x";
				} else {
					xBoard[row][column] = "0";
				}
			}
		}
	}
	
	/*Function Purpose: Checks if a specific number is safe to
	 * be placed at a specific spot on the board
	 */
	protected boolean safeToPlace(int row, int column, String num) {
		//Check up
		if (row != 0) {
			for (int i = 1; i <= row; i++) {
				if (board[row-i][column].equals(num)) {
					return false;
				}
			}
		}
		
		//Check left
		if (column != 0) {
			for (int i = 1; i <= column; i++) {
				if (board[row][column-i].equals(num)) {
					return false;
				}
			}
		}
		
		
		//Check left up diagonal
		if (column != 0 && column != 3 && column != 6) {
			if (row == 2 || row == 5 || row == 8) {
				if (column == 2 || column == 5 || column == 8) {
					for (int i = 1; i <= 2; i++) {
						for(int j = 1; j <=2; j++) {
							if (board[row-i][column-j].equals(num)) {
								return false;
							}
						}
					}
				} else {
					for (int i = 1; i <= 2; i++) {
						if (board[row-i][column-1].equals(num)) {
							return false;
						}
					}
				}
			} else if (row == 1 || row == 4 || row == 7) {
				if (column == 2 || column == 5 || column == 8) {
					for (int i = 1; i <= 2; i++) {
						if (board[row-1][column-i].equals(num)) {
							return false;
						}
					}
				} else {
					if (board[row-1][column-1].equals(num)) {
						return false;
					}
				}
			}
		}
		
		//Check right up diagonal
		if (row != 0 && column != 2 && column != 5 && column != 8) {
			if (row == 2 || row == 5 || row == 8) {
				if (column == 0 || column == 3 || column == 6) {
					for (int i = 1; i <= 2; i++) {
						for(int j = 1; j <=2; j++) {
							if (board[row-i][column+j].equals(num)) {
								return false;
							}
						}
					}
				} else {
					for (int i = 1; i <= 2; i++) {
						if (board[row-i][column+1].equals(num)) {
							return false;
						}
					}
				}			
				
			} else if (row == 1 || row == 4 || row == 7) {
				if (column == 0 || column == 3 || column == 6) {
					for (int i = 1; i <= 2; i++) {
						if (board[row-1][column+i].equals(num)) {
							return false;
						}
					}
				} else {
					if (board[row-1][column+1].equals(num)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	//Function Purpose: Prints the solved sudoku game board
	protected void printBoard(String[][] specficBoard) {
		System.out.print(" -----------------------" + "\n" + "| ");
		for (int row = 0; row < specficBoard.length; row++) {
			for (int column = 0; column < specficBoard[0].length; column++) {
				if (column != 0 && column%8 == 0) {
					if (row == 2 || row == 5) {
						System.out.print(specficBoard[row][column] + " |" + "\n");
						System.out.print(" -----------------------" + "\n" + "| ");
					} else if (row == 8) {
						System.out.print(specficBoard[row][column] + " |" + "\n" + " -----------------------");
					} else {
						System.out.print(specficBoard[row][column] + " |" + "\n" + "| ");
					}
				} else {
					System.out.print(specficBoard[row][column] + " ");
					if (column == 2 || column == 5) {
						System.out.print("| ");
					}
				}
			}
		}
	}
	
	/*Function Purpose: Checks if an element is in an array*/
	protected static boolean contains(int num, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (num == array[i]) {
				return true;
			}
		}
		return false;
	}
	
	/*Function Purpose: Adds a number of the user's choosing
	 * to the copy of the original board*/
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
			sudoku.fillBoard(0, 0);
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
					if (response1.equals("answer")) {
						break;
					}
					System.out.print("Column: ");
					response2 = scan.next().toLowerCase();
					if (response2.equals("answer")) {
						break;
					}
					if (contains(Integer.parseInt(response1), sudokuNums) && contains(Integer.parseInt(response2), sudokuNums)) {
						if (!xBoard[Integer.parseInt(response1)-1][Integer.parseInt(response2)-1].equals("x")) {
							System.out.println("That spot has a fixed number");
						} else {
							sudoku.solve(response1, response2, scan);
						}
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
