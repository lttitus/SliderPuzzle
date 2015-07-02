package com.greenslimy.games.slider.display;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.greenslimy.games.slider.game.Game;
import com.greenslimy.games.slider.game.board.Board;


public class Display extends JFrame {
	
	private Game game;
	
	private Board playField = null;
	
	public Display(Game game, int w, int h, int cols, int rows, BufferedImage image) {
		this.game = game;
		this.setPreferredSize(new Dimension(w, h));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.playField = new Board(w, h, cols, rows, image);
		this.add(playField);
		this.pack();
		this.setVisible(true);
	}

}
