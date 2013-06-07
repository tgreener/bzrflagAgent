package agent;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import state.kalman.KalmanFilter;

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
	private KalmanFilter kf;
	private double time;
	private double resetTime;
	private int shotSpeed;
	
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
		int shotSpeed, int shotRadius){
		this.socket = socket;
		this.rp = rp;
		this.tankNumber = tankNumber;
		this.shotSpeed = shotSpeed;
		Tank tank = getTank();
		kf = new KalmanFilter(tank.getX(), tank.getY());
		previousAngle = 0;
		socket.sendDriveCommand(tankNumber, .0001f);
		socket.getResponse();
		time = System.currentTimeMillis();
		resetTime = 0;
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
		OtherTank ot = getOtherTank();
		double t = System.currentTimeMillis();
		double deltaT = t - time;
		time = t;
		if(t - resetTime > 2000){
			resetTime = t;
			Double p = kf.predict(0);
			kf = new KalmanFilter(p.x,p.y);
		}
		kf.update(ot.getX(), ot.getY(), deltaT);
		
	}
	public boolean shouldShoot(){
		OtherTank otherTank = getOtherTank();
		Double p = kf.predict(predictTime(otherTank.getX(), otherTank.getY()));
		if(isInLineOfFire(p.getX(), p.getY())){
			return true;
		}
		return false;
	}
	public boolean isInLineOfFire(double x, double y){
		double myAngle = tank.getAngle();
		double myX = tank.getX();
		double myY = tank.getY();
		double slope = Math.tan(myAngle);
		double diff = Math.abs(Math.abs((myY - y) / (myX - x)) - Math.abs(slope));
		return diff < .04;
	}
	
	public void fireShot(){
		socket.sendShootCommand(tankNumber);
		socket.getResponse();
	}
	public void updateAngle(){
		Tank tank = getTank();
		OtherTank ot = getOtherTank();
		double x = ot.getX();
		double y = ot.getY();
		Double p = kf.predict(predictTime(x,y) + 8);
		setTarget(p.getX(),p.getY());
		moveToVector(tank);
	}
	
	public double predictTime(double otherTankX, double otherTankY){
		return 2*(Math.sqrt(Math.pow(otherTankX - tank.getX(),2) + Math.pow(otherTankY - tank.getY(),2)))/shotSpeed;
	}
	
	public void moveToVector(Tank tank){
		Vector2d targetVector = calculateVector(tank);
		Vector2d tankVector = new Vector2d(tank.getVx(),tank.getVy()).normalize();
		double angle = tankVector.angle(targetVector);
		float angvel = (float) ((7*angle + 3.2*(angle - previousAngle))/(Math.PI));
		previousAngle = angle;
		if(tankVector.crossProduct(targetVector) < 0){
			angvel = angvel * -1;
		}
		if(angvel > .0001 || angvel < -.0001){
			socket.sendRotateCommand(tankNumber, angvel);
			socket.getResponse();
		}

	}
	
	public void setTarget(double x, double y){
		fields = new ArrayList<Field>();
		Field f = new AttractiveRadialField(1,1,x,y);
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







