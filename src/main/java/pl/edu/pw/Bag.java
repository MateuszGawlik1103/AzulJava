package pl.edu.pw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bag implements Serializable {
	private final List<Tile> tiles;

	public Bag(List<Tile> tiles) {
		this.tiles = tiles;
	}

	public Bag() {
		this.tiles = new ArrayList<>();
	}

	/**
	 * Add new tiles to the bag
	 *
	 * @param tiles list of tiles to add
	 */
	public void addTiles(List<Tile> tiles) {
		this.tiles.addAll(tiles);
	}

	/**
	 * Draw (get and remove) a given number of randomly selected tiles from the bag
	 *
	 * @param n number of tiles to draw
	 * @return list of drawn tiles
	 */
	public List<Tile> drawTiles(int n) {
		if (tiles.size() < n) {
			n = tiles.size();
		}
		if (n == 0) {
			return Collections.emptyList();
		}
		final Random random = new Random();
		final List<Tile> numbers = new ArrayList<>(
				random
						.ints(0, tiles.size())
						.distinct()
						.limit(n)
						.boxed()
						.sorted(Collections.reverseOrder())
						// length will decrease after removing an element, so we need to ensure each index will be smaller than the previous one
						.map(i -> tiles.remove(i.intValue()))
						.toList()
		);
		Collections.shuffle(numbers);
		return numbers;
	}


	/**
	 * Check if the bag is empty
	 *
	 * @return true if the bag has no tiles left, false otherwise
	 */
	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	@Override
	public String toString() {
		return "Bag{" +
				"tiles=" + tiles +
				'}';
	}
}
