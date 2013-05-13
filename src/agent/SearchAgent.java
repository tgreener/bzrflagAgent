package agent;

import agent.net.*;
import state.Occgrid;

public class SearchAgent {

	Occgrid grid;
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		sock.sendOccGridQuery(0);
		SearchAgent search = new SearchAgent(sock);
		
	}
	
	public SearchAgent(AgentClientSocket socket) {
		ResponseParser rp = new ResponseParser();
		grid = rp.parseOccgrid(socket.getResponse());
	}
	
}

