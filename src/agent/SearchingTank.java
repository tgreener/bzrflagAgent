
package agent;

import java.util.List;
import java.util.LinkedList;
import java.awt.Point;

import agent.net.Obstacle;
import agent.net.AgentClientSocket;
import agent.net.Tank;
import agent.net.Constants;

import search.BreadthFirstSearch;
import search.Path;

import state.SearchSpace;
import state.kalman.KalmanFilter;

public class SearchingTank extends FinalAgent{

	private Point searchTarget;
	private SearchSpace space;
	private SearchSpace searchThis;
	private boolean followPath;
	private Path path;

	public SearchingTank(AgentClientSocket s, List<Obstacle> os, List<KalmanFilter> filters, Constants c, int worldSize) {
		super(s, os, filters, c);

		space = new SearchSpace(true, worldSize);
		for(Obstacle o : os) {
			space.addObstacle(o);
		}
		searchTarget = null;
		followPath = false;
		path = null;
	}
	
	@Override
	public void updateSelf(Tank tank) {
		super.updateSelf(tank);

		if(followPath) {
			
		}
	}

	public Path search() {
		BreadthFirstSearch search = new BreadthFirstSearch(searchThis);
		path = search.search(new Point(Math.round((float)tank.getX()), Math.round((float)tank.getY())));
	
		return path;
	}

	public void setSearchTarget(double x, double y) {
		searchTarget = new Point((int)Math.round(x), (int)Math.round(y));
		searchThis = new SearchSpace(true, space);
		searchThis.setGoal(searchTarget.x, searchTarget.y);
	}

	public void setPathFollowing(boolean p) {
		followPath = p;
	}

	public static void test() {
		System.out.println("Beginning Searching Tank Test...");

		Obstacle o = new Obstacle();
		o.setCorners(new double[][]{{-3,-3}, {2,-3}, {2,2}, {-3,2}});
		List<Obstacle> obs = new LinkedList<Obstacle>();
		obs.add(o);
		SearchingTank st = new SearchingTank(null, obs, null, null, 10);
		Tank t = new Tank();
		t.setX(-4);
		t.setY(-4);
		st.updateSelf(t);

		st.setSearchTarget(4, 4);
		System.out.println(st.search());
	}
}

