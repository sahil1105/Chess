import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Nightrider -- Extension of the ChessPiece class. Used to represent a nightrider object in a game of Chess.
 * This piece can take multiple knight-like steps.
 * @author sahil1105
 */
public class Nightrider extends ChessPiece {

    /**
     * Constructor for the Nightrider class. Simply calls the constructor for the ChessPiece class.
     * @param pos The position piece is at on the Board.
     * @param player1 Whether or not the piece belongs to player 1.
     */
    public Nightrider(@NotNull int[] pos, boolean player1) {
        super(pos, player1);
    }

    /**
     * Implementation of the getPossibleMoves from the parent ChessPiece class.
     * @param gameBoard The Board object that the piece is placed on
     * @return A list of int[] objects specifying the positions the object can move to.
     * Doesn't check if the move would be legal (eg. leave the king in a check, that must be decided
     * by the Board object).
     */
    public List getPossibleMoves(@NotNull Board gameBoard) {

        List<int[]> possibleMoves = new ArrayList<>(); //create a list of int[] objects
        int[] incrementVector = new int[this.pos.length]; //initializes a vector of 0s the same length as position

        //check the possible directions the knight can move in, which are 2 steps in one direction and
        // then 1 step in the other
        //in the first two dimensions
        //allowed to skip over other objects
        //uses the possibleMovesHelper from the parent class, taking multiple step in each of the specified direction

        setFirstTwoDims(incrementVector, 2, 1);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, true);

        setFirstTwoDims(incrementVector, 2, -1);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, true);

        setFirstTwoDims(incrementVector, -2, 1);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, true);

        setFirstTwoDims(incrementVector, -2, -1);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, true);

        setFirstTwoDims(incrementVector, 1, 2);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, true);

        setFirstTwoDims(incrementVector, -1, 2);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, true);

        setFirstTwoDims(incrementVector, 1, -2);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, true);

        setFirstTwoDims(incrementVector, -1, -2);
        possibleMovesHelper(gameBoard, possibleMoves, incrementVector, true);

        return possibleMoves;
    }

    /**
     * Overriding toString function for the Nightrider class.
     * @return String representation of the Nightrider class. Capital if belongs to player 1, uncapitalized otherwise.
     */
    @Override
    public String toString() {
        return this.player1? "Ni": "ni";
    }

}
