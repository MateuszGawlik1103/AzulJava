package pl.edu.pw.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.edu.pw.Game;
import pl.edu.pw.Player;
import pl.edu.pw.net.Client;
import pl.edu.pw.net.Server;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller class for the game screen.
 */
public class GameController {
    @FXML
    private BorderPane nav;
    @FXML
    private GridPane grid_nav;
    @FXML
    private Text round;
    @FXML
    private Text currentPlayer;

    private Client client;
    private Server server;
    private Game game;
    private ArrayList<Stage> stages;

    /**
     * Initializes the game screen with the game object and stages.
     *
     * @param g The game object.
     * @param s The list of stages.
     * @throws IOException If an I/O error occurs.
     */
    public void init(Game g, ArrayList<Stage> s) throws IOException {
        if (this.game != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.game = g;
        this.stages = s;

        Button b = new Button("Factory");
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMaxHeight(Double.MAX_VALUE);
        b.setOnAction(event -> {
            try {
                changeToFactory();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        b.getStyleClass().add("btn");
        grid_nav.add(b, 0, 1);

        int i = 2;
        for (Player p : game.getPlayers()) {
            b = new Button(p.getName());
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            b.setOnAction(event -> {
                try {
                    changeToBoard(p, game);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            b.getStyleClass().add("btn");
            grid_nav.add(b, 0, i);
            i++;
        }

        changeToFactory();
        game.playTurn(this);
    }

    /**
     * Switches the view to the board for the specified player.
     *
     * @param p The player object.
     * @param g The game object.
     * @throws IOException If an I/O error occurs.
     */
    public void changeToBoard(Player p, Game g) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/edu/pw/Board.fxml"));
        AnchorPane view = loader.load();

        BoardController bc = loader.getController();
        bc.init(p, g, this);

        nav.setCenter(view);
        game.playTurn(this);
        updateInfo();
    }

    /**
     * Switches the view to the factory game space.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void changeToFactory() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/edu/pw/GameSpace.fxml"));
        AnchorPane view = loader.load();

        GameSpaceController gsc = loader.getController();
        gsc.init(game, this);

        BorderPane.setAlignment(view, Pos.CENTER);
        nav.setCenter(view);
        game.playTurn(this);
        updateInfo();
    }

    /**
     * Switches the view to the end screen and displays the final scores.
     *
     * @param g The game object.
     * @throws IOException If an I/O error occurs.
     */
    public void changeToEnd(Game g) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/edu/pw/End.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Scores");
        stage.setMinWidth(900);
        stage.setWidth(900);
        stage.setHeight(650);
        stage.setMinHeight(650);
        stage.getIcons().add(new Image("/pl/edu/pw/img/logo.png"));
        stage.initStyle(StageStyle.TRANSPARENT);

        stages.add(stage);

        Scene scene = new Scene(loader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        EndController ec = loader.getController();
        ec.init(g);
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {

    }

    /**
     * Updates the game information displayed on the screen.
     */
    public void updateInfo() {
        round.setText(Integer.toString(game.getRound()));
        currentPlayer.setText(game.getPlayers().get(game.getCurrentPlayerIndex()).getName());
    }

    /**
     * Closes all stages associated with the game.
     */
    public void close() {
        for (Stage stage : stages) {
            stage.close();
        }
    }
}
