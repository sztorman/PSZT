
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.util.*;

 /*  public class Freedom extends Application {
  @Override public void start(Stage stage) throws Exception {
        GameManager gameManager = new GameManager();

        Scene scene = gameManager.getGameScene();
        scene.getStylesheets().add(
                getResource(
                        "freedom-skin.css"
                )
        );

        stage.setTitle("Freedom PSZT");
        stage.getIcons().add(SquareSkin.blackImage);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    private String getResource(String resourceName) {
        return getClass().getResource(resourceName).toExternalForm();
    }

    public static void main(String[] args) {
        Application.launch(Freedom.class);
    }
}
 */

class GameManager {
    private Scene gameScene;
    private Game game;

    GameManager() {
        newGame();
    }

    public void newGame() {
        game = new Game(this);

        if (gameScene == null) {
            gameScene = new Scene(game.getSkin());
        } else {
            gameScene.setRoot(game.getSkin());
        }
    }

    public void quit() {
        gameScene.getWindow().hide();
    }

    public Game getGame() {
        return game;
    }

    public Scene getGameScene() {
        return gameScene;
    }
}

class GameControls extends HBox {
    GameControls(final GameManager gameManager, final Game game) {
        getStyleClass().add("game-controls");

        visibleProperty().bind(game.gameOverProperty());

        Label playAgainLabel = new Label("Play Again?");
        playAgainLabel.getStyleClass().add("info");

        Button playAgainButton = new Button("Yes");
        playAgainButton.getStyleClass().add("play-again");
        playAgainButton.setDefaultButton(true);
        playAgainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                gameManager.newGame();
            }
        });

        Button exitButton = new Button("No");
        playAgainButton.getStyleClass().add("exit");
        exitButton.setCancelButton(true);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameManager.quit();
            }
        });

        getChildren().setAll(
                playAgainLabel,
                playAgainButton,
                exitButton
        );
    }
}

class ScoresText extends Label {
    ScoresText() {
        getStyleClass().add("info");
    }
}

class StatusIndicator extends HBox {
    private final ImageView playerToken = new ImageView();
    private final Label playerLabel = new Label("Current Player: ");

    StatusIndicator(Game game) {
        getStyleClass().add("status-indicator");

        bindIndicatorFieldsToGame(game);

        playerToken.setFitHeight(32);
        playerToken.setPreserveRatio(true);

        playerLabel.getStyleClass().add("info");

        getChildren().addAll(playerLabel, playerToken);
    }

    private void bindIndicatorFieldsToGame(Game game) {
        playerToken.imageProperty().bind(
                Bindings.when(
                        game.currentPlayerProperty().isEqualTo(Square.State.WHITE)
                )
                        .then(SquareSkin.whiteImage)
                        .otherwise(
                                Bindings.when(
                                        game.currentPlayerProperty().isEqualTo(Square.State.BLACK)
                                )
                                        .then(SquareSkin.blackImage)
                                        .otherwise((Image) null)
                        )
        );

        playerLabel.textProperty().bind(
                Bindings.when(
                        game.gameOverProperty().not()
                )
                        .then("Current Player: ")
                        .otherwise(
                                Bindings.when(
                                        game.winnerProperty().isEqualTo(Square.State.EMPTY)
                                )
                                        .then("Draw")
                                        .otherwise("Winning Player: ")
                        )
        );
    }
}

class Game {


    private int pionkiCount;
    private ScoresText scoreText;

    public ScoresText getScoreText() {
        return scoreText;
    }

    public void setScoreText(ScoresText scoreText) {
        this.scoreText = scoreText;
    }

    public enum LineDirection {
        UP, UP_LEFT, LEFT, DOWN_LEFT, DOWN, DOWN_RIGHT, RIGHT, UP_RIGHT
    }

    private GameSkin skin;
    private Board board = new Board(this);
    private WinningStrategy winningStrategy = new WinningStrategy(board);
    private Square lastPlacedSquare;

    int whiteLines = 0;
    int blackLines = 0;

    private List<ConnectingLine> lines;

    public Square getLastPlacedSquare() {
        return lastPlacedSquare;
    }

    public void setLastPlacedSquare(Square lastPlacedSquare) {
        this.lastPlacedSquare = lastPlacedSquare;
    }

    private ReadOnlyObjectWrapper<Square.State> currentPlayer = new ReadOnlyObjectWrapper<>(Square.State.WHITE);
    public ReadOnlyObjectProperty<Square.State> currentPlayerProperty() {
        return currentPlayer.getReadOnlyProperty();
    }
    public Square.State getCurrentPlayer() {
        return currentPlayer.get();
    }

    private ReadOnlyObjectWrapper<Square.State> winner = new ReadOnlyObjectWrapper<>(Square.State.EMPTY);
    public ReadOnlyObjectProperty<Square.State> winnerProperty() {
        return winner.getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper drawn = new ReadOnlyBooleanWrapper(false);
    public ReadOnlyBooleanProperty drawnProperty() {
        return drawn.getReadOnlyProperty();
    }
    public boolean isDrawn() {
        return drawn.get();
    }

    private ReadOnlyBooleanWrapper gameOver = new ReadOnlyBooleanWrapper(false);
    public ReadOnlyBooleanProperty gameOverProperty() {
        return gameOver.getReadOnlyProperty();
    }
    public boolean isGameOver() {
        return gameOver.get();
    }

    public void pressSquare(int[] coordinates) {
        board.getSquare(coordinates[0], coordinates[1]).pressed();
    }

    public Game(GameManager gameManager) {
        gameOver.bind(
                winnerProperty().isNotEqualTo(Square.State.EMPTY)
                        .or(drawnProperty())
        );


        scoreText = new ScoresText();
        skin = new GameSkin(gameManager, this);
        lines = new ArrayList<>();
        pionkiCount = 0;
    }

    public Board getBoard() {
        return board;
    }

    public void nextTurn() {
        if (isGameOver()) return;

        switch (currentPlayer.get()) {
            case EMPTY:
            case WHITE: currentPlayer.set(Square.State.BLACK); break;
            case BLACK: currentPlayer.set(Square.State.WHITE); break;
        }
    }

    public void boardUpdated() {
        pionkiCount++;
        if(pionkiCount == 100){
            if(blackLines > whiteLines){
                winner.set(Square.State.BLACK);
                currentPlayer.set(Square.State.BLACK);
            } else if (whiteLines > blackLines){
                winner.set(Square.State.WHITE);
                currentPlayer.set(Square.State.WHITE);
            } else {
                drawn.set(true);
                currentPlayer.set(Square.State.EMPTY);
            }
        }
//        checkForWinner();
    }

    public Parent getSkin() {
        return skin;
    }

    public void addLine(ConnectingLine line){
        lines.add(line);
        skin.addLine(line);
        if(line.square1.getState().equals(Square.State.WHITE)) whiteLines++;
        else blackLines++;
        scoreText.setText("White: " + whiteLines + ", Black: " + blackLines);
    }


    public boolean canPlaceAnywhere() {
        if(lastPlacedSquare == null) return true;   // Start of game, last placed square is null

        Integer x = lastPlacedSquare.getLocation().getKey();
        Integer y = lastPlacedSquare.getLocation().getValue();

        if((x > 0 && board.getSquare(x - 1, y).getState().equals(Square.State.EMPTY)) ||  // left
                (x > 0 && y < 9 && board.getSquare(x - 1, y + 1).getState().equals(Square.State.EMPTY)) ||   // down left
                (y < 9 && board.getSquare(x, y + 1).getState().equals(Square.State.EMPTY)) ||    // down
                (x < 9 && y < 9 && board.getSquare(x + 1, y + 1).getState().equals(Square.State.EMPTY)) ||   // down right
                (x < 9 && board.getSquare(x + 1, y).getState().equals(Square.State.EMPTY)) ||   // right
                (x < 9 && y > 0 && board.getSquare(x + 1, y - 1).getState().equals(Square.State.EMPTY)) ||   // up right
                (y > 0 && board.getSquare(x, y - 1).getState().equals(Square.State.EMPTY)) ||   // up
                (x > 0 && y > 0 && board.getSquare(x - 1, y - 1).getState().equals(Square.State.EMPTY))){   // up left
//            System.out.println("Has adjacent empty");
            return false;
        }
//        System.out.println("NO adjacent empty");
        return true;
    }

    public void findLines(Square square) {
        Map<LineDirection, MyPair> directionValues = new HashMap<>();

        for(LineDirection d : LineDirection.values()){
            directionValues.put(d, count(square.getState(), d, square.getLocation().getKey(), square.getLocation().getValue(), square));
        }

        // Horizontal
        if(directionValues.get(LineDirection.LEFT).getFirst() + directionValues.get(LineDirection.RIGHT).getFirst() - 1 == 4){
            System.out.println("In horizontal line");
            addLine(new ConnectingLine(directionValues.get(LineDirection.LEFT).getSecond(), directionValues.get(LineDirection.RIGHT).getSecond()));
        }
        // Vertical
        if(directionValues.get(LineDirection.UP).getFirst() + directionValues.get(LineDirection.DOWN).getFirst() - 1 == 4){
            System.out.println("In vertical line");
            addLine(new ConnectingLine(directionValues.get(LineDirection.UP).getSecond(), directionValues.get(LineDirection.DOWN).getSecond()));
        }
        // Diagonal up left, down right
        if(directionValues.get(LineDirection.UP_LEFT).getFirst() + directionValues.get(LineDirection.DOWN_RIGHT).getFirst() - 1 == 4){
            System.out.println("In diagonal line up left, down right");
            addLine(new ConnectingLine(directionValues.get(LineDirection.UP_LEFT).getSecond(), directionValues.get(LineDirection.DOWN_RIGHT).getSecond()));
        }
        // Diagonal up right, down left
        if(directionValues.get(LineDirection.UP_RIGHT).getFirst() + directionValues.get(LineDirection.DOWN_LEFT).getFirst() - 1 == 4){
            System.out.println("In diagonal line up right, down left");
            addLine(new ConnectingLine(directionValues.get(LineDirection.UP_RIGHT).getSecond(), directionValues.get(LineDirection.DOWN_LEFT).getSecond()));
        }
    }

    private MyPair count(Square.State state, LineDirection direction, int x, int y, Square previousSquare){
//        System.out.println("Invoked count() for x = " + x + " y = " + y);
        Square currentSquare = board.getSquare(x, y);
        if(currentSquare.getState() != state) return new MyPair(0, previousSquare);
        switch (direction) {
            case LEFT:          if(x > 0) return count(state, direction, x - 1, y, currentSquare).incrementFirst(); break;
            case DOWN_LEFT:     if(x > 0 && y < 9) return count(state, direction, x - 1, y + 1, currentSquare).incrementFirst(); break;
            case DOWN:          if(y < 9) return count(state, direction, x, y + 1, currentSquare).incrementFirst(); break;
            case DOWN_RIGHT:    if(x < 9 && y < 9) return count(state, direction, x + 1, y + 1, currentSquare).incrementFirst(); break;
            case RIGHT:         if(x < 9) return count(state, direction, x + 1, y, currentSquare).incrementFirst(); break;
            case UP_RIGHT:      if(x < 9 && y > 0) return count(state, direction, x + 1, y - 1, currentSquare).incrementFirst(); break;
            case UP:            if(y > 0) return count(state, direction, x, y - 1, currentSquare).incrementFirst(); break;
            case UP_LEFT:       if(x > 0 && y > 0) return count(state, direction, x - 1, y - 1, currentSquare).incrementFirst(); break;
        }
        return new MyPair(1, previousSquare);
    }
}

class GameSkin extends VBox {
    private AnchorPane anchorPane = new AnchorPane();

    GameSkin(GameManager gameManager, Game game) {
        anchorPane.getChildren().add(game.getBoard().getSkin());
        getChildren().addAll(
                game.getScoreText(),
                anchorPane,
                new StatusIndicator(game),
                new GameControls(gameManager, game)
        );
    }


    public void addLine(Node e){
        anchorPane.getChildren().add(e);
    }
}

class WinningStrategy {
    private final Board board;

    private static final int WHITE_WON = 3;
    private static final int BLACK_WON = 30;

    private static final Map<Square.State, Integer> values = new HashMap<>();
    static {
        values.put(Square.State.EMPTY, 0);
        values.put(Square.State.WHITE, 1);
        values.put(Square.State.BLACK, 10);
    }

    public WinningStrategy(Board board) {
        this.board = board;
    }


    private Integer valueOf(int i, int j) {
        return values.get(board.getSquare(i, j).getState());
    }

    private boolean isWinning(int score) {
        return score == WHITE_WON || score == BLACK_WON;
    }

    private Square.State winner(int score) {
        if (score == WHITE_WON) return Square.State.WHITE;
        if (score == BLACK_WON) return Square.State.BLACK;

        return Square.State.EMPTY;
    }
}

class Board {
    private final BoardSkin skin;

    private final Square[][] squares = new Square[10][10];

    public Board(Game game) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                squares[i][j] = new Square(game, new Pair<>(i, j));
            }
        }

        skin = new BoardSkin(this);
    }

    public Square getSquare(int i, int j) {
        return squares[i][j];
    }

    public Node getSkin() {
        return skin;
    }
}

class BoardSkin extends GridPane {
    BoardSkin(Board board) {
        getStyleClass().add("board");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                add(board.getSquare(i, j).getSkin(), i, j);
            }
        }
    }
}


class Square {
    enum State { EMPTY, WHITE, BLACK}

    private final SquareSkin skin;

    private ReadOnlyObjectWrapper<State> state = new ReadOnlyObjectWrapper<>(State.EMPTY);
    public ReadOnlyObjectProperty<State> stateProperty() {
        return state.getReadOnlyProperty();
    }
    public State getState() {
        return state.get();
    }
    public void setState(State newState){
        state.set(newState);
    }

    private Pair<Integer, Integer> location;

    private final Game game;

    public Square(Game game, Pair<Integer, Integer> location) {
        this.game = game;
        this.location = location;

        skin = new SquareSkin(this);
    }

    public void pressed() {
        if(!game.isGameOver() && state.get() == State.EMPTY && ((game.canPlaceAnywhere() || game.getLastPlacedSquare().isAdjacentTo(this)))) {
            state.set(game.getCurrentPlayer());
            System.out.print(state.get() == State.WHITE ? "USER: " : "");
            game.nextTurn();
            game.setLastPlacedSquare(this);
            game.findLines(this);
            game.boardUpdated();
            System.out.println("Placed pionek in x = " + location.getKey() + " y = " + location.getValue());

            GameEngine engine = new GameEngine();
            if(game.getCurrentPlayer().equals(State.BLACK)){
                int[] result = engine.makeMove(game);
                System.out.print("  AI: makeMove() returned x = " + result[0] + " y = " + result[1] + ". Placing pionek... ");
                game.pressSquare(result);
            }
        } else {
            System.out.println("Unable to place pionek");
        }
    }

    public Node getSkin() {
        return skin;
    }

    public double getPhysicalCenterX(){
        return skin.getPhysicalCenterX();
    }

    public double getPhysicalCenterY(){
        return skin.getPhysicalCenterY();
    }

    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    public boolean isAdjacentTo(Square square){
        return Math.abs(location.getKey() - square.getLocation().getKey()) <= 1 &&
                Math.abs(location.getValue() - square.getLocation().getValue()) <= 1;
    }
}

class SquareSkin extends StackPane {
    private static double squareSide = 40;

    final static Image whiteImage = new Image(
            Main.class.getResource("white.png").toExternalForm()
    );
    final static Image blackImage = new Image(
            Main.class.getResource("black.png").toExternalForm()
    );

    private final ImageView imageView = new ImageView();

    SquareSkin(final Square square) {
        getStyleClass().add("square");

        imageView.setMouseTransparent(true);
        imageView.setFitHeight(squareSide);
        imageView.setFitWidth(squareSide);

        getChildren().setAll(imageView);
//        setPrefSize(blackImage.getHeight() + 20, blackImage.getHeight() + 20);
        setPrefSize(squareSide, squareSide);
        setMaxSize(squareSide, squareSide);

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                square.pressed();
            }
        });

        square.stateProperty().addListener(new ChangeListener<Square.State>() {
            @Override public void changed(ObservableValue<? extends Square.State> observableValue, Square.State oldState, Square.State state) {
                switch (state) {
                    case EMPTY: imageView.setImage(null); break;
                    case WHITE: imageView.setImage(whiteImage); break;
                    case BLACK: imageView.setImage(blackImage); break;
                }
            }
        });
    }

    public double getPhysicalCenterX(){
        return getLayoutX() + squareSide / 2;
    }

    public double getPhysicalCenterY(){
        return getLayoutY() + squareSide / 2;
    }
}