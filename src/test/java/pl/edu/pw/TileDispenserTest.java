package pl.edu.pw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TileDispenserTest {
	@BeforeEach
	@AfterEach
	void reset() {
		CenterDisplay.getInstance().reset();
		TileDispenser.getInstance().reset();
	}

	@Test
	void getInstance() {
		assertSame(TileDispenser.getInstance(), TileDispenser.getInstance());
	}

	@Test
	void getDiscardStack() {
		TileDispenser.getInstance().discardTiles(TileDispenser.getInstance().drawTiles(5));
		assertEquals(5, TileDispenser.getInstance().getDiscardStack().size());
	}

	@Test
	void drawTiles() {
		var t1 = TileDispenser.getInstance().drawTiles(4);
		assertEquals(4, t1.size());
		var t2 = TileDispenser.getInstance().drawTiles(100);
		assertEquals(96, t2.size());
		assertEquals(0, TileDispenser.getInstance().drawTiles(100).size());
		TileDispenser.getInstance().discardTiles(t1);
		TileDispenser.getInstance().discardTiles(t2);
		assertDoesNotThrow(TileDispenser.getInstance()::refillBag);
		assertEquals(100, TileDispenser.getInstance().drawTiles(100).size());
	}

	@Test
	void discardTile() {
		TileDispenser.getInstance().discardTiles(new Tile(Tile.Color.RED));
		assertEquals(1, TileDispenser.getInstance().getDiscardStack().size());
		assertEquals(Tile.Color.RED, TileDispenser.getInstance().getDiscardStack().get(0).getColor());
	}

	@Test
	void discardTiles() {
		var tiles = List.of(
				new Tile(Tile.Color.BLUE), new Tile(Tile.Color.YELLOW), new Tile(Tile.Color.BLUE),
				new Tile(Tile.Color.BLACK)
		);
		TileDispenser.getInstance().discardTiles(tiles);
		assertEquals(4, TileDispenser.getInstance().getDiscardStack().size());
		assertArrayEquals(tiles.toArray(), TileDispenser.getInstance().getDiscardStack().toArray());
	}

	@Test
	void refillBag() {
		var tiles = TileDispenser.getInstance().drawTiles(100);
		assertTrue(TileDispenser.getInstance().isBagEmpty());
		assertEquals(0, TileDispenser.getInstance().drawTiles(100).size());
		TileDispenser.getInstance().discardTiles(tiles);
		TileDispenser.getInstance().refillBag();
		assertFalse(TileDispenser.getInstance().isBagEmpty());
		tiles = TileDispenser.getInstance().drawTiles(100);
		assertEquals(100, tiles.size());
	}

	@Test
	void isBagEmpty() {
		assertFalse(TileDispenser.getInstance().isBagEmpty());
		TileDispenser.getInstance().drawTiles(100);
		assertTrue(TileDispenser.getInstance().isBagEmpty());
	}
}