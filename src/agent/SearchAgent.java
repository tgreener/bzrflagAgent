package agent;

import java.util.LinkedList;
import java.util.List;

import agent.net.*;
import search.UniformCostSearch;
import state.*;

public class SearchAgent {

	Occgrid grid;
	SearchSpace searchSpace;
	AgentClientSocket socket;
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		sock.sendOccGridQuery(0);
		SearchAgent search = new SearchAgent(sock);
		search.getUCSPath();
		
	}
	
	public SearchAgent(AgentClientSocket socket) {
		this.socket = socket;
		ResponseParser rp = new ResponseParser();
		grid = rp.parseOccgrid(socket.getResponse());
		searchSpace = new SearchSpace(grid,false);
		socket.sendOtherTanksQuery();
		List<OtherTank> tanks = rp.parseOtherTanks(socket.getResponse());
		for(OtherTank tank : tanks){
			searchSpace.addTank(tank.getX(), tank.getY());
		}
		
	}
	
	public LinkedList<SearchSpaceLocation> getUCSPath(){
		ResponseParser rp = new ResponseParser();
		socket.sendMyTanksQuery();
		List<Tank> tanks = rp.parseMyTanks(socket.getResponse());
		Tank myTank = tanks.get(0);
		UniformCostSearch ucs = new UniformCostSearch(searchSpace);
//		List<Flag> flags = rp.parse
		return ucs.getPath((int)myTank.getX()+199,(int)myTank.getY()+199);
	}
	
}

