
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

	private void traverse(Point sp) {
		visit(sp);

		if(space.isGoal(sp.x, sp.y)) return;
		
		List<Step> steps = expandChildren(sp);
		Iterator<Step> itr = steps.iterator();

		while(itr.hasNext()) {
			Step s = itr.next();
			traversing.addStep(s);
			traverse(s.getEndPoint());
			traversing.popStep();
		}

		leave(sp);
	}

	public void visit(Point p) {
		space.visit(p.x, p.y);
		if(space.isGoal(p.x, p.y)) {
			if(best == null || best.getCost() > traversing.getCost()) {
				best = new Path(traversing);
			}
		}
	}

	public void leave(Point p) {
		space.unvisit(p.x, p.y);
	}

	private List<Step> expandChildren(Point parent) {
		List<Step> children = new LinkedList<Step>();
		int x = parent.x;
		int y = parent.y;
	
		if(space.inBounds(x - 1, y)) {
			children.add(createStep(x, y, x - 1, y));
		}
		if(space.inBounds(x + 1, y)) {
			children.add(createStep(x, y, x + 1, y));
		}
		if(space.inBounds(x, y + 1)) {
			children.add(createStep(x, y, x, y + 1));
		}
		if(space.inBounds(x, y - 1)) {
			children.add(createStep(x, y, x, y - 1));
		}
		if(space.inBounds(x - 1, y + 1)) {
			children.add(createStep(x, y, x - 1, y + 1));
		}
		if(space.inBounds(x + 1, y + 1)) {
			children.add(createStep(x, y, x + 1, y + 1));
		}
		if(space.inBounds(x - 1, y - 1)) {
			children.add(createStep(x, y, x - 1, y - 1));
		}
		if(space.inBounds(x + 1, y - 1)) {
			children.add(createStep(x, y, x + 1, y - 1));
		}

		return children;
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
