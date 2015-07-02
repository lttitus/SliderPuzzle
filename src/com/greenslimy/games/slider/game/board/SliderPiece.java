package com.greenslimy.games.slider.game.board;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SliderPiece {
	
	int pieceIndex = -1;
	int piecePos = -1;
	int pieceCol = -1;
	int pieceRow = -1;
	int pieceWidth = -1;
	int pieceHeight = -1;
	
	Color backColor = Color.BLACK;
	Color foreColor = Color.RED;
	BufferedImage pieceImage = null;
	
	public SliderPiece(int pieceCol, int pieceRow, int pieceIndex, int pieceWidth, int pieceHeight) {
		this.pieceCol = pieceCol;
		this.pieceRow = pieceRow;
		this.pieceIndex = pieceIndex;
		this.piecePos = pieceIndex;
		this.pieceWidth = pieceWidth;
		this.pieceHeight = pieceHeight;
	}
	
	public void setBackColor(Color newColor) {
		this.backColor = newColor;
	}
	
	public void setForeColor(Color newColor) {
		this.foreColor = newColor;
	}
	
	public void setColorTheme(float back, float fore, Color newColor) {
		Color moddedBack = (new Color((int)(back*newColor.getRed())&255, 
									  (int)(back*newColor.getGreen())&255, 
									  (int)(back*newColor.getBlue())&255));
		Color moddedFore = (new Color((int)(fore*newColor.getRed())&255, 
									  (int)(fore*newColor.getGreen())&255, 
									  (int)(fore*newColor.getBlue())&255));
		setBackColor(moddedBack);
		setForeColor(moddedFore);
	}

}
