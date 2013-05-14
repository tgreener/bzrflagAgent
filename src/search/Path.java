
package search;

import java.util.List;
import java.util.LinkedList;

public class Path {
	
	private List<Step> path;
	private double length;

	public Path() {
		path = new LinkedList<Step>();
		length = 0d;
	}

	public void addStep(Step step) {
		path.add(step);
		length += step.getCost();
	}

	public double getLength() {
		return length;
	}
}

