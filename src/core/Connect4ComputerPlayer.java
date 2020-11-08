package core;

import java.util.Random;

/**
 * Computer Player - used when user opts to play against computer in Connect4
 * @author Jesse Wheeler
 * @version 1.0
 */
public class Connect4ComputerPlayer extends Player {

    /**
     * Enumerable to set difficulty of computer
     */
    enum ComputerDifficulty {easy, hard};

    // INSTANCE VARIABLES
    /** Connect 4 Board */
    Connect4.Board boardState;
    /** Difficulty level of computer */
    ComputerDifficulty difficulty;

    /**
     * Constructor that defaults difficulty to Easy
     * @param token Computer token for Connect 4
     */
    public Connect4ComputerPlayer(String token, String color) {
        this(token, color, ComputerDifficulty.easy);
    }

    /**
     * Constructor that takes difficulty input
     * @param token Computer token for Connect 4
     * @param difficulty Accepts easy or hard, and determines sophistication of computer-generated moves
     */
    public Connect4ComputerPlayer(String token, String color, ComputerDifficulty difficulty) {
        this.playerName = "Computer";
        this.playerToken = token;
        this.playerColor = color;
        this.isHuman = false;
        this.difficulty = difficulty;
    }

    /**
     * Entry point to make move
     * @return integer of column selected for move - can exit game here if board state not properly transferred
     */
    public int makeMove() {
        try {
            switch (difficulty) {
                case easy:
                    return this.randomizeMove();
                case hard:
                    return this.determineBestMove();
            }
        } catch (InvalidBoardStateException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return 0;
    }

    /**
     * Determines best column to choose for token placement
     * @return Column selection for move
     * @throws InvalidBoardStateException
     */
    private int determineBestMove() throws InvalidBoardStateException {
        if(boardState == null) throw new InvalidBoardStateException("Board state not set for computer player");
        // TODO: implement incentive to win/block opponent
        return randomizeMove();
    }

    /**
     * Selects a column at random to place token
     * @return Column selection for move
     * @throws InvalidBoardStateException
     */
    private int randomizeMove() throws InvalidBoardStateException {
        if(boardState == null) throw new InvalidBoardStateException("Board state not set for computer player");
        return new Random().nextInt(this.boardState.getBoardColumns()) + 1;
    }

    /**
     * Allows caller to pass board state to Computer player
     * @param board Connect 4 board
     */
    public void setBoardState(Connect4.Board board) {
        this.boardState = board;
    }
}
