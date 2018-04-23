package Noise;

import java.util.Random;

public class NoiseArray {

	//generates field of angles (in radians)
	
	public float[] noiseArr;
	public int width, height;
	private double x, y, z;
	Random rand = new Random();
	
	private Perlin perlin;
	
	public NoiseArray(int w, int h) {

		this.width = w;
		this.height = h;
		noiseArr = new float[w * h];
		
		x = rand.nextDouble();
		y = rand.nextDouble();
		z = rand.nextDouble();
	}
	
	public void update() {
		z -= 1;
		generatePerlin();
//		generateSimplex();
	}
	
	private void generatePerlin() {

		for (int b = 0; b < height; b++) {
			for (int a = 0; a < width; a++) {
				double xx = x + a;
				double yy = y + b;
				double noise = perlin.noise(xx, yy, z);
				noiseArr[a + b * width] = (float) noise;
//				noiseArr[a + b * width] = (float) ;
//				System.out.println(Math.toDegrees(noiseArr[a + b * width] * 2 * Math.PI));
			}
		}
//		System.out.println("finished");
	}
	
	private void generateSimplex() {
		Simplex simplexNoise=new Simplex(100,0.1,5000);

	    double xStart=0;
	    double XEnd=500;
	    double yStart=0;
	    double yEnd=500;

	    for(int i=0;i<height;i++){
	        for(int j=0;j<width;j++){
	            int x=(int)(xStart+i*((XEnd-xStart)/width));
	            int y=(int)(yStart+j*((yEnd-yStart)/height));
	            float noise =(float) ((1+simplexNoise.getNoise(x,y)));
	            noiseArr[j + i * width] = (float) (noise * 2 * Math.PI);
	            System.out.println(Math.toDegrees(noiseArr[j + i * width]));
	        }
	    }
	}
}
