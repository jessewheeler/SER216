package core;

/**
 * Exception for flagging an invalid board state for computer moves
 */
public class InvalidBoardStateException extends Exception {
    public InvalidBoardStateException(String errorMessage) {
        super(errorMessage);
    }
}
