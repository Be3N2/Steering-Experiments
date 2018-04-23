package Images;

public class Sprites {

	public int[] pixels;
	public int width, height;
	public int radius;
	private int col;
	
	public Sprites(int r, int color) { //radius
		this.pixels = new int[4 * r * r];
		this.width = 2 * r;
		this.height = 2 * r;
		this.radius = r;
		this.col = color;
	}
	
	public void circle() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int xCenter = width - radius;
				int yCenter = height - radius;
				if (Math.hypot(x - xCenter, y - yCenter) <= radius) {
					pixels[x + y * width] = col;
				} else {
					pixels[x + y * width] = 0xffff0ff;
				}
			}
		}
	}
	
	public void square() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = col;
			}
		}
	}
	
}
