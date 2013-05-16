
package state;

import state.Occgrid;
import agent.net.Tank;
import java.awt.Point;

public class SearchSpace {

	private static final int TRANSFORM = 200;

	private SearchSpaceLocation[][] grid;
	private boolean penalize;
	
	public SearchSpace(Occgrid g, boolean p) {
		grid = new SearchSpaceLocation[g.getDimension()[0]][g.getDimension()[1]];
		penalize = p;

		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new SearchSpaceLocation(g.getValueAt(i, j));
			}			
		}
	}

	public void addTank(Tank t) {
		addTank(t.getX(), t.getY());
	}

	public void addTank(double x, double y) {
		int gridX = transform(x);
		int gridY = transform(y);

		grid[gridX][gridY].putTank();

		if(penalize) {
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					if(inBounds(gridX + i, gridY +j)) {
						grid[gridX + i][gridY + j].penalize();
					}
				}
			}
		}
	}

	public void visit(int x, int y) {
		grid[transform(x)][transform(y)].visit();
	}

	public void setGoal(double x, double y) {
		grid[transform(x)][transform(y)].makeGoal();
	}

	public int getOccValue(int x, int y) {
		return grid[transform(x)][transform(y)].getOccValue();
	}

	public boolean isGoal(int x, int y) {
		return grid[transform(x)][transform(y)].isGoal();
	}

	public boolean hasTank(int x, int y) {
		return grid[transform(x)][transform(y)].hasTank();
	}

	public boolean hasPenalty(int x, int y) {
		return grid[transform(x)][transform(y)].hasPenalty();
	}

	public boolean visited(int x, int y) {
		return grid[transform(x)][transform(y)].visited();
	}

	public SearchSpaceLocation getLocation(int x, int y) {
		return grid[transform(x)][transform(y)];
	}

	private int transform(double c) {
		return transform((int)Math.round(c));
	}

	private int transform(int c) {
		return c + TRANSFORM;
	}

	private boolean inBounds(int x, int y) {
		return x >= -200 && x <= 200 &&
		       y >= -200 && y <= 200;
	}

	private boolean inBounds(Point p) {
		return inBounds(p.x, p.y);
	}

	/*public String toString() {
		String result;

		return result;
	}*/
}
