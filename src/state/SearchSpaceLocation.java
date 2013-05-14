
package state;

public class SearchSpaceLocation {

	private int occValue;
	private boolean isGoal;
	private boolean hasTank;
	private boolean hasPenalty;

	public SearchSpaceLocation(int ov, boolean g, boolean t, boolean p) {
		occValue = ov;
		isGoal = g;
		hasTank = t;
		hasPenalty = p;
	}

	public SearchSpaceLocation(int ov) {
		occValue = ov;
		isGoal = false;
		hasTank = false;
		hasPenalty = false;
	}

	public SearchSpaceLocation() {
		int occValue = 1;
		isGoal = false;
		hasTank = false;
		hasPenalty = false;
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

	public int getOccValue() {
		return occValue;
	}

	public boolean isGoal() {
		return isGoal;
	}

	public boolean hasTank() {
		return hasTank;
	}

	public boolean hasPenalty() {
		return hasPenalty;
	}
}