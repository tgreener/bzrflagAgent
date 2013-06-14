package agent;

import java.util.ArrayList;
import java.util.List;

import mytools.J;
import state.BayesGrid;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.ResponseParser;
import agent.net.Tank;

public class AgentController {
	
	List<GridAgent> agents;
	ResponseParser rp;
	AgentClientSocket socket;

	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		sock.sendConstantsQuery();
		ResponseParser rp = new ResponseParser();
		Constants consts = rp.parseConstants(sock.getResponse());
		double worldSize = consts.getWorldsize();
		sock.sendMyTanksQuery();
		List<Tank> myTanks = rp.parseMyTanks(sock.getResponse());
		int numTanks = myTanks.size();
		J.p("NUMT: " + numTanks);
		AgentController ag = new AgentController(numTanks,worldSize,sock);

	}
	
	public AgentController(int numTanks, double worldSize, AgentClientSocket socket){
		this.socket = socket;
		BayesGrid bg = new BayesGrid((int)worldSize, 0.05);
		rp = new ResponseParser();
		agents = new ArrayList<GridAgent>();
		for(int i = 0; i < numTanks; i++){
			System.out.println("CREATED: " + i);
			GridAgent a = new GridAgent(i,"red",worldSize,socket,bg,numTanks);
			agents.add(a);
		}
		socket.sendMyTanksQuery();
		List<Tank> myTanks = rp.parseMyTanks(socket.getResponse());
		int i = 0;
		for(GridAgent agent : agents){
			socket.sendDriveCommand(i, 1f);
			socket.getResponse();
			agent.updatePath(myTanks.get(i++));
		}
		int updateCount = 0;
		while(true){
			J.p("updateCount: " + updateCount);
//			if(updateCount++ % 1 == 0){
//				i = 0;
//				GridAgent agent = agents.get(updateCount / 1 % myTanks.size());
//				new Thread(new UpdatePath(agent,myTanks.get(i++))).start();
//			}
			i = 0;
			for(GridAgent agent : agents){
				socket.sendDriveCommand(i, 1f);
				socket.getResponse();
				agent.updatePath(myTanks.get(i++));
			}
			/**/
			if(updateCount % 10 == 0){
				bg.writeGNUPlotFile("bayes");
			}
			updateAgents();
		}
	}
	
	
	public void updateAgents(){
		socket.sendMyTanksQuery();
		List<Tank> myTanks = rp.parseMyTanks(socket.getResponse());
		for(int i = 0; i < agents.size(); i++){
			GridAgent agent = agents.get(i);
			agent.setTank(myTanks.get(i));
			agent.moveToVector();
			agent.observe();
		}
	}
	
	public static class UpdatePath implements Runnable{		
		GridAgent agent;			
		Tank tank;
		public UpdatePath(GridAgent a, Tank tank){
			agent = a;
			this.tank = tank;
		}		
		public void run(){
			agent.updatePath(tank);
		}
	}
	
}
