package pl.edu.pw.gui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.edu.pw.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSpaceController {
    public void init(Game g, GameController gc) {
        if (this.game != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.game = g;
        this.gc = gc;
        initFactories();
        initCenter();
    }

    private Game game;
    private GameController gc;

    @FXML
    private List<Circle> circle;
    @FXML
    private List<GridPane> grid;
    @FXML
    private Text white;
    @FXML
    private Text black;
    @FXML
    private Text yellow;
    @FXML
    private Text red;
    @FXML
    private Text blue;
    @FXML
    private ImageView middleFirst;
    @FXML
    private ImageView middleBlack;
    @FXML
    private ImageView middleWhite;
    @FXML
    private ImageView middleRed;
    @FXML
    private ImageView middleYellow;
    @FXML
    private ImageView middleBlue;

    public void initialize() {

    }

    /**
     * Generating factories and filling them with random tiles
     */
    public void printFactories(){
        FactoryDisplay[] factoryDisplays = game.getFactoryDisplays();
        for(int i = 0; i < 5; i++){
            List<Tile> tiles = factoryDisplays[i].getTiles();

            int row = 0;
            int col = 0;
            System.out.println(tiles);
            for(Tile tile : tiles){
                Button b = new Button();
                String dir = "/pl/edu/pw/img/"+tile.getColor()+".png";
                String string = String.format("-fx-cursor: hand; -fx-background-color: transparent; -fx-border-width: 0; -fx-background-size: 40 40; -fx-background-image: url(%s);", dir);
                b.setStyle(string);
                b.setMaxWidth(Double.MAX_VALUE);
                b.setMaxHeight(Double.MAX_VALUE);
                final int finalI = i;
                b.setOnAction(event -> {
                    int ind = game.getCurrentPlayerIndex();
                    Player currPlayer = game.getPlayers().get(ind);
                    currPlayer.getPlayerBoard().setHasToPick(true);
                    currPlayer.getPlayerBoard().setPickedTile(tile);
                    currPlayer.getPlayerBoard().setPickedFactory(factoryDisplays[finalI]);

                    try {
                        gc.changeToBoard(currPlayer, game);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                grid.get(i).add(b, row, col);

                if(col == 1) {
                    row++;
                    col = 0;
                }
                else col++;
            }
        }
    }

    /**
     * Initializing factories, then filling them with random tiles
     */
    public void initFactories(){
        Image factoryImg = new Image("/pl/edu/pw/img/factory.png",false);
        for(Circle c : circle) c.setFill(new ImagePattern(factoryImg));

        printFactories();
    }

    /**
     * Initializing center field, which will be filled with left tiles
     */
    public void initCenter(){


        printCenter();
    }

    /**
     * Setting listener to image of tiles in the center field
     * @param t
     * @param view ImageView object, clicking on that object will behave similarly as button
     */
    private void setListener(Tile t, ImageView view) {
        view.setOnMouseClicked(event -> {
            int ind = game.getCurrentPlayerIndex();
            Player currPlayer = game.getPlayers().get(ind);
            currPlayer.getPlayerBoard().setHasToPick(true);
            currPlayer.getPlayerBoard().setPickedTile(t);
            currPlayer.getPlayerBoard().setPickedFactory(null);

            try {
                gc.changeToBoard(currPlayer, game);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Generating center view
     */
    public void printCenter(){
        List<Tile> tiles = CenterDisplay.getInstance().getTiles();

        Map<Tile.Color, Integer> colorCount = new HashMap<>();
        colorCount.put(Tile.Color.BLUE, 0);
        colorCount.put(Tile.Color.BLACK, 0);
        colorCount.put(Tile.Color.RED, 0);
        colorCount.put(Tile.Color.WHITE, 0);
        colorCount.put(Tile.Color.YELLOW, 0);

                for (Tile tile : tiles) {
                    Tile.Color color = tile.getColor();
                    colorCount.put(color, colorCount.getOrDefault(color, 0) + 1);
                }

                white.setText(Integer.toString(colorCount.get(Tile.Color.WHITE)));
                black.setText(Integer.toString(colorCount.get(Tile.Color.BLACK)));
                yellow.setText(Integer.toString(colorCount.get(Tile.Color.YELLOW)));
                red.setText(Integer.toString(colorCount.get(Tile.Color.RED)));
                blue.setText(Integer.toString(colorCount.get(Tile.Color.BLUE)));

                middleFirst.setVisible(CenterDisplay.getInstance().isFirstPlayerTokenInCenter());


        Tile t = new Tile(Tile.Color.BLUE);
        if(colorCount.get(Tile.Color.BLUE) != 0) setListener(t, middleBlue);

        t = new Tile(Tile.Color.RED);
        if(colorCount.get(Tile.Color.RED) != 0) setListener(t, middleRed);

        t = new Tile(Tile.Color.YELLOW);
        if(colorCount.get(Tile.Color.YELLOW) != 0) setListener(t, middleYellow);

        t = new Tile(Tile.Color.WHITE);
        if(colorCount.get(Tile.Color.WHITE) != 0) setListener(t, middleWhite);

        t = new Tile(Tile.Color.BLACK);
        if(colorCount.get(Tile.Color.BLACK) != 0) setListener(t, middleBlack);
    }

}
