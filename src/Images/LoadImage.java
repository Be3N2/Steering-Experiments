package Images;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LoadImage {

	public final int WIDTH, HEIGHT;
	public int[] pixels;
	
	public static LoadImage pond = new LoadImage(1920, 1080, "/pond.png");	
	public static LoadImage green = new LoadImage(1920, 1080, "/green.png");	
	public static LoadImage candle = new LoadImage(1920, 1080, "/candle.png");	
	public static LoadImage abstractImg = new LoadImage(1920, 1080, "/abstract.png");
	
	public LoadImage(int w, int h, String path) {
		this.WIDTH = w;
		this.HEIGHT = h;
		
		pixels = new int[WIDTH * HEIGHT];
		
		load(path);
	}
	
	public void load(String path) {
		try {
			BufferedImage image = ImageIO.read(LoadImage.class.getResource(path));
			image.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
