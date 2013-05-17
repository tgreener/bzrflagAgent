package agent;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import agent.net.*;
import search.*;
import state.*;

public class SearchAgent {
	
	public LinkedList<SearchSpaceLocation> getAStarPath(){

		AStar aStar = new AStar(searchSpace);
		return aStar.getPath((int)tankStart.x,(int)tankStart.y);
	}
	
	Occgrid grid;
	SearchSpace searchSpace;
	AgentClientSocket socket;
	Point tankStart;
	List<Tank> tanks;
	Tank myTank;
	List<Flag> flags;
	Constants constants;
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		sock.sendOccGridQuery(0);
		SearchAgent search = new SearchAgent(sock);
		search.getUCSPath();
		search.getAStarPath();
		Path p = search.dfs();
		System.out.println(p);
		p.printToFile("dfs.dat");
	}
	
	public SearchAgent(AgentClientSocket socket) {
		this.socket = socket;
		ResponseParser rp = new ResponseParser();
		grid = rp.parseOccgrid(socket.getResponse());
		socket.sendConstantsQuery();
		constants = rp.parseConstants(socket.getResponse());
		searchSpace = new SearchSpace(grid,false,(int)constants.getWorldsize());
		socket.sendOtherTanksQuery();
		List<OtherTank> tanks = rp.parseOtherTanks(socket.getResponse());
		for(OtherTank tank : tanks){
			searchSpace.addTank(tank.getX(), tank.getY());
		}

		parse();
		System.out.println("MYX: " + myTank.getX() + " Y: " + myTank.getY());
		
		tankStart = new Point((int)myTank.getX()+(int)constants.getWorldsize() / 2 -1,
				(int)myTank.getY()+(int)constants.getWorldsize() / 2 - 1);
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
		return ucs.getPath((int)tankStart.x,(int)tankStart.y);
	}

	private Path dfs() {
		DepthFirstSearch dfs = new DepthFirstSearch(searchSpace);
		
		return dfs.search(tankStart);
	}
	
}

