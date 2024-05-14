package pl.edu.pw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

	/**
	 * Displays the state of the game, such as points, player boards, factory, etc.
	 */
	public static void displayGameState(List<Player> players, FactoryDisplay[] factoryDisplays) {
		for (Player player : players) {
			System.out.println(player.getName() + " ) points: " + player.getScore());
			System.out.println("Wall : " + Arrays.deepToString(player.getPlayerBoard().getWall()));
			System.out.println("PatternLine: " + Arrays.toString(player.getPlayerBoard().getPatternLines()));
			System.out.println("FloorLine: " + player.getPlayerBoard().getFloorLine());
			System.out.println();
		}
		int i = 0;
		for (FactoryDisplay factoryDisplay : factoryDisplays) {
			System.out.println(i + ") " + factoryDisplay.getTiles().toString());
			i++;
		}
		System.out.println("Discard Stack: " + TileDispenser.getInstance().getDiscardStack());
	}

	public static void main(String[] args) {
		ArrayList<Player> players = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);
		boolean addPlayer = true;
		while (addPlayer) {
			System.out.println("Enter player name: ");
			String name = scanner.nextLine();
			players.add(new Player(name));
			if (players.size() > 1) {
				System.out.println("Add another player? (y/n)");
				String answer = scanner.nextLine();
				if (answer.equalsIgnoreCase("n")) {
					addPlayer = false;
				}
			}
		}
		Game game = new Game(players);
		while (!game.isGameEnded()) {
			Player player = players.get(game.getCurrentPlayerIndex());
			FactoryDisplay[] factoryDisplays = game.getFactoryDisplays();
			CenterDisplay centerDisplay = game.getCenterDisplay();

			if (game.isRoundEnded()) {
				System.out.println("Round ended, but the game is not over yet.");
				displayGameState(players, factoryDisplays);
			}

			System.out.println("Player: " + player.getName());
			System.out.println("1) Take from center");
			System.out.println("2) Take from factory");

			System.out.println("Tiles in center: " + centerDisplay.getTiles());
			System.out.println("Tiles in factories: ");
			for(int i = 0 ; i < factoryDisplays.length; i++){
				System.out.println(i + ") " + factoryDisplays[i].getTiles().toString());
			}

			int from = scanner.nextInt();
			switch (from) {
				// taking from center
				case 1 : {
					boolean cont = true;
					Tile.Color color = null;
					if(centerDisplay.isFirstPlayerTokenInCenter()){
						centerDisplay.takeFirstPlayerToken();
						player.addToFloorLine(FirstPlayerToken.getInstance());
					}

					while(cont){
						scanner.nextLine();
						System.out.println("Pick a color: ");
						color = Tile.Color.from(scanner.nextLine());
						cont = false;
						if(!centerDisplay.isColorInCenter(color)){
							cont = true;
							System.out.println("There is no such color in the center");
						}
					}
					System.out.println("Pick a pattern line index (1 to 5): ");
					System.out.println(Arrays.toString(player.getPlayerBoard().getPatternLines()));
					int indexLine = scanner.nextInt() -1;
					var removedTiles = centerDisplay.removeTiles(color);
					for(Tile tile : removedTiles){
						player.addToPatternLines(tile, indexLine);
					}
					break;
				}
				//taking from factories
				case 2: {
					System.out.println("pick a factory (from 0 to " + (factoryDisplays.length-1) + "): " );
					int factoryIndex = scanner.nextInt();
					FactoryDisplay factory = factoryDisplays[factoryIndex];
					boolean cont = true;
					Tile.Color color = null;
					scanner.nextLine(); //consuming new line sign
					while(cont){
						System.out.println("pick a color: ");
						color = Tile.Color.from(scanner.nextLine());
						cont = false;
						if(!factory.isColorInFactory(color)){
							cont = true;
							System.out.println("there is no such color in factory");
						}
					}
					System.out.println("pick a pattern line (1-5): ");
					System.out.println(Arrays.toString(player.getPlayerBoard().getPatternLines()));
					int lineIndex = scanner.nextInt() - 1;
					List<Tile> removedTiles = factory.removeTiles(color);
					for(Tile tile : removedTiles){ // adding tiles to pattern line
						player.addToPatternLines(tile, lineIndex);
					}
					break;
				}
			}
			displayGameState(players, factoryDisplays);
			//game.playTurn();
			game.nextPlayer();
		}
		System.out.println("Game ended");
		System.out.println("Scores: ");
		Player winner = players.get(0);
		for(Player player : players){
			System.out.println(player.getName() + " : " + player.getScore());
			if(player.getScore() > winner.getScore()){
				winner = player;
			}
		}
		System.out.println("Winner: " + winner.getName());
	}
}
