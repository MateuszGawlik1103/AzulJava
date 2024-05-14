package pl.edu.pw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
	@BeforeEach
	@AfterEach
	void reset() {
		CenterDisplay.getInstance().reset();
		TileDispenser.getInstance().reset();
	}
	@Test
	void getPlayerBoard() {
		var player = new Player("test");
		assertNotNull(player.getPlayerBoard());
	}

	@Test
	void addToPatternLines() {
		var player = new Player("test");
		assertEquals(0, player.getPlayerBoard().getPatternLines()[0].size());
		player.addToPatternLines(new Tile(Tile.Color.BLUE), 0);
		assertEquals(1, player.getPlayerBoard().getPatternLines()[0].size());
	}

	@Test
	void addToFloorLine() {
		var player = new Player("test");
		assertEquals(0, player.getPlayerBoard().getFloorLine().size());
		player.addToFloorLine(new Tile(Tile.Color.BLUE));
		assertEquals(1, player.getPlayerBoard().getFloorLine().size());
	}

	@Test
	void moveTilesToWall() {
		var player = new Player("test");
		assertEquals(0, Arrays.stream(player.getPlayerBoard().getWall()[4]).filter(Objects::nonNull).count());
		var factory = new FactoryDisplay(new ArrayList<>(List.of(new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE))));
		var tiles = factory.removeTiles(Tile.Color.BLUE);
		for (var tile : tiles) {
			player.addToPatternLines(tile, 4);
		}
		assertDoesNotThrow(player::moveTilesToWall);
		assertEquals(1, Arrays.stream(player.getPlayerBoard().getWall()[4]).filter(Objects::nonNull).count());
	}

	@Test
	void getScore() {
		var player = new Player("test");
		assertEquals(0, player.getScore());
		var factory = new FactoryDisplay(new ArrayList<>(List.of(new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE))));
		var tiles = factory.removeTiles(Tile.Color.BLUE);
		for (var tile : tiles) {
			player.addToPatternLines(tile, 4);
		}
		assertDoesNotThrow(player::moveTilesToWall);
		assertEquals(0, player.getScore());
		player.updateScore();
		assertEquals(1, player.getScore());
	}

	@Test
	void getName() {
		var player = new Player("test");
		assertEquals("test", player.getName());
	}

	@Test
	void takeFromCenter() {
		var player = new Player("test");
		CenterDisplay.getInstance().addTiles(new ArrayList<>(List.of(new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.RED), new Tile(Tile.Color.BLACK))));
		player.takeTilesFromCenter(Tile.Color.BLUE, 1);
		assertEquals(2, player.getPlayerBoard().getPatternLines()[1].size());
		assertTrue(player.getPlayerBoard().getPatternLines()[1].stream().allMatch(tile -> tile.getColor() == Tile.Color.BLUE));
		assertEquals(2, CenterDisplay.getInstance().getTiles().size());
	}
	@Test
	void takeFromFactory() {
		var player = new Player("test");
		var factory = new FactoryDisplay(new ArrayList<>(List.of(new Tile(Tile.Color.BLUE), new Tile(Tile.Color.BLUE), new Tile(Tile.Color.RED), new Tile(Tile.Color.BLACK))));
		player.takeTilesFromFactory(Tile.Color.BLUE, factory, 1);
		assertEquals(2, player.getPlayerBoard().getPatternLines()[1].size());
		assertTrue(player.getPlayerBoard().getPatternLines()[1].stream().allMatch(tile -> tile.getColor() == Tile.Color.BLUE));
		assertEquals(0, factory.getTiles().size());
		assertEquals(3, CenterDisplay.getInstance().getTiles().size());
	}
}