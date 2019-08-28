package application;

import javafx.scene.image.Image;

public class gameSetting {

	// private static Image background,Play1image,Play2image;
	
	private static String player1, player2; 	// used to save players name
	private static int gameMode, gameLevel; 	// used for setting game mode and level
	private static int matchNumber; 			// used to set number of match need to play
	private static int FramX, FramY; 			// used to set width and height of game board scene
	private static int BoardSize; 				// used to set game board size
	private static Image imgPlayer1,imgPlayer2;	// used to assign images for players profiles

	/**
	 * game setting contractor will set all parameters for game setting and profile 
	 * collect all that from UI-class
	 * 
	 * @param player1
	 * @param player2
	 * @param gameMode
	 * @param gameLevel
	 * @param matchNumber
	 * @param FramX
	 * @param FramY
	 * @param BoardSize
	 */
	public gameSetting(String player1, String player2, int gameMode, int gameLevel, int matchNumber, int FramX,
			int FramY, int BoardSize) {

		this.player1 = player1;
		this.player2 = player2;
		this.gameLevel = gameLevel;
		this.gameMode = gameMode;
		this.matchNumber = matchNumber;
		this.FramX = FramX;
		this.FramY = FramY;
		this.BoardSize = BoardSize;
	}

	/**
	 * this contractor will allow other class to get setting the game
	 * for engine class and driver class
	 * 
	 */
	public gameSetting() {

	}

	/**
	 * this part contains all getter methods
	 * @return player1
	 * @return player2
	 * @return gameMode
	 * @return gameLevel
	 * @return matchNumber
	 * @return FramX
	 * @return FramY
	 * @return BoardSize
	 * @return imgPlayer1
	 * @return imgPlayer2
	 * 
	 */
	
	
	
	public String getPlayer1() {
		return player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public int getGameLevel() {
		return gameLevel;
	}

	public int getGameMode() {
		return gameMode;
	}

	public int getMatchNumber() {
		return matchNumber;
	}

	public int getFramX() {
		return FramX;
	}

	public int getFramY() {
		return FramY;
	}

	public int getBoardSize() {
		return BoardSize;
	}
	public Image getImgPlayer1() {
		
		return imgPlayer1= new Image("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\child1.png");
	}
	public Image getImgPlayer2() {
		if (gameMode==2) return imgPlayer2= new Image("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\androd1.png");
		else
			return imgPlayer2= new Image("File:C:\\Users\\Acer\\Desktop\\MsComputer\\Msc-Project\\pics\\person.png");
	}
}
