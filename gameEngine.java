package application;



import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.text.Font; 
import javafx.scene.text.FontPosture; 
import javafx.scene.text.FontWeight;

public class gameEngine {
	
	static int currentPlayer;  // keep the code of current player 
	static Text resultMsg;  //use to show game result
	private static int FstPlayer = 1, SndPlayer = 2; // codes for player1 and player 2
	private static int Bsize;	// use to define board size of game
	private static String FstPlayerName, SndPlayerName; // use to define players name (1 and 2)
	private static String currentPlayerName, winnerName;  //use to define current player name and winner name
	private static int  score1 = 0, score2 = 0;
	
	private static String color, mark; // use to define the color and mark for player
	private static String msg; //use for writing the result of game and other
	private static int[][] board; // define board matrix or array
	private static int game_mode; // 1 = Person vs. Person, 2 = Person vs. Computer
	private int level; // for defining the game level
	private int stabilizeGain = 0;  // used as gain to stabilize algorithm
	private int criticalWeight; // critical value where decide high cost or profit reach
	private int Alpha, Beta;  // alpha used as pruning build function and beta for block function
	private static int clickComputer; // use to define allowed turn for computer to play
	private static int clickPerson;	// use to define allowed turn for person to play
	private Rectangle[] rectangle; // define rectangles or grid for game and operate on it
	private static int win_row, win_col, win_dig1, win_dig2; // use for check win line
	private static boolean win_R = false, win_C = false, win_D1 = false, win_D2 = false; // use to activate win line
	private static Text Mark; // use to place mark of players in text
	

	
	/**
	 * This is game engine which set game setting and profile All that information
	 * come from game profile class and game driver 
	 * 		- Define number of rectangles that used as grid or cell for game board
	 * 		- Define size of board needed for game
	 *  	- Define game mode (person to person, or person to computer)
	 *   	- Define game level (easy (1) , medium (2), hard (3))
	 *  	- After define all that then initializing game board
	 * 
	 * 
	 * @param r
	 */
	// Contractor
	public gameEngine(Rectangle[] r) {

		this.rectangle = r;
		gameSetting setGame = new gameSetting();

		Bsize = setGame.getBoardSize(); // get board size of game
		board = new int[Bsize][Bsize]; // define board array or matrix
		game_mode = setGame.getGameMode(); // The game mode is to be requested from user profile
		level = setGame.getGameLevel(); // request from profile
		FstPlayerName=setGame.getPlayer1(); //get player1 name
		SndPlayerName=setGame.getPlayer2(); //get player2 name
		/*
		 * show setting of game on console window
		 */
		System.out.println("-------------------------------");
		System.out.println("Game Setting:");
		System.out.println("	Mode  " + game_mode);
		System.out.println("	Level  " + level);
		System.out.println("	Board size  " + Bsize);
		System.out.println("-------------------------------");
		System.out.println("Game Profile:");
		System.out.println("	First Player  " + FstPlayerName);
		System.out.println("	Second Player " + SndPlayerName);
		System.out.println("-------------------------------");
		
		initializeBoard(); // initialize game board
	}

	/**
	 * Package A: Game Setting and Checking
	 * 
	 * This package contains many methods that related to initialize and print the
	 * board of game, also all checker methods to check if board is full or not and
	 * check for win (row,column,diagonal) player checker (whose turn is to play?!)
	 * and step taken check. Also calculate the result of game.
	 * 
	 * 		1. initializeBoard() : initialize the game board and related parameters 
	 * 		2. printBoard() : print present board of game
	 * 		3. isBoardFull() : check if board is full or not
	 * 	 	4. checkForWin() : check if there is win case presented for current time
	 *  			a. checkRowsForWin() :check rows if win
	 *  			b. checkColumnsForWin():check columns if win
	 *  			c. checkDiagonalsForWin() :check diagonals if win 
	 * 		5. PlayerTurn() : check for current player turn and related mark
	 *  	6. stepChecker() : check the current action take by player
	 *  	7. result() :calculate the result of game
	 * 
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
			currentPlayerName=FstPlayerName;
			clickPerson = 1;
		}
		// Medium level
		if (level == 2) {
			currentPlayer = SndPlayer;
			currentPlayerName=SndPlayerName;
			clickComputer = 1;
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

				win_R = true;
				win_row = i;
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

				win_C = true;
				win_col = i;
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

			win_D1 = true;
			return true;
		}

		else if (checkRowCol(D2) == true) {

			win_D2 = true;
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
				currentPlayerName=SndPlayerName;
				clickComputer = 1;
				clickPerson = 0;
			} else {
				currentPlayer = FstPlayer;
				currentPlayerName=FstPlayerName;
				clickComputer = 0;
				clickPerson = 1;
			}

			if (game_mode == 1) {
				clickPerson = 1;
				clickComputer = 0;

			}
		}
	}

	/*
	 * This method run three methods to test the step of player if s/he win or not
	 * and if game draw or still going on then print final result
	 */
	public boolean stepChecker() {

		int counter=Bsize-1;
		
		PlayerTurn();
		if (isBoardFull() && checkForWin() == false) {

			msg = "The game is drawed!!!!!!!!";
			System.out.println(msg);
			resultMsg = new Text(msg);
			//Changing the font to "verdana" at a size of 40 pt
			resultMsg.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 40));
	        //The text gets a horizontal line in the middle through it.
			resultMsg.setStrikethrough(true);

			return false;

		}
		if (checkForWin() == true) {

			if (win_R == true) {

				for (int i = 0; i < Bsize; i++) {
					computerPlay(win_row, i);
			  
					
				}
				
				win_C=false;
				win_D1=false;
				win_D2=false;
			}

			if (win_C == true) {

				for (int i = 0; i < Bsize; i++) {
					computerPlay(i, win_col);
				}
				
				win_R=false;
				win_D1=false;
				win_D2=false;
			}
			if (win_D1 == true) {

				for (int i = 0; i < Bsize; i++) {
					computerPlay(i, i);
				}
				
				win_R=false;
				win_C=false;
				win_D2=false;
			}
			if (win_D2 == true) {

				for (int i = 0; i < Bsize; i++) {
					computerPlay(i, counter);
					counter--;
				}
				
				win_R=false;
				win_C=false;
				win_D1=false;
			}
			
			winnerName = currentPlayerName;
			msg = winnerName + " win";
			System.out.println(msg);
			resultMsg = new Text(msg);
			
			//Changing the font of result message
			resultMsg.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 40));
			
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
	 * to take player action or mark in the game.
	 * 		 1. playerMark(): this helper method that define current player mark
	 * 		 2. map(int index): this helper method map from board to grid index
	 *     	 3. placeMark(int row, int col) : this helper method need to place mark of player
	 * 				 a. placeMarkPerson(int row, int col, int player)
	 *  			 b. placeMarkComputer(int player)
	 * 
	 *
	 */

	/*
	 * Change player marks back and forth. for matrix use to see what is going and
	 * check for winner
	 */
	public void playerMark() {

		if (currentPlayer == FstPlayer) {
			color = "GREEN";  
			mark = "X";
		} else {
			color = "BLUE";			
			mark = "O";
		}

		

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

				if (row >= rectangle[counter].getX() && row <= (rectangle[counter].getX() + rectangle[counter].getWidth())
						&& col >= rectangle[counter].getY() && col <= (rectangle[counter].getY() + rectangle[counter].getHeight())) {

					if (rectangle[counter].getFill() == Color.WHITE) {

						rectangle[counter].setFill(Color.web(color));
						
						/*
						 * this text is not activated by driver 
						 * need the driver activated and show mark 
						 * on game board
						 */
						Mark= new Text(mark);
					    Mark.setFont(Font.font(50)); // use latter inside rectangular
						
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

	public void computerPlay(int x, int y) {

		playerMark();
		int counter = 0;
		int win_tic=0;

        
		if(checkForWin()==true) {
			
			color="RED";
	       win_tic=1;
			
		}

		for (int i = 0; i < Bsize; i++) {
			for (int j = 0; j < Bsize; j++) {

				if (i == x && j == y) {
					rectangle[counter].setFill(Color.web(color));
					/*
					 * this text is not activated by driver 
					 * need the driver activated and show mark 
					 * on game board
					 */
					Mark= new Text(mark);
				    Mark.setFont(Font.font(50)); // use latter inside rectangular
				    
					/*
					 *  Add animation to the win line
					 */
				      //Creating a rotate transition    
			        RotateTransition rotateTransition = new RotateTransition(); 
			      
			        //Setting the duration for the transition 
			        rotateTransition.setDuration(Duration.millis(10000)); 
			      
			        //Setting the angle of the rotation 
			        rotateTransition.setByAngle(360); 
			      
			        //Setting the cycle count for the transition 
			        rotateTransition.setCycleCount(1); 
			      
			        //Setting auto reverse value to false 
			        rotateTransition.setAutoReverse(false); 
			      
			        //Setting the nodes for the transition 
			        rotateTransition.setNode(rectangle[counter]); 
			        
			      //Creating scale Transition 
			        ScaleTransition scaleTransition = new ScaleTransition(); 
			      
			        //Setting the duration for the transition 
			        scaleTransition.setDuration(Duration.millis(1000)); 
			      
			        //Setting the node for the transition 
			        scaleTransition.setNode(rectangle[counter]); 
			      
			        //Setting the dimensions for scaling 
			        scaleTransition.setByY(-0.5); 
			        scaleTransition.setByX(-0.5); 
			      
			        //Setting the cycle count for the translation 
			        scaleTransition.setCycleCount(5); 
			      
			        //Setting auto reverse value to true 
			        scaleTransition.setAutoReverse(true); 
			        
			    
			      
			        if(win_tic==1) {
			        	 //Playing the scale animation 
			            scaleTransition.play();
			        	 //Playing the animation 
				        rotateTransition.play();
			        }
		
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
	 * but modified it. The strategies for this algorithm is 
	 *  	- Check priority of building and blocking
	 *  	- Reduce game tree by open-game strategy 
	 * 		- Build a win by dynamic go through rows,columns, diagonals
	 * 
	 * 
	 * 	1. nextMove(): This major algorithm method and other are helper 
	 *  2. Build():This method help to build a win for computer
	 *  3. checkRowWeight(): This priority checker for build or block in rows
	 *  4. checkColWeight(): This priority checker for build or block in columns
	 *  5. checkDiagonalWeight(): This priority checker for build or block in diagonals
	 * 
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
		 * High priority state (mode) this section calculate the heuristic functions
		 * (f(alpha),f(beta))
		 */
		if (priorityMode == true) {

			checkRowWeight(); 		// calculate alpha and beta in rows lines
			checkColWeight();		// calculate alpha and beta in columns lines
			checkDiagonalWeight();	// calculate alpha and beta in diagonals lines

			priorityMode = false;
		}

		/*
		 * Open-game State (Build only)
		 */

		if (priorityMode == false) {
			if (clickComputer == 1) { // this generalize form (status:General move Working)

				if (board[0][0] == 0) {

					computerPlay(0, 0);// computer selection on board
					board[0][0] = currentPlayer;

				} else if (board[0][Bsize - 1] == 0) {

					computerPlay(0, Bsize - 1);// computer selection on board
					board[0][Bsize - 1] = currentPlayer;
				} else if (board[(Bsize - 1) / 2][(Bsize - 1) / 2] == 0) {

					computerPlay((Bsize - 1) / 2, (Bsize - 1) / 2);// computer selection on board
					board[(Bsize - 1) / 2][(Bsize - 1) / 2] = currentPlayer;

				} else if (board[Bsize - 1][0] == 0) {
					computerPlay(Bsize - 1, 0);// computer selection on board
					board[Bsize - 1][0] = currentPlayer;

				} else if (board[Bsize - 1][Bsize - 1] == 0) {
					computerPlay(Bsize - 1, Bsize - 1);// computer selection on board
					board[Bsize - 1][Bsize - 1] = currentPlayer;

				}

				/*
				 * Dynamic move state(mode) 
				 * 		-try to build the best win line (diagonal, row,columns)
				 *  	-try to go generally in case no win only draw
				 */

				else {

					Build_win(); // this method response dynamically
					
					/*
					 * block or general move
					 */
					if (clickComputer == 1) {
						for (int i = 0; i < Bsize; i++) {
							for (int j = 0; j < Bsize; j++) {

								if (board[i][j] == 0 && board[i][j] != 1) {

									computerPlay(i, j);// computer selection on board
									board[i][j] = currentPlayer;

									return true; // this let go one step only now whole loop

								}
							}
						}
					}
				}
			}

			stabilizeGain++;
			
			clickComputer = 0;
			return true;

		}

		return false;

	}

	/**
	 * This method try to build win be choosing best play position which change
	 * dynamically by calculating the cost of previous and current action which let
	 * know where will be the highest priority to through
	 */
	public boolean Build_win() {

		//define counters for col, row, diagonal lines for latter use
		int col_counter = 0, row_counter = 0, dig_counter = Bsize - 1;
		//define logic boolean for activate col,row,diagonal lines to go first
		boolean tic_row = false, tic_col = false, tic_dig1 = false, tic_dig2 = false;
		//define array to collect values of evaluation throw row
		int[] go_row = new int[Bsize];
		//define array to collect values of evaluation throw column
		int[] go_col = new int[Bsize];
		//define array to collect values of evaluation throw diagonal 1
		int[] go_dig1 = new int[Bsize];
		//define array to collect values of evaluation throw diagonal 2
		int[] go_dig2 = new int[Bsize];
		//define calculate value for row,col, diagonal lines
		int val_col = 0, val_row = 0, val_dig = 0, dig1_state = 0, dig2_state = 0;
		//define priority value for col,row, diagonal lines
		int col_priority = 0, row_priority = 0, dig1_priority = 0, dig2_priority = 0;

		// ** Building move **

		/*
		 * go diagonals
		 */

		for (int i = 0; i < Bsize; i++) {

			if (board[i][i] == 1) {

				dig1_priority = -1;
				go_dig1[i] = dig1_priority;

				dig1_state = -1;
			} else if (board[i][i] == 2 && dig1_priority != -1) {

				dig1_priority = dig1_priority + 2;
				go_dig1[i] = dig1_priority;

				dig1_state = 1;

			} else if (board[i][i] == 0 && dig1_priority != -1) {

				dig1_priority = dig1_priority + 1;
				go_dig1[i] = dig1_priority;

				dig1_state = 1;
			}
			if (board[i][dig_counter] == 1) {

				dig2_priority = -1;
				go_dig2[i] = dig2_priority;

				dig2_state = -1;

			} else if (board[i][dig_counter] == 2 && dig2_priority != -1) {

				dig2_priority = dig2_priority + 2;
				go_dig2[i] = dig2_priority;

				dig2_state = 1;
			} else if (board[i][dig_counter] == 0 && dig2_priority != -1) {

				dig2_priority = dig2_priority + 1;
				go_dig2[i] = dig2_priority;

				dig2_state = 1;
			}

			dig_counter--;
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
				 * go rows
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

			if (dig1_state != -1 && go_dig1[m] >= go_dig2[m] && go_dig1[m] >= val_dig) {

				dig_counter = m;
				val_dig = go_dig1[m];
				tic_dig1 = true;
				tic_dig2 = false;

			} else if (dig2_state != -1 && go_dig1[m] < go_dig2[m] && go_dig2[m] >= val_dig) {
				dig_counter = m;
				val_dig = go_dig2[m];
				tic_dig1 = false;
				tic_dig2 = true;
			}
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
			tic_dig1 = false;
			tic_dig2 = false;
		} else if (val_dig >= val_row && val_dig >= val_col) {
			tic_col = false;
			tic_row = false;

		} else {

			tic_col = false;
			tic_row = true;
			tic_dig1 = false;
			tic_dig2 = false;
		}

		for (int k = 0; k < Bsize; k++) {

			// ** which should be choose (diagonal, column, row) for next move
			/*
			 * Build win through column line
			 */
			if (tic_col == true) {
				if (board[k][col_counter] == 0 && board[k][col_counter] != 1) {
					computerPlay(k, col_counter);// computer selection on board
					board[k][col_counter] = currentPlayer;

					stabilizeGain++;
					// test action
					clickComputer = 0;

					return true; // this let go one step only now whole loop

				}
			}

			/*
			 * Build win through row line
			 */
			if (tic_row == true) {
				if (board[row_counter][k] == 0 && board[row_counter][k] != 1) {
					computerPlay(row_counter, k);// computer selection on board
					board[row_counter][k] = currentPlayer;

					stabilizeGain++;
					// test action
					clickComputer = 0;
					return true; // this let go one step only now whole loop

				}
			}
		}

		/*
		 * Build win through diagonal line
		 */
		if (centerCase() == false) {

			if (board[0][0] != 1 && board[Bsize - 1][Bsize - 1] != 1 && tic_dig1 == true) {

				for (int i = 0; i < Bsize; i++) {

					if (board[i][i] == 0 && board[i][i] != 1) {
						computerPlay(i, i);// computer selection on board
						board[i][i] = currentPlayer;

						stabilizeGain++;
						// test action
						clickComputer = 0;
						return true; // this let go one step only now whole loop
					}
				}

			}
			if (board[0][Bsize - 1] != 1 && board[Bsize - 1][0] != 1 && tic_dig2 == true) {

				for (int i = 0; i < Bsize; i++) {

					if (board[i][dig_counter] == 0 && board[i][dig_counter] != 1) {
						computerPlay(i, dig_counter);// computer selection on board
						board[i][dig_counter] = currentPlayer;

						stabilizeGain++;
						// test action
						clickComputer = 0;
						return true; // this let go one step only now whole loop
					}

					dig_counter--;
				}

			}

		}

		return false;
	}
	public boolean checkRowWeight() {

		if (clickComputer == 1) {
			
			scanBuildBlockRow();


		}
		return false;

	}

	public boolean checkColWeight() {

		if (clickComputer == 1) {


			
			//* Reading sample and calculating heuristic function beta
			scanBuildBlockColumn();
			


		}
		return false;
	}

	public boolean checkDiagonalWeight() {

		/*
		 * only need to focus on one diagonal, the other one is killed by open game
		 */

		if (clickComputer == 1) {

			for (int i = 0; i < Bsize; i++) {

				if (board[i][i] == 2)
					Alpha++;

				if (board[i][i] == 1)
					Beta++;
			
			}
			//** diagonal 2 is killed by open game

			if (Alpha == criticalWeight) {
				for (int m = 0; m < Bsize; m++) {
					if (board[m][m] == 0) {
						computerPlay(m, m);// computer selection on board
						board[m][m] = currentPlayer;

						Alpha = 0;
						clickComputer = 0;

						return true;
					}
				}
			} else if (Beta == criticalWeight) {
				for (int m = 0; m < Bsize; m++) {
					if (board[m][m] == 0) {

						computerPlay(m, m);// computer selection on board
						board[m][m] = currentPlayer;

						Beta = 0;
						clickComputer = 0;
						return true;
					}
				}
			}

			Alpha = 0;
			Beta = 0;

		}

		return false;
	}


	public boolean scanBuildBlockRow() {
		
		int[] alphaArr= new int[Bsize];
		int[] betaArr= new int[Bsize];
		int counter=0;

			for ( int i =0; i < Bsize; i++) {
				for (int j = 0; j < Bsize; j++) {

				
					if (board[i][j] == 2)
						Alpha++; // computer
					if (board[i][j] == 1)
						Beta++; // person
				}
			alphaArr[counter]=Alpha;
			betaArr[counter]=Beta;
			counter++;
			Alpha=0;
			Beta=0;
			}
			for ( int m=0;m<Bsize;m++) {
				
				if(alphaArr[m]==criticalWeight) {
					
					for (int n=0;n<Bsize;n++) {
				
						if (board[m][n] == 0) {
							computerPlay(m, n);// computer selection on board
							board[m][n] = currentPlayer;

							Alpha = 0;
							clickComputer = 0;

							return true;
						}
					}
				}
				
			}
		for (int m=0;m<Bsize;m++) {
				
				if(betaArr[m]==criticalWeight) {
					
					for (int n=0;n<Bsize;n++) {
						if (board[m][n] == 0) {
							computerPlay(m, n);// computer selection on board
							board[m][n] = currentPlayer;

							Beta = 0;
							clickComputer = 0;

							return true;
						}
					}
				}
				
			}
			return false;
		}

		public boolean scanBuildBlockColumn() {
			
			int[] alphaArr= new int[Bsize];
			int[] betaArr= new int[Bsize];
			int counter=0;

				for ( int i =0; i < Bsize; i++) {
					for (int j = 0; j < Bsize; j++) {

					
						if (board[j][i] == 2)
							Alpha++; // computer
						if (board[j][i] == 1)
							Beta++; // person
					}
				alphaArr[counter]=Alpha;
				betaArr[counter]=Beta;
				counter++;
				Alpha=0;
				Beta=0;
				}
				for ( int m=0;m<Bsize;m++) {
					
					if(alphaArr[m]==criticalWeight) {
						
						for (int n=0;n<Bsize;n++) {
					
							if (board[n][m] == 0) {
								computerPlay(n, m);// computer selection on board
								board[n][m] = currentPlayer;

								Alpha = 0;
								clickComputer = 0;

								return true;
							}
						}
					}
					
				}
			for (int m=0;m<Bsize;m++) {
					
					if(betaArr[m]==criticalWeight) {
						
						for (int n=0;n<Bsize;n++) {
							if (board[n][m] == 0) {
								computerPlay(n, m);// computer selection on board
								board[n][m] = currentPlayer;

								Beta = 0;
								clickComputer = 0;

								return true;
							}
						}
					}
					
				}
				return false;
			}
		
		public boolean scanBuildBlockDiagonal() {
			
			for (int i = 0; i < Bsize; i++) {

				if (board[i][i] == 2)
					Alpha++;

				if (board[i][i] == 1)
					Beta++;
			
			}
			//** diagonal 2 is killed by open game

			if (Alpha == criticalWeight) {
				for (int m = 0; m < Bsize; m++) {
					if (board[m][m] == 0) {
						computerPlay(m, m);// computer selection on board
						board[m][m] = currentPlayer;

						Alpha = 0;
						clickComputer = 0;

						return true;
					}
				}
			} else if (Beta == criticalWeight) {
				for (int m = 0; m < Bsize; m++) {
					if (board[m][m] == 0) {

						computerPlay(m, m);// computer selection on board
						board[m][m] = currentPlayer;

						Beta = 0;
						clickComputer = 0;
						return true;
					}
				}
			}
//			Alpha = 0;
//			Beta = 0;
		
			
			
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
