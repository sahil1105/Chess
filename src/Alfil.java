import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Alfil -- Extension of the ChessPiece class. Used to represent a alfil object in a game of Chess.
 * This piece can take diagonal steps of size 2.
 * @author sahil1105
 */
public class Alfil extends ChessPiece {

    /**
     * Constructor for the ALfil class. Simply calls the constructor for the ChessPiece class.
     * @param pos The position piece is at on the Board.
     * @param player1 Whether or not the piece belongs to player 1.
     */
    public Alfil(@NotNull int[] pos, boolean player1) {
        super(pos, player1);
    }

    /**
     * Implementation of the getPossibleMoves from the parent ChessPiece class.
     * @param gameBoard The Board object that the piece is placed on
     * @return A list of int[] objects specifying the positions the object can move to.
     * Doesn't check if the move would be legal (eg. leave the king in a check, that must be decided by the
     * Board object).
     */
    public List getPossibleMoves(@NotNull Board gameBoard) {

        List<int[]> possibleMoves = new ArrayList<>(); //create a list of int[] objects
        int[] incrementVector = new int[this.pos.length]; //initializes a vector of 0s the same length as position

        //an alfil can take two steps diagonally in any direction

        setFirstTwoDims(incrementVector, 2, 2);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, false);

        setFirstTwoDims(incrementVector, 2, -2);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, false);

        setFirstTwoDims(incrementVector, -2, 2);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, false);

        setFirstTwoDims(incrementVector, -2, -2);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, false);

        return possibleMoves;
    }

    /**
     * Overriding toString function for the Alfil class.
     * @return String representation of the Alfil class. Capital if belongs to player 1, uncapitalized
     * otherwise.
     */
    @Override
    public String toString() {
        return this.player1? "Al": "al";
    }

}
