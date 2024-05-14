package pl.edu.pw;

import pl.edu.pw.gui.GameController;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
	private final CenterDisplay centerDisplay;
	private final List<Player> players;
	private final FactoryDisplay[] factoryDisplays;
	private int currentPlayerIndex = 0;
	private int round = 1;
	private boolean roundEnded = false;
	private boolean gameEnded = false;

	/**
	 * WARNING: THIS CONSTRUCTOR IS FOR SERIALIZATION ONLY, DO NOT USE IN ANY OTHER CASE
	 */
	protected Game() {
		centerDisplay = null;
		players = null;
		factoryDisplays = null;
	}

	public Game(List<Player> players) {
		int n = players.size();
		if (n == 2) {
			this.factoryDisplays = new FactoryDisplay[5];
		} else if (n == 3) {
			this.factoryDisplays = new FactoryDisplay[7];
		} else if (n == 4) {
			this.factoryDisplays = new FactoryDisplay[9];
		} else {
			throw new IllegalArgumentException("zbyt wielu graczy");
		}

		// ensure the bag is full
		TileDispenser.getInstance().reset();

		for (int i = 0; i < factoryDisplays.length; i++) {
			factoryDisplays[i] = new FactoryDisplay();
			factoryDisplays[i].refillTiles();
		}

		this.centerDisplay = CenterDisplay.getInstance();
		this.players = players;
		Collections.shuffle(players);  //mixing the order of players. Index 0 of shuffled list is the starting player
		centerDisplay.addFirstPlayerToken(); //adding FirstPlayerToken to the center
	}

	/**
	 * Play a single offer phase turn, and if round has ended play the wall-tiling phase and reset
	 */
	public void playTurn(GameController gc) throws IOException {
		if (isRoundEnded()) {
			round++;
			roundEnded = true;
			for (Player player : players) {
				try {
					player.moveTilesToWall();
				} catch (Exceptions.AzulLineException e) {
					System.out.println("You can't move tiles to wall");
				}
			}
			if (isGameEnded()) {
				gameEnded = true;
				calculateScores();
				gc.changeToEnd(this);
			}
			resetRound();
		} else {
			roundEnded = false;
		}
	}

	/**
	 * Move player cursor to the next player
	 */
	public void nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	/**
	 * Checks if all factories and the center are empty, which means that the round is over
	 *
	 * @return true if round has ended, false otherwise
	 */
	public boolean isRoundEnded() {
		return Arrays.stream(factoryDisplays).allMatch(FactoryDisplay::isEmpty) && centerDisplay.isEmpty();
	}

	/**
	 * game ends when at least one player completes a row of tiles
	 *
	 * @return boolean
	 */
	public boolean isGameEnded() {
		for (Player player : players) {
			PlayerBoard playerBoard = player.getPlayerBoard();
			Tile[][] wall = playerBoard.getWall();

			for (Tile[] row : wall) {
				// check if row is full
				if (Arrays.stream(row).allMatch(Objects::nonNull)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * calculates bonus scores after the game is over
	 */
	public void calculateScores() {
		//bonus points for row
		for (Player player : players) {
			Tile[][] wall = player.getPlayerBoard().getWall();
			for (int i = 0; i < 5; i++) {
				if (Arrays.stream(wall[i]).allMatch(Objects::nonNull)) {
					player.getPlayerBoard().updateScore(2);
				}
			}
			//bonus points for column
			for (int i = 0; i < 5; i++) {
				final int column = i;
				if (Arrays.stream(wall).allMatch((row) -> row[column] != null)) {
					player.getPlayerBoard().updateScore(7);
				}
			}
			//bonus points for colors
			for (Tile.Color color : Tile.Color.gameColors) {
				Tile tile = new Tile(color);
				if (Arrays.stream(wall).allMatch((row) -> Arrays.asList(row).contains(tile))) {
					player.getPlayerBoard().updateScore(10);
				}
			}
			player.updateScore();
		}
	}

	/**
	 * checks if the given player is the one whose turn it is
	 * @param player player to check
	 * @return true if it's the given players turn, false otherwise
	 */
	public boolean isCurrentPlayer(Player player) {
		return players.get(currentPlayerIndex).equals(player);
	}

	/**
	 * refills factories and puts First Player Token to the center
	 */
	public void resetRound() {
		for (FactoryDisplay factoryDisplay : factoryDisplays) {
			factoryDisplay.refillTiles();
			if (TileDispenser.getInstance().isBagEmpty()) {
				TileDispenser.getInstance().refillBag();
				factoryDisplay.refillTiles();
			}
		}
		centerDisplay.addFirstPlayerToken();
	}

	public CenterDisplay getCenterDisplay() {
		return centerDisplay;
	}

	public FactoryDisplay[] getFactoryDisplays() {
		return factoryDisplays;
	}

	public int getRound() {
		return round;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
}
