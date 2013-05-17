
package search;

import java.awt.Point;

public class Step {

	private double cost;
	private Point startPoint;
	private Point endPoint;

	public Step(Point ep) {
		startPoint = null;
		endPoint = ep;
		this.cost = 0;
	}
	
	public Step(Point sp, Point ep) {
		startPoint = sp;
		endPoint = ep;
		this.cost = sp.distance(ep);
	}

	public Step(Point sp, Point ep, double penalty) {
		startPoint = sp;
		endPoint = ep;
		this.cost = sp.distance(ep) * penalty;
	}

	public Step(Step s) {
		startPoint = s.getStartPoint();
		endPoint = s.getEndPoint();
		cost = s.getCost();		
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

	public String toString() {
		String result = "Start: (" + startPoint.x + ", " + startPoint.y + "), ";
		result += "End: (" + endPoint.x + ", " + endPoint.y + "), Cost: " + cost;

		return result;
	}
}

