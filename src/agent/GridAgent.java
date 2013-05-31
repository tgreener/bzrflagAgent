package agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mytools.J;

import search.AStar;
import search.AStar.TraverseNodeA;
import state.BayesGrid;
import state.BayesGrid.Occupation;
import state.Occgrid;
import state.SearchSpace;
import state.SearchSpaceLocation;

import agent.fields.AttractiveRadialField;
import agent.fields.Field;
import agent.fields.Vector2d;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.ResponseParser;
import agent.net.Tank;

public class GridAgent extends Agent{
	BayesGrid bg;
	double worldSize;
	double TRANSFORM;
	int updateCount;
	LinkedList<TraverseNodeA> path;

	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		sock.sendConstantsQuery();
		ResponseParser rp = new ResponseParser();
		Constants consts = rp.parseConstants(sock.getResponse());
		double worldSize = consts.getWorldsize();
		BayesGrid bg = new BayesGrid((int)worldSize, 0.05);
		for(int i = 0; i < 1; i++){
			System.out.println("CREATED: " + i);
			GridAgent a = new GridAgent(i,"red",worldSize,sock,bg);
			a.moveToVector();
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new GridUpdateVector(a),100,100);

		}

	}
	
	public static class GridUpdateVector extends TimerTask{		
		GridAgent agent;				
		public GridUpdateVector(GridAgent a){
			agent = a;
			
		}		
		public void run(){
			agent.moveToVector();
		}
	}
	
	
	public GridAgent(int tankNumber, String color, double worldSize,
			AgentClientSocket socket, BayesGrid bg){
		this.tankNumber = tankNumber;
		this.myColor = color;
		this.socket = socket;
		this.bg = bg;
		this.worldSize = worldSize;
		this.TRANSFORM = worldSize / 2;
		this.updateCount = 0;
		this.fields = new ArrayList<Field>();
		rp = new ResponseParser();
	}
	
	public void moveToVector(){
		Tank tank = getTank();
System.out.println("TANK " + this.tankNumber + " UPDATE: " + this.updateCount);
		if(path == null || updateCount % 10 == 0){
			updatePath(tank);
		}
		
		Vector2d targetVector = calculateVector();
		
		Vector2d tankVector = new Vector2d(tank.getVx(),tank.getVy()).normalize();
		double angle = tankVector.angle(targetVector);
		float angvel = (float) ((angle)/(Math.PI));
		if(tankVector.crossProduct(targetVector) < 0){
			angvel = angvel * -1;
		}
		if(angvel > .0001 || angvel < -.0001){
			socket.sendRotateCommand(tankNumber, angvel);
			socket.getResponse();
		}
		observe();
		if(updateCount == 0){
			socket.sendDriveCommand(tankNumber, 1f);
			socket.getResponse();
		}
		updateCount++;
	}
	
	public void observe(){
		socket.sendOccGridQuery(tankNumber);
		Occgrid oc = rp.parseOccgrid(socket.getResponse());
		J.p("OCx: " + oc.at().x + " OCy: " + oc.at().y);
		int ocx = (int) (oc.at().x + TRANSFORM);
		int ocy = (int) (oc.at().y + TRANSFORM);
		for(int x = 0; x < oc.getDimension()[0]; x++){
			for(int y = 0; y < oc.getDimension()[1]; y++){
				bg.updateWithObservation(x + ocx, y + ocy, oc.getValueAt(x, y));
			}
		}
	}
	
	public void updatePath(Tank tank){
		SearchSpace searchSpace = new SearchSpace(bg,(int)worldSize);
		searchSpace.setGoal(tank.getX(), tank.getY());
		AStar aStar = new AStar(searchSpace, worldSize);
		int goalX = 0;
		int goalY = 0;
		if(this.updateCount == 0){
			goalX = (int)this.worldSize / (this.tankNumber + 1) - 2;
		}
		else{
			int x = this.tankNumber;
			while(x < this.tankNumber * worldSize / 1){
				for(int y = 0; y < worldSize - 2; y++){
					if(bg.isOccupied(x, y) == Occupation.UNKNOWN){
						J.p("SET NEW GOAL!!!");
						goalX = x;
						goalY = y;
						break;
					}
				}
				x++;
			}
		}
		J.p("Goal: X:" + goalX + " Y: " + goalY);
		path = aStar.getPath(goalX, goalY); //Get unknown node for this
		
		if(path == null){ //If no path, turn all visited nodes into occupied
			HashSet<TraverseNodeA> visited = aStar.getVisitedNodes();
			for(TraverseNodeA node : visited){
				bg.updateWithObservation(node.getX(), node.getY(), 1);
			}
		}
		setFields();
	}
	
	public void setFields(){
		fields = new ArrayList<Field>();
		for(TraverseNodeA node : path){
			Field f = new AttractiveRadialField(.3,5,50,node.getX(),node.getY());
			fields.add(f);
		}
	}
	
}
