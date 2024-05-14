package pl.edu.pw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import pl.edu.pw.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller of main menu
 */
public class MainMenuController extends AnchorPane implements Initializable {

    public ChoiceBox<String> numberOfPlayersTextField;
    public MenuItem ExitMenuItem;
    public TextField hostPortTextField, playerNameTextField, serverSocketTextField, player1NameTextField, player2NameTextField, player3NameTextField;
    public RadioButton joinGameRadioButton, localGameRadioButton, hostGameRadioButton;
    public Text hostNumberOfPlayersText, localNumberOfPlayersText;
    public ChoiceBox hostNumberOfPlayersChoiceBox, localNumberOfPlayersChoiceBox;
    private Main application;
    private ToggleGroup modeToggleGroup;


    public void setApp(Main application){
        this.application = application;
    }

    /**
     * Joining network game
     */
    public void joinGame() {
        String serverString = serverSocketTextField.getText();
        String name = playerNameTextField.getText();
        application.joinGame(name, serverString);

    }

    /**
     * Starting local game, player need to specify number of players
     */
    public void localGame() {
        String name = playerNameTextField.getText();
        int numberOfPlayers = Integer.parseInt((String) localNumberOfPlayersChoiceBox.getValue());
        List<Player> players = new ArrayList<>();
        players.add(new Player(player1NameTextField.getText()));
        if (numberOfPlayers == 3)
            players.add(new Player(player2NameTextField.getText()));
        if (numberOfPlayers == 4)
            players.add(new Player(player3NameTextField.getText()));

        application.startLocalGame(name, numberOfPlayers, players);
    }

    /**
     * Staring network game as host
     */
    public void hostGame() {
        int numberOfPlayers = Integer.parseInt((String) hostNumberOfPlayersChoiceBox.getValue());
        String name = playerNameTextField.getText();
        int port = Integer.parseInt(hostPortTextField.getText());
        application.hostGame(name, numberOfPlayers, port);
    }

    /**
     * Starting game
     * @param actionEvent on click starting game
     */
    public void startGame(ActionEvent actionEvent){
        RadioButton selectedRadioButton = (RadioButton) modeToggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedMode = selectedRadioButton.getText();
            if (Objects.equals(selectedMode, "Local game"))
                localGame();
            if (Objects.equals(selectedMode, "Host game"))
                hostGame();
            if (Objects.equals(selectedMode, "Join game"))
                joinGame();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modeToggleGroup = new ToggleGroup();
        joinGameRadioButton.setToggleGroup(modeToggleGroup);
        hostGameRadioButton.setToggleGroup(modeToggleGroup);
        localGameRadioButton.setToggleGroup(modeToggleGroup);

        hostPortTextField.managedProperty().bind(hostPortTextField.visibleProperty());
        hostNumberOfPlayersText.managedProperty().bind(hostNumberOfPlayersText.visibleProperty());
        hostNumberOfPlayersChoiceBox.managedProperty().bind(hostNumberOfPlayersChoiceBox.visibleProperty());

        serverSocketTextField.managedProperty().bind(serverSocketTextField.visibleProperty());

        localNumberOfPlayersChoiceBox.managedProperty().bind(localNumberOfPlayersChoiceBox.visibleProperty());
        localNumberOfPlayersText.managedProperty().bind(localNumberOfPlayersText.visibleProperty());

        player1NameTextField.managedProperty().bind(player1NameTextField.visibleProperty());
        player2NameTextField.managedProperty().bind(player2NameTextField.visibleProperty());
        player3NameTextField.managedProperty().bind(player3NameTextField.visibleProperty());

    }

    /**
     * Exiting app
     * @param actionEvent - on click exiting app
     */
    public void exit(ActionEvent actionEvent) {
        application.close();
    }

    /**
     * Generating buttons in main menu
     */
    public void showButtons(){
        hostPortTextField.setVisible(hostGameRadioButton.isSelected());
        hostNumberOfPlayersText.setVisible(hostGameRadioButton.isSelected());
        hostNumberOfPlayersChoiceBox.setVisible(hostGameRadioButton.isSelected());

        serverSocketTextField.setVisible(joinGameRadioButton.isSelected());

        localNumberOfPlayersChoiceBox.setVisible(localGameRadioButton.isSelected());
        localNumberOfPlayersText.setVisible(localGameRadioButton.isSelected());

        player1NameTextField.setVisible(localGameRadioButton.isSelected() && Integer.parseInt((String) localNumberOfPlayersChoiceBox.getValue()) >= 2);
        player2NameTextField.setVisible(localGameRadioButton.isSelected() && Integer.parseInt((String) localNumberOfPlayersChoiceBox.getValue()) >= 3);
        player3NameTextField.setVisible(localGameRadioButton.isSelected() && Integer.parseInt((String) localNumberOfPlayersChoiceBox.getValue()) >= 4);
    }

    public void handleHostGame() {
        showButtons();

    }

    public void handleJoinGame() {
        showButtons();
    }

    public void handleLocalGame() {
        showButtons();
    }

}
