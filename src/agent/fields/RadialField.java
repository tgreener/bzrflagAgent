
package agent.fields;

public abstract class RadialField extends Field{

	protected double radius;
	protected double spread;

	protected double gx;
	protected double gy;

	public RadialField(double a, double r, double x, double y) {
		super(a);
		radius = r;
		spread = Double.POSITIVE_INFINITY;
		this.gx = x;
		this.gy = y;
	}

	public RadialField(double a, double r, double s, double x, double y) {
		super(a);
		radius = r;
		spread = s;
		this.gx = x;
		this.gy = y;
	}

	public double getMaxMagnitude() {
		return spread * alpha;
	}

	protected double distanceToGoal(double x, double y) {
		return (new Vector2d(x - this.gx, y - this.gy)).length();
	}

	protected double getTheta(double x, double y) {
		return Math.atan2((gy - y), (gx - x));
	}
}

