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
		socket.sendFlagsQuery();
		List<Flag> flags = rp.parseFlags(socket.getResponse());
		for(Flag flag : flags){
			if(flag.getFlagColor().equals("green")){
				searchSpace.setGoal(flag.getX(), flag.getY());
			}
		}
		UniformCostSearch ucs = new UniformCostSearch(searchSpace);
		return ucs.getPath((int)myTank.getX()+200,(int)myTank.getY()+200);
	}
	
}

