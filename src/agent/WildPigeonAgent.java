package agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mytools.J;
import agent.fields.AttractiveRadialField;
import agent.fields.Field;
import agent.fields.Vector2d;
import agent.net.AgentClientSocket;
import agent.net.Constants;
import agent.net.ResponseParser;
import agent.net.Tank;

public class WildPigeonAgent {
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
		WildPigeonAgent pigeon = new WildPigeonAgent(sock,rp,0);
		pigeon.beWild();
	}
	
	public WildPigeonAgent(AgentClientSocket socket, ResponseParser rp, int tankNumber){
		this.socket = socket;
		this.rp = rp;
	}
	
	public void beWild(){
		socket.sendDriveCommand(tankNumber, 1);
		socket.getResponse();
		while("poop" != "delicious"){
			moveToVector();
		}
	}
	
	public Tank getTank(){
		socket.sendMyTanksQuery();
		List<Tank> tanks = rp.parseMyTanks(socket.getResponse());
		Tank tank = tanks.get(this.tankNumber);
		return tank;
	}
	
	
	public void moveToVector(){
		Tank tank = getTank();
		setRandomFields();
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
	
	public void setRandomFields(){
		Random r = new Random();
		int max = r.nextInt(5);
		fields = new ArrayList<Field>();
		for(int i = 0; i < max; i++){	
			Field f = new AttractiveRadialField(.3,1, r.nextDouble() * 100 - 100,r.nextDouble() * 100 - 100);
			fields.add(f);
		}
	}
}
