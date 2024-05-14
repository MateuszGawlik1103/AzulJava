package pl.edu.pw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
	@BeforeEach
	@AfterEach
	void reset() {
		CenterDisplay.getInstance().reset();
		TileDispenser.getInstance().reset();
	}
	@Test
	void playTurnWithoutChanges() {
		var players = new ArrayList<>(Arrays.asList(new Player("a"), new Player("b")));
		var game = new Game(players);
		game.playTurn();
		assertFalse(game.isRoundEnded());
		assertFalse(game.isGameEnded());
		assertEquals(1, game.getRound());
	}

	@Test
	void nextPlayer() {
		var players = new ArrayList<>(Arrays.asList(new Player("a"), new Player("b")));
		var game = new Game(players);
		assertEquals(0, game.getCurrentPlayerIndex());
		game.nextPlayer();
		assertEquals(1, game.getCurrentPlayerIndex());
		game.nextPlayer();
		assertEquals(0, game.getCurrentPlayerIndex());
	}

	@Test
	void isRoundEnded() {
		var players = new ArrayList<>(Arrays.asList(new Player("a"), new Player("b")));
		var game = new Game(players);
		assertFalse(game.isRoundEnded());
		for (var factory : game.getFactoryDisplays()) {
			factory.removeAll();
		}
		game.getCenterDisplay().takeFirstPlayerToken();
		assertTrue(game.isRoundEnded());
	}

	@Test
	void isGameEnded() throws Exceptions.AzulLineException {
		var players = new ArrayList<>(Arrays.asList(new Player("a"), new Player("b")));
		var game = new Game(players);
		assertFalse(game.isGameEnded());
		var player = players.get(0);
		for (var color : Tile.Color.gameColors) {
			player.getPlayerBoard().addToPatternLines(new Tile(color), 0);
			player.getPlayerBoard().addToPatternLines(new Tile(color), 0);
			player.moveTilesToWall();
		}
		assertTrue(game.isGameEnded());
	}
	@Test
	void resetRound() {
		var players = new ArrayList<>(Arrays.asList(new Player("a"), new Player("b")));
		var game = new Game(players);
		assertTrue(game.getCenterDisplay().isFirstPlayerTokenInCenter());
		assertFalse(Arrays.stream(game.getFactoryDisplays()).anyMatch(FactoryDisplay::isEmpty));
		game.getCenterDisplay().takeFirstPlayerToken();
		for (var factory : game.getFactoryDisplays()) {
			factory.removeAll();
		}
		game.getCenterDisplay().takeFirstPlayerToken();
		assertFalse(game.getCenterDisplay().isFirstPlayerTokenInCenter());
		assertTrue(Arrays.stream(game.getFactoryDisplays()).allMatch(FactoryDisplay::isEmpty));

		game.resetRound();
		assertTrue(game.getCenterDisplay().isFirstPlayerTokenInCenter());
		assertFalse(Arrays.stream(game.getFactoryDisplays()).anyMatch(FactoryDisplay::isEmpty));
	}

	@Test
	void getCenterDisplay() {
		assertSame(CenterDisplay.getInstance(), new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b")))).getCenterDisplay());
	}

	@Test
	void getFactoryDisplays() {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		var factoryDisplays = game.getFactoryDisplays();
		assertEquals(5, factoryDisplays.length);
		for (var factoryDisplay : factoryDisplays) {
			assertNotNull(factoryDisplay);
			assertFalse(factoryDisplay.isEmpty());
		}
	}

	@Test
	void getRound() {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		assertEquals(1, game.getRound());
		game.playTurn();
		assertEquals(1, game.getRound());
		for (var factory : game.getFactoryDisplays()) {
			factory.removeAll();
		}
		game.getCenterDisplay().takeFirstPlayerToken();
		game.playTurn();
		assertEquals(2, game.getRound());
	}

	@Test
	void getPlayers() {
		var players = new ArrayList<>(Arrays.asList(new Player("a"), new Player("b")));
		var game = new Game(players);
		assertSame(players, game.getPlayers());
	}

	@Test
	void factoryCount() {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		assertEquals(5, game.getFactoryDisplays().length);
		game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"), new Player("c"))));
		assertEquals(7, game.getFactoryDisplays().length);
		game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"), new Player("c"), new Player("d"))));
		assertEquals(9, game.getFactoryDisplays().length);
	}

	@Test
	void scoreNoBonus() {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		var player = game.getPlayers().get(0);
		game.calculateScores();
		assertEquals(0, player.getScore());
	}
	@Test
	void scoreRowBonus() throws Exceptions.AzulLineException {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		var player = game.getPlayers().get(0);
		int i=0;
		for (var color : Tile.Color.gameColors) {
			player.getPlayerBoard().getWall()[0][i++] = new Tile(color);
		}
		game.calculateScores();
		assertEquals(2, player.getScore());
	}
	@Test
	void scoreColumnBonus() {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		var player = game.getPlayers().get(0);
		int i=0;
		for (var color : Tile.Color.gameColors) {
			player.getPlayerBoard().getWall()[i++][0] = new Tile(color);
		}
		game.calculateScores();
		assertEquals(7, player.getScore());
	}
	@Test
	void scoreDiagonalBonus() {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		var player = game.getPlayers().get(0);
		for (int i=0; i<5; i++) {
			player.getPlayerBoard().getWall()[i][i] = new Tile(Tile.Color.BLUE);
		}
		game.calculateScores();
		assertEquals(10, player.getScore());
	}
	@Test
	void scoreAllBonus() {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		var player = game.getPlayers().get(0);
		for (int i=0; i<5; i++) {
			player.getPlayerBoard().getWall()[i][i] = new Tile(Tile.Color.BLUE);
		}
		int i=0;
		for (var color : Tile.Color.gameColors) {
			player.getPlayerBoard().getWall()[0][i] = new Tile(color);
			player.getPlayerBoard().getWall()[i++][0] = new Tile(color);
		}
		game.calculateScores();
		assertEquals(19, player.getScore());
	}
	@Test
	void serializationTest() throws IOException, ClassNotFoundException {
		var game = new Game(new ArrayList<>(Arrays.asList(new Player("a"), new Player("b"))));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(game);
		byte[] result = out.toByteArray();
		oos.close();

		ByteArrayInputStream in = new ByteArrayInputStream(result);
		ObjectInputStream ois = new ObjectInputStream(in);
		Object deserialized = ois.readObject();
		ois.close();
		Game deserializedGame = (Game) deserialized;
		assertEquals(game.getRound(), deserializedGame.getRound());
		assertEquals(game.getPlayers().size(), deserializedGame.getPlayers().size());
		assertEquals(game.getPlayers().get(0).getName(), deserializedGame.getPlayers().get(0).getName());
		assertEquals(game.getPlayers().get(1).getName(), deserializedGame.getPlayers().get(1).getName());
		assertEquals(game.getPlayers().get(0).getScore(), deserializedGame.getPlayers().get(0).getScore());
		assertEquals(game.isGameEnded(), deserializedGame.isGameEnded());
		assertEquals(game.isRoundEnded(), deserializedGame.isRoundEnded());
		assertEquals(game.getCenterDisplay().isFirstPlayerTokenInCenter(), deserializedGame.getCenterDisplay().isFirstPlayerTokenInCenter());
		assertEquals(game.getCenterDisplay().getTiles().size(), deserializedGame.getCenterDisplay().getTiles().size());
		assertEquals(game.getFactoryDisplays().length, deserializedGame.getFactoryDisplays().length);
		for (int i=0; i<game.getFactoryDisplays().length; i++) {
			assertEquals(game.getFactoryDisplays()[i].getTiles().size(), deserializedGame.getFactoryDisplays()[i].getTiles().size());
		}


	}
}