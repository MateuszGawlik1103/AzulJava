package pl.edu.pw.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.edu.pw.Game;
import pl.edu.pw.Player;
import pl.edu.pw.net.Client;
import pl.edu.pw.net.Server;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {
    ArrayList<Stage> stages;
    ArrayList<Scene> scenes;
    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 900.0;
    private final double MINIMUM_WINDOW_HEIGHT = 800.0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            stages = new ArrayList<>();
            stage = primaryStage;
            stage.setTitle("Azul menu");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            stage.getIcons().add(new Image("/pl/edu/pw/img/logo.png"));
            gotoMainMenu();
            primaryStage.show();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Starting local game
     * @param name name of player
     * @param playersNum number of players in game
     * @param players list of players
     */
    public void startLocalGame(String name, int playersNum, List<Player> players){
        try {
            Player p = new Player(name);
            players.add(p);

            Game game = new Game(players);
            GameController gc = loadGame();
            gc.init(game, stages);


        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Hosting game method
     * @param name name of hosting player
     * @param playersNum number of players in game
     * @param port port of server
     */
    public void hostGame(String name, int playersNum, int port){
        try {
            GameController gameController = loadGame();
            gameController.server = new Server(port);
            gameController.client = new Client(new Player(name));
            gameController.client.connect("localhost:" + port);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Joining remote game
     * @param name name of player
     * @param serverString "IP:PORT" format
     */
    public void joinGame(String name, String serverString){
        try {
            GameController gameController = loadGame();
            gameController.client = new Client(new Player(name));
            gameController.client.connect(serverString);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Going to main menu of app
     */
    private void gotoMainMenu() {
        try {
            MainMenuController mainMenu = (MainMenuController) replaceSceneContent("/pl/edu/pw/MainMenu.fxml");
            mainMenu.setApp(this);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Close all stages
     */
    public void close(){
        for (Stage stage : stages){
            stage.close();
        }
        stage.close();
    }

    /**
     *
     * @return GameController of the new game stage
     * @throws IOException Unable to load game stage
     */

    private GameController loadGame() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Azul game");
        stage.setMinWidth(900);
        stage.setWidth(900);
        stage.setHeight(650);
        stage.setMinHeight(650);
        stage.getIcons().add(new Image("/pl/edu/pw/img/logo.png"));
        stage.initStyle(StageStyle.TRANSPARENT);

        stages.add(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/edu/pw/Game.fxml"));
        Scene scene = new Scene(loader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        return loader.getController();
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page;
        try {
            page = loader.load(in);
        } finally {
            assert in != null;
            in.close();
        }
        Scene scene = new Scene(page, 600, 480);
        stage.setScene(scene);
        stage.sizeToScene();
        return loader.getController();
    }
}

