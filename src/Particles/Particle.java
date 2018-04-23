package Particles;

import java.util.ArrayList;
import java.util.Random;

import Images.Sprites;
import Screen.Screen;

public class Particle {

	private Vector position = new Vector(); //position
	private Vector velocity = new Vector(); //velocity 
	private Vector accel = new Vector(); //acceleration
	private float r;
	private float maxForce;
	private float maxSpeed;
	private Sprites sprite;
	private float separation;
	
	private Vector target = new Vector();
	
	private int width, height;
	
	private Random rand = new Random();
	
	public Particle(float startX, float startY, float mxSpeed, float mxForce, int width, int height) {
		position.x = startX;
		position.y = startY;
		this.maxSpeed = mxSpeed;
		this.maxForce = mxForce;
		
		r = 12; // was 20
		separation = (float) 2 * r;
		velocity.x = rand.nextInt(20) - 10;
		velocity.y = rand.nextInt(20) - 10;
		accel.x = 0;
		accel.y = 0;

		this.width = width;
		this.height = height;
		
		sprite = new Sprites((int) r, 0x50D0DE);
		sprite.circle(); //set to square
	}
	
	public Particle(float startX, float startY, float mxSpeed, float mxForce, int width, int height, int targetX, int targetY) {
		position.x = startX;
		position.y = startY;
		this.maxSpeed = mxSpeed;
		this.maxForce = mxForce;
		
		r = 20;
		separation = 2 * r;
		velocity.x = rand.nextInt(20) - 10;
		velocity.y = rand.nextInt(20) - 10;
		accel.x = 0;
		accel.y = 0;

		this.width = width;
		this.height = height;
		
		sprite = new Sprites((int) r, 0x50D0DE);
		sprite.circle(); //set to square
		
		target.x = targetX;
		target.y = targetY;
	}
	
	public void update() {
		
		//update velocity and speed
		velocity.add(accel);
				
		//check max speed
		velocity.limit(maxSpeed);
				
		//move to new position
		position.add(velocity);
			
		//reset acceleration for next cycle
		accel.x = 0;
		accel.y = 0;
				
		//checkBorder
		checkBorder();
	}
	
	public void render(Screen screen) {
		screen.renderSprite(sprite, (int) position.x, (int) position.y);
	}
	
	public void applyForce(Vector force) {
		//could implement mass with A = F / M (F = M * A)
		accel.add(force);
	}
	
	private void checkBorder() {
		if (position.x < 0) position.x += width;
		if (position.y < 0) position.y += height;
		if (position.x > width) position.x -= width;
		if (position.y > height) position.y -= height;
	}
	
	//STEER = DESIRED - VELOCITY
	
	public Vector seek(float targetX, float targetY) {
		
		Vector force = new Vector();
		
		//target - position
		force.x = targetX - position.x;
		force.y = targetY - position.y;
		
		//scale to maximum speed
		force.maxMag(maxSpeed);
		
		//steer = desired - velocity 
		force.sub(velocity);
		
		//also make sure its limited
		force.limit(maxForce);
		
		return force;
	}
	
	public Vector separation(ArrayList<Particle> particles) {
		
	    Vector sum = new Vector();
	    int count = 0;
	    
	    for (Particle p: particles) {
	    	
	    	float d = position.dist(p.position);
	      
	    	//distance = 0 then its comparing itself
	    	if ((d > 0) && (d < separation)) {
	   
	    		// Calculate vector pointing away from neighbor
	    		
	    		//diff = position makes them the same object!!!! BAD
	    		Vector diff = new Vector();
	    		diff.x = position.x;
	    		diff.y = position.y;
	    		
	    		//instead of target - position this does position - "target"
	    		diff.sub(p.position);
	        
	    		diff.normalize();   // idk what this does
	    		diff.div(d);        // Weight by distance
	    		sum.add(diff);
	    		count++;            // Keep track of how many
	    	}
	    }
	    
	    if (count > 0) {
	    	
	    	//sum / count
	    	sum.div(count);
	    	// Our desired vector is moving away maximum speed
	    	sum.maxMag(maxSpeed);
	    	//steer = desired - velocity
	     	sum.sub(velocity);
	     	
	     	sum.limit(maxForce);
	     	
	    	return sum;
	    }
	    //empty force vector
	    return sum;
	}
	
	public Vector align(ArrayList<Particle> particles) {
		
	    Vector sum = new Vector();
	    int count = 0;
	    int align = 50;
	    
	    for (Particle p: particles) {
	    	
	    	float d = position.dist(p.position);
	      
	    	//distance = 0 then its comparing itself
	    	if ((d > 0) && (d < align)) {
	    		      
	    		//apply force in the same direction as neighbor
	    		sum.add(p.velocity);
	    		
	    		count++;      
	    	}
	    }
	    
	    if (count > 0) {
	    	
	    	//sum / count
	    	sum.div(count);
	    	// Our desired vector average of all neighbor vectors
	    	sum.maxMag(maxSpeed);
	    	//steer = desired - velocity
	     	sum.sub(velocity);
	     	
	     	sum.limit(maxForce);
	     	
	    	return sum;
	    }
	    //empty force vector
	    return sum;
	}
	
	//going to center of all neighbors
	public Vector cohesion(ArrayList<Particle> particles) {
		
	    Vector sum = new Vector();
	    int count = 0;
	    int align = 50;
	    
	    for (Particle p: particles) {
	    	
	    	float d = position.dist(p.position);
	      
	    	//distance = 0 then its comparing itself
	    	if ((d > 0) && (d < align)) {
	    		      
	    		//apply force in the same direction as neighbor
	    		sum.add(p.position);
	    		
	    		count++;      
	    	}
	    }
	    
	    if (count > 0) {
	    	
	    	//sum / count
	    	sum.div(count);
	     	
	    	return seek(sum.x, sum.y);
	    }
	    //empty force vector
	    return sum;
	}
	
	public Vector arrive() {
		Vector force = new Vector();
		
		if (target.x != 0 && target.y != 0) {
			force.x = target.x;
			force.y = target.y;
			//distance (with mag)
			force.sub(velocity);
			float d = force.getMag();
			
			if (d < 100) { //slow down a little towards target
				float m = d / 100 * maxSpeed; //scale from 0-100 to 0-maxspeed
				force.maxMag(m);
			} else {
				//go max speed
				force.maxMag(maxSpeed);
			}
		}
		//steer = desired - velocity
		force.sub(velocity);
		force.limit(maxForce);
		
		return force;
	}
	
	public Vector arrive(int mouseX, int mouseY) {
		Vector force = new Vector();
		
		
		force.x = mouseX;
		force.y = mouseY;
		//distance (with mag)
		force.sub(position);
		float d = force.getMag();
		
		if (d < 100) { //slow down a little towards target
			float m = d / 100 * maxSpeed; //scale from 0-100 to 0-maxspeed
			force.maxMag(m);
		} else {
			//go max speed
			force.maxMag(maxSpeed);
		}
		
		//steer = desired - velocity
		force.sub(velocity);
		force.limit(maxForce);
		
		return force;
	}
	
	public Vector perpendicular() {
		Vector force = new Vector();
		//same as arrive but apply force perpendicular to target
		
		if (target.x != 0 && target.y != 0) {
			force.x = target.x;
			force.y = target.y;
			//distance (with mag)
			force.sub(velocity);
			
			float d = force.getMag();
			d = 1000;
			if (d < 100) { //slow down a little towards target
				float m = d / 100 * maxSpeed; //scale from 0-100 to 0-maxspeed
				force.maxMag(m);
			} else {
				//go max speed
				force.maxMag(maxSpeed);
			}
		}
		
		//to go perpendicular switch x and y and mult new y by -1
		float temp = force.x;
		force.x = force.y;
		force.y = -1 * temp;
		
		//steer = desired - velocity
		force.sub(velocity);
		force.limit(maxForce);
		
		return force;
	}
	
	public Vector flowField(float[] field) {
		Vector force = new Vector();
		
		//width / 8 is width of field
		int accessX = ((int) position.x) / 4;
		int accessY = ((int) position.y) / 4;
		
		float angle = (float) (field[accessX + (accessY * width / 4)] * 2 * Math.PI);
		force.x = (float) Math.cos(angle); //adjacent / hypot which if unit vector is 1
		force.y = (float) Math.sin(angle);
		
		//scale it up to max speed
		force.mult(maxSpeed);
		
		//steer = desired - velocity
		force.sub(velocity);
		force.limit(maxForce);
		
		return force;
	}
	
	public Vector imageFlow(float[] luminance) {
		Vector force = new Vector();
		
		//scales the luminance value to 0-2PI
		float angle = (float)((luminance[ (((int)position.x) + ((int)position.y) * width)] / 255) * 2 * Math.PI);
		force.x = (float) Math.cos(angle);
		force.y = (float) Math.sin(angle);
		
		//scale it up to max speed
		force.mult(maxSpeed);
		
		//steer = desired - velocity
		force.sub(velocity);
		force.limit(maxForce);
				
		return force;
	}

}
