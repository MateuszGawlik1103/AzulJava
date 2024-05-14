package pl.edu.pw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CenterDisplayTest {
	@BeforeEach
	@AfterEach
	void reset() {
		CenterDisplay.getInstance().reset();
		TileDispenser.getInstance().reset();
	}

	@AfterEach
	void tearDown() {
		for (Tile.Color color : Tile.Color.values()) {
			CenterDisplay.getInstance().removeTiles(color);
		}
	}

	@Test
	void getInstance() {
		assertSame(CenterDisplay.getInstance(), CenterDisplay.getInstance());
	}

	@Test
	void addTiles() {
		var tiles = List.of(
				new Tile(Tile.Color.BLUE), new Tile(Tile.Color.YELLOW), new Tile(Tile.Color.BLUE),
				new Tile(Tile.Color.BLACK)
		);
		assertDoesNotThrow(() -> CenterDisplay.getInstance().addTiles(tiles));
		CenterDisplay.getInstance().takeFirstPlayerToken();
		assertArrayEquals(tiles.toArray(), CenterDisplay.getInstance().getTiles().toArray());
	}


	@Test
	void removeTiles() {
		var tiles = List.of(
				new Tile(Tile.Color.BLUE), new Tile(Tile.Color.YELLOW), new Tile(Tile.Color.BLUE),
				new Tile(Tile.Color.BLACK)
		);
		assertDoesNotThrow(() -> CenterDisplay.getInstance().addTiles(tiles));
		var removedTiles = CenterDisplay.getInstance().removeTiles(Tile.Color.BLUE);
		assertEquals(2, removedTiles.size());
		assertTrue(removedTiles.stream().allMatch(tile -> tile.getColor() == Tile.Color.BLUE));
	}

	@Test
	void firstPlayerToken() {
		assertDoesNotThrow(CenterDisplay.getInstance()::addFirstPlayerToken);
		assertTrue(CenterDisplay.getInstance().isFirstPlayerTokenInCenter());
		assertDoesNotThrow(CenterDisplay.getInstance()::takeFirstPlayerToken);
		assertFalse(CenterDisplay.getInstance().isFirstPlayerTokenInCenter());
	}

	@Test
	void isColorInCenter() {
		var tiles = List.of(
				new Tile(Tile.Color.BLUE), new Tile(Tile.Color.YELLOW), new Tile(Tile.Color.BLUE),
				new Tile(Tile.Color.BLACK)
		);
		assertDoesNotThrow(() -> CenterDisplay.getInstance().addTiles(tiles));
		assertTrue(CenterDisplay.getInstance().isColorInCenter(Tile.Color.BLUE));
		assertFalse(CenterDisplay.getInstance().isColorInCenter(Tile.Color.RED));
	}
}