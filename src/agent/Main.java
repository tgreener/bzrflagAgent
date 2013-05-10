package agent;

import java.util.Timer;

import agent.Agent.UpdateVector;
import agent.net.AgentClientSocket;

public class Main {
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		
		for(int i = 0; i < 9; i++){
			Agent a = new Agent(i,sock,"red","blue");
			a.moveToVector(a.calculateVector());
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new UpdateVector(a),100,100);
		}

		
				
	}
}
