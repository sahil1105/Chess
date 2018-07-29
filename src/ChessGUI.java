import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * ChessGUI -- GUI Implementation for a game of chess using a ChessBoard8x8 object.
 * Extends the JFrame class.
 * References:
 * 1. http://cnx.org/content/m17186/latest/
 * 2. http://infovis.cs.vt.edu/oldsite/GUI/java/
 * 3. http://java.sun.com/docs/books/tutorial/uiswing/
 * 4. https://stackoverflow.com/questions/21142686/making-a-robust-resizable-swing-chess-gui
 * 5. http://www.javatraineronline.com/java/create-chess-board-using-java-swing/
 * 6. https://stackoverflow.com/questions/10129580/how-to-make-a-dialog-box-to-insert-name
 * 7. https://stackoverflow.com/questions/16215307/how-to-add-a-color-indicator-led-lookalike-to-a-jframe
 *
 * @author sahil1105
 */
public class ChessGUI extends JFrame{

    /**
     * ChessBoard8x8 object which serves as the model for the game. Will be used to relay
     * user inputs to the backend and effect change in the output.
     */
    private ChessBoard8x8 gameBoard;

    /**
     * Getter for the ChessBoard8x8 object that serves as the model for this GUI (view)
     * @return ChessBoard8x8 object that reflects the current game state.
     */
    public ChessBoard8x8 getGameBoard() {
        return gameBoard;
    }

    /**
     * Image to assign to empty squares on the chessboard.
     */
    public static ImageIcon emptySquareIcon = new ImageIcon(new BufferedImage(64,64, BufferedImage.TYPE_INT_ARGB));

    /**
     * JPanel object which as the name suggests, serves as the main GUI component of the
     * Chess game. Holds athe chessBoard8x8 JPanel object, the toolbar and other
     * necessary components of the game.
     */
    private JPanel mainGUI;

    /**
     * JPanel object which holds the grid layout representing the actual chessboard.
     * This will ultimately be held inside a GridBagLayout.
     */
    private JPanel chessBoard8x8;

    /**
     * The panel on top of the actual chessboard that reflects the current game status
     */
    protected JLabel statusBar;

    /**
     * Name of the first player, obtained when the class is first initialized.
     */
    protected String player1Name;

    /**
     * Name of the second player, obtained when the class is first initialized.
     */
    protected String player2Name;

    /**
     * Array of JButton objects that represent the actual squares on the chess board.
     * Some of them carry the pieces while the others are empty.
     */
    protected JButton[] squares;

    /**
     * Label where player1'score is shown.
     */
    protected JLabel player1Score;

    /**
     * Label where player2'score is shown.
     */
    protected JLabel player2Score;

    /**
     * Label with a colored dot indicating whether it is player1's turn
     */
    protected JLabel player1StatusIcon;

    /**
     * Label with a colored dot indicating whether it is player2's turn
     */
    protected JLabel player2StatusIcon;

    /**
     * Reference to the "NEW" button in the game.
     */
    protected JButton newButton;

    /**
     * Reference to the "UNDO" button in the game.
     */
    protected JButton undoButton;

    /**
     * Reference to the "FORFEIT" button in the game.
     */
    protected JButton forfeitButton;

    /**
     * Reference to the "RESTART" button in the game.
     */
    protected JButton restartButton;


    /**
     * Constructor for the ChessGUI class. It initializes all the components, sets the appropriate title
     * for the game window frame, sets the appropriate restrictions and options and displays the game
     * board in a window of appropriate size.
     * @param gameBoard A ChessBoard8x8 object that will be used to serve as the model for the game
     */
    public ChessGUI(ChessBoard8x8 gameBoard) {

        this.gameBoard = gameBoard;
        //initialize JButton array corresponding to the chess squares
        this.squares = new JButton[this.gameBoard.board.length];
        //create the main JPanel that contains the whole game
        this.mainGUI = new JPanel(new BorderLayout(3, 3));
        //get the name of the two players
        this.player1Name = JOptionPane.showInputDialog("Enter Player 1's name:");
        this.player2Name = JOptionPane.showInputDialog("Enter Player 2's name:");
        //set up the main GUI panel
        this.setupMainGUI(player1Name, player2Name);

        //set properties of the JFrame
        this.setTitle("CHESS");
        this.add(mainGUI);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setMinimumSize(this.getSize());
        this.pack();
        this.setVisible(true); //make the window visible

    }

    /**
     * Does most of the heavy-lifting and calls the appropriate functions to setup the mainGUI JPanel object.
     * Sets up the board, the status bar and the toolbar at the bottom. Initializes all these components.
     */
    private void setupMainGUI(String player1Name, String player2Name) {

        this.mainGUI.setBorder(new EmptyBorder(5,5,5,5)); // set a suitable border
        JToolBar toolBar = new JToolBar();
        JPanel playerPanel = new JPanel();
        this.setupPlayerPanel(playerPanel, player1Name, player2Name); //set up the player panel
        this.setupToolBar(toolBar); //setup the toolbar
        toolBar.add(playerPanel, BorderLayout.LINE_END); //add player panel at the end of the toolbar
        mainGUI.add(toolBar, BorderLayout.AFTER_LAST_LINE); //add the toolbar at the end of the gui
        mainGUI.add(this.setupStatusPanel(), BorderLayout.NORTH); //add the status panel at the top
        mainGUI.add(this.setupBoard()); //add the GridBagLayout object containing the chessBoard grid

    }

    /**
     * Sets up a statusPanel object which consists of a JLabel which displays a status message for the game.
     * Also initializes the statusBar panel and adds it to the statusPanel.
     * @return JPanel object corresponding to the set up status panel.
     */
    private JPanel setupStatusPanel() {

        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(new Color(158, 110, 39));
        //initialize with a generic message
        this.statusBar = new JLabel("LET'S PLAY");
        //add the status bar to the status panel
        statusPanel.add(statusBar);
        //modify the look of the text
        statusBar.setFont(new Font("Verdana", Font.BOLD, 20));
        //start off with a blue font
        statusBar.setForeground(Color.BLUE);
        return statusPanel;

    }

    /**
     * Sets up the player panel next to the toolbar. Consists of the names of the players, their scores
     * and indicators to show whose turn it is.
     * @param playerPanel the JPanel object to add the above listed components to
     * @param player1Name name of the first player
     * @param player2Name name of the second player
     */
    private void setupPlayerPanel(JPanel playerPanel, String player1Name, String player2Name) {

        playerPanel.add(this.player1StatusIcon = new JLabel("•")); //status icon for player1
        playerPanel.add(new JLabel(player1Name));
        playerPanel.add(this.player1Score = new JLabel("0"));
        playerPanel.add(this.player2StatusIcon = new JLabel("•")); //status icon for player2
        playerPanel.add(new JLabel(player2Name));
        playerPanel.add(this.player2Score = new JLabel("0"));

    }

    /**
     * Utility function to set up the toolbar for the chessBoard.
     * Reference: https://stackoverflow.com/questions/1692677/how-to-create-a-jbutton-with-a-menu
     * @param toolBar The JToolBar object to modify.
     */
    private void setupToolBar(JToolBar toolBar) {

        toolBar.setFloatable(false);
        //add the four required buttons to the toolbar
        toolBar.add(newButton = new JButton("NEW"));
        toolBar.add(undoButton = new JButton("UNDO"));
        toolBar.add(forfeitButton = new JButton("FORFEIT"));
        toolBar.add(restartButton = new JButton("RESTART"));

    }

    /**
     * Utility function to setup the chess board JPanel object.
     * Adds the appropriate pieces to the board, initializes the chessBoardLocations and assigns
     * the chess pieces' images to the appropriate icons.
     * @return A JPanel object of GridBagLayout type which contains the main ChessBoard JPanel.
     */
    private JPanel setupBoard() {

        chessBoard8x8 = new JPanel(new GridLayout(8,8,0,0)); //8x8 grid
        chessBoard8x8.setBackground(new Color(94, 79, 47));
        chessBoard8x8.setBorder(new CompoundBorder(new EmptyBorder(8,8,8,8),
                                new LineBorder(Color.WHITE))); //set borders
        initializeSquares(); //initialize the chessBoardLocations array with JButton objects
        this.addPiecesToBoard(chessBoard8x8, squares); //add these JButtons to the board
        JPanel outerGrid = new JPanel(new GridBagLayout());
        outerGrid.add(chessBoard8x8); //add the Grid to the GridBagLayout
        outerGrid.setBackground(new Color(244, 163, 65));
        return outerGrid;

    }

    /**
     * Utility function which initializes the chessBoardLocations JButton array.
     * Applies appropriate background and sets the appropriate margins.
     */
    private void initializeSquares() {
        Insets chessSquareMargin = new Insets(0,0,0,0);
        //colors for the squares
        Color color2 = new Color(155, 128, 73);
        Color color1 = new Color(204, 198, 189);
        //set up all the JButton objects and set the appropriate properties
        for (int idx = 0; idx < squares.length; idx++) {
            JButton chessPieceButton = new JButton();
            chessPieceButton.setMargin(chessSquareMargin);
            chessPieceButton.setIcon(emptySquareIcon); //initialize as empty
            chessPieceButton.setOpaque(true);
            //useful when showing possible moves
            chessPieceButton.setBorder(BorderFactory.createLineBorder(Color.RED));
            //can be turned to true when showing possible moves
            chessPieceButton.setBorderPainted(false);

            //set background as color1 and color2 based on location
            //alternating color2 and color1
            if (idx % 8 == 0) {
                Color temp = color1;
                color1 = color2;
                color2 = temp;
            }
            if (idx % 2 == 0) {
                chessPieceButton.setBackground(color1);
            }
            else {
                chessPieceButton.setBackground(color2);
            }
            this.squares[idx] = chessPieceButton;
        }
    }

    /**
     * Utility function which simply adds the JButtons to the JPanel
     * Reference: https://stackoverflow.com/questions/22580243/get-position-of-the-button-on-gridlayout
     * @param chessBoard8x8 JPanel object to the JButtons to
     * @param squares JButton objects to add to the JPanel
     */
    private void addPiecesToBoard(JPanel chessBoard8x8, JButton[] squares) {
        //add all the JButtons to the chessBoard grid
        for (int i = 0; i < squares.length; i++) {
            chessBoard8x8.add(squares[i]);
        }
    }

    /**
     * Utility function to assign the chess pieces' images to the JButton array that is ultimately added to
     * the Chess Board Grid. Updates the squares whenever it is called.
     */
    public void updateSquares() {

        //for each of the squares on the gameBoard
        for (int count = 0; count < this.gameBoard.board.length; count++) {
            //if there is a piece
            if (this.gameBoard.board[count] != null) {
                //assign its image as the icon
                this.squares[count].setIcon(new ImageIcon(this.gameBoard.board[count].getPieceIcon()));
            }
            else {
                //otherwise assign the default icon
                this.squares[count].setIcon(emptySquareIcon);
            }
        }
    }

}
