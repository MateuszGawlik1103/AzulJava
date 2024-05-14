package pl.edu.pw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {
	@BeforeEach
	@AfterEach
	void reset() {
		CenterDisplay.getInstance().reset();
		TileDispenser.getInstance().reset();
	}
	@Test
	void addToPatternLines() {
		var playerBoard = new PlayerBoard();
		var tile = new Tile(Tile.Color.BLUE);
		playerBoard.addToPatternLines(tile, 0);
		assertEquals(tile, playerBoard.getPatternLines()[0].get(0));
	}

	@Test
	void moveTilesToWall() {
		var playerBoard = new PlayerBoard();
		var tile = new Tile(Tile.Color.BLUE);
		playerBoard.addToPatternLines(tile, 0);
		playerBoard.addToPatternLines(tile, 0);
		assertDoesNotThrow(playerBoard::moveTilesToWall);
		assertEquals(0, playerBoard.getPatternLines()[0].size());
		assertEquals(tile, playerBoard.getWall()[0][0]);
	}

	@Test
	void addToFloorLine() {
		var playerBoard = new PlayerBoard();
		var tile = new Tile(Tile.Color.BLUE);
		playerBoard.addToFloorLine(tile);
		assertEquals(tile, playerBoard.getFloorLine().get(0));
	}

	@Test
	void clearPatternLines() {
		var playerBoard = new PlayerBoard();
		var tile = new Tile(Tile.Color.BLUE);
		playerBoard.addToPatternLines(tile, 0);
		playerBoard.addToPatternLines(tile, 0);
		playerBoard.clearPatternLines();
		assertEquals(0, playerBoard.getPatternLines()[0].size());
	}

	@Test
	void clearFloorLine() {
		var playerBoard = new PlayerBoard();
		var tile = new Tile(Tile.Color.BLUE);
		playerBoard.addToFloorLine(tile);
		playerBoard.clearFloorLine();
		assertEquals(0, playerBoard.getFloorLine().size());
	}

	@Test
	void hasFirstPlayerToken() {
		var playerBoard = new PlayerBoard();
		var tile = FirstPlayerToken.getInstance();
		playerBoard.addToFloorLine(tile);
		assertTrue(playerBoard.hasFirstPlayerToken());
	}
	@Test
	void getScore() {
		var playerBoard = new PlayerBoard();
		assertEquals(0, playerBoard.getScore());
		var tile = new Tile(Tile.Color.BLUE);
		playerBoard.addToPatternLines(tile, 0);
		assertDoesNotThrow(playerBoard::moveTilesToWall);
		assertEquals(1, playerBoard.getScore());
		playerBoard.addToPatternLines(tile, 1);
		playerBoard.addToPatternLines(tile, 1);
		playerBoard.addToPatternLines(tile, 1); // this tile will fall to the floor
		assertEquals(1, playerBoard.getScore());
	}
	@Test
	void updateScore() {
		var playerBoard = new PlayerBoard();
		assertEquals(0, playerBoard.getScore());
		playerBoard.updateScore(1);
		assertEquals(1, playerBoard.getScore());
		playerBoard.updateScore(1);
		assertEquals(2, playerBoard.getScore());
	}

	@Test
	void overflowFloorLine() {
		var playerBoard = new PlayerBoard();
		var tile = new Tile(Tile.Color.BLUE);
		for (int i=0; i<8; i++)
			playerBoard.addToFloorLine(tile);
		assertEquals(7, playerBoard.getFloorLine().size());
		assertEquals(0, playerBoard.getScore());
		assertEquals(1, TileDispenser.getInstance().getDiscardStack().size());
	}
	@Test
	void lineAlreadyFilled() throws Exceptions.AzulLineException {
		var playerBoard = new PlayerBoard();
		var tile = new Tile(Tile.Color.BLUE);
		playerBoard.addToPatternLines(tile, 0);
		playerBoard.moveTilesToWall();
		playerBoard.getPatternLines()[0].add(tile);
		assertThrows(Exceptions.AzulLineAlreadyFilledException.class, playerBoard::moveTilesToWall);
	}

	@Test
	void lineDifferentColors() {
		var playerBoard = new PlayerBoard();
		playerBoard.getPatternLines()[1].add(new Tile(Tile.Color.BLUE));
		playerBoard.getPatternLines()[1].add(new Tile(Tile.Color.RED));
		assertThrows(Exceptions.AzulLineDifferentColorsException.class, playerBoard::moveTilesToWall);
	}

	@Test
	void tileAlreadyAdded() throws Exceptions.AzulLineException {
		var playerBoard = new PlayerBoard();
		var tile = new Tile(Tile.Color.BLUE);
		playerBoard.addToPatternLines(tile, 0);
		playerBoard.moveTilesToWall();
		playerBoard.addToPatternLines(tile, 0);
		assertEquals(1, playerBoard.getFloorLine().size());
		assertEquals(tile, playerBoard.getFloorLine().get(0));
		assertEquals(0, playerBoard.getPatternLines()[0].size());
	}
}