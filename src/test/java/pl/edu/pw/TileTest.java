package pl.edu.pw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

	@Test
	void getColor() {
		final var tile = new Tile(Tile.Color.RED);
		assertEquals(Tile.Color.RED, tile.getColor());
	}
	@Test
	void createStringColor() {
		final var tile = new Tile("red");
		assertEquals(Tile.Color.RED, tile.getColor());
	}

	@Test
	void testToString() {
		final var tile = new Tile(Tile.Color.RED);
		assertEquals("Tile{color=RED}", tile.toString());
	}
	@Test
	void colorAt() {
		assertEquals(Tile.Color.at(0), Tile.Color.BLUE);
		assertEquals(Tile.Color.at(1), Tile.Color.YELLOW);
		assertEquals(Tile.Color.at(2), Tile.Color.RED);
		assertEquals(Tile.Color.at(3), Tile.Color.BLACK);
		assertEquals(Tile.Color.at(4), Tile.Color.WHITE);
	}
}