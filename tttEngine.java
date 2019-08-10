package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class tttEngine {

	static int player, currentPlayer;
	static int FstPlayer = 1, SndPlayer = 2, winner, Bsize;
	static int role, score1 = 0, score2 = 0;
	static String color, mark, msg;
	private static int[][] board;
	private int currentPlayerMark;
	private static int game_mode; // 1 = Person ver Person, 2 = Person ver Computer
	private int level;
	int row = 0, col = 1; // need that for maping matrix index to figure of graph
	int c = 0, countSelf, countOp, criticalWeight, Alpha, Beta;
	static int clickC;
	static int clickP;
	Rectangle[] r;
	static int[] cc = new int[Bsize];
	static Text resultMsg, Mark;
	HBox ResultWindow;

	// Contractor
	/**
	 * This is game engine which set game setting and profile All that information
	 * come from game profile class and game driver - Define number of rectangles
	 * that used as grid or cell for game board - Define size of board needed for
	 * game - Define game mode (person to person, or person to computer) - Define
	 * game level (easy (1) , medium (2), hard (3)) - After define all that then
	 * initializing game board
	 * 
	 * @param r
	 */
	public tttEngine(Rectangle[] r) {

		this.r = r;
		Bsize = 5;
		board = new int[Bsize][Bsize];
		game_mode = 2; // The game mode is to be requested from user profile
		level = 1; // request from profile

		initializeBoard();
	}

	/**
	 * Package A: Game Setting and Checking
	 * 
	 * This package contains many methods that related to initialize and print the
	 * board of game, also all checker methods to check if board is full or not and
	 * check for win (row,column,diagonal) player checker (whose turn is to play?!)
	 * and step taken check. Also calculate the result of game.
	 * 
	 * 1. initializeBoard() : initialize the game board and related parameters 2.
	 * printBoard() : print present board of game 3. isBoardFull() : check if board
	 * is full or not 4. checkForWin() : check if there is win case presented for
	 * current time a. checkRowsForWin() :check rows if win b. checkColumnsForWin()
	 * :check columns if win c. checkDiagonalsForWin() :check diagonals if win 5.
	 * PlayerTurn() : check for current player turn and related mark 6.
	 * stepChecker() : check the current action take by player 7. result() :
	 * calculate the result of game
	 * 
	 */

	// Set/Reset the board for new game.
	public void initializeBoard() {

		/**
		 * Initialize game parameters
		 */
		Alpha = 0; // the profit value for computer action
		Beta = 0; // the cost value on computer due to person action
		criticalWeight = Bsize - 1;

		/**
		 * Initialize game players
		 */

		// Easy level
		if (level == 1) {
			currentPlayer = FstPlayer;
			clickP = 1;
		}
		// Medium level
		if (level == 2) {
			currentPlayer = SndPlayer;
			clickC = 1;
		}

		/**
		 * Initialize game board
		 */
		// Loop through rows
		for (int i = 0; i < Bsize; i++) {

			// Loop through columns
			for (int j = 0; j < Bsize; j++) {
				board[i][j] = 0;
			}
		}
	}

	// Print the current board (this for underground game not user interface)
	public void printBoard() {
		System.out.println("-------------");

		for (int i = 0; i < Bsize; i++) {
			System.out.print("| ");
			for (int j = 0; j < Bsize; j++) {
				System.out.print(board[i][j] + " | ");
			}
			System.out.println();
			System.out.println("-------------");

		}
	}

	// check if the board is full or not
	public static boolean isBoardFull() {

		boolean isFull = true;
		for (int i = 0; i < Bsize; i++) {
			for (int j = 0; j < Bsize; j++) {
				if (board[i][j] == 0) {
					return false;
				}
			}
		}

		return isFull;
	}

	/*
	 * Returns true if there is a win, false otherwise. This calls our other win
	 * check functions to check the entire board.
	 */
	public static boolean checkForWin() {
		return (checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin());

	}

	/*
	 * Loop through rows and see if any are winners.
	 */
	private static boolean checkRowsForWin() {

		int[] c = new int[Bsize];

		for (int i = 0; i < Bsize; i++) {
			for (int j = 0; j < Bsize; j++) {
				c[j] = board[i][j];

			}
			if (checkRowCol(c) == true) {

				return true;
			}

		}
		return false;
	}

	/*
	 * Loop through columns and see if any are winners.
	 */
	private static boolean checkColumnsForWin() {

		int[] c = new int[Bsize];

		for (int i = 0; i < Bsize; i++) {

			for (int j = 0; j < Bsize; j++) {
				c[j] = board[j][i];

			}

			if (checkRowCol(c) == true) {

				return true;
			}

		}
		return false;
	}

	/*
	 * Check the two diagonals to see if either is a win. Return true if either
	 * wins.
	 */

	private static boolean checkDiagonalsForWin() {

		int[] D1 = new int[Bsize];
		int[] D2 = new int[Bsize];
		int counter = Bsize - 1;

		for (int i = 0; i < Bsize; i++) {

			D1[i] = board[i][i];
			D2[i] = board[i][counter];
			counter--;
		}
		if (checkRowCol(D1) == true) {

			return true;
		}

		else if (checkRowCol(D2) == true) {

			return true;
		}

		return false;
	}

	/*
	 * Check to see if all three values are the same (and not empty) indicating a
	 * wins.
	 */

	private static boolean checkRowCol(int[] c) {

		boolean Equal = false;
		int tic = 0;
		int counter = 1;

		for (int i = 0; i < Bsize - 1; i++) {

			if ((c[i] != 0 && c[i] == c[counter]) == true) {

				tic = tic + 1;
			}
			counter++;
		}
		if (tic == Bsize - 1)
			Equal = true;

		return Equal;

	}

	/*
	 * Change player back and forth.
	 */
	public static void PlayerTurn() {

		if (checkForWin() == false && isBoardFull() == false) {
			if (currentPlayer == FstPlayer) {
				currentPlayer = SndPlayer;
				clickC = 1;
				clickP = 0;
			} else {
				currentPlayer = FstPlayer;
				clickC = 0;
				clickP = 1;
			}

			if (game_mode == 1) {
				clickP = 1;
				clickC = 0;

			}
		}
	}

	/*
	 * This method run three methods to test the step of player if s/he win or not
	 * and if game draw or still going on then print final result
	 */
	public boolean stepChecker() {

		PlayerTurn();
		if (isBoardFull() && checkForWin() == false) {

			msg = "The game drawed!!!!!!!!";
			System.out.println(msg);
			resultMsg = new Text(msg);

			return false;

		}
		if (checkForWin() == true) {

			winner = currentPlayer;
			msg = winner + " win";
			System.out.println(msg);
			resultMsg = new Text(msg);

			return false;
		}

		return true;

	}

	public static void result() {
		if (score1 > score2) {
			System.out.println("The Winner is " + FstPlayer);
			if (score1 < score2)
				System.out.println("The Winner is " + SndPlayer);
		} else
			System.out.println("No Winner the game tie and draw");

	}

	/**
	 * Package B: Take Players Actions This package contains all helper methods need
	 * to take player action or mark in the game. 1. playerMark(): this helper
	 * method that define current player mark 2. map(int index): this helper method
	 * map from board to grid index 3. placeMark(int row, int col) : this helper
	 * method need to place mark of player a. placeMarkPerson(int row, int col, int
	 * player) b. placeMarkComputer(int player)
	 * 
	 *
	 */

	/*
	 * Change player marks back and forth. for matrix use to see what is going and
	 * check for winner
	 */
	public void playerMark() {

		if (currentPlayer == FstPlayer) {
			color = "#ed4b00";
			mark = "X";
		} else {
			color = "#008000";
			mark = "O";
		}

		// Mark.setFont(Font.font(72));

	}

	/*
	 * Places a mark int the specified cell by row and col with the mark of the
	 * current player
	 */
	public boolean map(int index) {

		int counter = 0;
		for (int i = 0; i < Bsize; i++) {
			for (int j = 0; j < Bsize; j++) {

				if (counter == index) {

					if (board[i][j] == 0) {
						board[i][j] = currentPlayer;
						return true;

					}
				}
				counter++;
			}
		}
		return false;
	}

	/*
	 * The job of this method is place mark of current player in the board and grid
	 */
	public boolean placeMark(int row, int col) {

		int counter = 0;
		playerMark();

		for (int i = 0; i < Bsize; i++) {
			for (int j = 0; j < Bsize; j++) {

				if (row >= r[counter].getX() && row <= (r[counter].getX() + r[counter].getWidth())
						&& col >= r[counter].getY() && col <= (r[counter].getY() + r[counter].getHeight())) {

					if (r[counter].getFill() == Color.WHITE) {

						r[counter].setFill(Color.web(color));
					} /**
						 * Make sure that row and column are in bounds of the board. this board use
						 * background that calculate the game and how is going on. this for board-matrix
						 */
					if (board[i][j] == 0) { // place on board and make sure allowed
						board[i][j] = currentPlayer;
						return true;
					}
				}
				counter++;
			}
		}

		return false;
	}

	/*
	 * place mark for person
	 */

	public boolean placeMarkPerson(int row, int col, int player) {

		if (game_mode == 2 && player == 2)
			return false;
		else
			return placeMark(row, col);

	}

	/*
	 * place mark for computer (need for game mode person to computer )
	 */

	public boolean placeMarkComputer(int player) {

		if (game_mode == 1 || player == 1) // if mode 1 activated this part will be false always
			return false;

		// ** define if mode of game is P2C and computer turn to play (computer first)

		if (game_mode == 2 && currentPlayer == SndPlayer) {

			/*
			 * computer take action in his turn based on Build-Block algorithm
			 */
			nextMove();

		}

		return true;

	}

	public void compRec(int x, int y) {

		playerMark();
		int counter = 0;

		for (int i = 0; i < Bsize; i++) {
			for (int j = 0; j < Bsize; j++) {

				if (i == x && j == y) {
					r[counter].setFill(Color.web(color));
				}
				counter++;
			}
		}

	}

	/**
	 * Package C: Game Algorithm
	 * 
	 * This package contains all methods related to game algorithm which called
	 * "Build-Block Algorithm". The core operation of this algorithm is A*-algorithm
	 * but modified it. The strategies for this algorithm is - Check priority of
	 * building and blocking - Reduce game tree by open-game strategy - Build a win
	 * by dynamic go through rows,columns, diagonals
	 * 
	 * 1. nextMove(): This major algorithm method and other are helper 2. Build():
	 * This method help to build a win for computer 3. checkRowWeight(): This
	 * priority checker for build or block in rows 4. checkColWeight(): This
	 * priority checker for build or block in columns 5. checkDiagonalWeight(): This
	 * priority checker for build or block in diagonals
	 * 
	 */
	public boolean nextMove() {

		/**
		 * Description of next move steps:
		 * 
		 * High priority Build-Block Steps always next move start first of all to
		 * priority mode then when informed there is not high priority action need jump
		 * to open mode action. the open game is based on winning strategies in
		 * tic-tac-toe. If all important position conquered, then algorithm jump to
		 * dynamic building mode and general mode.
		 * 
		 */

		boolean priorityMode = true; // define priority parameters
		/*
		 * High priority state (mode)
		 * 		this section calculate the heuristic functions (f(alpha),f(beta))
		 */
		if (priorityMode == true) {

			checkRowWeight(); // generalized form of method (status:Working)
			checkColWeight();// generalized form of method (status:Working)
			checkDiagonalWeight();

			priorityMode = false;
		}

		/*
		 * Open-game State (Build only)
		 */

		if (priorityMode == false) {
			if (clickC == 1) { // this generalize form (status:General move Working)

				if (board[0][0] == 0) {
					board[0][0] = currentPlayer;
					compRec(0, 0);// computer selection on board

				} else if (board[0][Bsize - 1] == 0) {
					board[0][Bsize - 1] = currentPlayer;
					compRec(0, Bsize - 1);// computer selection on board
				} else if (board[(Bsize - 1) / 2][(Bsize - 1) / 2] == 0) {
					board[(Bsize - 1) / 2][(Bsize - 1) / 2] = currentPlayer;
					compRec((Bsize - 1) / 2, (Bsize - 1) / 2);// computer selection on board
				} else if (board[Bsize - 1][0] == 0) {
					board[Bsize - 1][0] = currentPlayer;
					compRec(Bsize - 1, 0);// computer selection on board
				} else if (board[Bsize - 1][Bsize - 1] == 0) {
					board[Bsize - 1][Bsize - 1] = currentPlayer;
					compRec(Bsize - 1, Bsize - 1);// computer selection on board
				}

				/*
				 * Dynamic move state(mode) -try to build the best win line (diagonal, row,
				 * columns) -try to go generally in case no win only draw
				 */

				else {

					Build_win(); // this method response dynamically
					/*
					 *  block or general move
					 */
					if (clickC == 1) {
						for (int i = 0; i < Bsize; i++) {
							for (int j = 0; j < Bsize; j++) {

								if (board[i][j] == 0 && board[i][j] != 1) {
									board[i][j] = currentPlayer;
									compRec(i, j);// computer selection on board

									return true; // this let go one step only now whole loop

								}
							}
						}
					}
				}
			}

			c++;
			// test action
			clickC = 0;
			return true;

		}

		return false;

	}

	/**
	 * This method try to build win be choosing best play position which change 
	 * dynamically by calculating the cost of previous and current action
	 * which let know where will be the highest priority to through
	 */
	public boolean Build_win() {

		int col_counter = 0, row_counter = 0, dig_counter = Bsize - 1;
		boolean tic_row = false, tic_col = false, tic_dig = false;
		int[] go_row = new int[Bsize];
		int[] go_col = new int[Bsize];
		int val_col = 0, val_row = 0, val_dig = 0;
		int col_priority = 0, row_priority = 0, dig1_priority = 0, dig2_priority = 0;

		// ** Building move

		/*
		 *  go diagonals
		 */

		for (int i = 0; i < Bsize; i++) {

			if (board[i][i] == 1) {

				dig1_priority = -1;
			}
			if (board[i][dig_counter] == 1) {

				dig2_priority = -1;
			}
			dig_counter--;

			val_dig = val_dig + 2;
		}

		dig_counter = Bsize - 1;

		for (int i = 0; i < Bsize; i++) {
			for (int j = 0; j < Bsize; j++) {

				/*
				 * go columns
				 */

				if (board[j][i] == 1) {

					col_priority = -1;
					go_col[i] = col_priority;

				} else if (board[j][i] == 2 && col_priority != -1) {

					col_priority = col_priority + 2;
					go_col[i] = col_priority;
				} else if (board[j][i] == 0 && col_priority != -1) {

					col_priority = col_priority + 1;
					go_col[i] = col_priority;
				}

				/*
				 *  go rows
				 */

				if (board[i][j] == 1) {

					row_priority = -1;
					go_row[i] = row_priority;

				} else if (board[i][j] == 2 && row_priority != -1) {

					row_priority = row_priority + 2;
					go_row[i] = row_priority;
				} else if (board[i][j] == 0 && row_priority != -1) {
					row_priority++;
					go_row[i] = row_priority;
				}

			}

			row_priority = 0;
			col_priority = 0;
		}

		/*
		 * calculation priorities (Diagonals,Columns,Rows) 
		 */

		for (int m = 0; m < Bsize; m++) {

			// System.out.println("vCol= " + go_col[m] + " " + "vRow= " + go_row[m]);

			if (go_col[m] > val_col) {
				col_counter = m;
				val_col = go_col[m];

			}
			if (go_row[m] > val_row) {
				row_counter = m;
				val_row = go_row[m];

			}

		}
		if (val_col >= val_row && val_col > val_dig) {

			tic_col = true;
			tic_row = false;
			tic_dig = false;
		} else if (val_dig >= val_row && val_dig >= val_col) {
			tic_col = false;
			tic_row = false;
			tic_dig = true;
		} else {

			tic_col = false;
			tic_row = true;
			tic_dig = false;
		}

		for (int k = 0; k < Bsize; k++) {

			// ** which should be choose (diagonal, column, row) for next move
			/*
			 * Build win through column line
			 */
			if (tic_col == true) {
				if (board[k][col_counter] == 0 && board[k][col_counter] != 1) {
					board[k][col_counter] = currentPlayer;
					compRec(k, col_counter);// computer selection on board
					c++;
					// test action
					clickC = 0;

					return true; // this let go one step only now whole loop

				}
			}

			/*
			 * Build win through row line
			 */
			if (tic_row == true) {
				if (board[row_counter][k] == 0 && board[row_counter][k] != 1) {
					board[row_counter][k] = currentPlayer;
					compRec(row_counter, k);// computer selection on board
					c++;
					// test action
					clickC = 0;
					return true; // this let go one step only now whole loop

				}
			}
		}

		/*
		 * Build win through diagonal line
		 */
		if (tic_dig == true && centerCase() == false) {

			if (board[0][0] != 1 && board[Bsize - 1][Bsize - 1] != 1 && dig1_priority != -1) {

				for (int i = 0; i < Bsize; i++) {

					if (board[i][i] == 0 && board[i][i] != 1) {
						board[i][i] = currentPlayer;
						compRec(i, i);// computer selection on board
						c++;
						// test action
						clickC = 0;
						return true; // this let go one step only now whole loop
					}
				}

			}
			if (board[0][Bsize - 1] != 1 && board[Bsize - 1][0] != 1 && dig2_priority != -1) {

				for (int i = 0; i < Bsize; i++) {

					if (board[i][dig_counter] == 0 && board[i][dig_counter] != 1) {
						board[i][dig_counter] = currentPlayer;
						compRec(i, dig_counter);// computer selection on board
						c++;
						// test action
						clickC = 0;
						return true; // this let go one step only now whole loop
					}

					dig_counter--;
				}

			}

		}

		return false;
	}

	public boolean checkRowWeight() {

		if (clickC == 1) {

			for (int i = 0; i < Bsize; i++) {
				for (int j = 0; j < Bsize; j++) {

					if (board[i][j] == 2)
						Alpha++; // computer
					if (board[i][j] == 1)
						Beta++; // person

					if (Alpha == criticalWeight) {

						for (int m = 0; m < Bsize; m++) {

							if (board[i][m] == 0) {

								board[i][m] = currentPlayer;

								compRec(i, m);// computer selection on board
								Alpha = 0;
								clickC = 0;

								return true;
							}

						}
					} else if (Beta == criticalWeight) {

						for (int m = 0; m < Bsize; m++) {

							if (board[i][m] == 0) {
								board[i][m] = currentPlayer;
								compRec(i, m);// computer selection on board
								Beta = 0;
								clickC = 0;
								return true;
							}
						}
					}

				}

				Alpha = 0;
				Beta = 0;
			}

		}
		return false;

	}

	public boolean checkColWeight() {

		if (clickC == 1) {

			for (int i = 0; i < Bsize; i++) {
				for (int j = 0; j < Bsize; j++) {

					if (board[j][i] == 2)
						Alpha++; // computer alpha-cost (profit)
					if (board[j][i] == 1)
						Beta++; // person beta-cost (cost)

					if (Alpha == criticalWeight) {

						for (int m = 0; m < Bsize; m++) {

							if (board[m][i] == 0) {

								board[m][i] = currentPlayer;
								compRec(m, i);// computer selection on board
								Alpha = 0;
								clickC = 0;

								return true;
							}

						}
					} else if (Beta == criticalWeight) {

						for (int m = 0; m < Bsize; m++) {

							if (board[m][i] == 0) {
								board[m][i] = currentPlayer;
								compRec(m, i);// computer selection on board
								Beta = 0;
								clickC = 0;
								return true;
							}
						}
					}

				}

				Alpha = 0;
				Beta = 0;
			}

		}
		return false;
	}

	public boolean checkDiagonalWeight() {

		/*
		 * only need to focus on one diagonal, the other one is killed by open game
		 */

		if (clickC == 1) {

			for (int i = 0; i < Bsize; i++) {

				if (board[i][i] == 2)
					Alpha++;

				if (board[i][i] == 1)
					Beta++;
				// ** could add calculation for other diagonal
				// ** but that will be additional code only

			}
			System.out.println(Beta + "  " + criticalWeight);

			if (Alpha == criticalWeight) {
				for (int m = 0; m < Bsize; m++) {
					if (board[m][m] == 0) {

						board[m][m] = currentPlayer;
						compRec(m, m);// computer selection on board
						Alpha = 0;
						clickC = 0;

						return true;
					}
				}
			} else if (Beta == criticalWeight) {
				for (int m = 0; m < Bsize; m++) {
					if (board[m][m] == 0) {
						board[m][m] = currentPlayer;
						compRec(m, m);// computer selection on board
						Beta = 0;
						clickC = 0;
						return true;
					}
				}
			}

			Alpha = 0;
			Beta = 0;

		}

		return false;
	}

	public boolean centerCase() {

		if (board[Bsize - ((Bsize - 1) / 2)][Bsize - ((Bsize - 1) / 2)] == 1)
			return true;

		return false;
	}

	public boolean cornerCase() {

		if (board[0][0] == 1 || board[0][Bsize - 1] == 1 || board[Bsize - 1][0] == 1
				|| board[Bsize - 1][Bsize - 1] == 1)
			return true;

		return false;
	}

}
