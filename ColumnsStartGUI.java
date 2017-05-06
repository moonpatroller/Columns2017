/**
 * Created by bucky on 4/30/2017.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
public class ColumnsStartGUI {
    int rows =0, columns = 0, difficulty =0;
    JLabel rowLabel, columnLabel, difficultyLabel;
    JFrame startPopUpBase;
    ColumnsStartGUI(){
        JButton defualtButton = new JButton("Default Settings");
        defualtButton.addActionListener(this::actionPerformed);
        JButton startButton = new JButton("Start Game");
        startButton.setSize(100,50);
        startButton.addActionListener(this::actionPerformed);
        JButton rowButton = new JButton();
        rowLabel = new JLabel("rows: " + rows);
        rowButton.add(rowLabel);
        JButton rowPlus = new JButton("+r");
        rowPlus.addActionListener(this::actionPerformed);
        JButton rowMinus = new JButton("-r");
        rowMinus.addActionListener(this::actionPerformed);
        JButton columnButton = new JButton();
        JButton columnPlus = new JButton("+c");
        columnPlus.addActionListener(this::actionPerformed);
        columnLabel = new JLabel("columns: " + columns);
        columnButton.add(columnLabel);
        JButton colMinus = new JButton("-c");
        colMinus.addActionListener(this::actionPerformed);
        JButton difficultyButton = new JButton();
        JButton difficultyPlus = new JButton("+d");
        difficultyLabel = new JLabel("difficulty: " + difficulty);
        difficultyButton.add(difficultyLabel);
        difficultyPlus.addActionListener(this::actionPerformed);
        JButton difficutlyMinus = new JButton("-d");
        difficutlyMinus.addActionListener(this::actionPerformed);
        JPanel rowPanel = new JPanel();
        rowPanel.setSize(100,50);
        rowPanel.add(rowButton);
        rowPanel.add(rowPlus);
        rowPanel.add(rowMinus);
        JPanel colPanel = new JPanel();
        colPanel.setSize(100,50);
        colPanel.add(columnButton);
        colPanel.add(columnPlus);
        colPanel.add(colMinus);
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setSize(100,50);
        difficultyPanel.add(difficultyButton);
        difficultyPanel.add(difficultyPlus);
        difficultyPanel.add(difficutlyMinus);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(startButton);
        bottomPanel.add(defualtButton);
        startPopUpBase = new JFrame("Columns");
        startPopUpBase.add(rowPanel, BorderLayout.WEST);
        startPopUpBase.add(colPanel, BorderLayout.CENTER);
        startPopUpBase.add(difficultyPanel, BorderLayout.EAST);
        startPopUpBase.add(bottomPanel, BorderLayout.SOUTH);
        startPopUpBase.setSize(700,110);
        startPopUpBase.setVisible(true);
        startPopUpBase.setLocationRelativeTo(null);
        startPopUpBase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /*
    Handles the actions that occur within the GUI.
    @entry param ae: the actionEvent object that occurred.
    @exit param: NONE
     */
    private void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand().equals("+r")){
            rows++;
            rowLabel.setText("" + rows + " rows");
        }else if(ae.getActionCommand().equals("+c")){
            columns++;
            columnLabel.setText("" + columns + " columns");
        }else if(ae.getActionCommand().equals("+d")){
            if(difficulty <= 8){
                difficulty++;
                difficultyLabel.setText("" + difficulty + " difficulty");
            }
        }else if(ae.getActionCommand().equals("-r")){
            rows--;
            rowLabel.setText("" + rows + " rows");
        }else if(ae.getActionCommand().equals("-c")){
            columns--;
            columnLabel.setText("" + columns + " columns");
        }else if(ae.getActionCommand().equals("-d")){
            difficulty--;
            difficultyLabel.setText("" + difficulty + " difficulty");
        }else if(ae.getActionCommand().equals("Default Settings")){
            rows = 25;
            rowLabel.setText("" + rows + " rows");
            columns= 10;
            columnLabel.setText("" + columns + " columns");
            difficulty = 4;
            difficultyLabel.setText("" + difficulty + " difficulty");
        }else if(ae.getActionCommand().equals("Start Game")){
            if(rows == 0){
                rows = 25;
            }
            if(columns == 0){
                columns = 10;
            }
            if(difficulty == 0){
                difficulty = 4;
            }
            BlockManager gameState = new BlockManager(rows, columns, difficulty);
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    new ColumnsGameGUI(gameState);
                }
            });
            startPopUpBase.setVisible(false);
        }else{}
    }

    public static void main(String args[]){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new ColumnsStartGUI();
            }
        });
    }
}
