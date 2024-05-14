package pl.edu.pw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FactoryDisplayTest {
	@BeforeEach
	@AfterEach
	void reset() {
		CenterDisplay.getInstance().reset();
		TileDispenser.getInstance().reset();
	}
	@Test
	void refillTiles() {
		var factoryDisplay = new FactoryDisplay();
		assertDoesNotThrow(() -> factoryDisplay.refillTiles());
	}

	@Test
	void removeTiles() {
		var factoryDisplay = new FactoryDisplay();
		assertDoesNotThrow(factoryDisplay::refillTiles);
		var tilesRemoved = factoryDisplay.removeTiles(Tile.Color.BLACK);
		assertTrue(tilesRemoved.stream().allMatch(tile -> tile.getColor() == Tile.Color.BLACK));
	}

	@Test
	void isNewEmpty() {
		var factoryDisplay = new FactoryDisplay();
		assertTrue(factoryDisplay.isEmpty());
	}
	@Test
	void isNotEmpty() {
		var factoryDisplay = new FactoryDisplay();
		var bag = new Bag(new ArrayList<>(List.of(new Tile(Tile.Color.RED), new Tile(Tile.Color.YELLOW), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLACK), new Tile(Tile.Color.WHITE), new Tile(Tile.Color.BLACK))));
		factoryDisplay.refillTiles();
		assertFalse(factoryDisplay.isEmpty());
	}
	@Test
	void isEmptyAfterRemoval() {
		var factoryDisplay = new FactoryDisplay();
		TileDispenser.getInstance().refillBag();
		factoryDisplay.refillTiles();
		assertFalse(factoryDisplay.isEmpty());
		factoryDisplay.removeTiles(Tile.Color.RED);
		factoryDisplay.removeTiles(Tile.Color.BLUE);
		factoryDisplay.removeTiles(Tile.Color.YELLOW);
		factoryDisplay.removeTiles(Tile.Color.WHITE);
		factoryDisplay.removeTiles(Tile.Color.BLACK);
		assertTrue(factoryDisplay.isEmpty());
	}
	@Test
	void isColorInFactory() {
		var factoryDisplay = new FactoryDisplay();
		var originalTiles = TileDispenser.getInstance().drawTiles(100);
		TileDispenser.getInstance().discardTiles(Collections.nCopies(10, new Tile(Tile.Color.RED)));
		TileDispenser.getInstance().refillBag();
		factoryDisplay.refillTiles();
		assertTrue(factoryDisplay.isColorInFactory(Tile.Color.RED));

		// cleanup
		TileDispenser.getInstance().drawTiles(100);
		TileDispenser.getInstance().discardTiles(originalTiles);
		TileDispenser.getInstance().refillBag();
	}
	@Test
	void removeAll() {
		var factoryDisplay = new FactoryDisplay();
		factoryDisplay.refillTiles();
		assertFalse(factoryDisplay.isEmpty());
		assertDoesNotThrow(factoryDisplay::removeAll);
		assertTrue(factoryDisplay.isEmpty());
	}
}