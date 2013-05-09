
package agent;


import java.util.List;
import agent.fields.*;
import java.util.Timer;
import java.util.TimerTask;


import agent.net.*;

public class Agent {
	
	int tankNumber;
	ResponseParser rp;
	AgentClientSocket socket;
	double previousAngle;
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		
		Agent a = new Agent(2,sock);
		
		/* Obstacle field: tangential = Repellent + current direction
		 *  - base on corners and points inbetween corners
		 * Base field : strong attractive towards center of corners
		 * 
		 * 
		 */
		a.moveToVector(a.calculateVector());
		Timer timer = new Timer();
		timer.schedule(new UpdateVector(a),100);
		
	}
	public static class UpdateVector extends TimerTask{
		
		Agent agent;
				
		public UpdateVector(Agent a){
			agent = a;
			
		}
		
		public void run(){
			Timer timer = new Timer();
			timer.schedule(new UpdateVector(agent),100);
			agent.moveToVector(agent.calculateVector());
		}
	}
	
	public Agent(int tankNumber, AgentClientSocket socket){
		this.tankNumber = tankNumber;
		rp = new ResponseParser();
		previousAngle = 0;
		this.socket = socket;
		socket.sendDriveCommand(tankNumber, 1);
		socket.getResponse();
	}
	
	public void moveToVector(Vector2d v){
		socket.sendMyTanksQuery();
		List<Tank> tanks = rp.parseMyTanks(socket.getResponse());
		Tank tank = tanks.get(this.tankNumber);
		Vector2d tankVector = new Vector2d(tank.getVx(),tank.getVy());
		double angle = tankVector.angle(v);
		float angvel = (float) ((angle + angle - previousAngle)/(Math.PI/2));
		previousAngle = angle;
		if(angvel > .0001 || angvel < -.0001){
			socket.sendRotateCommand(tankNumber, angvel);
			socket.getResponse();
		}
		if(tank.getTimeToReload() == 0){
			socket.sendShootCommand(tankNumber);
			socket.getResponse();
		}
		System.out.println("angle:" + angle + " angvel " + angvel);
	}
	
	public Vector2d calculateVector(){
		
		
		Vector2d vector = new Vector2d(18d,18d);
		
		return vector;
	}
}


