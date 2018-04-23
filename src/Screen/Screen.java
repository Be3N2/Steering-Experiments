package Screen;

import Images.LoadImage;
import Images.Sprites;

public class Screen {
	
	private final int WIDTH, HEIGHT;
	public int[] pixels;
	
	public Screen(int w, int h) {
		this.WIDTH = w;
		this.HEIGHT = h;
		
		this.pixels = new int[WIDTH * HEIGHT];
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0x4D4D4D;
//			pixels[i] = LoadImage.candle.pixels[i];
		}
	}
	
	public void renderSprite(Sprites sprite, int posX, int posY) {
		posX -= sprite.radius; //center the objects correctly
		posY -= sprite.radius;
		for (int y = 0; y < sprite.height; y++) {
			for (int x = 0; x < sprite.width; x++) {
				int xx = x + posX;
				int yy = y + posY;

				if (xx >= WIDTH) xx -= WIDTH;
				if (yy >= HEIGHT) yy -= HEIGHT;
				if (xx < 0) xx += WIDTH;
				if (yy < 0) yy += HEIGHT;

				int col = sprite.pixels[x + y * sprite.width];
				if (col != 0xffff0ff) pixels[xx + yy * WIDTH] = col;
			}
		}
	}
}
