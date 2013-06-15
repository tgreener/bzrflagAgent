package agent;

import java.util.List;

import state.kalman.KalmanFilter;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.Obstacle;
import agent.net.Tank;

public class FinalBaitAgent extends FinalAgent{

	//I'm a turkey
	public FinalBaitAgent(AgentClientSocket s, List<Obstacle> os,
			List<KalmanFilter> filters, Constants c) {
		super(s, os, filters, c);
		setTarget(0,0);
	}
	
	public void updateSelf(Tank tank){
		this.tank = tank;
		if(tank.getX() < 50 && tank.getVx() > .1){
			setSpeed(0);
		}
		moveToTarget();
	}
	
	public void resetObstacles(){
		//honey badger don't care bout no obstacles
	}

}
