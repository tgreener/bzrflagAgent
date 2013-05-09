
package agent.fields;

import java.awt.geom.Point2D;

public abstract class Field {
	
	protected double alpha;

	public Field(double a) {
		alpha = a;
	}

	public abstract Vector2d fieldAtPoint(double x, double y);
	public abstract Vector2d fieldAtPoint(Point2D p);
	
}


