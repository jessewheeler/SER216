package ui;
import core.Connect4;
import java.util.Scanner;

/**
 * Console UI for Connect4 Game
 * @author Jesse Wheeler
 * @version 1.0
 */
public class Connect4TextConsole
{
    //INSTANCE VARIABLES
    /** Connect4 Game */
    private Connect4 game;
    /** Continue Playing */
    private boolean play;
    /** User Input utility */
    private Scanner input;

    /**
     * Private constructor accessible only from startGame method (Singleton)
     * @param game Game to instantiate console from
     */
    private Connect4TextConsole(Connect4 game) {
        this.game = game;
        this.play = true;
        this.input = new Scanner(System.in);
    }

    /**
     * Orchestrator of UI console logic
     * @param game Connect4 game to start the console UI with
     */
    public static void startGame(Connect4 game) {
        Connect4TextConsole console = new Connect4TextConsole(game);
        while(console.play) {
            console.play = false; // we'll prompt the user after the game concludes
            console.displayBoard();
            console.logStartUpMessage();
            // Play game
            while(!console.game.gameWon() && !console.game.gameTied()) {
                int selection = console.promptForTurn();
                while(!console.game.facilitateTurn(selection)) {
                    console.logErrorMessage();
                    selection = console.promptForTurn();
                }
                console.displayBoard();
            }
            // Process end game results
            if(console.game.gameTied())
                console.logTieMessage();
            else
                console.logWinnerToConsole();
            // See if users would like to play once more
            console.promptForPlayAgain();
        }
        // Close program gracefully
        console.logGoodbyeMessage();
        console.input.close();
    }

    /**
     * Displays current game board state
     */
    private void displayBoard() {
        for(int i = 1; i <= this.game.getBoard().getBoardRows(); i++) {
            System.out.print("|");
            for(int j = 1; j <= this.game.getBoard().getBoardColumns(); j++) {
                System.out.print(this.game.getBoard().getTokenAt(i,j) + "|");
            }
            System.out.print("\n");
        }
    }

    /**
     * @return integer representing column selection provided by user
     */
    private int promptForTurn() {
        return getUserIntegerInput(
                this.game.getCurrentPlayer().getPlayerName() +
                        " - your turn. Choose a column number from 1 - " +
                        this.game.getBoard().getBoardColumns() +
                        ". Your token is " +
                        this.game.getCurrentPlayer().getPlayerToken() +
                        "."
        );
    }

    /**
     * Prompts user for response whether they'd like to play again
     */
    private void promptForPlayAgain() {
        if(getUserIntegerInput("Would you like to play again? Enter 1 for yes, or any other integer for no.") == 1) {
            this.game.restartGame();
            this.play = true;
        }
    }

    /**
     * Helper method for parsing user integer input
     * @param prompt to show user
     * @return integer input by user
     */
    private int getUserIntegerInput(String prompt) {
        System.out.println(prompt);
        while(!input.hasNextInt()) {
            System.out.println("That's not an integer.");
            System.out.println(prompt);
            input.next();
        }
        return input.nextInt();
    }

    /** Log Startup message to console */
    private void logStartUpMessage() {
        System.out.println("Beginning Game!");
    }

    /** Log Error message to console */
    private void logErrorMessage() {
        System.out.println("Invalid Column Selection. Please Try Again.");
    }

    /** Log Tie message to console */
    private void logTieMessage() {
        System.out.println("Game over. The game has resulted in a tie.");
    }

    /** Log Winner message to console */
    private void logWinnerToConsole() {
        System.out.println("Congratulations! " + this.game.getWinner().getPlayerName() + " has won the game.");
    }

    /** Log Goodbye message to console */
    private void logGoodbyeMessage() {
        System.out.println("Thank you for playing " + this.game.getGameName());
    }

}
