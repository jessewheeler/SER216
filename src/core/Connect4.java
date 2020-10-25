package core;

import ui.Connect4TextConsole;

/**
 * Connect4 Game
 * @author Jesse Wheeler
 * @version 1.0
 */

public class Connect4
{
    /**
     * Enumerable for UI type selection
     */
    public enum uiType {
        CONSOLE
    }

    //INSTANCE VARIABLES
    /** Maximum number of players that can play game */
    private final int MAX_PLAYERS = 2;
    /** Name of Game (used for UI References) */
    private final String gameName = "Connect4";
    /** Array of players and their attributes */
    private Player[] players;
    /** Game Board */
    private Board board;
    /** Winner of game */
    private Player winner;
    /** Current player */
    private int currentPlayerIndex;
    /** UI Type */
    private uiType uiSelection;

    /**
     * Public constructor - sets up default players and UI mode
     */
    public Connect4()
    {
        this.players = new Player[MAX_PLAYERS];
        this.setDefaultPlayers();
        this.uiSelection = uiType.CONSOLE;
        this.currentPlayerIndex = 0;
        this.board = new Board();
    }

    /**
     * @return name of game
     */
    public String getGameName() {
        return this.gameName;
    }

    /**
     * @return current game board state
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * @return Winning player if game is won, or null if game is not won
     */
    public Player getWinner() { return this.winner; }

    /**
     * @return Player whose turn it is
     */
    public Player getCurrentPlayer() { return this.players[currentPlayerIndex]; }

    /**
     * Sets up default players
     */
    private void setDefaultPlayers() {
        this.players[0] = new Player("Player 1", "X");
        this.players[1] = new Player("Player 2", "O");
    }

    /**
     * Turn utility that attempts to make player move and update current player
     * @param columnSelection The column selected by the current player
     * @return true if turn was successful
     */
    public boolean facilitateTurn(int columnSelection) {
        boolean successfulMove = this.board.makeMove(columnSelection, players[currentPlayerIndex]);
        if(successfulMove) this.currentPlayerIndex = (this.currentPlayerIndex + 1) % MAX_PLAYERS;
        return successfulMove;
    }

    /**
     * Checks for win condition and sets winner player variable if won
     * @return true if game won
     */
    public boolean gameWon() {
        boolean gameWon = this.board.winCondition();
        if(gameWon) this.winner = players[(this.currentPlayerIndex + 1) % MAX_PLAYERS];
        return gameWon;
    }

    /**
     * Checks for tie
     * @return true if game is tie
     */
    public boolean gameTied() { return this.board.tieCondition(); }

    /**
     * Starts game instance
     */
    public void startGame() {
        switch (this.uiSelection) {
            case CONSOLE:
                Connect4TextConsole.startGame(this);
                break;
        }
    }

    /**
     * Utility to clear game board and reset game state
     */
    public void restartGame() {
        this.board = null;
        this.board = new Board();
        this.currentPlayerIndex = 0;
    }

    /**
     * Abstract representation of Connect4 Vertical Game Board and board operations
     */
    public class Board {
        // INSTANCE VARIABLES
        /** Row count for board construction. <a href="https://en.wikipedia.org/wiki/Connect_Four">Standard game definition</a> indicates 6 rows. */
        private final int BOARD_ROWS = 6;
        /** Row count for board construction. <a href="https://en.wikipedia.org/wiki/Connect_Four">Standard game definition</a> indicates 7 columns. */
        private final int BOARD_COLUMNS = 7;
        /** Counter for number of moves remaining */
        private int movesRemaining;
        /** 2d Array representation of board */
        private String[][] board;

        /**
         * Constructor, instantiates board and default values
         */
        public Board() {
            board          = new String[BOARD_ROWS][BOARD_COLUMNS];
            movesRemaining = BOARD_COLUMNS * BOARD_ROWS;
        }

        // PUBLIC METHODS
        /**
         * Utility to log player move on board.
         * @param column Column selected by player
         * @param player Player making move
         * @return true if move was successful
         */
        public boolean makeMove(int column, Player player) {
            if(!validMove(column)) return false;
            updateColumn(column, player);
            movesRemaining--;
            return true;
        }

        /**
         * Identify token at specific location on board
         * @param row Row index
         * @param column Column index
         * @return token string at location indicated
         */
        public String getTokenAt(int row, int column) {
            if(board[row-1][column-1] == null) return " ";
            return board[row-1][column-1];
        }

        /**
         * @return number of columns on board
         */
        public int getBoardColumns() {
            return this.BOARD_COLUMNS;
        }

        /**
         * @return number of rows on board
         */
        public int getBoardRows() {
            return this.BOARD_ROWS;
        }

        /**
         * Checks for tie condition
         * @return true if game has resulted in tie condition
         */
        public boolean tieCondition() { return movesRemaining == 0; }

        /**
         * Checks for win condition
         * @return true if game has resulted in win condition
         */
        public boolean winCondition() {
            // check that enough moves have been played prior to checking win conditions
            if(this.movesRemaining < 36) {
                if(checkColumnsForWin()) return true;
                if(checkRowsForWin()) return true;
                if(checkDiagonalsLeftToRight()) return true;
                if(checkDiagonalsRightToLeft()) return true;
            }
            return false;
        }

        // HELPER METHODS

        /**
         * Checks each column for a series of 4 tokens from one player
         * @return true if a win is found in a column
         */
        private boolean checkColumnsForWin() {
            String token;
            int connected;
            for(int j = 0; j < this.BOARD_COLUMNS; j++) {
                for(int i = 0; i < ((this.BOARD_ROWS % 4) + 1); i++) {
                    if(board[i][j] != null) {
                        token = board[i][j];
                        connected = 1;
                        while(token.equals(board[i + connected][j]))
                        {
                            connected++;
                            if(connected == 4) return true;
                        }
                    }
                }
            }
            return false;
        }

        /**
         * Checks each row for a series of 4 tokens from one player
         * @return true if a win is found in a row
         */
        private boolean checkRowsForWin() {
            String token;
            int connected;
            for(int i = 0; i < this.BOARD_ROWS; i++) {
                for(int j = 0; j < ((this.BOARD_COLUMNS % 4) + 1); j++) {
                    if(board[i][j] != null) {
                        token = board[i][j];
                        connected = 1;
                        while (token.equals(board[i][j + connected]))
                        {
                            connected++;
                            if (connected == 4) return true;
                        }
                    }
                }
            }
            return false;
        }

        /**
         * Checks each eligible diagonal for a series of 4 tokens from one player
         * @return true if a win is found along a diagonal slanted right
         */
        private boolean checkDiagonalsLeftToRight() {
            String token;
            int connected;
            for(int i = 0; i < ((this.BOARD_ROWS % 4) +1 ); i++) {
                for(int j = 0; j < ((this.BOARD_COLUMNS % 4) + 1); j++) {
                    if(board[i][j] != null) {
                        token = board[i][j];
                        connected = 1;
                        while (token.equals(board[i + connected][j + connected]))
                        {
                            connected++;
                            if (connected == 4) return true;
                        }
                    }
                }
            }
            return false;
        }

        /**
         * Checks each eligible diagonal for a series of 4 tokens from one player
         * @return true if a win is found along a diagonal slanted left
         */
        private boolean checkDiagonalsRightToLeft() {
            String token;
            int connected;
            for(int i = 0; i < ((this.BOARD_ROWS % 4) +1 ); i++) {
                for(int j = BOARD_COLUMNS - 1; j >= (this.BOARD_COLUMNS % 4); j--) {
                    if(board[i][j] != null) {
                        token = board[i][j];
                        connected = 1;
                        while (token.equals(board[i + connected][j - connected]))
                        {
                            connected++;
                            if (connected == 4) return true;
                        }
                    }
                }
            }
            return false;
        }

        /**
         * Validates legal move of player
         * @param column column selected by player to place token
         * @return true if valid move, false if invalid move
         */
        private boolean validMove(int column) {
            // validate column selected in range of board
            if(column < 1 || column > BOARD_COLUMNS) return false;
            // check to see if column is full
            if(board[0][column-1] != null) return false;
            return true;
        }

        /**
         * Updates column with player token
         * @param column Column selected by player
         * @param player Player who's token to populate column with
         */
        private void updateColumn(int column, Player player) {
            int row = BOARD_ROWS - 1;
            while(board[row][column-1] != null)
                row--;
            board[row][column-1] = player.getPlayerToken();
        }
    }

    /**
     * Representation of single player
     */
    public class Player {

        //INSTANCE METHODS
        private String playerName;
        private String playerToken;

        /**
         * Public Player Constructor - sets values
         * @param name Player Name
         * @param token Player token to be used on game board
         */
        public Player(String name, String token) {
            this.playerName  = name;
            this.playerToken = token;
        }

        /**
         * @return Player Token
         */
        public String getPlayerToken() { return playerToken; }

        /**
         * @return Player Name
         */
        public String getPlayerName()
        {
            return playerName;
        }
    }

    /**
     * Entry point of game
     * @param args Not used at this point
     */
    public static void main(String args[]) {
        Connect4 game = new Connect4();
        game.startGame();
    }
}
