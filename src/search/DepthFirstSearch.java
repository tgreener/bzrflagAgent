
package search;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.Point;
import state.SearchSpace;

public class DepthFirstSearch extends Search {
	
	Path best;
	Path traversing;

	public DepthFirstSearch(SearchSpace space) {
		super(space);
		traversing = new Path();
		best = null;
	}

	@Override
	public Path search(Point start) {
		traverse(start);
		
		return best;
	}

	private boolean traverse(Point sp) {
		if(space.getOccValue(sp.x, sp.y) == 0) {
			visit(sp);

			if(space.isGoal(sp.x, sp.y)) return true;
			
			List<Step> steps = expandChildren(sp);
			Iterator<Step> itr = steps.iterator();

			while(itr.hasNext()) {
				Step s = itr.next();
				traversing.addStep(s);
				if(traverse(s.getEndPoint())) return true;
				traversing.popStep();
			}
	
			//leave(sp);
		}
		
		return false;
	}

	private void visit(Point p) {
		space.visit(p.x, p.y);
		if(space.isGoal(p.x, p.y)) {
			if(best == null || best.getCost() > traversing.getCost()) {
				best = new Path(traversing);
			}
		}
	}

	private void leave(Point p) {
		space.unvisit(p.x, p.y);
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
		return space.inBounds(x, y) && !space.visited(x ,y) && (space.getOccValue(x, y) == 0);
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

		return step;
	}
}
