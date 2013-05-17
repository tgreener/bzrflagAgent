package agent;

import java.util.LinkedList;
import java.util.List;
import java.awt.Point;

import agent.net.*;
import search.*;
import state.*;

public class SearchAgent {

	Occgrid grid;
	SearchSpace searchSpace;
	AgentClientSocket socket;
	Point tankStart;
	List<Tank> tanks;
	Tank myTank;
	List<Flag> flags;
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		sock.sendOccGridQuery(0);
		SearchAgent search = new SearchAgent(sock);
		//search.getUCSPath();
		
		Path p = search.dfs();
		p.printToFile("dfs.dat");
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

		parse();
		
		tankStart = new Point((int)myTank.getX()+199,(int)myTank.getY()+199);
	}

	private void parse() {
		ResponseParser rp = new ResponseParser();
		socket.sendMyTanksQuery();
		tanks = rp.parseMyTanks(socket.getResponse());
		myTank = tanks.get(0);
		socket.sendFlagsQuery();
		flags = rp.parseFlags(socket.getResponse());
		for(Flag flag : flags){
			if(flag.getFlagColor().equals("green")){
				searchSpace.setGoal(flag.getX(), flag.getY());
			}
		}
	}
	
	public LinkedList<SearchSpaceLocation> getUCSPath(){
		UniformCostSearch ucs = new UniformCostSearch(searchSpace);
		return ucs.getPath((int)myTank.getX()+200,(int)myTank.getY()+200);
	}

	private Path dfs() {
		DepthFirstSearch dfs = new DepthFirstSearch(searchSpace);
		
		return dfs.search(tankStart);
	}
	
}

