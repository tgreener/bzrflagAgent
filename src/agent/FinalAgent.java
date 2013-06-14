package agent;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import mytools.J;

import org.jblas.DoubleMatrix;

import state.kalman.KalmanFilter;

import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.Obstacle;
import agent.net.OtherTank;
import agent.net.Tank;
import agent.fields.*;

public class FinalAgent {
	
	protected List<Field> fields;
	protected List<Obstacle> obstacles;
	protected Tank tank;
	protected AgentClientSocket socket;
	protected List<KalmanFilter> filters;
	protected Constants constants;
	private double previousAngle;
	private List<Tank> myTanks;

	public FinalAgent(AgentClientSocket s, List<Obstacle> os, List<KalmanFilter> filters, Constants c){
		socket = s;
		obstacles = os;
		this.filters = filters;
		constants = c;
	}
	
	public void updateSelf(Tank tank){
		this.tank = tank;
		
	}
	
	public void setTarget(double x, double y){
		fields = new ArrayList<Field>();
		Field f = new AttractiveRadialField(1,1,x,y);
		fields.add(f);
		resetObstacles();
	}
	
	public void resetObstacles(){
		for(Obstacle ob : obstacles){
			double[] p = ob.getCenter();
			Field f = new TangentialRadialField(.1,.1,p[0],p[1]);
			fields.add(f);
		}
	}	
	
	public void moveToTarget(){
		Vector2d targetVector = calculateTargetVector();
		Vector2d tankVector = new Vector2d(tank.getVx(),tank.getVy()).normalize();
		double angle = tankVector.angle(targetVector);
		J.p("AN: " + tank.getVx());
		float angvel = (float) ((7*angle + 3.2*(angle - previousAngle))/(Math.PI));
		previousAngle = angle;
		if(tankVector.crossProduct(targetVector) < 0){
			angvel = angvel * -1;
		}
		if(angvel > .001 || angvel < -.001){
			socket.sendRotateCommand(tank.getIndex(), angvel);
			socket.getResponse();
		}
	}
	
	//Calculate vector to current target
	public Vector2d calculateTargetVector(){
		Vector2d vector = new Vector2d(0,0);
		J.p("calc");
		for(Field f : fields){
			J.p("F");
			vector = vector.add(f.fieldAtPoint(tank.getX(), tank.getY()).normalize());
		}
		return vector.normalize();
	}
	
	/** Kalman stuff **/
	
	public boolean shouldShoot(){
		if(isFriendlyFire()){
			return false;
		}
		KalmanFilter kf = filters.get(tank.getIndex());
		DoubleMatrix mu = kf.getMu();
		Double p = kf.predict(predictTime(mu.get(0), mu.get(3), kf));
		if(isInLineOfFire(p.getX(), p.getY())){
			return true;
		}
		return false;
	}
	
	public boolean isFriendlyFire(){
		for(Tank t : myTanks){
			if(isInLineOfFire(t.getX(),t.getY())){
				return true;
			}
		}
		return false;
	}
	
	public boolean isInLineOfFire(double x, double y){
		double myAngle = tank.getAngle();
		double myX = tank.getX();
		double myY = tank.getY();
		double slope = Math.tan(myAngle);
		double diff = Math.abs(Math.abs((myY - y) / (myX - x)) - Math.abs(slope));
		return diff < .01;
	}
	
	public double predictTime(double otherTankX, double otherTankY, KalmanFilter kf){
		Targeter t = new Targeter();
		t.setMyPosition(tank.getX(), tank.getY());
		t.setProjectileSpeed(constants.getShotspeed());
		t.setTargetPosition(otherTankX, otherTankY);
		DoubleMatrix dm = kf.getMu();
		t.setTargetVelocity(dm.get(1), dm.get(4));
		double time = t.getIntersectTime();
		return time < 0 ? 2 : time*3;
	}
	
	public void fireShot(){
		socket.sendShootCommand(tank.getIndex());
		socket.getResponse();
	}
	
	/** End Kalman Stuff */
	
	public void setSpeed(float speed){
		socket.sendDriveCommand(tank.getIndex(), speed);
		socket.getResponse();
	}

	public void updateTeam(List<Tank> myTanks) {
		this.myTanks = myTanks;		
	}
	
}
