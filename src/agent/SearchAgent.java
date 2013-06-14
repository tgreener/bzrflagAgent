package agent;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

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
	Constants constants;
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		sock.sendOccGridQuery(0);
		SearchAgent search = new SearchAgent(sock);
		search.getUCSPath();
//		search.getAStarPath();
		Path id = search.iddfs();
		id.printToFile("iddfs.dat", 45);
		id.printToVectorFile("iddfsVecs.dat");
		Path p = search.bfs();
		p.printToVectorFile("bfsVecs.dat");
		p.printToFile("bfs.dat", 10);
		Path d = search.dfs();
		d.printToFile("dfsNodes.dat", 30);
		d.printToVectorFile("dfsVecs.dat");
	}
	
	public SearchAgent(AgentClientSocket socket) {
		this.socket = socket;
		ResponseParser rp = new ResponseParser();
		grid = rp.parseOccgrid(socket.getResponse());
		socket.sendConstantsQuery();
		constants = rp.parseConstants(socket.getResponse());
		searchSpace = new SearchSpace(grid,true,(int)constants.getWorldsize());
		socket.sendOtherTanksQuery();
		List<OtherTank> tanks = rp.parseOtherTanks(socket.getResponse());
		for(OtherTank tank : tanks){
			searchSpace.addTank(tank.getX(), tank.getY());
		}
		searchSpace.penalizeObstacles();

		parse();
		tankStart = new Point((int)myTank.getX(),(int)myTank.getY());
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
		UniformCostSearch ucs = new UniformCostSearch(searchSpace,constants.getWorldsize());
		return ucs.getPath((int)myTank.getX() +(int)constants.getWorldsize() / 2 -1
				,(int)myTank.getY() +(int)constants.getWorldsize() / 2 -1);
	}

	private Path dfs() {
		DepthFirstSearch dfs = new DepthFirstSearch(searchSpace);
		searchSpace.reset();
		return dfs.search(tankStart);
	}
//	public LinkedList<SearchSpaceLocation> getAStarPath(){
//		AStar aStar = new AStar(searchSpace, constants.getWorldsize());
//		return aStar.getPath((int)myTank.getX() +(int)constants.getWorldsize() / 2 -1
//				,(int)myTank.getY() +(int)constants.getWorldsize() / 2 -1);
//	}
	private Path iddfs() {
		IDDFS iddfs = new IDDFS(searchSpace);
		//iddfs.printNodes(45);
		searchSpace.reset();
		return iddfs.search(tankStart);
	}
	private Path bfs() {
		BreadthFirstSearch bfs = new BreadthFirstSearch(searchSpace, "bfsNodes.dat", 45);
		searchSpace.reset();
		return bfs.search(tankStart);
	}
	
}

