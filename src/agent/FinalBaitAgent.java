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
		setSpeed(1f);
	}
	
	public void updateSelf(Tank tank){
		this.tank = tank;
		moveToTarget();
	}

}
