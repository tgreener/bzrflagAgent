package agent;

import java.util.ArrayList;
import java.util.List;

import state.kalman.KalmanFilter;

import mytools.J;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.Obstacle;
import agent.net.OtherTank;
import agent.net.ResponseParser;
import agent.net.Tank;

public class FinalAgentController {

	AgentClientSocket socket;
	ResponseParser rp;
	List<FinalAgent> agents;
	List<KalmanFilter> filters;
	List<Obstacle> obstacles;
	List<Tank> myTanks;
	double worldSize;
	int numTanks;
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		sock.sendConstantsQuery();
		ResponseParser rp = new ResponseParser();
		FinalAgentController controller = new FinalAgentController(sock,rp);
		controller.init();
		controller.run();
	}
	
	public FinalAgentController(AgentClientSocket socket, ResponseParser rp){
		this.socket = socket;
		this.rp = rp;
	}
	
	public void init(){
		Constants consts = rp.parseConstants(socket.getResponse());
		worldSize = consts.getWorldsize();
		socket.sendMyTanksQuery();
		List<Tank> myTanks = rp.parseMyTanks(socket.getResponse());
		socket.sendOtherTanksQuery();
		List<OtherTank> otherTanks = rp.parseOtherTanks(socket.getResponse());
		socket.sendObstaclesQuery();
		List<Obstacle> obstacles = rp.parseObstacles(socket.getResponse());
		filters = new ArrayList<KalmanFilter>();
		for(OtherTank tank : otherTanks){
			KalmanFilter kf = new KalmanFilter(tank.getX(),tank.getY());
			filters.add(kf);
		}
		numTanks = myTanks.size();
		agents = new ArrayList<FinalAgent>();
		for(Tank tank : myTanks){
			FinalAgent a = new FinalAssaultAgent(socket, obstacles, filters,consts);
			a.updateSelf(tank);
			a.setSpeed(1f);
			agents.add(a);
		}
	}
	
	public void run(){
		int loopCount = 0;
		while(true){
			updateMyTanks();
			updateOtherTanks();
			loopCount++;
		}
	}
	
	public void updateMyTanks(){
		socket.sendMyTanksQuery();
		myTanks = rp.parseMyTanks(socket.getResponse());
		for(Tank tank : myTanks){
			FinalAgent a = agents.get(tank.getIndex());
			a.updateTeam(myTanks);
			a.updateSelf(tank);
		}
	}
	
	public void updateOtherTanks(){
		socket.sendOtherTanksQuery();
		List<OtherTank> otherTanks = rp.parseOtherTanks(socket.getResponse());
		int i = 0;
		for(OtherTank tank : otherTanks){
			KalmanFilter kf = filters.get(i++);
			kf.update(tank.getX(), tank.getY());
		}
	}
}
