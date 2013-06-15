
package agent;

import agent.net.Obstacle;
import search.AStar;
import state.SearchSpace;
import java.awt.Point;

public class SearchingTank {

	private Point searchTarget;
	private SearchSpace space;
	private SearchSpace searchThis;
	private boolean followPath;
	private List<TraverseNodeA> path;

	public class SearchingTank(AgentClientSocket s, List<Obstacle> os, List<KalmanFilter> filters, Constants c, int worldSize) {
		this.super(s, os, filters, c);

		space = new SearchSpace(true, worldSize);
		for(Obstacle o : os) {
			space.addObstacle(o);
		}
		searchTarget = null;
		followPath = false;
		path = null;
	}
	
	@override
	public void updateSelf(Tank tank) {
		super.updateSelf(tank);
	}

	public void search() {
		AStar search = new AStar(searchThis, searchThis.getWorldSize());
		path = search.getPath(tank.getX(), tank.getY());
	}

	public void setSearchTarget(double x, double y) {
		searchTarget = new Point((int)Math.round(x), (int)Math.round(y));
		searchThis.setGoal(searchTarget.x, searchTarget.y);
	}

	public void setPathFollowing(boolean p) {
		followPath = p;
	}
}

