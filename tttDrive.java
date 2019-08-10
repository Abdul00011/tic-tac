package application;



import java.awt.Font;
import java.lang.reflect.Array;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javafx.stage.Stage;

public class tttDrive extends Application {

	static int x;
	static int y;
	Scene result, page1, page2;
	Group root;
	Rectangle[] r;
	
	Text Mark;


	
	
	tttGenerator drwGame = new tttGenerator();
	public tttDrive() {
		
		
		this.r=drwGame.r;
		
	
	}
	
	/**
	 * create object of profile that contains all setting of going game
	 */
	//static profile2 pro = new profile2();

	// ** need constructor or game setting that get all setting of game from profile
	// ** such as game mode, game level, game size

	/**
	 * start method contains all operation need to drive game
	 */
	public void start(Stage stage) {

		
			 
			// create window for result
			HBox ResultWindow = new HBox();

			/**
			 * create game object to run the game engine through driver of game
			 */
			tttEngine game = new tttEngine(r);

			// start with game
			game.initializeBoard();

			EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

			/**
			 * all operation of game happen due to mouse click or event
			 * this method define how the game work
			 */
				public void handle(MouseEvent e) {
					System.out.println("inside game engine11111");
					/**
					 * x and y very important input for game that define the player is playing 
					 * and where s/he clicked 
					 */
					x = (int) e.getSceneX();
					y = (int) e.getSceneY();
					System.out.println(x);
					System.out.println(y);
					
					game.placeMark(x, y);
					
					/**
					 * this if-condition make sure that game is valid that board not full 
					 * and no winner current time.
					 */
				//	if (game.checkForWin() == false && game.isBoardFull() == false) {

//						// this if statement to sure only peson turn to play not computer(Not allowed)
//						if (game.placeMarkPerson(x, y, game.currentPlayer) == true) {
//
//							System.out.println("Gaming Mode P2C level Easy");
//							game.placeMark(x, y);// this step could be deleted and add inside placMarkPerson
//
//							System.out.println("Person Turn Played");
//							System.out.println("----------------------------------------");
//							
//							// this method check selection of player(win or board full-end game)
//							if (game.stepChecker()==false) {
//								
//								
//
//								ResultWindow.setAlignment(Pos.CENTER);
//								ResultWindow.getChildren().add(game.resultMsg);
//								
//								result = new Scene(ResultWindow, 300, 200);
//								result.setFill(Color.LAVENDER);
//								// Setting title to the Stage
//								stage.setTitle("Result of game");
//
//								// Adding scene to the stage
//								stage.setScene(result);
//								
//							}
//
//							//this method print updated board only for backgroud use not for user
//							game.printBoard();
//							
//							System.out.println("----------------------------------------");
//							System.out.println(game.currentPlayer);
//
//						}
//
//						/**
//						 *  this if statement to sure computer turn (Not allowed person)
//						 *  also the method placeMarkComputer() is response for next move 
//						 *  of computer
//						 */
//						if (game.placeMarkComputer(game.currentPlayer) == true) {
//
//							// this method check selection of player(win or board full-end game)
//							if (game.stepChecker()==false) {
//								
//								
//
//								ResultWindow.setAlignment(Pos.CENTER);
//								ResultWindow.getChildren().add(game.resultMsg);
//								result = new Scene(ResultWindow, 300, 200);
//								result.setFill(Color.LAVENDER);
//								// Setting title to the Stage
//								stage.setTitle("Result of game");
////								try {
////									Thread.sleep(40000);
////								} catch (InterruptedException e1) {
////									// TODO Auto-generated catch block
////									e1.printStackTrace();
////								}
//								// Adding scene to the stage
//								stage.setScene(result);
//								
//							}
//
//							System.out.println("Computer Turn Played");
//							System.out.println("----------------------------------------");
//							game.printBoard();
//							System.out.println("----------------------------------------");
//							System.out.println(game.currentPlayer);
//						}
//
//					//}
//
				}

		};

			// Creating a Group object
			root = new Group(drwGame.genBoard());

			root.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

			// Creating a scene object
			page2 = new Scene(root, 600, 600);
			page2.setFill(Color.LAVENDER);
			// Setting title to the Stage
			stage.setTitle("Tic Tac Toe");

			// Adding scene to the stage
			stage.setScene(page2);

			// Displaying the contents of the stage
			stage.show();

		
	}

	public static void main(String[] args) {
	
		launch(args);

	}
}

