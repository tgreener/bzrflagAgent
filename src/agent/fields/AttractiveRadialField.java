
package agent.fields;

import java.awt.geom.Point2D;

public class AttractiveRadialField extends RadialField{

	public AttractiveRadialField(double a, double r, double x, double y) {
		super(a, r, x, y);
	}

	public AttractiveRadialField(double a, double r, double s, double x, double y) {
		super(a, r, s, x, y);
	}

	public Vector2d fieldAtPoint(Point2D p) {
		return fieldAtPoint(p.getX(), p.getY());
	} 

	public Vector2d fieldAtPoint(double x, double y) {
		double d = distanceToGoal(x, y);
		double theta = getTheta(x, y);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);

		double dx = 0;
		double dy = 0;
		
		if(radius <= d && d <= (spread + radius)) {
			dx = alpha * (d - radius) * cosTheta;
			dy = alpha * (d - radius) * sinTheta;
		}
		else if(d > (spread + radius)){
			dx = alpha * spread * cosTheta;
			dy = alpha * spread * sinTheta;
		}
		
		Vector2d result = new Vector2d(dx, dy);

		return result;
	} 
}

