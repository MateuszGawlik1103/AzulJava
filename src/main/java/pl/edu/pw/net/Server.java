package pl.edu.pw.net;

import pl.edu.pw.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Server {
	private int port;
	private Game game;
	private ServerSocket serverSocket;
	private List<Socket> clients;
	private Map<Socket, Player> clientPlayers;
	private boolean gameStarted = false;

	public Server(int port) {
		this.port = port;
	}

	public void start() {
		try {
			serverSocket = new ServerSocket(port);
			while (!gameStarted) {
				Socket socket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(socket);
				clientHandler.start();
			}

		} catch (IOException ignored) {
			// this exception is thrown when the serverSocket is closed, so there is nothing to do here
		}

	}

	public void stop() {
		try {
			serverSocket.close();
		} catch (IOException ignored) {
			// we're stopping the server anyway
		}
	}

	private void updateGame() throws IOException {
		var byteOutputStream = new ByteArrayOutputStream();
		var objectOutputStream = new ObjectOutputStream(byteOutputStream);
		objectOutputStream.writeObject(game);
		objectOutputStream.flush();
		String encoded = Base64.getEncoder().encodeToString(byteOutputStream.toByteArray());

		var message = Protocol.UPDATEGAME +
				" " +
				encoded;
		for (Socket client : clients) {
			var stream = client.getOutputStream();
			var writer = new PrintWriter(stream, true);
			writer.println(message);
		}
	}

	public void startGame() throws IOException {
		if (gameStarted) {
			return;
		}
		if (game == null)
			game = new Game(clientPlayers.values().stream().toList());
		gameStarted = true;
		updateGame();
	}

	/**
	 * Stops the game
	 * @throws IOException
	 */
	public void stopGame() throws IOException {
		if (!gameStarted) {
			return;
		}
		gameStarted = false;
		updateGame();
	}

	/**
	 * Removes the given player from the game
	 * @param p the player to remove
	 */
	public void kickPlayer(Player p) {
		if (clientPlayers.containsValue(p)) {
			var socket = clientPlayers.entrySet().stream()
					.filter(entry -> entry.getValue().equals(p))
					.map(Map.Entry::getKey)
					.findFirst()
					.orElseThrow();
			clients.remove(socket);
			clientPlayers.remove(socket);
		}
	}

	/**
	 * Saves the game to the given file, including the tile dispenser state
	 *
	 * @param f the file to save the game to
	 */
	public void saveGame(File f) {
		try (
				var fileOutputStream = new FileOutputStream(f);
				var objectOutputStream = new ObjectOutputStream(fileOutputStream)
		) {
			SaveGameState saveGameState = new SaveGameState(game, TileDispenser.getInstance());
			objectOutputStream.writeObject(saveGameState);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Loads the game from the given file, including the tile dispenser state
	 *
	 * @param f the file to load the game from
	 */
	public void loadGame(File f) {
		try (
				var fileInputStream = new FileInputStream(f);
				var objectInputStream = new ObjectInputStream(fileInputStream)
		) {
			var saveGameState = (SaveGameState) objectInputStream.readObject();
			game = saveGameState.getGame();
			TileDispenser.setInstance(saveGameState.getTileDispenser());
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

	}

	private static class SaveGameState implements Serializable {
		private final Game game;
		private final TileDispenser tileDispenser;

		public SaveGameState() {
			game = null;
			tileDispenser = null;
		}

		public SaveGameState(Game game, TileDispenser tileDispenser) {
			this.game = game;
			this.tileDispenser = tileDispenser;
		}

		public Game getGame() {
			return game;
		}

		public TileDispenser getTileDispenser() {
			return tileDispenser;
		}
	}

	private class ClientHandler extends Thread {
		private final Socket socket;
		private Player player;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try (var inputStream = new InputStreamReader(socket.getInputStream())) {
				var reader = new BufferedReader(inputStream);
				String line;
				while ((line = reader.readLine()) != null) {
					Scanner data = new Scanner(line);
					Protocol command = Protocol.valueOf(data.next("(HELLO|TAKEFROMCENTER|TAKEFROMFACTORY)"));
					switch (command) {
						case HELLO -> {
							// format: HELLO <name>
							var name = data.next();
							if (clientPlayers.values().stream().anyMatch((p) -> p.getName().equals(name))) {
								try (var outputStream = new PrintWriter(socket.getOutputStream(), true)) {
									outputStream.println(Protocol.HELLO + " " + "NAME_TAKEN");
								}
								return;
							}
							player = game.getPlayers().stream().filter((p) -> p.getName().equals(name)).findAny().orElse(new Player(name));
							clients.add(socket);
							clientPlayers.put(socket, player);
							updateGame();
						}
						case TAKEFROMCENTER -> {
							// format: TAKEFROMCENTER <color> <lineIndex>
							if (!game.isCurrentPlayer(player)) {
								break;
							}
							Tile.Color color = Tile.Color.from(data.next());
							int lineIndex = data.nextInt();
							clientPlayers.get(socket).takeTilesFromCenter(color, lineIndex);
							//game.playTurn();
							game.nextPlayer();
							updateGame();
						}
						case TAKEFROMFACTORY -> {
							// format: TAKEFROMFACTORY <color> <lineIndex> <factoryDisplayIndex>
							if (!game.isCurrentPlayer(player)) {
								break;
							}
							Tile.Color color = Tile.Color.from(data.next());
							int lineIndex = data.nextInt();
							FactoryDisplay factoryDisplay = game.getFactoryDisplays()[data.nextInt()];
							clientPlayers.get(socket).takeTilesFromFactory(color, factoryDisplay, lineIndex);
							//game.playTurn();
							game.nextPlayer();
							updateGame();
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
