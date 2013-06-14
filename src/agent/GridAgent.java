package agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
import agent.fields.RepulsiveRadialField;
import agent.fields.TangentialRadialField;
import agent.fields.Vector2d;
import agent.fields.tests.FieldListPrinter;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.ResponseParser;
import agent.net.Tank;

public class GridAgent extends Agent{
	BayesGrid bg;
	double worldSize;
	double TRANSFORM;
//	int updateCount;
	int numTanks;
	LinkedList<TraverseNodeA> path;
	Tank tank;

//	public static void main(String[] args) {
//		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
//		sock.getResponse();
//		sock.sendIntroduction();
//		sock.sendConstantsQuery();
//		ResponseParser rp = new ResponseParser();
//		Constants consts = rp.parseConstants(sock.getResponse());
//		double worldSize = consts.getWorldsize();
//		BayesGrid bg = new BayesGrid((int)worldSize, 0.05);
//		sock.sendMyTanksQuery();
//		List<Tank> myTanks = rp.parseMyTanks(sock.getResponse());
//		int numTanks = myTanks.size();
//		J.p("NUMT: " + numTanks);
//		for(int i = 0; i < numTanks; i++){
//			System.out.println("CREATED: " + i);
//			GridAgent a = new GridAgent(i,"red",worldSize,sock,bg,numTanks);
//			a.moveToVector();
//			Timer timer = new Timer();
//			timer.scheduleAtFixedRate(new GridUpdateVector(a),100,100);
//
//		}
//
//	}
	
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
			AgentClientSocket socket, BayesGrid bg, int numTanks){
		this.tankNumber = tankNumber;
		this.myColor = color;
		this.socket = socket;
		this.bg = bg;
		this.worldSize = worldSize;
		this.TRANSFORM = worldSize / 2;
//		this.updateCount = 0;
		this.fields = new ArrayList<Field>();
		this.numTanks = numTanks;
		rp = new ResponseParser();
		previousAngle = 0;
	}
	
	public void moveToVector(){
//		Tank tank = getTank();
//		if(path == null || updateCount % 10 == 0){
//			updatePath(tank);
//		}
		
		Vector2d targetVector = calculateVector();
		Vector2d tankVector = new Vector2d(tank.getVx(),tank.getVy()).normalize();
		double angle = tankVector.angle(targetVector);
		float angvel = (float) ((angle + angle - previousAngle)/(Math.PI));
		previousAngle = angle;
		if(tankVector.crossProduct(targetVector) < 0){
			angvel = angvel * -1;
		}
		if(angvel > .0001 || angvel < -.0001){
			socket.sendRotateCommand(tankNumber, angvel);
			socket.getResponse();
		}
//		observe();
//		if(updateCount == 0){
//			socket.sendDriveCommand(tankNumber, .2f);
//			socket.getResponse();
//		}
//		updateCount++;
	}
	
	public void observe(){
		socket.sendOccGridQuery(tankNumber);
		Occgrid oc = rp.parseOccgrid(socket.getResponse());
		int ocx = (int) (oc.at().x + TRANSFORM);
		int ocy = (int) (oc.at().y + TRANSFORM);
		for(int x = 0; x < oc.getDimension()[0]; x++){
			for(int y = 0; y < oc.getDimension()[1]; y++){
				bg.updateWithObservation(x + ocx, y + ocy, oc.getValueAt(x, y));
			}
		}
	}
	
	public void updatePath(Tank tank){
//		SearchSpace searchSpace = new SearchSpace(bg,(int)worldSize);
//		searchSpace.setGoal(tank.getY(), tank.getX());
//		AStar aStar = new AStar(searchSpace, worldSize);
		int goalX = 0;
		int goalY = 0;
		int x = (int) ((worldSize / numTanks) * (tankNumber));
		while(x < worldSize - 1){
			for(int y = 0; y < worldSize - 2; y++){
				if(bg.isOccupied(x, y) == Occupation.UNKNOWN){
					goalX = x;
					goalY = y;
					break;
				}
			}
			if(goalX != 0){
				break;
			}
			x++;
		}
		J.p("GOAL T:" + tankNumber + " X: " + goalX + " goalY: " + goalY);
//		path = aStar.getPath(goalX, goalY); //Get unknown node for this
		fields = new ArrayList<Field>();
		Field f = new AttractiveRadialField(.3,1,1,goalX-TRANSFORM,goalY-TRANSFORM);
		fields.add(f);
		
		
		for(x = 0; x < worldSize; x++){
			for(int y = 0; y < worldSize; y++){
				if(bg.isOccupied(x, y) == Occupation.OCCUPIED){
					Field f2 = new RepulsiveRadialField(8,1,3,x-TRANSFORM,y-TRANSFORM);
					fields.add(f2);
				}
			}
		}
//		FieldListPrinter flp = new FieldListPrinter(fields,(int)worldSize, 5);
//		flp.printFieldsFile("fieldsFile");
		

//		if(path == null){ //If no path, turn all visited nodes into occupied
//			HashSet<TraverseNodeA> visited = aStar.getVisitedNodes();
//			for(TraverseNodeA node : visited){
//				bg.updateWithObservation(node.getX(), node.getY(), 1);
//			}
//		}
//		setFields();
	}
	
	public void setFields(){
		fields = new ArrayList<Field>();
		if(path == null){
			return;
		}
		
		for(TraverseNodeA node : path){
			Field f = new AttractiveRadialField(.3,1,1,node.getX()-TRANSFORM,node.getY()-TRANSFORM);
			fields.add(f);
		}
		
////		for(int i = 0; i < 5; i++){
//			if(path.size() >= 15){
//				TraverseNodeA node = path.get(path.size() - 15 );
//				Field f = new AttractiveRadialField(.3,1,1,node.getX()-TRANSFORM,node.getY()-TRANSFORM);
//				fields.add(f);
//			}
////		}
//		for(int x = 0; x < worldSize; x++){
//			for(int y = 0; y < worldSize; y++){
//				if(bg.isOccupied(x, y) == Occupation.OCCUPIED){
////					Field f = new TangentialRadialField(.1,1,3,x-TRANSFORM,y-TRANSFORM);
////					fields.add(f);
//					Field f2 = new RepulsiveRadialField(.1,1,3,x-TRANSFORM,y-TRANSFORM);
//					fields.add(f2);
//				}
//			}
//		}
		FieldListPrinter flp = new FieldListPrinter(fields,(int)worldSize, 5);
		flp.printFieldsFile("fieldsFile");

	}
	
	public void setTank(Tank tank){
		this.tank = tank;
	}
	public Tank getTank(){
		return this.tank;
	}
	
	public Vector2d calculateVector(){
//		Random r = new Random((long)tank.getTimeToReload()*10000);
		Vector2d vector = new Vector2d(0,0);
		for(Field f : fields){
			vector = vector.add(f.fieldAtPoint(tank.getX(), tank.getY()).normalize());
		}
		return vector.normalize();
	}
	
}
