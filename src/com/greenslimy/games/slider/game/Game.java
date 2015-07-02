package com.greenslimy.games.slider.game;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.greenslimy.games.slider.display.Display;
import com.greenslimy.games.slider.game.board.Board;


public class Game {
	
	//private static final String sliderImagePath = "image/minecraft.png";
	private static final int WIDTH = 1440;
	private static final int HEIGHT = 810;
	private Display display = null;
	
	private Board sliderBoard;
	
	public Game(int cols, int rows) {
		createGame(cols, rows);
	}
	
	public Game(int cols, int rows, String imagePath) {
		createGame(cols, rows, imagePath);
	}
	
	private void createGame(int cols, int rows) {
		display = new Display(this, WIDTH, HEIGHT, cols, rows, null);
	}
	
	private void createGame(int cols, int rows, String path) {
		BufferedImage img = getUsableImage(path);
		if(img != null) {
			display = new Display(this, WIDTH, HEIGHT, cols, rows, img);
		}else{
			System.err.println("Could not find image at path \""+path+"\"");
			this.createGame(cols,  rows);
		}
	}
	
	private BufferedImage getUsableImage(String imagePath) {
		BufferedImage sliderImage = null;
		try {
			File f = new File(imagePath);
			if(f.exists()) {
				sliderImage = resizeImage(ImageIO.read(f));
			}else{
				sliderImage = resizeImage(ImageIO.read(new URL(imagePath)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sliderImage;
	}
	
	private BufferedImage resizeImage(BufferedImage img) {
		if((img.getWidth() > WIDTH || img.getWidth() < WIDTH) || 
		   (img.getHeight() < HEIGHT || img.getHeight() > HEIGHT)) {	//Resize the image
			Image tmp = img.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
			img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();		//Provided at: "http://stackoverflow.com/questions/9417356/bufferedimage-resize"
		}
		return img;
	}

}
