package pl.edu.pw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

	@Test
	void addTiles() {
		var bag = new Bag();
		assertDoesNotThrow(() -> bag.addTiles(
				new ArrayList<>(Arrays.asList(new Tile("red"), new Tile("blue"), new Tile("white")))));
	}

	@Test
	void drawAllTiles() {
		var bag = new Bag(new ArrayList<>(Arrays.asList(new Tile("red"), new Tile("blue"), new Tile("white"))));
		var tiles = bag.drawTiles(3);
		assertEquals(3, tiles.size());
		assertTrue(tiles.contains(new Tile("red")));
		assertTrue(tiles.contains(new Tile("blue")));
		assertTrue(tiles.contains(new Tile("white")));
	}

	@Test
	void isNewBagEmpty() {
		var bag = new Bag();
		assertTrue(bag.isEmpty());
	}
	@Test
	void isNewFullBagEmtpy() {
		var bag = new Bag(new ArrayList<>(Arrays.asList(new Tile("red"), new Tile("blue"), new Tile("white"))));
		assertFalse(bag.isEmpty());
	}
	@Test
	void isBagEmptyAfterDrawingAllTiles() {
		var bag = new Bag(new ArrayList<>(Arrays.asList(new Tile("red"), new Tile("blue"), new Tile("white"))));
		bag.drawTiles(3);
		assertTrue(bag.isEmpty());
	}
	@Test
	void isBagEmptyAfterDrawingSomeTiles() {
		var bag = new Bag(new ArrayList<>(Arrays.asList(new Tile("red"), new Tile("blue"), new Tile("white"))));
		bag.drawTiles(2);
		assertFalse(bag.isEmpty());
	}
	@Test
	void testToString() {
		var bag = new Bag(new ArrayList<>(Arrays.asList(new Tile(Tile.Color.RED), new Tile(Tile.Color.BLUE))));
		assertEquals("Bag{tiles=[Tile{color=RED}, Tile{color=BLUE}]}", bag.toString());
	}
}