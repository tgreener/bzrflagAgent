
package agent.fields;

public class Vector2d {
		
	private double x;
	private double y;

	public Vector2d() {
		x = 0;
		y = 0;
	}

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2d(Vector2d vec) {
		x = vec.getX();
		y = vec.getY();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Vector2d add(Vector2d vec) {
		return new Vector2d(x + vec.getX(), y + vec.getY());
	}

	public Vector2d negate() {
		return new Vector2d(-x, -y);
	}

	public Vector2d normalize() {
		double length = length();

		return new Vector2d(x / length, y / length);
	}

	public double dot(Vector2d vec) {
		return (x * vec.getX()) + (y * vec.getY());
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}	

	public double lengthSquared() {
		return Math.pow(x, 2) + Math.pow(y, 2);
	}

	public double angle(Vector2d vec) {
		return Math.acos(dot(vec) / (vec.length() * length()));
	}
}



