
package search;

import java.awt.Point;
import state.SearchSpace;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class BreadthFirstSearch extends Search {
	
	Path best;
	LinkedList<Node> frontier;
	PrintWriter p;

	public BreadthFirstSearch(SearchSpace space, String filename) {
		super(space);
		best = null;
		frontier = new LinkedList<Node>();
		
		try {
			p = new PrintWriter(filename, "UTF-8");
		}
		catch(FileNotFoundException e) {
			System.out.println(e);
		}
		catch(UnsupportedEncodingException e) {
			System.out.println(e);
		}
		p.println("set title \"" + filename +"\"");
		p.println("set xrange [-200.0: 200.0]");
		p.println("set yrange [-200.0: 200.0]");
		p.println("unset key");
		p.println("set size square");	

		
	}

	@Override
	public Path search(Point start) {
		traverse(start);
			
		return best;
	}

	private void traverse(Point p) {		
		if(space.getOccValue(p.x, p.y) == 0) {
			frontier.add(new Node(null, new Step(p)));
			space.visit(p.x, p.y);			

			while(frontier.size() > 0) {
				Node n = frontier.pop();
				printNodeToFile(n);
				
				Point position = n.step.getEndPoint();
				if(space.isGoal(position.x, position.y))
					generatePath(n);

				List<Step> edges = expandChildren(position);
				Iterator<Step> itr = edges.iterator();
				while(itr.hasNext()) {
					Step s = itr.next();
					Node child = new Node(n, s);
					frontier.add(child);
				}
			}
		}
	}


	private void generatePath(Node n) {
		Path newPath = new Path();
		Node current = n;
		LinkedList<Step> temp = new LinkedList<Step>();
		//System.out.println("Goal! " + n.step);
		
		while(current.parent != null) {
			temp.add(current.step);
			//System.out.println(current.step + " " + temp.size());
			current = current.parent;
		}

		while(temp.size() > 0) {
			Step addThis = temp.removeLast();
			//System.out.println(addThis + " " + temp.size());
			newPath.addStep(addThis);
		}

		if(best == null || best.getCost() > newPath.getCost()) {
			best = newPath;
		}
	}

	private List<Step> expandChildren(Point parent) {
		List<Step> children = new LinkedList<Step>();
		int x = parent.x;
		int y = parent.y;
	
		if(canVisit(x - 1, y)) {
			children.add(createStep(x, y, x - 1, y));
		}
		if(canVisit(x + 1, y)) {
			children.add(createStep(x, y, x + 1, y));
		}
		if(canVisit(x, y + 1)) {
			children.add(createStep(x, y, x, y + 1));
		}
		if(canVisit(x, y - 1)) {
			children.add(createStep(x, y, x, y - 1));
		}
		if(canVisit(x - 1, y + 1)) {
			children.add(createStep(x, y, x - 1, y + 1));
		}
		if(canVisit(x + 1, y + 1)) {
			children.add(createStep(x, y, x + 1, y + 1));
		}
		if(canVisit(x - 1, y - 1)) {
			children.add(createStep(x, y, x - 1, y - 1));
		}
		if(canVisit(x + 1, y - 1)) {
			children.add(createStep(x, y, x + 1, y - 1));
		}

		return children;
	}

	private void printNodeToFile(Node n) {
		Step s = n.step;

		p.print("set arrow from ");

		p.print(s.getStartPoint().getX() + ", " + s.getStartPoint().getY() + " to ");
		p.print(s.getEndPoint().getX() + ", " + s.getEndPoint().getY() + " nohead lt 2\n");

		p.println("plot \'-\' with lines");
		p.println("0 0 0 0\ne\n");
	}

	private boolean canVisit(int x, int y) {
		return space.inBounds(x, y) && !space.visited(x ,y) && space.getOccValue(x, y) == 0;
	}

	private Step createStep(int sx, int sy, int ex, int ey) {
		boolean penalizeStart = space.hasPenalty(sx, sy);
		boolean penalizeEnd = space.hasPenalty(ex, ey);

		Step step;

		if(penalizeStart && penalizeEnd) {
			step = new Step(new Point(sx, sy), new Point(ex, ey), 1.5d);
		}
		else if(penalizeStart) {
			step = new Step(new Point(sx, sy), new Point(ex, ey), 1.1d);
		}
		else if(penalizeEnd) {
			step = new Step(new Point(sx, sy), new Point(ex, ey), 1.3d);	
		}
		else {
			step = new Step(new Point(sx, sy), new Point(ex, ey));
		}

		space.visit(ex, ey);

		return step;
	}

	private class Node {
		public Node parent;
		public Step step;

		public Node(Node p, Step s) {
			parent = p;
			step = s;
		}

	}
}





