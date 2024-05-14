package pl.edu.pw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileDispenser implements Serializable {
	private static TileDispenser instance = null;
	private final Bag bag;
	private final ArrayList<Tile> discardStack;

	public TileDispenser() {
		discardStack = new ArrayList<>();
		bag = new Bag();
	}

	//there is only one instance of object TileDispenser
	public static synchronized TileDispenser getInstance() {
		if (instance == null) {
			instance = new TileDispenser();
		}
		return instance;
	}

	/**
	 * Sets the instance of TileDispenser, should only be used for loading a game
	 * @param tileDispenser the instance to set
	 */
	public static synchronized void setInstance(TileDispenser tileDispenser) {
		instance = tileDispenser;
	}

	public ArrayList<Tile> getDiscardStack() {
		return discardStack;
	}

	/**
	 * Draws n tiles from the bag
	 * NOTE: this method is already defined in bag.
	 */
	public List<Tile> drawTiles(int n) {
		return bag.drawTiles(n);
	}

	/**
	 * Discards used tiles, moving them to the discard tiles
	 * @param tiles
	 */
	public void discardTiles(List<Tile> tiles) {
		discardStack.addAll(tiles);
	}

	public void discardTiles(Tile tile) {
		discardStack.add(tile);
	}

	/**
	 * Refills the bag with tiles from the discard tiles (when the bag is empty)
	 */
	public void refillBag() {
		bag.addTiles(discardStack.subList(0, Math.min(100, discardStack.size())));
		discardStack.clear();
	}

	/**
	 * Checks if the bag is empty
	 * @return true if the bag is empty, false otherwise
	 */
	public boolean isBagEmpty() {
		return bag.isEmpty();
	}

	/**
	 * Reset all state to the beginning of the game
	 */
	public void reset() {
		bag.drawTiles(200);
		discardStack.clear();
		// fill the bag with 100 tiles - 20 of each color
		for (Tile.Color color : Tile.Color.gameColors) {
			bag.addTiles(Collections.nCopies(20, new Tile(color)));
		}
	}
}
