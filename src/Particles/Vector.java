package Particles;

public class Vector {

	public float x = 0,y = 0;
	
	public void limit(float max) {
		if (x > max) x = max;
		if (x < -max) x = -max;
		if (y > max) y = max;
		if (y < -max) y = -max;
	}
	
	public void add(Vector add) {
		x += add.x;
		y += add.y;
	}
	
	public void sub(Vector sub) {
		x -= sub.x;
		y -= sub.y;
	}
	
	public void mult(float scaler) {
		x *= scaler;
		y *= scaler;
	}
	
	public void div(float scaler) {
		x /= scaler;
		y /= scaler;
	}
	
	public float dist(Vector distVector) {
		return (float) Math.hypot(x - distVector.x, y - distVector.y);
	}
	
	public float getMag() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public void maxMag(float maxSpeed) {
		float oldMag = getMag();
		x = x * (maxSpeed / oldMag);
		y = y * (maxSpeed / oldMag);
	}
	
	public void normalize() {
		float mag = getMag();
		x /= mag;
		y /= mag;
	}
	
	
	
}
