/**
 * Created by bucky on 4/30/2017.
 */
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class ColumnsGameGUI {
    JButton startPauseButton;
    JPanel boardPanel, rightPanel, scorePanel, nextPiecePanel, keyPanel;
    LineBorder blackBorder = new LineBorder(Color.black);
    JLabel nextPieceLabel = new JLabel("Next Piece:");
    JLabel scoreLabel, startPauseLabel;
    JFrame gameFrame;
    int delay = 1000;
    Timer timer;
    GameAdapter gameAdapter = new GameAdapter();

    public static BlockManager gameState;

    ColumnsGameGUI(BlockManager gameState){
        keyPanel = new JPanel();
        this.gameState = gameState;
        gameFrame = new JFrame("Columns");
        keyPanel.addKeyListener(gameAdapter);
        gameFrame.setLayout(new GridLayout(1,2));
        gameFrame.addKeyListener(new GameAdapter());
        int rows = gameState.rows, columns = gameState.columns;
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for(int rowCount = gameState.rows -1; rowCount >= 0; rowCount--) {
            for (int colCount = 0; colCount < gameState.columns; colCount++) {
                char boardChar = gameState.board[rowCount][colCount];
                JPanel additionPanel = new JPanel();
                additionPanel.setBackground(charToColor(boardChar));
                additionPanel.setBorder(blackBorder);
                additionPanel.setSize(10,10);
                boardPanel.add(additionPanel);
            }
        }
        rightPanel = new JPanel(new BorderLayout());
        nextPiecePanel = new JPanel(new FlowLayout());
        nextPiecePanel.add(nextPieceLabel);
        for(int i = 0; i < 3; i++){
            char boardChar = gameState.nextPiece.pieceArray[i];
            JPanel additionPanel = new JPanel();
            additionPanel.setBackground(charToColor(boardChar));
            additionPanel.setSize(10,10);
            additionPanel.setBorder(blackBorder);
            nextPiecePanel.add(additionPanel);
        }
        scoreLabel = new JLabel("Your score is " + gameState.score);
        scorePanel = new JPanel();
        scorePanel.add(scoreLabel);
        rightPanel.setSize(300,600);
        rightPanel.add(nextPiecePanel, BorderLayout.NORTH);
        rightPanel.add(scorePanel, BorderLayout.CENTER);
        startPauseButton = new JButton("");
        startPauseButton.addActionListener(this::actionPerformedButton);
        startPauseLabel = new JLabel();
        startPauseLabel.setText("Pause");
        startPauseButton.add(startPauseLabel);
        rightPanel.add(startPauseButton, BorderLayout.SOUTH);
        rightPanel.setSize(300,600);
        boardPanel.setSize(600,600);
        keyPanel.add(boardPanel);
        keyPanel.add(rightPanel);
        gameFrame.add(keyPanel);
        gameFrame.setSize(900,600);
        gameFrame.pack();
        timer = new Timer(delay, this::actionPerformed);
        timer.setRepeats(true);
        timer.start();
        keyPanel.requestFocus();
        gameFrame.setVisible(true);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*
    Handles the actions preformed by the timer on the GUI.
    @entry param ae: the actionEvent object that occurred.
    @exit param: NONE
     */
    private void actionPerformed(ActionEvent ae) {
        if(!gameState.tooTall){
            gameState.oneDropPiece(gameState.currentPiece);
            refreshGUI();
        }
        if(gameState.tooTall){
            gameFrame.setVisible(false);
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    new ColumnsEndGUI(gameState.score, gameState);
                }
            });
            timer.stop();
        }
    }

    /*
    Handles the button actions of the GUI
    @entry param ae: the actionEvent object that occurred.
    @exit param: NONE
     */
    private void actionPerformedButton(ActionEvent ae){
        if(ae.getActionCommand().equals("")){
            if(startPauseLabel.getText().equals("Pause")){
                startPauseLabel.setText("Start");
                timer.stop();
                keyPanel.requestFocus();
            }else{
                startPauseLabel.setText("Pause");
                timer.start();
                keyPanel.requestFocus();
            }
        }
    }

    /*
    Refreshes the GUI components
     */
    private void refreshGUI(){
        refreshBoard();
        refreshRightPanel();
    }
    /*
    Refreshes the board Panel
     */
    private void refreshBoard(){
        keyPanel.remove(boardPanel);
        boardPanel.removeAll();
        for(int rowCount = gameState.rows -1; rowCount >= 0; rowCount--) {
            for (int colCount = 0; colCount < gameState.columns; colCount++) {
                char boardChar = gameState.board[rowCount][colCount];
                JPanel additionPanel = new JPanel();
                additionPanel.setBackground(charToColor(boardChar));
                additionPanel.setBorder(blackBorder);
                additionPanel.setSize(10,10);
                boardPanel.add(additionPanel);
            }
        }
        boardPanel.setSize(600,600);
        keyPanel.add(boardPanel);
        boardPanel.revalidate();

    }

    /*
    Refreshed the right game panel which has score and next piece as well as the pause/start button
     */
    private void refreshRightPanel(){
        keyPanel.remove(rightPanel);
        nextPiecePanel.removeAll();
        nextPiecePanel.add(nextPieceLabel);
        for(int i = 0; i < 3; i++){
            char boardChar = gameState.nextPiece.pieceArray[i];
            JPanel additionPanel = new JPanel();
            additionPanel.setBackground(charToColor(boardChar));
            additionPanel.setBorder(blackBorder);
            additionPanel.setSize(10,10);
            nextPiecePanel.add(additionPanel);
        }
        rightPanel.add(nextPiecePanel, BorderLayout.NORTH);
        rightPanel.add(scorePanel, BorderLayout.CENTER);
        rightPanel.add(startPauseButton, BorderLayout.SOUTH);
        rightPanel.setSize(300,600);
        keyPanel.add(rightPanel);
        rightPanel.revalidate();
    }

    /*
    Returns a Color for a given char from the gameState's board representation
    @entry param letter: char at a given place on the game state array.
    @exit param: NONE
     */
    private Color charToColor(char letter){
        if(letter == '-'){
            return Color.WHITE;
        }else if(letter == 'a') {
            return  Color.BLUE;
        }else if(letter == 'b'){
            return Color.RED;
        }else if(letter == 'c'){
            return Color.GREEN;
        }else if(letter == 'd'){
            return Color.ORANGE;
        }else if(letter == 'e'){
            return Color.DARK_GRAY;
        }else if(letter == 'f'){
            return Color.PINK;
        }else if(letter == 'g'){
            return Color.CYAN;
        }else{
            return Color.MAGENTA;
        }
    }

    /*
    Handles the key inputs of the game
     */
    private class GameAdapter extends KeyAdapter{
        @Override
        /*
        Handles the key presses of the game
        @entry param ke: A KeyEvent object representing the key press
        @exit param: NONE
        */
        public void keyPressed(KeyEvent ke){
            int key = ke.getExtendedKeyCode();
            if(key == KeyEvent.VK_LEFT &&
                    gameState.currentPiece.startingCol >= 0 && gameState.currentPiece.startingCol < gameState.columns){
                gameState.blockCleanUp(-1);
                refreshGUI();
            }else if(key == KeyEvent.VK_RIGHT &&
                    gameState.currentPiece.startingCol >= 0 && gameState.currentPiece.startingCol < gameState.columns){
                gameState.blockCleanUp(1);
                refreshGUI();
            }else if(key == KeyEvent.VK_UP){
                gameState.blockShiftCleanUp();
                refreshGUI();
            }else if(key == KeyEvent.VK_DOWN){
                gameState.oneDropPiece(gameState.currentPiece);
                gameState.oneDropPiece(gameState.currentPiece);
                gameState.oneDropPiece(gameState.currentPiece);
                refreshGUI();
            }else if(Character.isSpaceChar(ke.getKeyChar())){
                gameState.hardDropPiece(gameState.currentPiece);
                gameState.boardScan();
                refreshGUI();
            }
        }

        @Override
        /*
        Handles the key releases of the game
        @entry param: NONE
        @exit param: NONE
        */
        public void keyReleased(KeyEvent e) {
        }

        @Override
        /*
        Handles the key typed of the game
        @entry param: NONE
        @exit param: NONE
        */
        public void keyTyped(KeyEvent e) {
        }
    }


    public static void main(String args[]){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new ColumnsGameGUI(gameState);
            }
        });
    }
}
