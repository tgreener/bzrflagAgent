
package search;

import java.awt.Point;
import state.SearchSpace;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class IDDFS extends Search {
	
	Path nodes;
	Path traversing;
	long depth;
	boolean goal;
	PrintWriter p;
	int counter;
	int granularity;

	public IDDFS(SearchSpace space) {
		super(space);
		traversing = new Path();
		nodes = new Path();
		depth = 0;
		goal = false;

		try {
			p = new PrintWriter("iddfsNodes.dat", "UTF-8");
		}
		catch(FileNotFoundException e) {
			System.out.println(e);
		}
		catch(UnsupportedEncodingException e) {
			System.out.println(e);
		}
		p.println("set title \"iddfsNodes.dat\"");
		p.println("set xrange [-190.0: -170.0]");
		p.println("set yrange [-10.0: 10.0]");
		p.println("unset key");
		p.println("set size square");

		granularity = 0;
		counter = 0;			
	}

	private void printStepToFile(Step s) {
		if(granularity == counter) {
			counter = 0;		

			p.print("set arrow from ");

			p.print(s.getStartPoint().getX() + ", " + s.getStartPoint().getY() + " to ");
			p.print(s.getEndPoint().getX() + ", " + s.getEndPoint().getY() + " nohead lt 2\n");

			p.println("plot \'-\' with lines");
			p.println("0 0 0 0\ne");
			p.println("pause 0.2");
			p.flush();
		}
		else {
			counter++;
		}
	}

	@Override
	public Path search(Point start) {
		while(!this.goal && depth < 6) {
			System.out.println("Depth: " + depth);
			traverse(start, 0);
			space.reset();
			depth++;
			
		}
		
		return traversing;
	}

	public void printNodes(int granularity) {
		nodes.printToFile("iddfsNodes.dat", granularity);
	}

	private boolean traverse(Point sp, long d) {
		if(space.getOccValue(sp.x, sp.y) == 0 && d < depth) {
			visit(sp);

			if(space.isGoal(sp.x, sp.y) ) return true;
			
			List<Step> steps = expandChildren(sp);
			Iterator<Step> itr = steps.iterator();

			while(itr.hasNext()) {
				Step s = itr.next();
				//System.out.println(s + " " + d + ", " + depth);
				traversing.addStep(s);
				printStepToFile(s);
				nodes.addStep(s);
				if(traverse(s.getEndPoint(), d+1)) return true;
				traversing.popStep();
			}
	
			//leave(sp);
		}
		
		return false;
	}

	private void visit(Point p) {
		space.visit(p.x, p.y);
		if(space.isGoal(p.x, p.y)) {
			this.goal = true;
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
