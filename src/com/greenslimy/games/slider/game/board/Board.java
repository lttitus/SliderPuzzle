package com.greenslimy.games.slider.game.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener {
	
	private Color backColor = Color.BLACK;
	private SliderPiece[][] sliderPieces;
	private SliderPiece[][] winningBoard;
	
	private int boardCols = 0;	//Number of columns on the board
	private int boardRows = 0;	//Number of rows on the board
	private int emptySlot = -1;	//The slot that is empty
	private int emptyCol = -1;	//The Column and Row of the empty slot...
	private int emptyRow = -1;	//Used when shuffling pieces
	
	private Color[] rainbow = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY, Color.CYAN};
	private BufferedImage boardImage = null;
	
	private int pieceSpacing = 1;	//# of pixels between adjacent pieces
	private int PIECE_WIDTH = 0;
	private int PIECE_HEIGHT = 0;
	
	private int movesMade = 0;
	
	public Board(int width, int height, int cols, int rows, BufferedImage image) {
		this.setSize(width, height);
		this.setBackground(backColor);
		this.boardImage = image;
		this.addMouseListener(this);
		createBoard(cols, rows);
		this.repaint();
	}
	
	public void createBoard(int cols, int rows) {
		movesMade = 0;
		winningBoard = sliderPieces = createSlider(cols, rows);
		shufflePieces(10*(cols*rows));	//10x board area so a 5x5 will be shuffled 250x
	}
	
	public SliderPiece[][] createSlider(int cols, int rows) {
		boardCols = cols;
		boardRows = rows;
		int pieceCount = cols*rows;
		int pieceIndex = 0;
		PIECE_WIDTH = (this.getWidth()/cols)-1;
		PIECE_HEIGHT = (this.getHeight()/rows)-1;
		SliderPiece[][] pieces = new SliderPiece[cols][rows];	//Create a piece array with max of cols*rows
		for(int c=0;c<cols;c++) {	//Cant cheat on Java with C++!
			for(int r=0;r<rows;r++) {
				if(pieceIndex != pieceCount-1) {	//Dont create the last piece because this is a slider puzzle
					SliderPiece newPiece = new SliderPiece(c, r, pieceIndex, PIECE_WIDTH, PIECE_HEIGHT);
					//Instantiate a new Slider Piece under this
					if(boardImage == null) {	//Check for a board image
						newPiece.setColorTheme(1F, 0.5F, Color.WHITE);
						//newPiece.setBackColor(Color.WHITE);
						//newPiece.setForeColor(Color.BLACK);
					}else{
						newPiece.pieceImage = boardImage.getSubimage(c*PIECE_WIDTH, r*PIECE_HEIGHT, PIECE_WIDTH, PIECE_HEIGHT);
					}
					pieces[c][r] = newPiece;
					pieceIndex++;
				}else{
					this.emptySlot = pieceIndex;	//This should always be rows*cols, but this is just to make sure
					this.emptyCol = c;
					this.emptyRow = r;
				}
			}
		}
		return pieces;
	}
	
	public void shufflePieces(int entropy) {
		Random r = new Random();
		for(int i=0;i<entropy;i++) {
			int randDir = r.nextInt(4);
			switch(randDir) {
			case 0:	//Move top piece down -> Up
				if(emptyRow+1 < boardRows) {
					movePiece(getPieceAtPos(emptyCol, emptyRow+1));
				}else{
					i-=1;	//Give it another try, just to keep up with Entropy.
				}
				break;
			case 1:	//Move right piece left -> Right
				System.out.println("Cols: "+boardCols+", "+(emptyCol+1));
				if(emptyCol+1 < boardCols) {
					movePiece(getPieceAtPos(emptyCol+1, emptyRow));
				}else{
					i-=1;
				}
				break;
			case 2:	//Move bottom piece up -> Down
				if(emptyRow-1 >= 0) {
					movePiece(getPieceAtPos(emptyCol, emptyRow-1));
				}else{
					i-=1;
				}
				break;
			case 3:	//Move left piece right -> Left
				if(emptyCol-1 >= 0) {
					movePiece(getPieceAtPos(emptyCol-1, emptyRow));
				}else{
					i-=1;
				}
				break;
			}
		}
		this.repaint();
	}
	
	public void changePieceAtPos(int col, int row, SliderPiece newPiece) {
		this.sliderPieces[col][row] = newPiece;
	}
	
	public SliderPiece getPieceAtPos(int col, int row) {
		return this.sliderPieces[col][row];
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(!hasWon) {
			for(int c=0;c<sliderPieces.length;c++) {
				for(int r=0;r<sliderPieces[c].length;r++) {
					
					SliderPiece piece = sliderPieces[c][r];
					int px = (c*PIECE_WIDTH);	//Piece x and y coordinates
					int py = (r*PIECE_HEIGHT);
					int pw = PIECE_WIDTH;		//Piece width and height
					int ph = PIECE_HEIGHT;
					int sx = px+pw/2;			//Writing x and y coordinates
					int sy = py+ph/2;
					
					if(piece != null) {
						g.setColor(piece.backColor);	//The square piece
						g.fillRect(px, py, pw, ph);
						if(piece.pieceImage != null) {	//must be placed here or else the outline and writing will not be visible
							g.drawImage(piece.pieceImage, px, py, null);
						}
						g.setColor(piece.foreColor);	//Piece outline
						g.drawRect(px, py, pw, ph);
						g.setColor(piece.foreColor);	//Writing on the piece
						g.drawString(""+(piece.pieceIndex+1), sx-10, sy+10);
					}else{
						if(emptySlot+1 == boardCols*boardRows) {
							if(!hasWon) {
								g.setColor(Color.RED);	//Writing on the piece
								g.drawString("Click here to check!", sx-10, sy+10);
							}
						}
						
						/*	DEBUG STUFF
						g.setColor(Color.RED);
						g.fillRect(c*PIECE_WIDTH, r*PIECE_HEIGHT, PIECE_WIDTH, PIECE_HEIGHT);
						g.setColor(Color.BLACK);
						g.drawRect(c*PIECE_WIDTH, r*PIECE_HEIGHT, PIECE_WIDTH, PIECE_HEIGHT);
						g.drawString(""+(emptySlot+1), (c*PIECE_WIDTH+PIECE_WIDTH/2)-10, (r*PIECE_HEIGHT+PIECE_HEIGHT/2)+10);
						*/
					}
				}
			}
		}else{
			int cx = this.getWidth()/2;
			int cy = this.getHeight()/2;
			String congrats = "This is a secret message that you should never get!! Well done!";
			String moveString = "You made "+movesMade+" moves out of "+(boardCols*boardRows*10)+" iterations.";
			int coffs = 2*congrats.length();
			int moffs = 2*moveString.length();	//J-Roc's offset
			cx = cx-coffs;
			if(boardImage != null) {
				congrats = "You solved the puzzle, click here to do it again with "+(boardCols+1)+" columns and "+(boardRows+1)+" rows!";
				g.drawImage(boardImage, 0, 0, null);
				clickReset = true;
			}else{
				congrats = "Conglaturation! You done it!";
			}
			g.setColor(Color.RED);
			g.drawString(congrats, cx, cy-10);
			cx = (cx+coffs)-moffs;
			g.drawString(moveString, cx, cy);
		}
	}
	
	/**
	 * Changes the color of a piece
	 * @param piece The SliderPiece object
	 * @param layerId 0 - Background, 1 - Foreground
	 * @param newColor The new color of the piece
	 */
	public void changePieceColor(SliderPiece piece, int layerId, Color newColor) {
		if(layerId == 0) {
			piece.backColor = newColor;
		}else if(layerId == 1) {
			piece.foreColor = newColor;
		}
		this.repaint();
	}
	
	public void movePiece(SliderPiece piece) {
		if(piece.piecePos != emptySlot) {
			int slotDiff = emptySlot-piece.piecePos;
			String moveTo = "Nowhere";
			if(piece.pieceCol == emptyCol || piece.pieceRow == emptyRow) {
				if(slotDiff == 1) {			//Down
					changePieceAtPos(piece.pieceCol, piece.pieceRow, null);
					changePieceAtPos(piece.pieceCol, piece.pieceRow+1, piece);
					piece.pieceRow += 1;
					piece.piecePos += 1;
					emptyRow -=1;
					emptySlot -= 1;
					moveTo = "Down";
				}else if(slotDiff == -1) {	//Up
					changePieceAtPos(piece.pieceCol, piece.pieceRow, null);
					changePieceAtPos(piece.pieceCol, piece.pieceRow-1, piece);
					piece.pieceRow -= 1;
					piece.piecePos -= 1;
					emptyRow +=1;
					emptySlot += 1;
					moveTo = "Up";
				}else if(slotDiff == boardRows) {	//Right
					changePieceAtPos(piece.pieceCol, piece.pieceRow, null);
					changePieceAtPos(piece.pieceCol+1, piece.pieceRow, piece);
					piece.pieceCol += 1;
					piece.piecePos += boardRows;
					emptyCol -=1;
					emptySlot -= boardRows;
					moveTo = "Right";
				}else if(slotDiff == -boardRows) {	//Left
					changePieceAtPos(piece.pieceCol, piece.pieceRow, null);
					changePieceAtPos(piece.pieceCol-1, piece.pieceRow, piece);
					piece.pieceCol -= 1;
					piece.piecePos -= boardRows;
					emptyCol +=1;
					emptySlot += boardRows;
					moveTo = "Left";
				}else{
					System.err.println("Bad slotDiff: "+slotDiff);
				}
				System.out.println("Moved "+moveTo);
				if(!moveTo.equals("Nowhere")) {
					movesMade++;
				}
			}
		}
		this.repaint();
	}
	
	public boolean checkWin() {
		int correctPieces = 0;
		for(int c=0;c<sliderPieces.length;c++) {
			for(int r=0;r<sliderPieces[c].length;r++) {
				SliderPiece userPiece = sliderPieces[c][r];
				if(userPiece != null) {
					if(userPiece.piecePos == userPiece.pieceIndex) {	//Compare the piece's position with the piece's starting index
						correctPieces++;
					}
				}else{	//The empty slot
					if((c+1)*(r+1) == boardCols*boardRows) {	//Check if our current index is the very last piece in the puzzle (Last slot is empty when puzzle is completed)
						correctPieces++;
					}
				}
			}
		}
		return correctPieces == boardCols*boardRows;
	}
	
	private boolean hasWon = false;
	public void doVictory() {
		hasWon = true;
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	private boolean clickReset = false;

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		if(!hasWon) {
			int apx = (mx/PIECE_WIDTH);		//Approximate x index(column) of piece
			int apy = (my/PIECE_HEIGHT);	//Approximate y index(row) of piece
			SliderPiece piece = getPieceAtPos(apx, apy);
			if(piece != null) {
				movePiece(piece);
				//Does not automatically check for a winning move. See if statement below...
			}else{
				if((apx+1)*(apy+1) == boardCols*boardRows) {	//When the user clicks on the last tile while it is empty. Slider puzzles are USUALLY completed when the bottom right corner is empty.
					if(checkWin()) {
						doVictory();
					}
				}else{
					System.err.println("There is no piece in this slot.");
				}
			}
		}else{
			if(clickReset) {
				hasWon = false;
				clickReset = false;
				this.createBoard(boardCols+1, boardRows+1);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

}
