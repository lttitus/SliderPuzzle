package com.greenslimy.games.slider;

import com.greenslimy.games.slider.game.Game;


public class Start {
	
	private static final int stdc = 4;	//Standard columns
	private static final int stdr = 4;	//Standard rows
	private static Game game;

	public static void main(String[] args) {
		int cols = 0;
		int rows = 0;
		String imgPath = null;
		if(args.length > 0) {
			if(args.length >= 2) {
				cols = Integer.parseInt(args[0]);
				rows = Integer.parseInt(args[1]);
				if(args.length == 3) {
					imgPath = args[2];
				}
			}
		}else{	//No arguments supplied, set up a standard board. See stdc and stdr above...
			System.out.println("No arguments were supplied, creating a "+stdc+"x"+stdr+" board with no image.");
			game = new Game(stdc, stdr);
		}
		if(imgPath != null) {
			System.out.println("Creating a "+cols+"x"+rows+" board with image at path \""+imgPath+"\"");
			game = new Game(cols, rows, imgPath);
		}else{
			System.out.println("Creating a "+cols+"x"+rows+" board with no image.");
			game = new Game(cols, rows);
		}
	}

}
