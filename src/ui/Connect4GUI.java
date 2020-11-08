package ui;
import core.Connect4;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.Optional;

/**
 * Graphical UI for Connect4 Game
 * @author Jesse Wheeler
 * @version 1.0
 */
public class Connect4GUI extends Application{

    // CLASS VARIABLES
    /** window width */
    public static final int startingWidth = 550;
    /** window height */
    public static final int startingHeight = 525;
    /** game state */
    public static Connect4 game;
    /** stage value */
    private Stage stage;
    /** font value */
    private static final Font font = new Font("Verdana", 16);

    /**
     * Orchestrator of GUI console logic
     * @param connect4 Connect4 game to start the console UI with
     */
    public static void startGame(Connect4 connect4) {
        game = connect4;
        try {
            Application.launch(Connect4GUI.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Entry point to game GUI
     * @param primaryStage default stage
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Scene start = startScene();
        primaryStage.setTitle("Connect 4"); // Set the stage title
        primaryStage.setScene(start); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    /**
     * Initialization scene
     * @return starting scene with game options
     */
    public Scene startScene() {
        StackPane pane = new StackPane();
        Button humanOpponent = new Button("Play against a human");
        humanOpponent.setOnAction(event -> {
            game.setDefaultPlayers();
            stage.setScene(gameScene());
        });
        Button computerOpponent = new Button("Play against a computer");
        computerOpponent.setOnAction(event -> {
            game.setSinglePlayerMode();
            stage.setScene(gameScene());
        });
        HBox buttonRow = new HBox(humanOpponent, computerOpponent);
        buttonRow.setSpacing(15);
        buttonRow.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(startMenuImage(), buttonRow);
        pane.setAlignment(Pos.CENTER);
        return new Scene(pane, buttonRow.getWidth() * 2, buttonRow.getHeight() * 4);
    }

    /**
     * Game Scene
     * @return board with button options
     */
    public Scene gameScene() {
        Connect4.Board gameBoard = game.getBoard();
        StackPane pane = new StackPane();
        BorderPane contentPane = new BorderPane();
        contentPane.setCenter(board(gameBoard));
        contentPane.autosize();
        Text prompt = new Text(
        game.getCurrentPlayer().getPlayerName() +
            " - your turn! You are " +
            game.getCurrentPlayer().getPlayerColor() +
            ". Click a button to place a tile."
        );
        prompt.setFont(font);
        if(!game.gameTied() && !game.gameWon()) {
            contentPane.setTop(prompt);
        }
        pane.getChildren().add(contentPane);
        pane.setAlignment(Pos.CENTER);
        return new Scene(pane, startingWidth, startingHeight);
    }

    /**
     * Image node
     * @return start menu image
     */
    public ImageView startMenuImage(){
        return new ImageView(new Image("/ui/images/connect4image.jpg"));
    }

    /**
     * Board State representation
     * @param board game board
     * @return board and button scene
     */
    public GridPane board(Connect4.Board board)
    {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        for (int i = 1; i <= board.getBoardRows(); i++)
        {
            for (int j = 1; j <= board.getBoardColumns(); j++)
            {
                StackPane cell = new StackPane();
                cell.setMinWidth(75);
                cell.setMinHeight(75);
                cell.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                Circle circle = new Circle();
                circle.setRadius(35);
                switch (board.getTokenAt(i, j))
                {
                    case "X":
                        circle.setStroke(Color.RED);
                        circle.setFill(Color.RED);
                        break;
                    case "O":
                        circle.setStroke(Color.YELLOW);
                        circle.setFill(Color.YELLOW);
                        break;
                    default:
                        circle.setStroke(Color.WHITE);
                        circle.setFill(Color.WHITE);
                }
                cell.getChildren().add(circle);
                grid.add(cell, j, i);
            }
        }
        if (!board.winCondition() && !board.tieCondition()) {
            int buttonRow = board.getBoardRows() + 1;
            for (int i = 0; i < board.getBoardColumns(); i++)
            {
                StackPane pane = new StackPane();
                pane.setPadding(new Insets(11.5, 11.5, 11.5, 11.5));
                Button b = new Button(String.valueOf(i + 1));
                b.setOnAction(event -> {
                    Object node = event.getSource();
                    Button source = (Button) node;
                    if(!game.facilitateTurn(Integer.parseInt(source.getText()))) { invalidMoveAlert(); }
                    if(game.isSinglePlayerMode() && !game.gameWon() && !game.gameTied()) facilitateComputerMove();
                    stage.setScene(gameScene());
                    if(game.gameWon())  promptForPlayAgain(game.getWinner().getPlayerName() + " won the game!");
                    if(game.gameTied()) promptForPlayAgain("The game has resulted in a tie.");
                });
                pane.getChildren().add(b);
                grid.add(pane, i + 1, buttonRow);
            }
        }
        return grid;
    }

    /**
     * Helper method to move for computer
     */
    private void facilitateComputerMove() {
        game.getComputer().setBoardState(game.getBoard());
        int selection = game.getComputer().makeMove();
        while (!game.facilitateTurn(selection)) {
            selection = game.getComputer().makeMove();
        }
    }

    /**
     * Alerts user that move selected is illegal
     */
    public void invalidMoveAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Move");
        alert.setHeaderText("That column is full.");
        alert.setContentText("Please select a different column.");
        alert.showAndWait();
    }

    /**
     * Prompts user asking if they'd like to play again
     * @param message message to display to user at the end of the game
     */
    public void promptForPlayAgain(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(message);
        alert.setContentText("Would you like to play again?");
        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            game.restartGame();
            stage.setScene(gameScene());
        } else if (result.get() == buttonTypeTwo) {
            game.restartGame();
            stage.setScene(startScene());
        } else {
            System.exit(0);
        }
    }
}