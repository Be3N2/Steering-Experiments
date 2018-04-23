package Particles;

import java.util.ArrayList;
import java.util.Random;

import Images.LoadImage;
import Noise.NoiseArray;
import Screen.Screen;

public class ParticleManager {

	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private int width, height; //simulation boundaries
	private float maxForce, maxVelocity;
	
	private int[] starX = {0,21,43,111,179,124,69,90,111,55,0, -21,-43,-111,-179,-124,-69,-90,-111,-55};
	private int[] starY = {-185,120,-55,-55,-55,-29,26,-14,156,116,76 ,-120,55,55,55,29,-26,14,-156,-116};
	
	private NoiseArray flowfield;
	
	private float[] luminance;
	
	private Random rand = new Random();
	
	public ParticleManager(int w, int h, int startNum, int force, int velocity) {
		this.width = w;
		this.height = h;
		this.maxForce = force;
		this.maxVelocity = velocity;
	
		for (int i = 0; i < startNum; i++) {
			int randX = rand.nextInt(width / 2) + width / 4;
			int randY = rand.nextInt(height / 2) + height / 4;
//			Particle part = new Particle(randX, randY, maxForce, maxVelocity, width, height);
			Particle part = new Particle(randX, randY, maxForce, maxVelocity, width, height, width/2, height/2);
			particleList.add(part);
		}
		
		flowfield = new NoiseArray(width / 4, height / 4);
		flowfield.update();//generates noise
		
		luminance = new float[width * height];
		loadLuminance();
	}
	
	public void update() {
		for (Particle i: particleList) {
			i.applyForce(i.separation(particleList)); //no need to weight
			i.update();
		}
	}
	
	public void updateFlow() {
		for (Particle i: particleList) {
			Vector flowForce = i.flowField(flowfield.noiseArr);
			Vector sepForce = i.separation(particleList);
			flowForce.mult(1);
			sepForce.mult(1);
			i.applyForce(flowForce);
			i.applyForce(sepForce);
			
			i.update();
		}
	}
	
	public void imageFlow() {
		for (Particle i: particleList) {
			Vector flowForce = i.imageFlow(luminance);
			Vector sepForce = i.separation(particleList);
			flowForce.mult(1);
//			sepForce.mult(0.5f);
			i.applyForce(flowForce);
//			i.applyForce(sepForce);
			
			i.update();
		}
	}
	
	public void updateShape() {
		for (int i = 0; i < particleList.size(); i++) {
			
			int object = i % starX.length;
			
			Vector arriveForce = particleList.get(i).arrive(starX[object]*2 + width /2, starY[object]*2 + height / 2);
			Vector sepForce = particleList.get(i).separation(particleList);
			arriveForce.mult(1);
			sepForce.mult(0);
			particleList.get(i).applyForce(arriveForce);
			particleList.get(i).applyForce(sepForce);
			
			particleList.get(i).update();
		}
	}
	
	public void updateFlock() {
		for (int i = 0; i < particleList.size(); i++) {
			
			Vector sepForce = particleList.get(i).separation(particleList);
			Vector alignForce = particleList.get(i).align(particleList);
			Vector cohesionForce = particleList.get(i).cohesion(particleList);
			sepForce.mult(1); //1
			alignForce.mult(0.7f); //10.5
			cohesionForce.mult(0.3f); //0.1
			particleList.get(i).applyForce(sepForce);
			particleList.get(i).applyForce(alignForce);
			particleList.get(i).applyForce(cohesionForce);
			
			particleList.get(i).update();
		}
	}
	
	public void update(int mouseX, int mouseY) {
		for (Particle i: particleList) {
			
			Vector seekForce = i.seek(mouseX, mouseY);
			Vector sepForce = i.separation(particleList);
			seekForce.mult(0);
			sepForce.mult(0);
			i.applyForce(seekForce);
			i.applyForce(sepForce);
	
			Vector arriveForce = i.arrive(mouseX, mouseY);
			i.applyForce(arriveForce);
			
			i.update();
		}
	}
	
	public void perpendic() {
		for (Particle i: particleList) {
			
			Vector seekForce = i.seek(width/2, height/2);
			Vector perpForce = i.perpendicular();
			seekForce.mult(1);
			perpForce.mult(1);
			i.applyForce(seekForce);
			i.applyForce(perpForce);
			
			i.update();
		}
	}
	
	public void render(Screen screen) {
		for (Particle i: particleList) {
			i.render(screen);
		}
	}
	
	public void addParticle(int x, int y) {
		Particle part = new Particle(x, y, maxForce, maxVelocity, width, height);
		particleList.add(part);
	}
	
	public void loadLuminance() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int xx = x;
				int yy = y;
				if (xx > LoadImage.candle.WIDTH) xx -= LoadImage.candle.WIDTH;
				if (yy > LoadImage.candle.HEIGHT) yy -= LoadImage.candle.HEIGHT;
				int col = LoadImage.candle.pixels[xx + yy * LoadImage.candle.WIDTH];
				int red = -1 * ((int) col / (255 * 255)) % 255; //red digits
				int green = -1 * ((int) col / 255) % 255; //green digits
				int blue = -1 * col % 255; //last two digits in hex
				luminance[x + y * width] = (float) (255 - (0.2126*red + 0.7152*green + 0.0722*blue));
			}
		}
	}
	
	
	
}
