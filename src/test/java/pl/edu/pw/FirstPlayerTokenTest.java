package pl.edu.pw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstPlayerTokenTest {

	@Test
	void testToString() {
		final var tile = FirstPlayerToken.getInstance();
		assertEquals("FirstPlayerToken", tile.toString());
	}
}