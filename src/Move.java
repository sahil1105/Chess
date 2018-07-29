/**
 * Move -- Class to store a move played during a chess Game
 * @author sahil1105
 */
public class Move {


    /**
     * Constructor for Move class.
     * @param startIdx index on board from where the move was played
     * @param destIdx index to where the piece was moved
     * @param originallyAtDest reference to the piece that was originally at the destIdx (if there was one)
     */
    public Move(int startIdx, int destIdx, ChessPiece originallyAtDest) {
        this.startIdx = startIdx;
        this.destIdx = destIdx;
        this.originallyAtDest = originallyAtDest;
    }

    /**
     * Getter for the startIdx
     * @return startIdx of the move
     */
    public int getStartIdx() {
        return startIdx;
    }

    /**
     * Getter for destination index.
     * @return destIdx of the move
     */
    public int getDestIdx() {
        return destIdx;
    }

    /**
     * Getter for piece originally at the destination location.
     * @return originallyAtDest (a ChessPiece instance) or null if there wasn't a piece originally at destIdx
     */
    public ChessPiece getOriginallyAtDest() {
        return originallyAtDest;
    }

    /**
     * index on board from where the move was played from
     */
    private int startIdx;

    /**
     * index to where the piece was moved
     */
    private int destIdx;

    /**
     * reference to the piece that was originally at the destIdx (if there was one)
     */
    private ChessPiece originallyAtDest;

}
