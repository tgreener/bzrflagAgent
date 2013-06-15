package agent;

import java.util.ArrayList;
import java.util.List;

import mytools.J;

import state.kalman.KalmanFilter;
import agent.fields.AttractiveRadialField;
import agent.fields.Field;
import agent.fields.RepulsiveRadialField;
import agent.net.AgentClientSocket;
import agent.net.Base;
import agent.net.Constants;
import agent.net.Flag;
import agent.net.Obstacle;
import agent.net.OtherTank;
import agent.net.ResponseParser;
import agent.net.Tank;

public class FinalFlagAgent extends FinalAgent{
	
	ResponseParser rp;
	Flag flag;
	Base base;
	
	public FinalFlagAgent(AgentClientSocket s, List<Obstacle> os,
			List<KalmanFilter> filters, Constants c) {
		super(s, os, filters, c);
		rp = new ResponseParser();
		socket.sendBasesQuery();
		List<Base> bases = rp.parseBases(socket.getResponse());
		for(Base b : bases){
			if(b.getColor().equals(constants.getTeam())){
				base = b;
			}
		}
	}
	
	public void updateSelf(Tank tank){
		this.tank = tank;
		getFlag();
		if(!hasDaFlag()){
			setTarget(flag.getX(), flag.getY());
		}
		else{
			double[] center = base.getCenter();
			setTarget(center[0],center[1]);
		}
		Field rf = new RepulsiveRadialField(1,10,300d,0,0);
		fields.add(rf);
		moveToTarget();
	}

	
	public void setTarget(double x, double y){
		target.x = x;
		target.y = y;
		fields = new ArrayList<Field>();
		Field f = new AttractiveRadialField(2,1,1,x,y);
		fields.add(f);
	}
	
	public boolean hasDaFlag(){
		return flag.getPossesingColor().equals(constants.getTeam());
	}
	
	public void getFlag(){
		socket.sendFlagsQuery();
		List<Flag> flags = rp.parseFlags(socket.getResponse());
		OtherTank ot = otherTanks.get(0);
		String color = ot.getColor();
		for(Flag f : flags){
			if(f.getFlagColor().equals(color)){
				flag = f;
			}
		}
	}
	
}
