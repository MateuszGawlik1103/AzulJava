package pl.edu.pw;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CenterDisplay implements Serializable {
	private static CenterDisplay instance = null;
	private List<Tile> tiles;

	public CenterDisplay() {
		tiles = new ArrayList<>();
	}

	//there is only one instance of object CenterDisplay
	public static synchronized CenterDisplay getInstance() {
		if (instance == null) {
			instance = new CenterDisplay();
		}
		return instance;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	/**
	 * Adds tiles to the center of the table
	 *
	 * @param tiles
	 */
	public void addTiles(List<Tile> tiles) {
		this.tiles.addAll(tiles);
	}

	/**
	 * Removes all tiles of the same color from the center of the table.
	 * @param color
	 * @return List<Tile> tiles that were removed
	 */
	public List<Tile> removeTiles(Tile.Color color) {
		List<Tile> removedTiles = this.tiles.stream().filter(tile -> tile.getColor() == color).toList();
		tiles.removeAll(removedTiles);
		return removedTiles;
	}

	/**
	 * Adds First Player Token to the center (at the beginning of the round)
	 */
	public void addFirstPlayerToken() {
		if (!tiles.contains(FirstPlayerToken.getInstance()))
			tiles.add(FirstPlayerToken.getInstance());
	}

	/**
	 * The player takes the first player token, if available.
	 */
	public void takeFirstPlayerToken() {
		tiles.remove(FirstPlayerToken.getInstance());
	}

	/**
	 * Checks whether First Player Token is currently in the center
	 * @return true if First Player Token is in the center, false if it was already taken
	 */
	public boolean isFirstPlayerTokenInCenter() {
		return tiles.contains(FirstPlayerToken.getInstance());
	}

	public boolean isColorInCenter(Tile.Color color) {
		return tiles.contains(new Tile(color));
	}

	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	/**
	 * Reset all state to the beginning of the game
	 */
	public void reset() {
		tiles.clear();
		addFirstPlayerToken();
	}


}
