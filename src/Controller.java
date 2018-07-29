import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Controller extends Applet{

    /**
     * ChessBoard8x8 object that serves as the model for this controller
     */
    private ChessBoard8x8 gameBoard;

    /**
     * Getter for the ChessBoard8x8 model
     * @return ChessBoard8x8 object that is the model of the game.
     */
    public ChessBoard8x8 getGameBoard() {
        return gameBoard;
    }

    /**
     * A stack of Move objects to store the moves played.
     */
    private Stack<Move> pastMoves;

    /**
     * Getter for isPlayer1Turn boolean.
     * @return Boolean signifying whether it is player1's turn or not.
     */
    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    /**
     * Boolean signifying whether it is player1's turn or not.
     */
    private boolean player1Turn;

    /**
     * Getter for the current game state.
     * @return 0 if game is ongoing
     *         1 if it's a checkmate
     *        -1 if it's a stalemate
     */
    public int getGameState() {
        return gameState;
    }


    /**
     * Integer specifying game's state. Gets its value from the isGameOver method of the model.
     * 0 if game is ongoing
     * 1 if it's a checkmate
     *-1 if it's a stalemate
     */
    private int gameState;

    /**
     * ChessGUI object that serves as the view for the game.
     */
    private ChessGUI chessGUI;


    /**
     * A reference to this Controller object, useful when referring to this controller
     * inside of ActionListeners.
     */
    public Controller controller = this;

    /**
     * List of possible moves that are currently being highlighted on the chessBoard.
     */
    private List<int[]> currentPossibleMoves;

    /**
     * Index of the last JButton selected on the chess board.
     */
    private int lastSelectedButtonIndex;

    /**
     * Getter for player1Score
     * @return player 1's current score
     */
    public int getPlayer1Score() {
        return player1Score;
    }

    /**
     * Setter for player 1's score.
     * @param player1Score score to set the player 1's score to.
     */
    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    /**
     * Getter for player2Score
     * @return player 2's current score
     */
    public int getPlayer2Score() {
        return player2Score;
    }

    /**
     * Setter for player 2's score.
     * @param player2Score score to set the player 2's score to.
     */
    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    /**
     * Player 1's current score.
     */
    private int player1Score;

    /**
     * Player 2's current score.
     */
    private int player2Score;

    /**
     * Getter for current game type
     * @return GAME_TYPE.STANDARD or GAME_TYPE.CUSTOM based on whether playing according to standard configuration
     * or the custom one.
     */
    public GAME_TYPE getCurrentGameType() {
        return currentGameType;
    }

    /**
     * Setter for the current game type.
     * @param currentGameType GAME_TYPE enum type to set the current game type to.
     */
    public void setCurrentGameType(GAME_TYPE currentGameType) {
        this.currentGameType = currentGameType;
    }

    /**
     * Enum for the two possible game types. Can be extended in the future.
     */
    enum GAME_TYPE {
        STANDARD, CUSTOM;
    }

    /**
     * Current Game Type.
     */
    private GAME_TYPE currentGameType;

    /**
     * How much to increment score on a win
     */
    private static final int WIN_INCREMENT = 3;

    /**
     * How much to increment score on a tie
     */
    private static final int TIE_INCREMENT = 1;

    /**
     * Constructor for the Controller.
     * Makes all the necessary assignments and sets up callbacks on the GUI components.
     * @param gameBoard ChessBoard8x8 object that serves as the model.
     */
    public Controller(ChessBoard8x8 gameBoard) {

        this.gameBoard = gameBoard;
        this.pastMoves = new Stack<>();
        this.player1Turn = true;
        this.gameState = -1; //start in a tie state, since no game played
        this.chessGUI = new ChessGUI(this.gameBoard); //initialize the GUI
        this.currentPossibleMoves = null;
        this.lastSelectedButtonIndex = -1;
        this.player1Score = 0;
        this.player2Score = 0;

        this.setupCallBacks(); //setup callback on ChessGUI components

    }

    /**
     * Utility function to set up callback/listener function on the GUI components
     * such as the chess board squares, the toolbar buttons, etc.
     */
    private void setupCallBacks() {

        //setup listeners on:
        //1. the squares
        this.setupSquaresListeners();
        //2. the new button
        this.setupNewButtonListener();
        //3. the undo button
        this.setupUndoButtonListener();
        //4. the restart button
        this.setupRestartButtonListener();
        //5. the forfeit button
        this.setupForfeitButtonListener();


    }

    /**
     * Setup the listener for the forfeit button.
     * Only works if game is in progress.
     */
    private void setupForfeitButtonListener() {

        this.chessGUI.forfeitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.gameState == 0) { //if ongoing game
                    controller.gameState = 1; //set to a win for the other player
                    controller.updateScores(); //update scores to reflect the win
                    controller.updateStatusPanel(); //update the status panel to reflect the win/loss
                }
            }
        });

    }

    /**
     * Sets up the listener on the Restart button.
     * Only works if game is in progress.
     */
    private void setupRestartButtonListener() {

        this.chessGUI.restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.gameState == 0) { //if game is ongoing
                    controller.gameState = -1; //set it to a tie
                    controller.updateScores(); //update the scores to reflect the tie
                    controller.startNewGameLoop(); //reset the board and start a new game
                }
            }
        });

    }

    /**
     * Sets up the listener on the Undo button.
     * Only works if game is in progress.
     */
    private void setupUndoButtonListener() {
        this.chessGUI.undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.gameState == 0) { //if game in progress
                    controller.undoLastMove(); //undo the previous moves
                }

            }
        });
    }

    /**
     * Sets up listener on the New Button.
     * Only works if game is NOT ongoing.
     */
    private void setupNewButtonListener() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new JMenuItem(new AbstractAction("Standard Board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.getGameState() != 0) { //if game is not ongoing
                    controller.setCurrentGameType(GAME_TYPE.STANDARD); //set game type for next game
                    controller.startNewGameLoop(); //reset assignments and start a new game
                }
            }
        }));
        popupMenu.add(new JMenuItem(new AbstractAction("Custom Board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.getGameState() != 0) { //if game is not ongoing
                    controller.setCurrentGameType(GAME_TYPE.CUSTOM); //set game type for next game
                    controller.startNewGameLoop(); //reset assignments and start a new game
                }

            }
        }));

        this.chessGUI.newButton.addMouseListener(new MouseAdapter() { //add listener for the new button
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
            public void mousePressed(MouseEvent e) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY()); //call the appropriate function.
            }
        });

    }

    /**
     * Set up listeners on the JButtons (the chess squares)
     * Buttons only respond when game is ongoing.
     */
    private void setupSquaresListeners() {

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.gameState != 0) { //check that game is in progress
                    return; //if not, then return
                }
                JButton pressedButton = (JButton) e.getSource(); //get the pressed button
                mainLoop: for (int i = 0; i < controller.chessGUI.squares.length; i++) {
                    if (controller.chessGUI.squares[i] == pressedButton) { //find it in the array of JButtons
                        unhighlightAllSquares();
                        if (lastSelectedButtonIndex != -1) { //if a button has been pressed before this
                            for (int j = 0; j < currentPossibleMoves.size(); j++) {
                                //and it is a legal move for the previously selected piece
                                if (Arrays.equals(currentPossibleMoves.get(j),
                                        controller.getGameBoard().boardIdxToPosition(i))) {
                                    performMove(lastSelectedButtonIndex, i); //then make the move
                                    lastSelectedButtonIndex = -1; //reset the last pressed variable
                                    currentPossibleMoves = null; //reset the possible moves variable
                                    break mainLoop; // break out of the whole loop
                                }
                            }
                        }

                        lastSelectedButtonIndex = -1; //reset the last pressed variable
                        currentPossibleMoves = null; //reset the possible moves variable

                        //if not a possible move, but pressed another piece
                        if (controller.chessGUI.getGameBoard().board[i] != null) {
                            //get the possible moves for the piece
                            List<int[]> possibleMoves =
                                    controller.getGameBoard().possibleMovesForAPiece(i, controller.player1Turn);

                            if (possibleMoves.size() > 0) {
                                lastSelectedButtonIndex = i; //update this as the last pressed button
                                currentPossibleMoves = possibleMoves; //update the current possible moves
                                highlightPossibleMoves(); //highlight the possible moves for this piece
                            }
                        }
                        break;
                    }
                }
            }
        };

        //add above listener to each JButton/square on the chessboard.
        for (JButton square: controller.chessGUI.squares) {
            square.addActionListener(buttonListener);
        }

    }

    /**
     * Utility function to highlight possible moves for the currently selected piece.
     */
    private void highlightPossibleMoves() {

        if (this.currentPossibleMoves != null) {
            for (int[] move: currentPossibleMoves) {
                //paint the border for these JButtons
                this.chessGUI.squares[this.getGameBoard().twoDPositionToBoardIdx(move)].setBorderPainted(true);
            }
        }

    }

    /**
     * Utility function to unhighlight (un-paint the border) on all JButtons/squares on the chessboard.
     */
    private void unhighlightAllSquares() {

        for (int i = 0; i < this.chessGUI.squares.length; i++) {
            this.chessGUI.squares[i].setBorderPainted(false);
        }

    }

    /**
     * Utility function that performs a specific move (if legal), adds it to the stack of previous moves,
     * communicates changes to the model and the view, updates the GUI with the changes, changes turn
     * to the other player, updates game state after this move, and updates the scores and the game status
     * based on this move.
     * @param startIdx Index of piece to move
     * @param destIdx Index where to move the piece to
     */
    private void performMove(int startIdx, int destIdx) {

        //form the move object to add to stack
        Move newMove = new Move(startIdx, destIdx, this.gameBoard.board[destIdx]);
        //check if a legal move
        boolean moveSuccessful = this.gameBoard.move(startIdx, destIdx, this.player1Turn);
        if (!moveSuccessful) {
            //if not, then print there was an error and return
            System.out.println("MOVE WAS NOT EXECUTED!");
            return;
        }
        this.chessGUI.updateSquares(); //update the GUI
        this.reverseTurn(); // change the turn
        this.pastMoves.add(newMove); //add this move to the stack
        this.gameState = this.gameBoard.isGameOver(this.isPlayer1Turn()); //update game state
        this.updateScores(); //update scores based on updated game state
        this.updateStatusPanel(); //update game status/ status panel message based on move

    }

    /**
     * Utility function to undo the last move, if there exists one in the stack
     */
    private void undoLastMove() {

        if (!this.pastMoves.empty()) { //if there exists a past move
            Move lastMove = this.pastMoves.pop(); //get the last move
            //undo the move
            this.gameBoard.undoTheMove(lastMove.getStartIdx(), lastMove.getDestIdx(), lastMove.getOriginallyAtDest());
            //give the turn back to the player of the last move
            this.reverseTurn();
            //update GUI to reflect the change
            this.chessGUI.updateSquares();
            //update the game state
            this.gameState = this.gameBoard.isGameOver(this.player1Turn);
            //update the status panel message based on game state
            this.updateStatusPanel();
        }

    }

    /**
     * Utility function that updates the scores if the game is over. Also performs the update on the
     * score panel in the GUI.
     */
    private void updateScores() {

        if (this.gameState == 1) { //if game over
            if (this.player1Turn) {
                this.setPlayer2Score(this.getPlayer2Score() + WIN_INCREMENT); //increment score of winning player
            }
            else {
                this.setPlayer1Score(this.getPlayer1Score() + WIN_INCREMENT); //increment score of winning player
            }
        }
        else if (this.gameState == -1) { //if its a tie
            //increment scores of both players
            this.setPlayer1Score(this.getPlayer1Score() + TIE_INCREMENT);
            this.setPlayer2Score(this.getPlayer2Score() + TIE_INCREMENT);
        }

        //update the scores on the GUI
        this.chessGUI.player1Score.setText(""+this.getPlayer1Score());
        this.chessGUI.player2Score.setText(""+this.getPlayer2Score());

    }

    /**
     * Utility function to set the message on the status panel according to the current game situation
     */
    private void updateStatusPanel() {

        if (this.gameState != 0) { //if game is over
            if (this.gameState == 1) { //if checkmate, announce the winner
                this.chessGUI.statusBar.setText("GAME OVER! " +
                        (this.player1Turn? this.chessGUI.player2Name : this.chessGUI.player1Name) + " WINS");
            }
            else if (this.gameState == -1) { //if a tie, then announce as such
                this.chessGUI.statusBar.setText("IT'S A TIE");
            }
            this.chessGUI.statusBar.setForeground(Color.RED); //in either case, the message should be in red
            return; //return if game over
        }
        if (this.gameBoard.isInCheck(this.isPlayer1Turn())) { //if not over, but current player is in check
            this.chessGUI.statusBar.setText("YOU ARE IN CHECK"); //let the player know he is in check
            this.chessGUI.statusBar.setForeground(Color.BLUE); //blue for this
        }
        else {
            this.chessGUI.statusBar.setText( //otherwise say whose turn it is
                    (this.isPlayer1Turn()? this.chessGUI.player1Name : this.chessGUI.player2Name) + "'s TURN");
            this.chessGUI.statusBar.setForeground(Color.BLUE); //blue for this message as well
        }
    }

    /**
     * Utility function that gives the turn to the other player. Also changes the status icons
     * on the GUI to reflect the change in turn.
     */
    private void reverseTurn() {
        this.player1Turn = !this.player1Turn; //change the turn
        //change the LIVE icon on the board
        this.chessGUI.player1StatusIcon.setForeground((this.player1Turn? Color.GREEN : Color.RED));
        this.chessGUI.player2StatusIcon.setForeground((!this.player1Turn? Color.GREEN : Color.RED));
    }

    /**
     * Utility function that sets up a new game by reinitializing required components.
     */
    private void startNewGameLoop() {

        this.gameState = 0; //set the game to ongoing
        //initialize game board according to game type
        if (this.getCurrentGameType() == GAME_TYPE.STANDARD) {
            this.gameBoard.initBoard();
        }
        else {
            this.gameBoard.initCustomBoard();
        }
        this.pastMoves = new Stack<>(); //empty the move stack
        this.player1Turn = false;
        this.reverseTurn(); //this passes turn to player 1
        this.currentPossibleMoves = null;
        this.lastSelectedButtonIndex = -1;
        this.unhighlightAllSquares(); //no possible moves at start since nothing selected
        this.chessGUI.updateSquares(); //tell GUI to update
        this.updateStatusPanel(); //update the status panel

    }

    /**
     * Runner for the Chess Game.
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        ChessBoard8x8 gameBoard = new ChessBoard8x8();
        Controller gameController = new Controller(gameBoard);

    }


}
