package pl.edu.pw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FactoryDisplay implements Serializable {


	private List<Tile> tiles;

	public FactoryDisplay() {
		tiles = new ArrayList<>(4);
	}

	public FactoryDisplay(List<Tile> tiles) {
		this.tiles = tiles;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	/**
	 * Fills factories with tiles from bag
	 * NOTE: in specification there was an argument List<Tile> in function. There is no need to make this.
	 */
	public void refillTiles() {
		tiles = TileDispenser.getInstance().drawTiles(4 - tiles.size());
	}

	/**
	 * Removes all tiles of the same color from the factory and adds remaining tiles to the center of the table.
	 *
	 * @param color color of tiles to remove
	 * @return List<Tile> tiles that were removed
	 */
	public List<Tile> removeTiles(Tile.Color color) {
		List<Tile> tiles = this.tiles.stream().filter(tile -> tile.getColor() == color).toList();
		this.tiles.removeAll(tiles);
		CenterDisplay centerDisplay = CenterDisplay.getInstance();
		centerDisplay.addTiles(this.tiles);
		this.tiles.clear();
		return tiles;
	}

	/**
	 * Returns true if factory is empty
	 *
	 * @return boolean
	 */
	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	public boolean isColorInFactory(Tile.Color color) {
		return tiles.contains(new Tile(color));
	}

	public void removeAll() {
		tiles = new ArrayList<>();
	}
}
