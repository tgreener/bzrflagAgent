
package agent;


import java.util.List;
import agent.fields.*;
import agent.fields.tests.FieldListPrinter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import mytools.J;
import agent.net.*;

public class Agent {
	
	int tankNumber;
	ResponseParser rp;
	AgentClientSocket socket;
	double previousAngle;
	List<Field> fields;
	String opponentColor;
	String targetBase;
	String myColor;

	

	public static class UpdateVector extends TimerTask{
		
		Agent agent;
				
		public UpdateVector(Agent a){
			agent = a;
			
		}
		
		public void run(){
			agent.moveToVector(agent.calculateVector());
		}
	}
	
	public Agent(int tankNumber, AgentClientSocket socket, String opponentColor, String myColor){
		this.tankNumber = tankNumber;
		rp = new ResponseParser();
		previousAngle = 0;
		this.socket = socket;
		socket.sendDriveCommand(tankNumber, 1);
		socket.getResponse();
		this.fields = new ArrayList<Field>();
		targetBase = opponentColor;
		this.opponentColor = opponentColor;
		this.myColor = myColor;
		createFields(targetBase);
	}
	
	public Agent(){
		
	}
	
	public void moveToVector(Vector2d v){
		J.p("that'n");
		Tank tank = getTank();
		Vector2d tankVector = new Vector2d(tank.getVx(),tank.getVy()).normalize();
		double angle = tankVector.angle(v);
		float angvel = (float) ((angle + angle - previousAngle)/(Math.PI));

		if(tankVector.crossProduct(v) < 0){
			angvel = angvel * -1;
		}
		previousAngle = angle;
		if(angvel > .0001 || angvel < -.0001){
			socket.sendRotateCommand(tankNumber, angvel);
			socket.getResponse();
		}
		if(tank.getTimeToReload() <= 0){
			socket.sendShootCommand(tankNumber);
			socket.getResponse();
		}
		if(tank.getFlag().equals(this.opponentColor))
			this.targetBase = this.myColor;
		else
			this.targetBase = this.opponentColor;
		if(this.getDistanceToGoal() < 50){
			socket.sendDriveCommand(this.tankNumber, .1f);
			socket.getResponse();
		}
		else{
			socket.sendDriveCommand(this.tankNumber, 1f);
			socket.getResponse();
		}
		
		createFields(this.targetBase);
	}
	
	public Vector2d calculateVector(){
		Tank tank = getTank();
		Random r = new Random((long)tank.getTimeToReload()*10000);
		Vector2d vector = new Vector2d(r.nextDouble()*2-1,r.nextDouble()*2-1);
		for(Field f : fields){
			vector = vector.add(f.fieldAtPoint(tank.getX(), tank.getY()).normalize());
		}
		return vector.normalize();
	}
	
	public void createFields(String targetBase){
		fields = new ArrayList<Field>();
		socket.sendObstaclesQuery();
		List<Obstacle> obstacles = rp.parseObstacles(socket.getResponse());
		for(Obstacle obstacle : obstacles){
			double[] center = obstacle.getCenter();
			Field f = new RepulsiveRadialField(10,.1,100d,center[0],center[1]);
			this.fields.add(f);
			Field f2 = new TangentialRadialField(.1,.1,100d,center[0],center[1]);
			this.fields.add(f2);
		}
		Base base = getTargetBase();
		double[] center = base.getCenter();
		Field f = new AttractiveRadialField(.3,base.getRadius(),800d,center[0],center[1]);
		this.fields.add(f);
		FieldListPrinter flp = new FieldListPrinter(this.fields,800,10);
		flp.printFieldsFile("fieldsOut");
		System.exit(0);
	}
	public Tank getTank(){
		socket.sendMyTanksQuery();
		List<Tank> tanks = rp.parseMyTanks(socket.getResponse());
		Tank tank = tanks.get(this.tankNumber);
		return tank;
	}
	
	public Base getTargetBase(){
		Base b = null;
		socket.sendBasesQuery();
		List<Base> bases = rp.parseBases(socket.getResponse());
		for(Base base : bases){
			if(base.getColor().equals(targetBase)){
				b = base;
			}
		}
		return b;
	}
	public double getDistanceToGoal(){
		Base b = getTargetBase();
		double[] c = b.getCenter();
		Tank t = getTank();
		return Math.sqrt(Math.pow(c[0] - t.getX(), 2) + Math.pow(c[1] - t.getY(), 2));
	}
}


