package agent;

import java.awt.geom.Point2D.Double;
import java.util.List;

import mytools.J;

import org.jblas.DoubleMatrix;

import state.kalman.KalmanFilter;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.Obstacle;
import agent.net.Tank;

public class FinalAssaultAgent extends FinalAgent {

	public FinalAssaultAgent(AgentClientSocket s, List<Obstacle> os,
			List<KalmanFilter> filters, Constants c) {
		super(s, os, filters, c);
	}
	
	public void updateSelf(Tank tank){
		this.tank = tank;
		KalmanFilter kf = this.filters.get(tank.getIndex()); // improve this
		DoubleMatrix mu = kf.getMu();
		Double p = kf.predict(predictTime(mu.get(0),mu.get(3),kf));
		setTarget(p.getX(),p.getY());
		J.p("Target: x: " + p.getX() + " y: " + p.getY());
		moveToTarget();
	}
	
}
