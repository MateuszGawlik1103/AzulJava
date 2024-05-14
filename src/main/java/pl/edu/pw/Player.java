package pl.edu.pw;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
	private final String name;
	private final PlayerBoard playerBoard;
	private int score;

	public Player(String name) {
		this.name = name;
		this.playerBoard = new PlayerBoard();
		this.score = 0;
	}

	public PlayerBoard getPlayerBoard() {
		return playerBoard;
	}

	/**
	 * The player takes tiles of the same color from the selected factory.
	 * @param color color of the tiles to take
	 * @param factory to take the tiles from
	 * @param indexLine index of the line to place the tile in (0-indexed)
	 */
	public void takeTilesFromFactory(Tile.Color color, FactoryDisplay factory, int indexLine) {
		var tiles = factory.removeTiles(color);
		for (Tile tile : tiles) {
			playerBoard.addToPatternLines(tile, indexLine);
		}
	}

	/**
	 * The player takes tiles of the same color from the center and takes First Player Token if in the center
	 * @param color
	 * @param indexLine
	 */
	public void takeTilesFromCenter(Tile.Color color, int indexLine) {
		CenterDisplay centerDisplay = CenterDisplay.getInstance();
		var tiles = centerDisplay.removeTiles(color);
		if (centerDisplay.isFirstPlayerTokenInCenter()) {
			centerDisplay.takeFirstPlayerToken();
			playerBoard.addToFloorLine(FirstPlayerToken.getInstance());
		}
		for (Tile tile : tiles) {
			playerBoard.addToPatternLines(tile, indexLine);
		}
	}


	/**
	 * The player places the tiles on his board in one of the pattern lines.
	 * @param tile      to be placed
	 * @param lineIndex index of the line to place the tile in (0-indexed)
	 */
	public void addToPatternLines(Tile tile, int lineIndex) {
		playerBoard.addToPatternLines(tile, lineIndex);
	}

	/**
	 * The player places excess tiles on his floor line.
	 *
	 * @param tile
	 */
	public void addToFloorLine(Tile tile) {
		playerBoard.addToFloorLine(tile);
	}

	/**
	 * The player moves tiles from completed lines of patterns onto the wall
	 * NOTE: this function is redundant because it can be done automatically in PlayerBoard class.
	 */
	public void moveTilesToWall() throws Exceptions.AzulLineException {
		playerBoard.moveTilesToWall();
	}

	/**
	 * updates the player's score after each round or during the final tally.
	 */
	public void updateScore() {
		score = playerBoard.getScore();
	}


	public int getScore() {
		return score;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player player)) return false;

		if (score != player.score) return false;
		if (!name.equals(player.name)) return false;
		return playerBoard.equals(player.playerBoard);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + playerBoard.hashCode();
		result = 31 * result + score;
		return result;
	}
}
