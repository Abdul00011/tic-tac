package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class gamUI extends Application {

	private Scene page1, page2;											// the scene needed as page to dialog with user as interface
	private Image mode1 = new Image("File:C:\\Users\\Acer\\Desktop"
			+ "\\MsComputer\\Msc-Project\\pics\\person.png");			// image as icon for mode1 of game (person2person)
	
	private Image mode2 = new Image("File:C:\\Users\\Acer\\Desktop"
			+ "\\MsComputer\\Msc-Project\\pics\\androd.png");			// image as icon for mode2 of game (person2computer)
	private int BoardSize, FramX, FramY, numGame = 1, gMode, gLevel; 	// game setting parameters need to take
	private static String nameP1, nameP2; 								// game user profile parameters need to take
	
//	private static Image background = new Image(
//	"File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\X-O.png");
	// private static Image Play1image, Play2image;

	public void start(Stage primaryStage) {
		try {
			/**
			 * 
			 * collect players names for game mode person to person
			 */

			// creating label First Number
			Text player1 = new Text("First Player");

			// creating label Second Number
			Text player2 = new Text("Second Player");

			// Creating Text Field for First Number
			TextField name1 = new TextField();

			// Creating Text Field for Second Number
			TextField name2 = new TextField();

			/**
			 * collecting game setting -getting the desired level user of app need to play
			 */

			/*
			 * level buttons container -button level low or easy -button level normal or
			 * medium -button level high or hard and all required setting for that
			 */
			GridPane levelBtn = new GridPane();
			// Setting the padding
			levelBtn.setPadding(new Insets(10, 10, 10, 10));

			Button level1 = new Button();
			level1.setMinSize(60, 60);
			ImageView easy = new ImageView("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\low1.jpg");
			easy.setFitWidth(60);
			easy.setFitHeight(60);
			level1.setGraphic(easy);

			Button level2 = new Button();
			level2.setMinSize(60, 60);
			ImageView normal = new ImageView(
					"File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\normal2.png");
			normal.setFitWidth(60);
			normal.setFitHeight(60);
			level2.setGraphic(normal);

			Button level3 = new Button();
			level3.setMinSize(60, 60);
			ImageView hard = new ImageView("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\high1.png");
			hard.setFitWidth(60);
			hard.setFitHeight(60);
			level3.setGraphic(hard);

			level1.setOnAction(e -> {

				/*
				 * parameters of this state
				 */

				BoardSize = 3;
				gLevel = 1;
				FramX = FramY = 600;

				System.out.println(nameP1);
				System.out.println(nameP2);
				gameSetting setGame = new gameSetting(nameP1, nameP2, gMode, gLevel, numGame, FramX, FramY, BoardSize);

				gameDriver run = new gameDriver();
				run.start(primaryStage);

			});

			level2.setOnAction(e -> {

				BoardSize = 5;
				gLevel = 1;
				FramX = FramY = 600;

				gameSetting setGame = new gameSetting(nameP1, nameP2, gMode, gLevel, numGame, FramX, FramY, BoardSize);

				gameDriver run1 = new gameDriver();
				run1.start(primaryStage);

			});

			level3.setOnAction(e -> {

				BoardSize = 9;
				gLevel = 2;
				FramX = FramY = 600;

				gameSetting setGame = new gameSetting(nameP1, nameP2, gMode, gLevel, numGame, FramX, FramY, BoardSize);

				gameDriver run2 = new gameDriver();
				run2.start(primaryStage);

			});

			// }

			levelBtn.setPadding(new Insets(10, 10, 10, 10));

			// Setting the vertical and horizontal gaps between the columns
			levelBtn.setVgap(20);

			// Setting the Grid alignment
			levelBtn.setAlignment(Pos.CENTER);

			// Arranging all the nodes in the grid
			levelBtn.add(level1, 0, 0);
			levelBtn.add(level2, 0, 1);
			levelBtn.add(level3, 0, 2);

			levelBtn.setMinSize(200, 200);

			/**
			 * main page which contain the desired mode or mode option for user of game
			 * -person to person mode -person to android or computer mode all required
			 * setting for that
			 * 
			 */
			/*
			 * image setting for representing mode
			 */
			ImageView pp = new ImageView(mode1);
			pp.setFitWidth(100);
			pp.setFitHeight(100);

			ImageView pc = new ImageView(mode2);
			pc.setFitWidth(100);
			pc.setFitHeight(100);

			/*
			 * grid pane contains all modes and relating setting
			 */
			GridPane Mode = new GridPane();
			Mode.setPadding(new Insets(10, 10, 10, 10));
			Mode.setStyle("-fx-background-color: ANTIQUEWHITE;");

			/*
			 * button mode person to person and relating setting for this mode
			 */
			Button P2P = new Button();
			P2P.setMinSize(100, 100);
			P2P.setStyle("-fx-background-color: ANTIQUEWHITE;");
			P2P.setGraphic(pp);

			P2P.setOnAction(e -> {

				/*
				 * Parameters this state
				 */
				gMode = 1;
				nameP1 = name1.getText();
				nameP2 = name2.getText();

				if (name1.getText().isEmpty() || name2.getText().isEmpty()) {

					nameP1 = "Player 1-Green";
					nameP2 = "player 2-Blue";
				}

				/**
				 * drawing all info and setting in single grid pane
				 */

				/*
				 * creating grid pane contains all elements to get player info in person to
				 * person mode
				 */
				GridPane gridPane = new GridPane();

				// Setting the padding
				gridPane.setPadding(new Insets(10, 10, 10, 10));

				// Setting the vertical and horizontal gaps between the columns
				gridPane.setVgap(5);
				gridPane.setHgap(5);

				// Setting the Grid alignment
				gridPane.setAlignment(Pos.CENTER);

				// Arranging all the nodes in the grid
				gridPane.add(player1, 0, 0);
				gridPane.add(name1, 1, 0);
				gridPane.add(player2, 0, 1);
				gridPane.add(name2, 1, 1);

				/*
				 * creating border pane contains all elements in person to person mode page and
				 * relating setting
				 */
				BorderPane rProfile = new BorderPane();
				rProfile.setTop(gridPane);
				rProfile.setCenter(levelBtn);
				rProfile.setStyle("-fx-background-color:ANTIQUEWHITE; -fx-text-fill:     white;");

				// set scene for person to person mode page
				page2 = new Scene(rProfile, 300, 300);
				primaryStage.setScene(page2);

			});

			/*
			 * button mode person to computer and relating setting for this mode
			 */
			Button P2C = new Button();
			P2C.setMinSize(100, 100);
			P2C.setStyle("-fx-background-color: ANTIQUEWHITE;");
			P2C.setGraphic(pc);

			P2C.setOnAction(e -> {

				/*
				 * Parameters this state
				 */
				gMode = 2;
				nameP1 = "Person";
				nameP2 = "Robot";
				/*
				 * creating border pane contains all elements in person to computer mode page
				 * and relating setting
				 */
				BorderPane rProfile = new BorderPane();
				rProfile.setCenter(levelBtn);
				rProfile.setStyle("-fx-background-color:ANTIQUEWHITE; -fx-text-fill:     white;");

				// set scene for person to computer mode page
				page2 = new Scene(rProfile, 400, 400);
				primaryStage.setScene(page2);

			});

			Mode.add(P2P, 0, 0);
			Mode.add(P2C, 1, 0);
			// Mode.setVgap(5);
			Mode.setHgap(15);
			Mode.setAlignment(Pos.CENTER);
			/*
			 * Creating exist button to exist from game
			 * and relating setting
			 * 
			 */
			Button exist = new Button();
			exist.setStyle("-fx-background-color: ANTIQUEWHITE;");
			exist.setMinSize(50, 50);
			ImageView ext = new ImageView("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\out.png");
			ext.setFitWidth(50);
			ext.setFitHeight(50);
			exist.setGraphic(ext);
			exist.setPadding(new Insets(10, 10, 10, 10));
			exist.setOnAction(e -> {
				/*
				 * exist the game
				 */
				System.exit(-1);

			});
			
			
			/*
			 * creating border pane as root contains all modes
			 */
			BorderPane rMode = new BorderPane();
			rMode.setCenter(Mode);
			rMode.setTop(exist);
			rMode.setStyle("-fx-background-color:ANTIQUEWHITE; -fx-text-fill: white;");

			// set scene for mode page and welcom page
			page1 = new Scene(rMode, 400, 300);
			primaryStage.setTitle("Tic-Tac-Toe for Children");
//			ImagePattern pattern = new ImagePattern(background);
//			page1.setFill(pattern);
			page1.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(page1);
			primaryStage.setX(500);
			primaryStage.setY(100);
			primaryStage.show();
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		launch(args);
	}
}