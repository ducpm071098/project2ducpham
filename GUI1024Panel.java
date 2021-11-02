package Project2;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GUI1024Panel extends JPanel {
    private JLabel numSession,numMove,numHighestAllSession,numHighestCurrent,numHighestPrevious;
    private JLabel[][] gameBoardUI;
    private NumberGameArrayList gameLogic;
    private int nRows, nCols,goal;
    private JButton upButton, downButton, leftButton, rightButton,undoButton,quitButton;
    private JPanel playPanel, gamePanel, scorePanel, statPanel,buttonPanel;
    private ArrayList<Integer> previousHighest;
    private JMenuItem reset,setGoal,resize,quitItem;
    private JFrame gui;

    public GUI1024Panel(JFrame gui,int width,int height,int goal,JMenuItem reset,JMenuItem setGoal,JMenuItem resize,JMenuItem quitItem) {
        previousHighest=new ArrayList<>();
        this.gui=gui;
        this.reset=reset;
        this.setGoal=setGoal;
        this.resize=resize;
        this.quitItem=quitItem;
        nRows=height;
        nCols=width;
        this.goal=goal;
        // Use helper function to initialize game
        // This lets us reuse the function to allow the user to
        // change the board size or goal
        initializeGame();
        
    }

    private void initializeGame() {

        // Initialize GridBagLayoutConstraints
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        // Indicate which column
        c.gridx = 0;
        // Indicate which row
        c.gridy = 0;
        // Indicate number of columns to span
        //				c.gridwidth = 1;
        // Specify custom height
        //				c.ipady = 40;

        // Initialize the game board appearance
        setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        setLayout(new GridBagLayout());
     

        scorePanel = new JPanel();
        scorePanel.setLayout(new GridBagLayout());
        add(scorePanel,c);
        c.gridy=1;
        statPanel = new JPanel();
        statPanel.setLayout(new GridBagLayout());
        add(statPanel,c);
        c.gridy=2;
        playPanel = new JPanel();
        playPanel.setLayout(new GridBagLayout());
        add(playPanel, c);
        c.gridx=0;
        c.gridy=0;
        // Initialize the game panel
        gamePanel = new JPanel();
        playPanel.add(gamePanel, c);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        c.gridx=0;
        c.gridy=3;
        add(buttonPanel,c);
        // Allow keys to be pressed to control the game
        setFocusable(true);
        addKeyListener(new SlideListener());
        setUpButtonPanel();
        setUpScorePanel();
        setUpStatPanel();
        // Initialize the game GUI and logic
        resizeBoard();
        quitItem.addActionListener(new SlideListener());
		reset.addActionListener(new SlideListener());
		setGoal.addActionListener(new SlideListener());
		resize.addActionListener(new SlideListener());
       
    
    }
    private void setUpStatPanel(){
        GridBagConstraints c = new GridBagConstraints();
        JLabel a = new JLabel("Number of Move :");
        c.gridx=0;
        c.gridy=0;
        statPanel.add(a,c);
        c.gridx=1;
        c.ipadx=5;
        numMove=new JLabel("0");
        statPanel.add(numMove,c);
        c.gridx=2;
        statPanel.add(new JLabel(" Highest Score Achieve :"),c);
        c.gridx=3;
        c.ipadx=5;
        numHighestAllSession= new JLabel(Integer.toString(0));
        statPanel.add(numHighestAllSession,c);
        c.gridx=4;
        statPanel.add(new JLabel("Number of Session"),c);
        c.gridx=5;
        numSession= new JLabel("1");
        statPanel.add(numSession,c);


    }
    private void setUpScorePanel(){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        JLabel currentScorelabel = new JLabel("Current Highest Score :");
        scorePanel.add(currentScorelabel,c);
        numHighestCurrent = new JLabel("0");
        c.gridx=1;
        c.ipadx=5;
        scorePanel.add(numHighestCurrent,c);
        JLabel previousScoreLabel = new JLabel("Previous Highest Score :");
        c.gridx=2;
        scorePanel.add(previousScoreLabel,c);
        numHighestPrevious  = new JLabel("0");
        c.gridx=3;
        scorePanel.add(numHighestPrevious,c);
    }
    private void setUpButtonPanel(){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        upButton = new JButton("UP");
        c.gridx=2;
        buttonPanel.add(upButton,c);
        c.gridy=3;
        downButton = new JButton("DOWN");
        buttonPanel.add(downButton,c);
        c.gridy=4;
        c.gridx=3;
        undoButton = new JButton("UNDO");
        buttonPanel.add(undoButton,c);
        quitButton= new JButton("QUIT");
        c.gridx=0;
        buttonPanel.add(quitButton,c);
        c.gridy=1;
        leftButton = new JButton("LEFT");
        buttonPanel.add(leftButton,c);
        c.gridx=3;
        rightButton = new JButton("RIGHT");
        buttonPanel.add(rightButton,c);
        
        upButton.addActionListener(new SlideListener());
		downButton.addActionListener(new SlideListener());
		rightButton.addActionListener(new SlideListener());
		leftButton.addActionListener(new SlideListener());
        undoButton.addActionListener(new SlideListener());
        buttonPanel.addKeyListener(new SlideListener());
        quitButton.addActionListener(new SlideListener());



    }
    private void updateBoard() {
        for (JLabel[] row : gameBoardUI)
            for (JLabel s : row) {
                s.setText("");
            }

        ArrayList<Cell> out = gameLogic.getNonEmptyTiles();
        if (out == null) {
            JOptionPane.showMessageDialog(null, "Incomplete implementation getNonEmptyTiles()");
            return;
        }
        for (Cell c : out) {
            JLabel z = gameBoardUI[c.getRow()][c.getColumn()];
            z.setText(String.valueOf(Math.abs(c.getValue())));
            z.setForeground(c.getValue() > 0 ? Color.BLACK : Color.RED);
            z.setOpaque(true);
			double green =255-(255.0/(gameLogic.getWinningValue()-1)*c.getValue());
			if(green<0){green=0;}
			z.setBackground(new Color(255,(int)green,0));
        }
        ArrayList<Cell> empty = gameLogic.getEmptyCell();
		for(Cell c : empty){
			gameBoardUI[c.getRow()][c.getColumn()].setOpaque(true);
			gameBoardUI[c.getRow()][c.getColumn()].setBackground(Color.decode("#bbada0"));
		}
        numMove.setText(String.valueOf(gameLogic.getUndoSize()));
        if(previousHighest.isEmpty()){
        numHighestAllSession.setText(String.valueOf(gameLogic.getHighest()));}
        numHighestCurrent.setText(String.valueOf(gameLogic.getHighest()));
        if(previousHighest.isEmpty()){
            numHighestPrevious.setText("0");
        }else{
            numHighestPrevious.setText(String.valueOf(previousHighest.get(previousHighest.size()-1)));
        }
        numSession.setText(String.valueOf(previousHighest.size()+1));

    }

    private class SlideListener implements KeyListener, ActionListener {
        @Override
        public void keyTyped(KeyEvent e) { }

        @Override
        public void keyPressed(KeyEvent e) {

            boolean moved = false;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    moved = gameLogic.slide(SlideDirection.UP);
                    break;
                case KeyEvent.VK_LEFT:
                    moved = gameLogic.slide(SlideDirection.LEFT);
                    break;
                case KeyEvent.VK_DOWN:
                    moved = gameLogic.slide(SlideDirection.DOWN);
                    break;
                case KeyEvent.VK_RIGHT:
                    moved = gameLogic.slide(SlideDirection.RIGHT);
                    break;
                case KeyEvent.VK_U:
                    try {
                        System.out.println("Attempt to undo");
                        gameLogic.undo();
                        moved = true;
                    } catch (IllegalStateException exp) {
                        JOptionPane.showMessageDialog(null, "Can't undo beyond the first move");
                        moved = false;
                    }
            }
            if (moved) {
                updateBoard();
                if (gameLogic.getStatus().equals(GameStatus.USER_WON)){
                    JOptionPane.showMessageDialog(null, "You won"); 
                    int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "TentOnly Over!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if (resp == JOptionPane.YES_OPTION) {
                        previousHighest.add(gameLogic.getHighest());
                     gameLogic.reset();
                     updateBoard();
                    } else {
                     System.exit(0);
                    }  
                }
               if (gameLogic.getStatus().equals(GameStatus.USER_LOST)) {
                    JOptionPane.showMessageDialog(null, "You Lose");
                    int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "TentOnly Over!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.YES_OPTION) {
                        previousHighest.add(gameLogic.getHighest());
                        gameLogic.reset();
                        updateBoard();
                    } else {
                        System.exit(0);
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) { }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean moved = false;
            if(e.getSource()==quitItem||e.getSource()==quitButton){
				int resp = JOptionPane.showConfirmDialog(null, "Would You Like to Exit?",
						"Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(resp == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
            if(e.getSource()==resize){
				JPanel setSize = new JPanel(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx=0;
				c.gridy=0;
				JTextField widthValue=new JTextField();
				JTextField heightValue=new JTextField();
				setSize.add(new JLabel("Width :"),c);
				c.gridx=1;
				c.ipadx=50;
				setSize.add(widthValue,c);
				c.gridx=2;
				c.ipadx=0;
				setSize.add(new JLabel("Height :"),c);
				c.gridx=3;
				c.ipadx=50;
				setSize.add(heightValue,c);
				int resp = JOptionPane.showConfirmDialog(null,setSize,"Please enter width and " +
								"height value"
						,JOptionPane.OK_CANCEL_OPTION);
				if(resp==JOptionPane.OK_OPTION) {
					try {
						int tempwidth = Integer.parseInt(widthValue.getText());
						int tempheight = Integer.parseInt(heightValue.getText());
						if (tempwidth <= 0 || tempheight <= 0) {
							JOptionPane.showMessageDialog(null, "Enter positive integer for " +
									"height and width");
						} else {
							int winning = gameLogic.getWinningValue();
							previousHighest.add(gameLogic.getHighest());
							playPanel.remove(gamePanel);
							c = new GridBagConstraints();
							c.gridx = 0;
							c.gridy = 2;
							gamePanel = new JPanel();
							playPanel.add(gamePanel, c);
                            nCols=tempwidth;
                            nRows=tempheight;
                            goal=winning;
							resizeBoard();
                            gui.setSize(getSize());
							JOptionPane.showMessageDialog(null, "Resize Complete");
						}
					} catch (NumberFormatException io) {
						JOptionPane.showMessageDialog(null, "Enter an integer in all fields");
					} catch (IllegalArgumentException io) {
						JOptionPane.showMessageDialog(null, "Error in field");
					}
				}

			}
            if(e.getSource()==reset){
				int resp = JOptionPane.showConfirmDialog(null, "Do you want to reset board ?", "RESET BOARD!",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(resp==JOptionPane.YES_OPTION){
					gameLogic.reset();
					updateBoard();
					JOptionPane.showMessageDialog(null,"Reset Board Game Complete");
				}

			}
            if(e.getSource()==setGoal){
                JPanel setGoal = new JPanel(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.gridx=0;
                c.gridy=0;
                setGoal.add(new JLabel("Goal Value :"),c);
                JTextField setValue=new JTextField();
                c.gridx=1;
                c.gridy=0;
                c.ipadx=50;
                setGoal.add(setValue,c);
                int resp = JOptionPane.showConfirmDialog(null,setGoal,"Please enter Goal value"
                        ,JOptionPane.OK_CANCEL_OPTION);
                if(resp==JOptionPane.OK_OPTION){
                    try{
                    int winningTemp = Integer.parseInt(setValue.getText());
                    if(winningTemp>0&&((winningTemp &winningTemp-1) ==0)){
                        gameLogic.setWinningValue(winningTemp);
                        JOptionPane.showMessageDialog(null, "Set New Goal Complete \n New Goal is "+winningTemp);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Enter an valid goal value");
                    }
                    }
                    catch (NumberFormatException io) {
                        JOptionPane.showMessageDialog(null, "Enter an integer in all fields");
                    } catch (IllegalArgumentException io) {
                        JOptionPane.showMessageDialog(null, "Error in field");
                    }
                }
            }
            if(e.getSource()==upButton){
                    moved = gameLogic.slide(SlideDirection.UP);
            
            }
            if(e.getSource()==downButton){
                moved = gameLogic.slide(SlideDirection.DOWN);
            }
            if(e.getSource()==leftButton){
                moved = gameLogic.slide(SlideDirection.LEFT);
            }
            if(e.getSource()==rightButton){
                moved = gameLogic.slide(SlideDirection.RIGHT);
            }
            if(e.getSource()==undoButton){
                try {
                    System.out.println("Attempt to undo");
                    gameLogic.undo();
                    moved = true;
                } catch (IllegalStateException exp) {
                    JOptionPane.showMessageDialog(null, "Can't undo beyond the first move");
                    moved = false;
                }
            }
            if (moved) {
                updateBoard();
                if (gameLogic.getStatus().equals(GameStatus.USER_WON)){
                    JOptionPane.showMessageDialog(null, "You won"); 
                    int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "TentOnly Over!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if (resp == JOptionPane.YES_OPTION) {
                        previousHighest.add(gameLogic.getHighest());
                     gameLogic.reset();
                     updateBoard();
                    } else {
                     System.exit(0);
                    }  
                }
               if (gameLogic.getStatus().equals(GameStatus.USER_LOST)) {
                    JOptionPane.showMessageDialog(null, "You Lose");
                    int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "TentOnly Over!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.YES_OPTION) {
                        previousHighest.add(gameLogic.getHighest());
                        gameLogic.reset();
                        updateBoard();
                    } else {
                        System.exit(0);
                    }
                }
            }
            playPanel.requestFocus(true);
           
        }
        }

    public void resizeBoard() {

        // Initialize the game logic
        if(nRows==0){
            nRows = 4;
        }
        if(nCols==0){
            nCols = 4;
        }
        if(this.goal==0){
            this.goal=1024;
        }
       
        gameLogic = new NumberGameArrayList();
        gameLogic.resizeBoard(nRows, nCols, this.goal);

        // Update the GUI
        // Start with changing the panel size and creating a new
        // gamePanel
        setSize(new Dimension(100*(nCols)+100, 100*(nRows)+200));
        gamePanel.setLayout(new GridLayout(nRows, nCols));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        // Initialize the game board GUI
        gameBoardUI = new JLabel[nRows][nCols];

        Font myTextFont = new Font(Font.SERIF, Font.BOLD, 40);
        for (int k = 0; k < nRows; k++) {
            for (int m = 0; m < nCols; m++) {
                gameBoardUI[k][m] = new JLabel();
                gameBoardUI[k][m].setFont(myTextFont);
                gameBoardUI[k][m].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                gameBoardUI[k][m].setPreferredSize(new Dimension(100, 100));
                gameBoardUI[k][m].setMinimumSize(new Dimension(100, 100));

                //						c.fill = GridBagConstraints.HORIZONTAL;
                // Indicate which column
                c.gridx = m;
                // Indicate which row
                c.gridy = k;
                // Indicate number of columns to span
                c.gridwidth = 1;
                // Specify custom height
                //						c.ipady = 40;
                gamePanel.add(gameBoardUI[k][m]);
            }
        }

        gameLogic.reset();
        updateBoard();
    }
}