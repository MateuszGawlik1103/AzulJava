package pl.edu.pw.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import pl.edu.pw.Game;
import pl.edu.pw.Player;
import pl.edu.pw.Tile;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for the board screen.
 */
public class BoardController {
    private Player player;
    private Game game;
    private GameController gc;

    /**
     * Initializes the board screen with the player, game, and game controller objects.
     *
     * @param p  The player object.
     * @param g  The game object.
     * @param gc The game controller object.
     */
    public void init(Player p, Game g, GameController gc) {
        player = p;
        game = g;
        this.gc = gc;
        regenerateBoard();
    }

    /**
     * Regenerates the board by generating the score track, pattern lines, wall, floor, and pick button.
     */
    public void regenerateBoard() {
        generateScoreTrack();
        generatePatternLine();
        generateWall();
        generateFloor();
        generatePickBtn();
        playerBoard.setText(player.getName());
    }

    @FXML
    private GridPane patterns_grid;
    @FXML
    private GridPane score_grid;
    @FXML
    private GridPane wall_grid;
    @FXML
    private GridPane floor_grid;
    @FXML
    private GridPane pick_btn;
    @FXML
    private Text playerBoard;

    /**
     * Initializes the controller.
     */
    public void initialize() {

    }

    /**
     * Generating buttons, which will pick up chosen tile
     */
    private void generatePickBtn() {
        if (player.getPlayerBoard().isHasToPick()) {
            for (int i = 0; i < 5; i++) {
                Button b = new Button("+");
                b.setStyle("-fx-background-color: #cbc367; -fx-border-radius: 6; -fx-background-radius:6; -fx-text-fill: #fff; -fx-border-color:transparent;-fx-font-size: 12;");
                final int finalI = i;
                b.setOnAction(event -> {
                    if (player.getPlayerBoard().getPickedFactory() != null) {
                        player.takeTilesFromFactory(player.getPlayerBoard().getPickedTile().getColor(), player.getPlayerBoard().getPickedFactory(), finalI);
                        player.getPlayerBoard().setPickedFactory(null);
                    } else {
                        player.takeTilesFromCenter(player.getPlayerBoard().getPickedTile().getColor(), finalI);
                    }
                    player.getPlayerBoard().setPickedTile(null);
                    player.getPlayerBoard().setHasToPick(false);
                    regenerateBoard();
                    game.nextPlayer();
                    try {
                        game.playTurn(gc);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        gc.changeToFactory();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                pick_btn.add(b, 0, i);
            }
        }
    }

    /**
     * Generating score board
     */
    private void generateScoreTrack() {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 20; c++) {
                int number = 20 * r + c + 1;
                Button button = new Button(String.valueOf(number));
                button.setStyle("-fx-background-color: #fff; -fx-border-radius: 0; -fx-background-radius:0; -fx-border-color:#000;-fx-font-size: 9;");
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);

                if (number % 5 == 0) {
                    button.setStyle("-fx-background-color: #ffad33; -fx-border-radius: 0; -fx-background-radius:0; -fx-border-color:#000;-fx-font-size: 9;");
                }

                if (number == player.getPlayerBoard().getScore()) {
                    button.setStyle("-fx-background-color: #0186d2; -fx-border-radius: 0; -fx-background-radius:0; -fx-border-color:#000;-fx-font-size: 9;");
                }
                score_grid.add(button, c, r);
            }
        }
    }

    /**
     * Generating wall, where tiles will be placed according to defined pattern
     */
    private void generateWall() {
        Tile[][] wall = player.getPlayerBoard().getWall();

        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[i].length; j++) {
                if (wall[i][j] == null) continue;
                wall_grid.add(createTile(wall[i][j]), j, i);
            }
        }
    }

    /**
     * Generating pattern wall, which is something like placeholder for Tiles
     */
    private void generatePatternLine() {
        List<Tile>[] patternLines = player.getPlayerBoard().getPatternLines();

        int row = 0;
        int col = 4;

        for (List<Tile> line : patternLines) {
            for (Tile tile : line) {
                if (tile == null) continue;
                patterns_grid.add(createTile(tile), col, row);
                col--;
            }
            col = 4;
            row++;
        }
    }

    /**
     * Generating floor with broken tiles
     */
    private void generateFloor() {
        List<Tile> floor = player.getPlayerBoard().getFloorLine();

        int i = 0;
        for (Tile tile : floor) {
            if (tile == null) continue;
            floor_grid.add(createTile(tile), i, 0);
            i++;
        }
    }

    /**
     * Creating appropriate button according to tile color
     * @param tile
     * @return
     */
    private Button createTile(Tile tile) {
        if (tile == null) return null;

        Button b = new Button();
        String dir = "/pl/edu/pw/img/" + tile.getColor() + ".png";
        String string = String.format("-fx-cursor: hand; -fx-background-color: transparent; -fx-border-width: 0; -fx-background-size: 40 40; -fx-background-image: url(%s);", dir);
        b.setStyle(string);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMaxHeight(Double.MAX_VALUE);

        return b;
    }
}
