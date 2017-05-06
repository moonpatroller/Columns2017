import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by bucky on 5/5/2017.
 */
public class ColumnsEndGUI {
    BlockManager gameState;
    JFrame endGameFrame;
    ColumnsEndGUI(int score, BlockManager gameState){
        this.gameState = gameState;
        endGameFrame = new JFrame();
        JPanel textPanel = new JPanel();
        JLabel scoreLabel = new JLabel("Your score was " + score);
        JPanel buttonsPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(this::actionPreformed);
        JButton replayGameButton = new JButton("Replay");
        replayGameButton.addActionListener(this::actionPreformed);
        buttonsPanel.add(newGameButton);
        buttonsPanel.add(replayGameButton);
        textPanel.add(scoreLabel);
        endGameFrame.add(textPanel, BorderLayout.CENTER);
        endGameFrame.add(buttonsPanel, BorderLayout.SOUTH);
        endGameFrame.setSize(400,100);
        endGameFrame.setVisible(true);
        endGameFrame.setLocationRelativeTo(null);
        endGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /*
    Handles the actions that occur within the GUI.
    @entry param ae: the actionEvent object that occurred.
    @exit param: NONE
     */
    private void actionPreformed(ActionEvent ae){
        if(ae.getActionCommand().equals("New Game")){
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    new ColumnsStartGUI();
                }
            });
            endGameFrame.setVisible(false);
        }else if(ae.getActionCommand().equals("Replay")){
            gameState.score = 0;
            gameState.fillBlankBoard();
            gameState.tooTall = false;
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    new ColumnsGameGUI(gameState);
                }
            });
            endGameFrame.setVisible(false);
        }
    }
}
