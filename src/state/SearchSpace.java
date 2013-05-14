
package state;

import state.Occgrid;
import agent.net.Tank;

public class SearchSpace {
	
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
		int gridX = (int)Math.round(x);
		int gridY = (int)Math.round(y);

		grid[gridX][gridY].putTank();

		if(penalize) {
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					grid[gridX + i][gridY + j].penalize();
				}
			}
		}
	}

	public void setGoal(double x, double y) {
		grid[(int)Math.round(x)][(int)Math.round(y)].makeGoal();
	}

	/*public String toString() {
		String result;

		return result;
	}*/
}