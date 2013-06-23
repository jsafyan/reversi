import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Game {
    
	public static boolean toggleTimer = true;
    public static boolean toggleInstr = true;
    public static boolean glowOn = false;
	public static JLabel black = new JLabel();
	public static JLabel white = new JLabel();
	public static JLabel player = new JLabel();
	public static JLabel over = new JLabel();
	private static JLabel title = new JLabel();
	private static JLabel instructions = new JLabel();
	
    /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
    	// Top-level frame
        final JFrame frame = new JFrame("Reversi");
        frame.setLocation(500,300);
        frame.setPreferredSize(new Dimension(600,600));

        // Main playing area
        final Board game = new Board();
        final JPanel gameView = new JPanel();
        gameView.add(game);
        frame.add(gameView, BorderLayout.CENTER);
        
        
        // Create a glass panel for a spash page
        final JPanel glass = (JPanel) frame.getGlassPane();
        glass.setVisible(true);
        glass.setLayout(new BorderLayout());
        
        // Create a separate bottom panel for the splash page
        JPanel flowPanel = new JPanel(new FlowLayout());
        JButton playButton = new JButton("Play!");
        playButton.setPreferredSize(new Dimension(100, 40));
        flowPanel.add(playButton);
        
        // The splash panel
        JPanel splash = new JPanel();
        splash.setLayout(new BorderLayout());
        splash.setBackground(Color.BLACK);
        glass.add(splash, BorderLayout.CENTER);
        
        //The title
        title.setFont(new Font("Monospaced", Font.BOLD, 30));
        title.setText("Welcome to Reversi!");
        title.setForeground(Color.RED);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        //The instructions
        instructions.setVisible(false);
        instructions.setText("<html><b><em>Instructions:</em></b><br> <ul>" +
        		"<li>Place your pieces on spaces adjacent to " +
        		"the opposite color's pieces.</li>" +
        		"<li>Flip opposing pieces by surrounding them " +
        		"horizontally or diagonally.</li>" +
        		"<li>Toggle valid glow to view valid moves; " +
        		"yellow squares indicate available moves.</li>" +
        		"<li>Win!</li>" +
        		"</ul></html>");
        instructions.setForeground(Color.CYAN);
        instructions.setFont(new Font(Font.SANS_SERIF, 18, 18));
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        
        //Add the components to the splash page
        splash.add(instructions, BorderLayout.CENTER);
        splash.add(title, BorderLayout.NORTH);
        splash.add(flowPanel, BorderLayout.SOUTH);
        
        

        
        // Reset button
        final JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.NORTH);
        final JPanel lowerPanel = new JPanel();
        frame.add(lowerPanel, BorderLayout.SOUTH);
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
              game.reset();
           }
        });
        reset.setFocusable(false);
        panel.add(reset);
        
        
        // Button to go the start page
        JButton main = new JButton("Start Page");
        main.setFocusable(false);
        panel.add(main);
        
        //Make move assistance flash
        final Timer timer = new Timer(400, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				glowOn = !glowOn;
				Board.updateScore();
				Board.validGlow();
				
			}
			
		});
        
        
        //Toggle valid move assistance
        final JCheckBox glow = new JCheckBox("Toggle Valid Moves");
        glow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (toggleTimer) {
					glowOn = true;
					timer.start();
				} else {
					glowOn = false;
					Board.updateScore();
					timer.stop();
				}
				toggleTimer = !toggleTimer;
			}	
        });
        lowerPanel.add(glow);
        
        // Add the toggleable instructions 
        JButton instr = new JButton("Instructions");
        flowPanel.add(instr);
        instr.setPreferredSize(new Dimension(100, 40));
        instr.setFocusable(false);
        instr.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		instructions.setVisible(toggleInstr);
        		toggleInstr = !toggleInstr;
        	}
        });
        
        //Go to start page button
        main.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		glass.setVisible(true);
        		gameView.repaint();
        	}
        });
        
        //Go to main game view button
        playButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		glass.setVisible(false);
        		gameView.repaint();
        	}
        });
        
        //Add score labels
    	lowerPanel.add(black);
        lowerPanel.add(white);
        
        //Add the player and gameover info to the panel
        int fontSize = 20;
        over.setFont(new Font("Monospaced", Font.BOLD, fontSize));
        panel.add(player);
        panel.add(over);
       
        
    	//Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        game.gameBoard(game.getRootPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}