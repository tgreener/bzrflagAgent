
package state;

import state.Occgrid;
import agent.net.Tank;
import agent.net.Obstacle;
import java.awt.Point;
import java.util.Arrays;

public class SearchSpace {

	private int TRANSFORM;

	private SearchSpaceLocation[][] grid;
	private boolean penalize;
	private int worldSize;
	
	public SearchSpace(Occgrid g, boolean p, int worldSize) {
		init(p, worldSize);

		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new SearchSpaceLocation(g.getValueAt(i, j));
			}			
		}
	}

	public SearchSpace(boolean p, int worldSize) {
		init(p, worldSize);

		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new SearchSpaceLocation(0);
			}			
		}
	}

	public SearchSpace(boolean p, SearchSpace other) {
		init(p, other.getWorldSize());

		for(int i = -TRANSFORM; i < TRANSFORM; i++) {
			for(int j = -TRANSFORM; j < TRANSFORM; j++) {
				grid[transform(i)][transform(j)] = new SearchSpaceLocation(other.getLocation(i, j));
			}
		}
	}

	private void init(boolean p, int worldSize) {
		this.worldSize = worldSize;
		TRANSFORM = worldSize / 2;
		grid = new SearchSpaceLocation[worldSize][worldSize];
		penalize = p;
	}


	public void addObstacle(Obstacle obstacle) {
		double[][] corners = obstacle.getCorners();
		addObstacle(corners);
	}

	public void addObstacle(double[][] corners) {
		double[] least = corners[getLeastCornerIndex(corners)];
		double[] greatest = corners[getGreatestCornerIndex(corners)];

		for(int i = (int)Math.round(least[0]); i <= Math.round(greatest[0]); i++) {
			for(int j = (int)Math.round(least[1]); j <= Math.round(greatest[1]); j++) {
				grid[transform(i)][transform(j)].setOccValue(1);
			}
		}
	}

	private static int getLeastCornerIndex(double[][] obCorners) {
		double smallest = Double.POSITIVE_INFINITY;
		int index = -1;

		for(int i = 0; i < obCorners.length; i++) {
			double size = obCorners[i][0] + obCorners[i][1];

			if(size < smallest) {
				smallest = size;
				index = i;
			}
		}
		
		return index;
	}

	private static int getGreatestCornerIndex(double[][] obCorners) {
		double biggest = Double.NEGATIVE_INFINITY;
		int index = -1;

		for(int i = 0; i < obCorners.length; i++) {
			double size = obCorners[i][0] + obCorners[i][1];

			if(size > biggest) {
				biggest = size;
				index = i;
			}
		}
		
		return index;
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
		penalize = true;
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

	public void reset() {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j].unvisit();
			}
		}
	}

	public int getWorldSize() {
		return worldSize;
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
		return x > -TRANSFORM && x < TRANSFORM &&
		       y > -TRANSFORM && y < TRANSFORM;
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

	public static void testGetCornersIndices() {
		double[][] obPoly = new double[][] {{-3, -3},{2, -3},{2, 3},{-3, 3}};

		System.out.print("Obstacle corner test");
		int least = getLeastCornerIndex(obPoly);
		int greatest = getGreatestCornerIndex(obPoly);
		printTestResult(least == 0 && greatest == 2);

		System.out.print("Obstacle test");
		SearchSpace space = new SearchSpace(false, 10);
		space.addObstacle(obPoly);
		boolean testPassed = true;

		for(int i = -3; i <= 2; i++) {
			for(int j = -3; j <= 3; j++) {
				int occV = space.getOccValue(i, j);
				if(occV != 1) {
					testPassed = false;
				}
			}
		}		

		printTestResult(testPassed); 

		System.out.print("Copy constructor test");
		testPassed = true;

		SearchSpace space2 = new SearchSpace(false, space);

		testPassed = space2.getWorldSize() == space.getWorldSize() &&
			     space2.getOccValue(0, 0) == space.getOccValue(0, 0);

		printTestResult(testPassed);
		
	}

	private static void printTestResult(boolean result) {
		if(result) {
			System.out.println("\tPASSED");
		}
		else {
			System.out.println("\tFAILED");
		}		
	}

	public String toString() {
		String result = new String();
		
		for(int j = worldSize - 1; j >= 0; j--) {
			for(int i = 0; i < worldSize; i++) {
				result += grid[i][j].getOccValue() + " ";
			}
			result += "\n";
		}

		return result;
	}
}
