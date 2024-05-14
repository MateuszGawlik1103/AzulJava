package pl.edu.pw.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import pl.edu.pw.Game;
import pl.edu.pw.Player;

import java.net.URL;
import java.util.*;

/**
 * Controller class for the end screen of the game.
 */
public class EndController implements Initializable {
    @FXML
    private Text player1Name;
    @FXML
    private Text player2Name;
    @FXML
    private Text player3Name;
    @FXML
    private Text player4Name;
    @FXML
    private Text player1Points;
    @FXML
    private Text player2Points;
    @FXML
    private Text player3Points;
    @FXML
    private Text player4Points;

    /**
     * Initializes the controller.
     *
     * @param url            The location used to resolve relative paths.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Initializes the end screen with game data.
     *
     * @param g The game object.
     */
    public void init(Game g) {
        List<Player> p = g.getPlayers();

        // Create a map to store player names and scores
        Map<String, Integer> map = new HashMap<>();

        // Populate the map with player names and scores
        for (Player player : p) {
            map.put(player.getName(), player.getPlayerBoard().getScore());
        }

        // Sort the map by scores in descending order
        List<Map.Entry<String, Integer>> nlist = new ArrayList<>(map.entrySet());
        nlist.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Display the top two players' names and scores
        player1Name.setText(nlist.get(0).getKey());
        player2Name.setText(nlist.get(1).getKey());
        player1Points.setText(Integer.toString(nlist.get(0).getValue()));
        player2Points.setText(Integer.toString(nlist.get(1).getValue()));

        // Display the third player's name and score if available
        if (nlist.size() >= 3) {
            player3Name.setText(nlist.get(2).getKey());
            player3Points.setText(Integer.toString(nlist.get(2).getValue()));
        } else {
            player3Name.setText("");
            player3Points.setText("");
        }

        // Display the fourth player's name and score if available
        if (nlist.size() >= 4) {
            player4Name.setText(nlist.get(3).getKey());
            player4Points.setText(Integer.toString(nlist.get(3).getValue()));
        } else {
            player4Name.setText("");
            player4Points.setText("");
        }
    }
}
