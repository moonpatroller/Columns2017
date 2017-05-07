import javax.swing._
import javax.swing.border.LineBorder
import java.awt._
import java.awt.event._
import javax.swing.Timer

object ColumnsGameGUI {
    val gameState = new BlockManager(25, 10, 4)

    def main(args: Array[String]): Unit = {
        new ColumnsGameGUI(gameState)
    }
}

class ColumnsGameGUI(gameState: BlockManager) extends ActionListener {

    val delay = 1000
    val gameAdapter = new GameAdapter()
    val blackBorder = new LineBorder(Color.black)

    val startPauseButton = new JButton("")

    val boardPanel = new JPanel()
    val rightPanel = new JPanel(new BorderLayout())
    val scorePanel = new JPanel()
    val nextPiecePanel = new JPanel(new FlowLayout())
    val keyPanel = new JPanel()

    val nextPieceLabel = new JLabel("Next Piece:")
    val scoreLabel = new JLabel("Your score is " + gameState.score)
    val startPauseLabel = new JLabel()

    val gameFrame = new JFrame("Columns");

    val timer = new Timer(delay, this)


    keyPanel.addKeyListener(gameAdapter)
    gameFrame.setLayout(new GridLayout(1,2))
    gameFrame.addKeyListener(new GameAdapter())
    val rows = gameState.rows
    val columns = gameState.columns
    
    boardPanel.setLayout(new GridLayout(rows, columns))
    for (rowCount <- (gameState.rows - 1) to 0 by -1) {
        for (colCount <- 0 until gameState.columns) {
            val boardChar = gameState.board(rowCount)(colCount)
            val additionPanel = new JPanel()
            additionPanel.setBackground(charToColor(boardChar))
            additionPanel.setBorder(blackBorder)
            additionPanel.setSize(10, 10)
            boardPanel.add(additionPanel)
        }
    }
    nextPiecePanel.add(nextPieceLabel)
    for(i <- 0 until 3) {
        val boardChar = gameState.nextPiece.pieceArray(i)
        val additionPanel = new JPanel()
        additionPanel.setBackground(charToColor(boardChar))
        additionPanel.setSize(10, 10)
        additionPanel.setBorder(blackBorder)
        nextPiecePanel.add(additionPanel)
    }
    scorePanel.add(scoreLabel)
    rightPanel.setSize(300, 600)
    rightPanel.add(nextPiecePanel, BorderLayout.NORTH)
    rightPanel.add(scorePanel, BorderLayout.CENTER)

    startPauseButton.addActionListener(new ActionListener() {
        /*
        Handles the button actions of the GUI
        @entry param ae: the actionEvent object that occurred.
        @exit param: NONE
         */
        override
        def actionPerformed(ae: ActionEvent): Unit = {
            if (ae.getActionCommand().equals("")) {
                if (startPauseLabel.getText().equals("Pause")) {
                    startPauseLabel.setText("Start")
                    timer.stop()
                    keyPanel.requestFocus()
                } else {
                    startPauseLabel.setText("Pause")
                    timer.start()
                    keyPanel.requestFocus()
                }
            }
        }
    })

    startPauseLabel.setText("Pause")
    startPauseButton.add(startPauseLabel)
    rightPanel.add(startPauseButton, BorderLayout.SOUTH)
    rightPanel.setSize(300,600)
    boardPanel.setSize(600,600)
    keyPanel.add(boardPanel)
    keyPanel.add(rightPanel)
    gameFrame.add(keyPanel)
    gameFrame.setSize(900,600)
    gameFrame.pack()
    timer.setRepeats(true)
    timer.start()
    keyPanel.requestFocus()
    gameFrame.setVisible(true)
    gameFrame.setLocationRelativeTo(null)
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)


    /*
    Handles the actions preformed by the timer on the GUI.
    @entry param ae: the actionEvent object that occurred.
    @exit param: NONE
     */
    override
    def actionPerformed(ae: ActionEvent): Unit = {
        if (!gameState.isOver()) {
            gameState.oneDropPiece(gameState.currentPiece)
            refreshGUI()
        } else {
            gameFrame.setVisible(false)
            SwingUtilities.invokeLater(new Runnable() {
                override
                def run(): Unit = {
                    // new ColumnsEndGUI(gameState.score, gameState)
                }
            })
            timer.stop()
        }
    }

    /*
    Refreshes the GUI components
     */
    def refreshGUI(): Unit = {
        refreshBoard()
        refreshRightPanel()
    }

    /*
    Refreshes the board Panel
     */
    def refreshBoard(): Unit = {
        keyPanel.remove(boardPanel)
        boardPanel.removeAll()
        for(rowCount <- (gameState.rows - 1) to 0 by -1) {
            for (colCount <- 0 until gameState.columns) {
                val boardChar = gameState.board(rowCount)(colCount)
                val additionPanel = new JPanel()
                additionPanel.setBackground(charToColor(boardChar))
                additionPanel.setBorder(blackBorder)
                additionPanel.setSize(10,10)
                boardPanel.add(additionPanel)
            }
        }
        boardPanel.setSize(600,600)
        keyPanel.add(boardPanel)
        boardPanel.revalidate()

    }

    /*
    Refreshed the right game panel which has score and next piece as well as the pause/start button
     */
    def refreshRightPanel(): Unit = {
        keyPanel.remove(rightPanel)
        nextPiecePanel.removeAll()
        nextPiecePanel.add(nextPieceLabel)
        for (i <- 0 until 3) {
            val boardChar = gameState.nextPiece.pieceArray(i)
            val additionPanel = new JPanel()
            additionPanel.setBackground(charToColor(boardChar))
            additionPanel.setBorder(blackBorder)
            additionPanel.setSize(10,10)
            nextPiecePanel.add(additionPanel)
        }
        rightPanel.add(nextPiecePanel, BorderLayout.NORTH)

        rightPanel.add(scorePanel, BorderLayout.CENTER)
        scoreLabel.setText("Your score is " + gameState.score)

        rightPanel.add(startPauseButton, BorderLayout.SOUTH)
        rightPanel.setSize(300,600)
        keyPanel.add(rightPanel)
        rightPanel.revalidate()
    }

    /*
    Returns a Color for a given char from the gameState's board representation
    @entry param letter: char at a given place on the game state array.
    @exit param: NONE
     */
    def charToColor(letter: Char): Color = {
        letter match {
            case '-' => Color.WHITE
            case 'a' => Color.BLUE
            case 'b' => Color.RED
            case 'c' => Color.GREEN
            case 'd' => Color.ORANGE
            case 'e' => Color.DARK_GRAY
            case 'f' => Color.PINK
            case 'g' => Color.CYAN
            case _ => Color.MAGENTA
        }
    }

    /*
    Handles the key inputs of the game
     */
    class GameAdapter extends KeyAdapter {
        /*
        Handles the key presses of the game
        @entry param ke: A KeyEvent object representing the key press
        @exit param: NONE
        */
        override
        def keyPressed(ke: KeyEvent): Unit = {

            val key = ke.getExtendedKeyCode()
            if (key == KeyEvent.VK_LEFT &&
                    gameState.currentPiece.startingCol >= 0 && gameState.currentPiece.startingCol < gameState.columns) {
                gameState.blockShiftSideways(-1)
                refreshGUI()
            } else if (key == KeyEvent.VK_RIGHT &&
                    gameState.currentPiece.startingCol >= 0 && gameState.currentPiece.startingCol < gameState.columns) {
                gameState.blockShiftSideways(1)
                refreshGUI()
            } else if (key == KeyEvent.VK_UP) {
                gameState.blockRotate()
                refreshGUI()
            } else if (key == KeyEvent.VK_DOWN) {
                gameState.oneDropPiece(gameState.currentPiece)
                gameState.oneDropPiece(gameState.currentPiece)
                gameState.oneDropPiece(gameState.currentPiece)
                refreshGUI()
            } else if (Character.isSpaceChar(ke.getKeyChar())) {
                gameState.hardDropPiece(gameState.currentPiece)
                gameState.boardScan()
                refreshGUI()
            }
        }

        override def keyReleased(e: KeyEvent): Unit = {}
        override def keyTyped   (e: KeyEvent): Unit = {}
    }
}
