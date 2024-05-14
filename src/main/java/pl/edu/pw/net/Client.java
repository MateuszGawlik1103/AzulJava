package pl.edu.pw.net;

import pl.edu.pw.Exceptions;
import pl.edu.pw.Game;
import pl.edu.pw.Player;
import pl.edu.pw.Tile;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class Client {
	private Socket socket;
	private Player player;
	private Game game;

	public Client(Player player) {
		this.player = player;
	}

	/**
	 * Takes tiles from the center
	 * @param color - color of the tiles to take
	 * @param lineIndex - index of the line to put the tiles in
	 * @throws IOException - thrown when there is a problem with the connection
	 */
	public void takeFromCenter(Tile.Color color, int lineIndex) throws IOException {
		try (
				var out = new PrintWriter(socket.getOutputStream(), true)
		) {
			out.println(Protocol.TAKEFROMCENTER + " " + color.toString() + " " + lineIndex);
			out.flush();
		}
	}

	/**
	 * Takes tiles from the factory
	 * @param color - color of the tiles to take
	 * @param factoryIndex - index of the factory to take the tiles from
	 * @param lineIndex - index of the line to put the tiles in
	 * @throws IOException - thrown when there is a problem with the connection
	 */
	public void takeFromFactory(Tile.Color color, int factoryIndex, int lineIndex) throws IOException {
		try (
				var out = new PrintWriter(socket.getOutputStream(), true)
		) {
			out.println(Protocol.TAKEFROMFACTORY + " " + color.toString() + " " + lineIndex + " " + factoryIndex);
			out.flush();
		}
	}

	/**
	 * Connects to the server
	 * warning - there is currently no main loop here, the frontend will need to handle that
	 * @param server - server host (optionally with a port after a colon)
	 */
	public void connect(String server) throws IOException, Exceptions.AzulNameTakenException, ClassNotFoundException {
		int ip = 42842;
		if (server.contains(":")) {
			String[] split = server.split(":");
			server = split[0];
			ip = Integer.parseInt(split[1]);
		}
		socket = new Socket(server, ip);
		try (
				var out = new PrintWriter(socket.getOutputStream(), true);
				var in = new InputStreamReader(socket.getInputStream())
		) {
			var reader = new BufferedReader(in);
			out.println(Protocol.HELLO + " " + player.getName());
			out.flush();
			String line = reader.readLine();
			if (line.startsWith(Protocol.HELLO.toString())) {
				throw new Exceptions.AzulNameTakenException();
			} else if (line.startsWith(Protocol.UPDATEGAME.toString())) {
				var encoded = Base64.getDecoder().decode(line.substring(Protocol.UPDATEGAME.toString().length() + 1));
				var objectInputStream = new ObjectInputStream(new ByteArrayInputStream(encoded));
				game = (Game) objectInputStream.readObject();
				player = game.getPlayers().stream().filter(
						p -> p.getName().equals(player.getName())).findFirst().orElseGet(() -> player);
			} else {
				throw new RuntimeException("Unknown command: " + line);
			}
		}
	}

	public void disconnect() throws IOException {
		socket.close();
	}
}
