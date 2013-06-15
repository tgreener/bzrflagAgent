package agent;

import java.awt.geom.Point2D.Double;
import java.util.List;

import mytools.J;

import org.jblas.DoubleMatrix;

import state.kalman.KalmanFilter;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.Obstacle;
import agent.net.OtherTank;
import agent.net.Tank;

public class FinalAssaultAgent extends FinalAgent {

	public FinalAssaultAgent(AgentClientSocket s, List<Obstacle> os,
			List<KalmanFilter> filters, Constants c) {
		super(s, os, filters, c);
	}
	
	public void updateSelf(Tank tank){
		this.tank = tank;		
		int index = tank.getIndex();
		OtherTank target = otherTanks.get(tank.getIndex());
		double distance = 2000;
		if(!target.getStatus().equals("alive")){
			int i = 0;
			for(OtherTank t : otherTanks){
				if(t.getStatus().equals("alive") && getDistanceTo(t) < distance){
					distance = getDistanceTo(t);
					index = i;
				}
				i++;
			}
			//TODO become flag agent if all enemies are dead
		}
		KalmanFilter kf = this.filters.get(index); // improve this
		DoubleMatrix mu = kf.getMu();
		Double p = kf.predict(predictTime(mu.get(0),mu.get(3),kf));
		setTarget(p.getX(),p.getY());
		moveToTarget();
//		if(!isFriendlyFire()){
			fireShot();
//		}
	}
	
	public double getDistanceTo(OtherTank t){
		return Math.sqrt(Math.pow(t.getX() - tank.getX(), 2) + Math.pow(t.getY()-tank.getY(),2));
	}
	
}
