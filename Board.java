import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Board extends JComponent implements MouseListener {

	private static Color GREEN = new Color(46, 139, 87);
	private static Color YELLOW = new Color(238, 221, 130);
	private static JPanel[][] cell = new JPanel[8][8];
	public static Piece turn = Piece.BLACK;
	public static Piece opponent = 
			(turn == Piece.BLACK? Piece.WHITE : Piece.BLACK);
	public static int blackScore = -2;
	public static int whiteScore = -2;
	public static Set<JPanel> moves = new HashSet<JPanel>();
	
	//Booleans to determine valid moves and flipping
	private static boolean N = false;
	private static boolean S = false;
	private static boolean E = false;
	private static boolean W = false;
	private static boolean NE = false;
	private static boolean NW = false;
	private static boolean SE = false;
	private static boolean SW = false;
	private static boolean hasN = false;
	private static boolean hasS = false;
	private static boolean hasE = false;
	private static boolean hasW = false;
	private static boolean hasNE = false;
	private static boolean hasNW = false;
	private static boolean hasSE = false;
	private static boolean hasSW = false;
	
	
	public Board() {
		super();
	}
	
    public static JPanel getCell(int row, int col) {
    	return cell[row][col];
    }
    
	public void gameBoard(final Container pane) {
		setLayout(new GridLayout(8,8));
         
        //Add buttons to experiment with Grid Layout
        for (int i = 0; i < 8; i++) {
        	for (int j = 0; j < 8; j++) {
        		initPiece(cell, i, j);
        	}
        }
        cell[4][4].setBackground(Color.WHITE);
        cell[3][3].setBackground(Color.WHITE);
        cell[3][4].setBackground(Color.BLACK);
        cell[4][3].setBackground(Color.BLACK);
        
        //Initialize the set of valid moves
        validMoves(turn);
        updateScore();
        validGlow();
        Game.black.setText("Black: "+ blackScore);
        Game.white.setText("White: " + whiteScore);
        
        Game.player.setText(turn.toString() + "'s turn");
        
    }
	
	public void reset() {
		N = false;
		S = false;
		E = false;
		W = false;
		NW = false;
		NE = false;
		SE = false;
		SW = false;
		hasN = false;
		hasS = false;
		hasE = false;
		hasW = false;
		hasNW = false;
		hasNE = false;
		hasSE = false;
		hasSW = false;
		turn = Piece.BLACK;
		for (int i = 0; i < 8; i++) {
        	for (int j = 0; j < 8; j++) {
        		cell[i][j].setBackground(GREEN);
        	}
        }
		//Initialize the first 4 pieces
        cell[4][4].setBackground(Color.WHITE);
        cell[3][3].setBackground(Color.WHITE);
        cell[3][4].setBackground(Color.BLACK);
        cell[4][3].setBackground(Color.BLACK);
        
        //Initialize the scores
        blackScore = -2;
        whiteScore = -2;
        Game.player.setVisible(true);
        Game.over.setVisible(false);
        updateScore();
        moves.clear();
        validMoves(turn);
        validGlow();
        
        //Add the scores to the score labels
        Game.black.setText("Black: "+ blackScore);
        Game.white.setText("White: " + whiteScore);
        
        Game.player.setText(turn.toString() + "'s turn");
	}
	
	public void initPiece(final JPanel[][] cell, final int i, final int j) {
		cell[i][j] = new JPanel();
		cell[i][j].setBackground(GREEN);
		cell[i][j].setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		cell[i][j].setPreferredSize(new Dimension(50, 50));
		cell[i][j].addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (isValidMove(cell, i, j, turn)) {
					addPiece(cell, i, j);
					flip(cell, i, j);
					switchTurn();
					gameResolve();
				}
				if (moves.isEmpty()) {
					switchTurn();
					gameResolve();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
		add(cell[i][j]);	
	}
	
	public static void addPiece(JPanel[][]cell, int i, int j) {
		switch(turn) {
		case BLACK:
			cell[i][j].setBackground(Color.BLACK);
			break;
		case WHITE:
			cell[i][j].setBackground(Color.WHITE);
			break;
		default:
			break;
		}
	}
	
	public void gameResolve() {
		updateScore();
		moves.clear();
		validMoves(turn);
		validGlow();
		//If the player has no available moves, switch turns
		if (moves.isEmpty()) {
			switchTurn();
			moves.clear();
			validMoves(turn);
			//Neither player has moves available; end the game
			if (moves.isEmpty()) {
				if (blackScore > whiteScore) {
					Game.player.setVisible(false);
					Game.over.setVisible(true);
					Game.over.setText("Game over! Black wins!");
				} else if (whiteScore > blackScore) {
					Game.player.setVisible(false);
					Game.over.setVisible(true);
					Game.over.setText("Game over! White wins!");
				} else {
					Game.player.setVisible(false);
					Game.over.setVisible(true);
					Game.over.setText("Game over! Tie Game!");
				}
			} else if (!moves.isEmpty()) {
				JOptionPane.showMessageDialog(null, 
						"No legal move available; turn skipped");
				updateScore();
				validGlow();
			}
		} else {
			updateScore();
			validGlow();
		}
	}
	
	//Highlights all the valid moves
	public static void validGlow() {
		if (Game.glowOn) {
			updateScore();
			moves.clear();
			validMoves(turn);
			Iterator<JPanel> iter = moves.iterator();
			while(iter.hasNext()) {
				JPanel square = (JPanel) iter.next();
				square.setBackground(YELLOW);
			}
		}
	}
	
	
	public static void updateScore() {
		blackScore = 0;
		whiteScore = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (cell[i][j].getBackground() == Color.BLACK) {
					blackScore++;
				}
				if (cell[i][j].getBackground() == Color.WHITE) {
					whiteScore++;
				}
				if (cell[i][j].getBackground() == YELLOW) {
					cell[i][j].setBackground(GREEN);
				}
			}
		}
		Game.black.setText("Black: "+ blackScore);
		Game.white.setText("White: " + whiteScore);
	}
	
	//Switches the turn between black and white
	public static void switchTurn() {
		if (turn == Piece.BLACK){
			turn = Piece.WHITE;
			Game.player.setText(turn.toString() + "'s turn");
		} else {
			turn = Piece.BLACK;
			Game.player.setText(turn.toString() + "'s turn");
		}
	}
	
	//Determines if the space is a valid move
	public static boolean isValidMove(JPanel[][]cell, int i, int j, Piece piece) {
		//The space must be empty
		if (cell[i][j].getBackground() == Color.WHITE || 
				cell[i][j].getBackground() == Color.BLACK) {
			return false;
		}
		Piece opponent = (piece == Piece.BLACK ? Piece.WHITE : Piece.BLACK);
		boolean hasNeighbor = false;
		//Reset directions
		N = false;
		S = false;
		E = false;
		W = false;
		NW = false;
		NE = false;
		SE = false;
		SW = false;
		hasN = false;
		hasS = false;
		hasE = false;
		hasW = false;
		hasNW = false;
		hasNE = false;
		hasSE = false;
		hasSW = false;
		int rowStart = i < 1 ? 0 : i -1;
		int rowEnd = i > 6 ? 8 : i + 2;
		int colStart = j < 1 ? 0 : j - 1;
		int colEnd = j > 6 ? 8 : j + 2;
		//Check that you are placing the piece adjacent to an opposing piece
		for (int row = rowStart; row < rowEnd; row++) {
			for (int col = colStart; col < colEnd; col++) {
				if (cell[row][col].getBackground() == pieceToColor(opponent)) {
					hasNeighbor = true;
					//East
					if (row == i && col > j) {
						E = true;
					}
					//West
					if (row == i && col < j) {
						W = true;
					}
					//North
					if (row < i && col == j) {
						N = true;
					}
					//South
					if (row > i && col == j) {
						S = true;
					}
					//NE
					if (row < i && col > j) {
						NE = true;
					}
					//SE 
					if (row > i && col > j) {
						SE = true;
					}
					//NW 
					if (row < i && col < j) {
						NW = true;
					}
					//SW
					if (row > i && col < j) {
						SW = true;
					}
				} 
			}
		}
		if (hasNeighbor) {
			if (E) {
				for (int index = j + 1; index < 8; index++) {
					if (cell[i][index].getBackground() == GREEN) {
						hasE = false;
						break;
					}
					if (cell[i][index].getBackground() == pieceToColor(turn)) {
						hasE = true;
						break;
					}
					
				}
			}
			if (W) {
				for (int index = j - 1; index >= 0; index--) {
					if (cell[i][index].getBackground() == GREEN) {
						hasW = false;
						break;
					}
					if (cell[i][index].getBackground() == pieceToColor(turn)) {
						hasW = true;
						break;
					}
				}
			}
			if (N) {
				for (int index = i - 1; index >= 0; index--) {
					if (cell[index][j].getBackground() == GREEN) {
						hasN = false;
						break;
					}
					if (cell[index][j].getBackground() == pieceToColor(turn)) {
						hasN = true;
						break;
					}
				}
			}
			if (S) {
				for (int index = i + 1; index < 8; index++) {
					if (cell[index][j].getBackground() == GREEN) {
						hasS = false;
						break;
					}
					if (cell[index][j].getBackground() == pieceToColor(turn)) {
						hasS = true;
						break;
					}
				}
			}
			if (NE) {
				int row = i - 1;
				int col = j + 1;
				do {
					if (cell[row][col].getBackground() == GREEN) {
						hasNE = false;
						break;
					}
					if (cell[row][col].getBackground() == pieceToColor(turn)) {
						hasNE = true;
						break;
					}
					row--;
					col++;
				} while (row >= 0 && col < 8);
			}
			if (NW) {
				int row = i - 1;
				int col = j - 1;
				do {
					if (cell[row][col].getBackground() == GREEN) {
						hasNW = false;
						break;
					}
					if (cell[row][col].getBackground() == pieceToColor(turn)) {
						hasNW = true;
						break;
					}
					row--;
					col--;
				} while (row >= 0 && col >= 0);
			}
			if (SW) {
				int row = i + 1;
				int col = j - 1;
				do {
					if (cell[row][col].getBackground() == GREEN) {
						hasSW = false;
						break;
					}
					if (cell[row][col].getBackground() == pieceToColor(turn)) {
						hasSW = true;
						break;
					}
					row++;
					col--;
				} while (row < 8 && col >= 0);
			}
			if (SE) {
				int row = i + 1;
				int col = j + 1;
				do {
					if (cell[row][col].getBackground() == GREEN) {
						hasSE = false;
						break;
					}
					if (cell[row][col].getBackground() == pieceToColor(turn)) {
						hasSE = true;
						break;
					}
					row++;
					col++;
				} while (row < 8 && col < 8);
			}
		}
		boolean val = (hasN || hasS || hasE || hasW 
				|| hasNE || hasNW || hasSE || hasSW);
		return val;
	}
	
	
	public static void validMoves(Piece player) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (isValidMove(cell, row, col, player)) {
					moves.add(cell[row][col]);
				}
			}
		}
	}
		
	public static void flip(JPanel[][] cell, int i, int j) {
		Piece opponent = turn == Piece.BLACK ? Piece.WHITE : Piece.BLACK;
		if (hasE) {
			int row = i;
			int col = j + 1;
			while (col < 8 
					&& cell[row][col].getBackground() == pieceToColor(opponent)) {
				cell[row][col].setBackground(pieceToColor(turn));
				col++;
			}
		}
		if (hasW) {
			int row = i;
			int col = j - 1;
			while (col >= 0 
					&& cell[row][col].getBackground() == pieceToColor(opponent)) {
				cell[row][col].setBackground(pieceToColor(turn));
				col--;
			}
		}
		if (hasN) {
			int row = i -1;
			int col = j;
			while (row >= 0 
					&& cell[row][col].getBackground() == pieceToColor(opponent)) {
				cell[row][col].setBackground(pieceToColor(turn));
				row--;
			}
		}
		if (hasS) {
			int row = i + 1;
			int col = j;
			while (row < 8 
					&& cell[row][col].getBackground() == pieceToColor(opponent)) {
				cell[row][col].setBackground(pieceToColor(turn));
				row++;
			}
		}
		if (hasNE) {
			int row = i - 1;
			int col = j + 1;
			while (row >= 0 && col < 8
					&& cell[row][col].getBackground() == pieceToColor(opponent)) {
				cell[row][col].setBackground(pieceToColor(turn));
				row--;
				col++;
			}
		}
		if (hasNW) {
			int row = i - 1;
			int col = j - 1;
			while (row >= 0 && col >= 0
					&& cell[row][col].getBackground() == pieceToColor(opponent)) {
				cell[row][col].setBackground(pieceToColor(turn));
				row--;
				col--;
			}
		}
		if (hasSW) {
			int row = i + 1;
			int col = j - 1;
			while (row < 8 && col >= 0 
					&& cell[row][col].getBackground() == pieceToColor(opponent)) {
				cell[row][col].setBackground(pieceToColor(turn));
				row++;
				col--;
			}
		}
		if (hasSE) {
			int row = i + 1;
			int col = j + 1;
		    while (row < 8 && col < 8
					&& cell[row][col].getBackground() == pieceToColor(opponent)) {
				cell[row][col].setBackground(pieceToColor(turn));
				row++;
				col++;
		    }
		}
	}
	
	public static Color pieceToColor(Piece piece) {
		Color color = null;
		switch(piece) {
		case BLACK:
			color = Color.BLACK;
			break;
		case WHITE:
			color = Color.WHITE;
			break;
		default:
			break;
		
		}
		return color;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
