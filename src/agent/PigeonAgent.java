package agent;

import java.util.ArrayList;
import java.util.List;

import mytools.J;
import agent.fields.AttractiveRadialField;
import agent.fields.Field;
import agent.fields.Vector2d;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.ResponseParser;
import agent.net.Tank;

public class PigeonAgent {
	ResponseParser rp;
	AgentClientSocket socket;
	int tankNumber;
	List<Field> fields;
	double previousAngle;

	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		ResponseParser rp = new ResponseParser();
		PigeonAgent pigeon = new PigeonAgent(sock,rp,0);
		pigeon.beClay();
	}
	
	public PigeonAgent(AgentClientSocket socket, ResponseParser rp, int tankNumber){
		this.socket = socket;
		this.rp = rp;
	}
	
	public void beClay(){
		socket.sendDriveCommand(tankNumber, 1);
		socket.getResponse();
		Field f = new AttractiveRadialField(.3,1,0,200);
		fields = new ArrayList<Field>();
		fields.add(f);
		while("poop" != "delicious"){
			moveToVector();
		}
	}
	
	public void beWild(){
		socket.sendDriveCommand(tankNumber, 1);
		socket.getResponse();
		socket.sendRotateCommand(tankNumber, 1);
	}
	
	public Tank getTank(){
		socket.sendMyTanksQuery();
		List<Tank> tanks = rp.parseMyTanks(socket.getResponse());
		Tank tank = tanks.get(this.tankNumber);
		return tank;
	}
	
	
	public void moveToVector(){
		Tank tank = getTank();
		if(tank.getY() > 170){
			Field f = new AttractiveRadialField(.3,1,30,-180);
			fields = new ArrayList<Field>();
			fields.add(f);
			socket.sendDriveCommand(this.tankNumber, .2f);
			socket.getResponse();
		}
		else if(tank.getY() < -170){
			Field f = new AttractiveRadialField(.3,1,30,180);
			fields = new ArrayList<Field>();
			fields.add(f);
			socket.sendDriveCommand(this.tankNumber, .2f);
			socket.getResponse();
		}
		else{
			socket.sendDriveCommand(this.tankNumber, 1f);
			socket.getResponse();
		}
		Vector2d targetVector = calculateVector(tank);
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

	}
	
	public Vector2d calculateVector(Tank tank){
		Vector2d vector = new Vector2d(0,0);
		for(Field f : fields){
			vector = vector.add(f.fieldAtPoint(tank.getX(), tank.getY()).normalize());
		}
		return vector.normalize();
	}
}
