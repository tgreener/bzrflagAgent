
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
		String result = "Path Cost: " + pathCost + "\n";
		result += "Path Steps: " + path.size();
		Iterator<Step> i = iterator();
		
		while(i.hasNext()) {
			result += "\n";
			result += i.next().toString();
		}

		return result;
	}

	public void printToFile(String fileName, int granularity) {
		try {
			PrintWriter p = new PrintWriter(fileName, "UTF-8");
			int counter = 0;

			p.println("set title \"" + fileName +"\"");
			p.println("set xrange [-200.0: 200.0]");
			p.println("set yrange [-200.0: 200.0]");
			p.println("unset key");
			p.println("set size square");		

			for(Step s : path) {
				if(granularity < counter) {
					counter = 0;
					p.print("set arrow from ");
					double dx = s.getEndPoint().getX() - s.getStartPoint().getX();
					double dy = s.getEndPoint().getY() - s.getStartPoint().getY();

					p.print(s.getStartPoint().getX() + ", " + s.getStartPoint().getY() + " to ");
					p.print(s.getEndPoint().getX() + ", " + s.getEndPoint().getY() + " nohead lt 2\n");

					p.println("plot \'-\' with lines");
					p.println("0 0 0 0\ne\n");

				}
				else {
					counter++;
				}
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

	public void printToVectorFile(String fileName) {
		try {
			PrintWriter p = new PrintWriter(fileName, "UTF-8");
			p.println("plot \"-\" with vectors nohead lt 2");

			for(Step s : path) {
				double dx = s.getEndPoint().getX() - s.getStartPoint().getX();
				double dy = s.getEndPoint().getY() - s.getStartPoint().getY();

				p.print(s.getStartPoint().getX() + ", " + s.getStartPoint().getY() + " ");
				p.print(dx + ", " + dy + "\n");

				
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

