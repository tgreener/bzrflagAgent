package agent;

import java.util.ArrayList;
import java.util.List;

import mytools.J;

import agent.fields.AttractiveRadialField;
import agent.fields.Field;
import agent.fields.Vector2d;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.OtherTank;
import agent.net.ResponseParser;
import agent.net.Tank;

public class KalmanAgent {

	private AgentClientSocket socket;
	private ResponseParser rp;
	private int tankNumber;
	private List<Field> fields;
	private double previousAngle;
	private Tank tank;
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		ResponseParser rp = new ResponseParser();
		sock.sendConstantsQuery();
		Constants consts = rp.parseConstants(sock.getResponse());
		int shotSpeed = (int)consts.getShotspeed();
		int shotRange = (int)consts.getShotrange();
		KalmanAgent agent = new KalmanAgent(sock,rp,0,shotSpeed,shotRange);
	}
	
	public KalmanAgent(AgentClientSocket socket, ResponseParser rp, int tankNumber,
		int shootSpeed, int shotRadius){
		this.socket = socket;
		this.rp = rp;
		this.tankNumber = tankNumber;
		previousAngle = 0;
		while("breakfast" != "waffles"){
			updateTank();
		}
	
	}
	public void updateTank(){
		this.tank = getTank();
		if(shouldShoot()){
			fireShot();
		}
		else{
			updateAngle();
		}
	}
	public boolean shouldShoot(){
		OtherTank otherTank = getOtherTank();
		if(isInLineOfFire(otherTank.getX(), otherTank.getY())){
			return true;
		}
		return false;
	}
	public boolean isInLineOfFire(double x, double y){
		double myAngle = tank.getAngle();
		double myX = tank.getX();
		double myY = tank.getY();
		double slope = Math.tan(myAngle);
		return (myX - x) / (myY - y) == slope;
	}
	
	public void fireShot(){
		socket.sendShootCommand(tankNumber);
		socket.getResponse();
	}
	public void updateAngle(){
		J.p("update angle");
		Tank tank = getTank();
		OtherTank ot = getOtherTank();
		setTarget(ot);
		moveToVector(tank);
	}
	
	public void moveToVector(Tank tank){
		Vector2d targetVector = calculateVector(tank);
		Vector2d tankVector = new Vector2d(tank.getVx(),tank.getVy()).normalize();
		double angle = tankVector.angle(targetVector);
		float angvel = (float) ((angle + angle - previousAngle)/(Math.PI));
		previousAngle = angle;
		J.p("ANGLE: " + angle);
		if(tankVector.crossProduct(targetVector) < 0){
			angvel = angvel * -1;
		}
		if(angvel > .0001 || angvel < -.0001){
			socket.sendRotateCommand(tankNumber, angvel);
			socket.getResponse();
		}

	}
	
	public void setTarget(OtherTank t){
		fields = new ArrayList<Field>();
		Field f = new AttractiveRadialField(1,1,t.getX(),t.getY());
		fields.add(f);
	}
	
	public OtherTank getOtherTank(){
		socket.sendOtherTanksQuery();
		List<OtherTank> tank = rp.parseOtherTanks(socket.getResponse());
		return tank.get(0);
	}

	public Vector2d calculateVector(Tank tank){
		Vector2d vector = new Vector2d(0,0);
		for(Field f : fields){
			vector = vector.add(f.fieldAtPoint(tank.getX(), tank.getY()).normalize());
		}
		return vector.normalize();
	}
	
	public Tank getTank(){
		socket.sendMyTanksQuery();
		List<Tank> tanks = rp.parseMyTanks(socket.getResponse());
		Tank tank = tanks.get(this.tankNumber);
		return tank;
	}
}







