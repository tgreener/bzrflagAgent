package agent;

import java.util.ArrayList;
import java.util.List;

import agent.net.AgentClientSocket;
import agent.net.Obstacle;
import agent.net.Tank;
import agent.fields.*;

public class FinalAgent {
	
	protected List<Field> fields;
	protected List<Obstacle> obstacles;
	protected Tank tank;
	protected AgentClientSocket socket;
	private double previousAngle;

	public FinalAgent(AgentClientSocket s, List<Obstacle> os){
		socket = s;
		obstacles = os;
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
		float angvel = (float) ((7*angle + 3.2*(angle - previousAngle))/(Math.PI));
		previousAngle = angle;
		if(tankVector.crossProduct(targetVector) < 0){
			angvel = angvel * -1;
		}
		if(angvel > .0001 || angvel < -.0001){
			socket.sendRotateCommand(tank.getIndex(), angvel);
			socket.getResponse();
		}
	}
	
	//Calculate vector to current target
	public Vector2d calculateTargetVector(){
		Vector2d vector = new Vector2d(0,0);
		for(Field f : fields){
			vector = vector.add(f.fieldAtPoint(tank.getX(), tank.getY()).normalize());
		}
		return vector.normalize();
	}
	
	
}
