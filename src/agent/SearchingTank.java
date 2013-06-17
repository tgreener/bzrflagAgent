
package agent;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.Point;

import agent.net.Obstacle;
import agent.net.AgentClientSocket;
import agent.net.Tank;
import agent.net.Constants;

import search.BreadthFirstSearch;
import search.Path;
import search.Step;

import state.SearchSpace;
import state.kalman.KalmanFilter;

public class SearchingTank extends FinalAgent{

	protected Point searchTarget;
	protected SearchSpace space;
	protected SearchSpace searchThis;
	protected boolean followPath;
	protected Path path;
	protected Iterator<Step> pathItr;

	public SearchingTank(AgentClientSocket s, List<Obstacle> os, List<KalmanFilter> filters, Constants c) {
		super(s, os, filters, c);

		space = new SearchSpace(true, (int)constants.getWorldsize());
		for(Obstacle o : os) {
			space.addObstacle(o);
		}
		searchTarget = null;
		followPath = false;
		path = null;
	}
	
//	@Override
//	public void updateSelf(Tank tank) {
//		super.updateSelf(tank);
//
//		if(followPath) {
//			if(pathItr.hasNext()) {
//				Step s = pathItr.next();
//				while(pathItr.hasNext() && tooClose(s)) {
//					s = pathItr.next();
//				}
//
//				setTarget(s.getEndPoint().getX(), s.getEndPoint().getY());
//				System.out.println("Heading to: " + s.getEndPoint());
//			}
//		}
//	}

	protected boolean tooClose(Step s) {
		double tooCloseRadius = 5;

		double tx = tank.getX();
		double ty = tank.getY();

		double sx = s.getEndPoint().getX();
		double sy = s.getEndPoint().getY();

		return Math.pow(tx - sx, 2) + Math.pow(ty - sy, 2) < Math.pow(tooCloseRadius, 2);
	}

	public Path search() {
		BreadthFirstSearch search = new BreadthFirstSearch(searchThis);
		path = search.search(new Point(Math.round((float)tank.getX()), Math.round((float)tank.getY())));
		pathItr = path.iterator();
	
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

}

