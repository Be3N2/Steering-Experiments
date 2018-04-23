package Main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import Input.Keyboard;
import Input.Mouse;
import Noise.NoiseArray;
import Particles.ParticleManager;
import Screen.Screen;

public class Start {

	public static class Game extends Canvas implements Runnable{
		private static final long serialVersionUID = 1L;

		private final int SCALE = 1;
		private final int WIDTH = 1920;
		private final int HEIGHT = 1080;
		
		private static Thread thread;
		private JFrame frame;
		private Keyboard key;
		private Mouse mouse;
		private Screen screen;
		private static boolean running = false;
		private static String title = "Particle Simulator";
		
		private int counter = 0;
		
		private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		private ParticleManager particles;
		
		public enum Mode{
			RANDOM, FOLLOW
		}
		
		Mode mode;
		
		public Game() {
			Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
			setPreferredSize(size);
			
			frame = new JFrame();
			key = new Keyboard();
			addKeyListener(key);
			mouse = new Mouse();
			addMouseListener(mouse);
			addMouseMotionListener(mouse);
			
			screen = new Screen(WIDTH, HEIGHT);
			
			//WIDTH, HEIGHT, startNum, force, velocity 
			particles = new ParticleManager(WIDTH, HEIGHT, 100, 20, 1);
			
			mode = Mode.RANDOM;
		}
		
		public void start() {
			running = true;
			thread = new Thread(this, "The Image Thread");
			thread.start();
		}
	
		public void stop() {
			running = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			long lastTime  = System.nanoTime();
			long timer = System.currentTimeMillis();
			final double ns = 1000000000.0 / 60.0;
			double delta = 0;
			int frames = 0;
			int updates = 0;
			requestFocus();
			while(running){
				long now = System.nanoTime();
				delta += (now - lastTime) / ns;
				lastTime = now;
				while (delta >= 1){
					update();
					updates++;
					delta--;
					render();
					frames++;
				}
				
				
				if(System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					//System.out.println(updates + "ups," + frames + "fps");
					frame.setTitle(title + " | " + updates +  "ups," + frames + "fps");
					updates = 0;
					frames = 0;
				}		
			}
			stop();
		}
	
		public void update() {
			
			counter++;
			if (counter > 100000) counter = 0;
			
			key.update();
			
			if (mode == Mode.FOLLOW) {
				particles.update(mouse.getX(), mouse.getY());
			} else {
//				particles.imageFlow();
//				particles.updateFlow();
//				particles.updateFlock();
//				particles.update();
				particles.perpendic();
			}
			
			if (mouse.getB() == 1) {
				mode = Mode.RANDOM;
			} else if (mouse.getB() == 3) {
				mode = Mode.FOLLOW;
			}
			
			if (key.esc) {
				System.exit(0);
			}
		}
		
		public void render() {
			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(3);
				return;
			}
			
			Graphics g = bs.getDrawGraphics();
			
			screen.clear();
			particles.render(screen);
			
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = screen.pixels[i];
			}
			
			g.drawImage(image, 0 , 0 , getWidth(), getHeight(), null);
			
			g.dispose();
			bs.show();
		}
		
		public static void main(String[] args) {
			
			Game game = new Game();
			
			game.frame.setResizable(true);
			game.frame.setUndecorated(true);
			game.frame.setTitle(title);
			game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			game.frame.add(game);
			game.frame.pack();
			game.frame.setLocationRelativeTo(null);
			game.frame.setVisible(true);
			
			game.start();
		}
		
	}
}
