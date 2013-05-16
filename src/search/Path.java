
package search;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Path {
	
	private List<Step> path;
	private double pathCost;

	public Path() {
		path = new LinkedList<Step>();
		pathCost = 0d;
	}

	public Path(Path p) {
		Iterator<Step> 
	}

	public void addStep(Step step) {
		path.add(step);
		pathCost += step.getCost();
	}

	public Step popStep() {
		Step popper = (LinkedList<Step>)path.removeLast();
		pathCost -= popper.getCost();

		return popper;
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
	
	public String toString() {
		String result = "Path Cost: " + pathCost;
		Iterator<Step> i = iterator();
		
		while(i.hasNext()) {
			result += "\n";
			result += i.next().toString();
		}

		return result;
	}
}

