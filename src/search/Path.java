
package search;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Path {
	
	private List<Step> path;
	private double pathCost;

	public Path() {
		path = new LinkedList<Step>();
		pathCost = 0d;
	}

	public Path(Path p) {
		path = new LinkedList<Step>();
		pathCost = 0d;
				
		Iterator<Step> i = p.iterator();
		
		while(i.hasNext()) {
			addStep(new Step(i.next()));
		}
	}

	public void addStep(Step step) {
		path.add(step);
		pathCost += step.getCost();
	}

	public Step popStep() {
		Step popper = ((LinkedList<Step>)path).removeLast();
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

	public void printToFile(String fileName) {
		try {
			PrintWriter p = new PrintWriter(fileName, "UTF-8");
			
			for(Step s : path) {
				p.print(s.getStartPoint().x + " " + s.getStartPoint().y + " ");
				p.print(s.getEndPoint().x + " " + s.getEndPoint().y + "\n");
			}

			p.close();
		}
		catch(FileNotFoundException e) {
			System.out.println(e);
		}
		catch(UnsupportedEncodingException e) {
			System.out.println(e);
		}
	}
}

