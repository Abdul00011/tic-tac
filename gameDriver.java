package application;



import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class gameDriver extends Application {

	private static int width, hight;	// frame dimension get from setting
	private static int BoardSize; 		// board size (required by game setting)
	private Rectangle[] rectangle; 		// define number of rectangular
	private Line[] vLine, hLine;		// define vertical and horizontal lines need to draw
	private int[] x_Graph; 					// array contains number of x-graph points
	private int[] y_Graph; 					// array contains number of y-graph points
	private int scale_initial, scale_final; 		// used to scale rectangle in board
	private static int x; 				//used to save x-position clicked on scene (user input)
	private static int y;				//used to save y-position clicked on scene (user input)
	private Scene result; 				// scene for show board,game, result of game
	private ImageView home,replay,ext;	// image used as icons for buttons home,replay,exist
	private Image imPlayer1,imPlayer2;
	/**
	 * this method will generate desired board which required by game setting
	 * 
	 * @return board and operational rectangular
	 */

	public gameDriver() {
		gameSetting setGame = new gameSetting();

		BoardSize = setGame.getBoardSize(); // use get method
		width = setGame.getFramX();
		hight = setGame.getFramY();
		imPlayer1=setGame.getImgPlayer1();
		imPlayer2=setGame.getImgPlayer2();

	}
/**
 *  This method use to add animation to player profile image
 * @param image
 */
	public void AnimatProfile(Node image) {
	//** add dynamic picture or box that show player 1 turn
	      //Creating a rotate transition    
        RotateTransition rotateTransition = new RotateTransition(); 
      
        //Setting the angle of the rotation 
        rotateTransition.setByAngle(360); 
      
        //Setting the cycle count for the transition 
        rotateTransition.setCycleCount(1); 
      
        //Setting auto reverse value to false 
        rotateTransition.setAutoReverse(false); 
      
        //Setting the nodes for the transition 
        rotateTransition.setNode(image);
        rotateTransition.play();
     
  }

/**
 * start method use to start game page and related pages 
 * and activation the game
 */
	public void start(Stage primaryStage) {
		try {
			
			 BorderPane rContner = new BorderPane();
			 
			 /**
			  * Here generate a place to plug player profile
			  *  picture represent first or second player played his/her turn 
			  *  and result in case of continue game
			  */
			 FlowPane profile =new FlowPane();
			 profile.setPadding(new Insets(10, 10, 10, 10));
			 profile.setHgap(350);
			 
			 ImageView viewPlayer1= new ImageView(imPlayer1);
			 viewPlayer1.setFitWidth(100);
			 viewPlayer1.setFitHeight(50);
			
			 ImageView viewPlayer2= new ImageView(imPlayer2);
			 viewPlayer2.setFitWidth(100);
			 viewPlayer2.setFitHeight(50);
			
		
			 profile.getChildren().addAll(viewPlayer1,viewPlayer2);
			 rContner.setTop(profile);// add profile pane to game page container
			 
			 /**
			  * bar flow pane contains the choose of 
			  * 	-go back to main page 
			  * 	-exist from game
			  * this pane is part of game page
			  */
			 FlowPane bar =new FlowPane();
			 bar.setPadding(new Insets(10, 10, 10, 10));
			 bar.setAlignment(Pos.CENTER);
			 bar.setHgap(185);
				//buttons and settings
				Button menu2 = new Button();
				menu2.setStyle("-fx-background-color: ANTIQUEWHITE;");
				menu2.setMinSize(50, 50);
				home = new ImageView("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\home.png");
				home.setFitWidth(50);
				home.setFitHeight(50);
				menu2.setGraphic(home);
				
				Button exist2 = new Button();
				exist2.setStyle("-fx-background-color: ANTIQUEWHITE;");
				exist2.setMinSize(50, 50);
				ext = new ImageView("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\out.png");
				ext.setFitWidth(50);
				ext.setFitHeight(50);
				exist2.setGraphic(ext);
				bar.getChildren().addAll(menu2,exist2);
				rContner.setBottom(bar);
			 
				menu2.setOnAction(e -> {
					/*
					 * go main page
					 */
					gamUI newGame = new gamUI();
					newGame.start(primaryStage);

				});
		
				exist2.setOnAction(e -> {
					/*
					 * exist the game
					 */
					System.exit(-1);

				});
				
			/**
			 * Here generate the game board
			 * 	 	- need generate number of lines the cut board in cells (Horizontal and Verticals)
			 *  	- need generate border lines (4-lines) cover the board border
			 * 	 	- need generate cells or grid (rectangles)
			 */
			Group rootBoard = new Group(); // contain the game board
			rContner.setCenter(rootBoard);
			// define size of x and y graph points
			x_Graph = new int[BoardSize];
			y_Graph = new int[BoardSize];

			Line Top_line, Rieght_line, Bottom_line, Left_line; // Border lines
			/**
			 * x and y here to define new frame size(width and height) for drawing board
			 */
			int xx = width; // frame width
			int yy = hight; // frame height

			/**
			 * always number of column lines and raw lines depend on board size (size-1) for
			 * each these parameters for drawing grid for a game, whole that depend on frame
			 * length and width.
			 * 
			 * Here define initial x-point and y-point for board (x0 and y0) and final
			 * x-point and y-point for board (xf and yf) and also defines board width and
			 * height
			 * 
			 * Generates all needed x and y points for drawing graph lines and rectangular
			 * (x_G and y_G)
			 * 
			 */

			// x0 and y0 new frame initial draw start points
			int x0 = xx / BoardSize, y0 = yy / BoardSize; // define initial x and y points
			int xf = xx - (xx / BoardSize), yf = yy - (yy / BoardSize); // define final x and points
			int Board_width = xf - x0, Board_hieght = yf - y0; // define width and height of board

			// generate number of x and y graph points where horizontal lines and vertical
			// lines and rectangular intersect
			x_Graph[0] = x0;
			y_Graph[0] = y0;
			for (int i = 1; i < BoardSize; i++) {
				x_Graph[i] = x0 + (i) * (Board_width / BoardSize);
				y_Graph[i] = y0 + (i) * (Board_hieght / BoardSize);

			}

			/**
			 * Here draw border lines that contains all grids be as new frame lines then
			 * define vertical and horizontal lines needed
			 */

			Top_line = new Line(x0, y0, xf, y0);
			Rieght_line = new Line(xf, y0, xf, yf);
			Bottom_line = new Line(xf, yf, x0, yf);
			Left_line = new Line(x0, yf, x0, y0);

			rootBoard.getChildren().addAll(Top_line, Rieght_line, Bottom_line, Left_line);

			// Vertical lines
			vLine = new Line[BoardSize - 1];

			for (int i = 0; i < (BoardSize - 1); i++) {
				vLine[i] = new Line(x_Graph[i + 1], y0, x_Graph[i + 1], yf);
				rootBoard.getChildren().add(vLine[i]);
			}

			// Horizontal lines
			hLine = new Line[BoardSize - 1];
			// Horizontal lines
			for (int i = 0; i < (BoardSize - 1); i++) {
				hLine[i] = new Line(x0, y_Graph[i + 1], xf, y_Graph[i + 1]);
				rootBoard.getChildren().add(hLine[i]);
			}

			/**
			 * Here draw all rectangular which act as grids for board
			 */

			// create needed number of grid for game
			rectangle = new Rectangle[BoardSize * BoardSize];

			// calculating scaling single rectangle inside section of line intersect
			if (BoardSize <= 3) {
				scale_initial = 1;
				scale_final = -1;
			} else if (BoardSize > 3 && BoardSize < 7) {
				scale_initial = 3;
				scale_final = BoardSize * BoardSize + 15;
			} else if (BoardSize > 6 && BoardSize < 9) {
				scale_initial = 3;
				scale_final = BoardSize + 37;
			} else {
				scale_initial = 3;
				scale_final = BoardSize + 30;

			}

			// Drawing a Rectangle
			for (int i = 0; i < BoardSize * BoardSize; i++) {

				rectangle[i] = new Rectangle();
				rectangle[i].setStroke(Color.RED);
				
			}
			// Setting the properties of the rectangle
			int count = 0;
			for (int i = 0; i < BoardSize; i++) {
				for (int j = 0; j < BoardSize; j++) {

					rectangle[count].setX(x_Graph[j] + scale_initial);
					rectangle[count].setY(y_Graph[i] + scale_initial);
					rectangle[count].setWidth((x0 / BoardSize) + scale_final);
					rectangle[count].setHeight((x0 / BoardSize) + scale_final);
					rectangle[count].setFill(Color.WHITE);
					rootBoard.getChildren().add(rectangle[count]);

					count++;
				}
			}

			/**
			 * Here creating result window contains end game result and option to Replay, go
			 * main menu, or Exist.
			 * 
			 */
			
			/**
			 * Border pane of final window contains all elements of result window
			 * 		-Hbox for result text show
			 * 		-Flow pane contains all button (home page, replay, exist)
			 * 		
			 */
			BorderPane FinalWindow = new BorderPane();   //container of final window
			FinalWindow.setStyle("-fx-background-color: ANTIQUEWHITE;-fx-text-fill: white;");
			HBox ResultWindow = new HBox();		//result msg show
			ResultWindow.setPadding(new Insets(10, 10, 10, 10)); //setting for result window
			
			FlowPane Play_Exist = new FlowPane();	//container for buttons 
			//setting for Play_Exist flow pane 
			Play_Exist.setPadding(new Insets(10, 10, 10, 10));
			Play_Exist.setHgap(185);
			
			//buttons and settings
			Button menu1 = new Button();
			menu1.setStyle("-fx-background-color: ANTIQUEWHITE;");
			menu1.setMinSize(50, 50);
			home = new ImageView("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\home.png");
			home.setFitWidth(50);
			home.setFitHeight(50);
			menu1.setGraphic(home);
			
			
			Button play = new Button();
			play.setStyle("-fx-background-color: ANTIQUEWHITE;");
			play.setMinSize(50, 50);
			replay = new ImageView("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\replayA.jpg");
			replay.setFitWidth(50);
			replay.setFitHeight(50);
			play.setGraphic(replay);
			
			Button exist1 = new Button();
			exist1.setStyle("-fx-background-color: ANTIQUEWHITE;");
			exist1.setMinSize(50, 50);
			ext = new ImageView("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\out.png");
			ext.setFitWidth(50);
			ext.setFitHeight(50);
			exist1.setGraphic(ext);
			
			menu1.setOnAction(e -> {

				/*
				 * go to main page
				 */
				gamUI newGame = new gamUI();
				newGame.start(primaryStage);

			});
			play.setOnAction(e -> {
				/*
				 *  replay the game
				 */
				start(primaryStage);

			});
			exist1.setOnAction(e -> {
				/*
				 * exist the game
				 */
				System.exit(-1);

			});

			Play_Exist.getChildren().addAll(menu1, play, exist1);

			/*
			 * organize the final window set elements
			 */
			FinalWindow.setTop(ResultWindow);
			FinalWindow.setBottom(Play_Exist);

			/**
			 * Here start real operation of game (real-time reaction of game)
			 * 
			 */
			gameEngine game = new gameEngine(rectangle);

			// start with game
			game.initializeBoard();
			
			/*
			 * add animation to player profile to show turn
			 */
	      
			EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

				//this Node use to place image view of player in animation method
				Node node;	
				/**
				 * all operation of game happen due to mouse click or event this method define
				 * how the game work
				 */
				public void handle(MouseEvent e) {
				
					/*
					 * set node of player image and apply animation method
					 */
					if(game.currentPlayer==1)
						node=viewPlayer1;
					if(game.currentPlayer==2)
						node=viewPlayer2;
					//Apply animation to profile image
					AnimatProfile(node);
					
					/**
					 * x and y very important input for game that define the player is played and
					 * where s/he clicked
					 */
					x = (int) e.getSceneX();
					y = (int) e.getSceneY();

				
					/**
					 * this if-condition make sure that game is valid that board not full and no
					 * winner current time.
					 */
					if (game.checkForWin() == false && game.isBoardFull() == false) {

						// this if statement to sure only person turn to play not computer(Not allowed)
						if (game.placeMarkPerson(x, y, game.currentPlayer) == true) {
							
							game.placeMark(x, y);// use to mark on board
							
							System.out.println("Person Turn Played");
							System.out.println("----------------------------------------");

							// this method check selection of player(win or board full-end game)
							if (game.stepChecker() == false) {
								/*
								 * show final window 
								 */
								FinalWindow.setCenter(rootBoard);
								ResultWindow.setAlignment(Pos.CENTER);
								ResultWindow.getChildren().add(game.resultMsg);
								result = new Scene(FinalWindow, 600, 600);
								result.setFill(Color.LAVENDER);
								
								// Setting title to the Stage
								primaryStage.setTitle("Result of game");
							
								// Adding scene to the stage
								primaryStage.setScene(result);

							}

							// this method print updated board only for backgroud use not for user
							game.printBoard();

							System.out.println("----------------------------------------");
							//System.out.println(game.currentPlayer);

						}

						/**
						 * this if statement to sure computer turn (Not allowed person) also the method
						 * placeMarkComputer() is response for next move of computer
						 */
						
						if (game.placeMarkComputer(game.currentPlayer) == true) {
							
							
							AnimatProfile(viewPlayer2);// use to add animation to computer turn profile image
					        
							// this method check selection of player(win or board full-end game)
							if (game.stepChecker() == false) {

								/*
								 * show final result
								 */
								FinalWindow.setCenter(rootBoard);
								ResultWindow.setAlignment(Pos.CENTER);
								ResultWindow.getChildren().add(game.resultMsg);
								result = new Scene(FinalWindow, 600, 600);
								result.setFill(Color.LAVENDER);
								// Setting title to the Stage
								primaryStage.setTitle("Result of game");
								// Adding scene to the stage
								primaryStage.setScene(result);

							}

							System.out.println("Computer Turn Played");
							System.out.println("----------------------------------------");
							game.printBoard();
							System.out.println("----------------------------------------");
							//System.out.println(game.currentPlayer);
						}

					}

				}

			};
			
			//add mouse event to game board
			rootBoard.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
			/*
			 * show game page and active game board
			 */
			Scene scene = new Scene(rContner, width, hight);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setX(400);
			primaryStage.setY(50);
			primaryStage.show();
	      
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
