
package agent.fields;

public class RepulsiveRadialField extends RadialField {
	
	public RepulsiveRadialField(double a, double r, double x, double y) {
		super(a, r, x, y);
	}

	public RepulsiveRadialField(double a, double r, double s, double x, double y) {
		super(a, r, s, x, y);
	}

	public Vector2d fieldAtPoint(double x, double y) {
		double d = distanceToGoal(x, y);
		double theta = getTheta(x, y);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);

		double dx = 0;
		double dy = 0;
		
		if(radius <= d && d <= (spread + radius)) {
			dx = -alpha * (spread + radius - d) * cosTheta;
			dy = -alpha * (spread + radius - d) * sinTheta;
		}
		else if(d < radius){
			dx = getMaxMagnitude() * cosTheta;
			dy = getMaxMagnitude() * sinTheta;
		}
		
		Vector2d result = new Vector2d(dx, dy);

		return result;
	} 
	
}


