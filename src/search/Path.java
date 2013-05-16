
package search;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Path {
	
	private List<Step> path;
	private double pathCost;

	public Path() {
		path = new LinkedList<Step>();
		length = 0d;
	}

	public void addStep(Step step) {
		path.add(step);
		pathCost += step.getCost();
	}

	public double getCost() {
		return pathCost;
	}

	public int getLength() {
		return path.size();
	}

	public Iterator<Step> iterator() {
		return path.iterator();
	}
}

