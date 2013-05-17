
package search;

import java.awt.Point;
import state.SearchSpace;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class BreadthFirstSearch extends Search {
	
	Path best;
	LinkedList<Node> frontier;

	public BreadthFirstSearch(SearchSpace space) {
		super(space);
		best = new Path();
		frontier = new LinkedList<Node>();
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
		
		while(current.parent != null) {
			temp.add(current.step);
			current = current.parent;
		}

		while(temp.size() > 0) {
			newPath.addStep(temp.pop());
		}

		if(best.getCost() > newPath.getCost()) {
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

	private boolean canVisit(int x, int y) {
		return space.inBounds(x, y) && space.visited(x ,y);
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





