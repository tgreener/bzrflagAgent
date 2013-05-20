
package state;

import state.Occgrid;
import agent.net.Tank;
import java.awt.Point;

public class SearchSpace {

	private int TRANSFORM;

	private SearchSpaceLocation[][] grid;
	private boolean penalize;
	
	public SearchSpace(Occgrid g, boolean p, int worldSize) {
		TRANSFORM = worldSize / 2;
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
	
	public void penalizeObstacles(){
		for(int i = 0; i < grid[0].length - 1; i++){
			for(int j = 0; j < grid[0].length - 1; j++){
				if(grid[i][j].getOccValue() == 1){
					if(j < grid[0].length)
						grid[i][j+1].penalize();
					if(i < grid[0].length && j < grid[0].length)
						grid[i+1][j+1].penalize();
					if(i < grid[0].length)
						grid[i+1][j].penalize();
					if(i < grid[0].length && j > 0)
						grid[i+1][j-1].penalize();
					if(j > 0)
						grid[i][j-1].penalize();
					if(i > 0 && j > 0)
						grid[i-1][j-1].penalize();
					if(i > 0)
						grid[i-1][j].penalize();
					if(i > 0 && j < grid[0].length)
						grid[i-1][j+1].penalize();
				}
			}
		}
	}

	public void visit(int x, int y) {
		grid[transform(x)][transform(y)].visit();
	}

	public void unvisit(int x, int y) {
		grid[transform(x)][transform(y)].unvisit();
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

	public boolean inBounds(int x, int y) {
		return x > -200 && x < 200 &&
		       y > -200 && y < 200;
	}

	public boolean inBounds(Point p) {
		return inBounds(p.x, p.y);
	}

	private int transform(double c) {
		return transform((int)Math.round(c));
	}

	private int transform(int c) {
		return c + TRANSFORM;
	}
	
	public SearchSpaceLocation untransformedGetLocation(int x, int y) {
		return grid[x][y];
	}
	
	public boolean untransformedGetLocationIsGoal(int x, int y) {
		return grid[x][y].isGoal();
	}

	/*public String toString() {
		String result;

		return result;
	}*/
}
