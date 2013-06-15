
package state;

public class SearchSpaceLocation {

	private int occValue;
	private boolean isGoal;
	private boolean hasTank;
	private boolean hasPenalty;
	private boolean visited;

	public SearchSpaceLocation(int ov, boolean g, boolean t, boolean p) {
		occValue = ov;
		isGoal = g;
		hasTank = t;
		hasPenalty = p;
		visited = false;
	}

	public SearchSpaceLocation(int ov) {
		occValue = ov;
		isGoal = false;
		hasTank = false;
		hasPenalty = false;
		visited = false;
	}

	public SearchSpaceLocation(SearchSpaceLocation other) {
		occValue = other.getOccValue();
		isGoal = other.isGoal();
		hasTank = other.hasTank();
		hasPenalty = other.hasPenalty();
		visited = other.visited();
	}

	public SearchSpaceLocation() {
		int occValue = 1;
		isGoal = false;
		hasTank = false;
		hasPenalty = false;
		visited = false;
	}

	public void setOccValue(int ov) {
		occValue = ov;
	}

	public void makeGoal() {
		isGoal = true;
	}

	public void putTank() {
		hasTank = true;
	}

	public void penalize() {
		hasPenalty = true;
	}

	public void visit() {
		visited = true;
	}

	public void unvisit() {
		visited = false;
	}

	public int getOccValue() {
		return occValue;
	}

	public boolean isGoal() {
		return isGoal;
	}

	public boolean hasTank() {
		return hasTank;
	}

	public boolean visited() {
		return visited;
	}

	public boolean hasPenalty() {
		return hasPenalty;
	}
}
