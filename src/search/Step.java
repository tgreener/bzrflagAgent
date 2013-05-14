
package search;

import java.awt.Point;

public class Step {

	private double cost;
	private Point startPoint;
	private Point endPoint;

	public Step(Point sp, Point ep) {
		startPoint = sp;
		endPoint = ep;
		this.cost = sp.distance(ep);
	}

	public Step(Point sp, Point ep, double cost) {
		startPoint = sp;
		endPoint = ep;
		this.cost = cost;
	}

	public double getCost() {
		return cost;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}
}
