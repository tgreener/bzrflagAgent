
package agent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import agent.net.AgentClientSocket;
import mytools.J;

public class DummyAgent {
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		sock.getResponse();
		sock.sendIntroduction();
		for(int tank = 0; tank < 9; tank++){
			Random r = new Random();
			Timer timer = new Timer();
			timer.schedule(new Shoot(sock,tank,r),1500);
			timer.schedule(new MoveAndRotate(sock,tank,1,r),1000);
		}
	}
	
	public static class Command extends TimerTask{
		
		protected AgentClientSocket socket;
		protected int tank;
		protected Timer timer;
		protected Random r;
				
		public Command(AgentClientSocket socket, int tank, Random r){
			this.socket = socket;
			this.tank = tank;
			timer = new Timer();
			this.r = r;
		}
		
		public void run(){
			//do nothing
		}
	}
	
	public static class Rotate extends Command{
		
		private float angle;
		
		public void run(){
			socket.sendRotateCommand(this.tank,this.angle);
			socket.getResponse();
		}
		
		public Rotate(AgentClientSocket socket,int tank, float angle, Random r){
			super(socket,tank, r);
			this.angle = angle;
		}
	}	
	
	public static class MoveAndRotate extends Command{
		
		private int speed;
		
		public void run(){
			int speed = 1;
			if(this.speed == 1)
				speed = 0;
			socket.sendDriveCommand(this.tank,this.speed);
			socket.getResponse();
			if(this.speed == 0){
				System.out.println("fire");
				socket.sendRotateCommand(this.tank,1);
				socket.getResponse();
				//Stop rotation after about 60 degrees
				this.timer.schedule(new Rotate(this.socket,this.tank,0, r),(long)(8/4.5 * 1000)); 
			}
			long moveTime = (long)(3000 +this.r.nextFloat()*5000);
			this.timer.schedule(new MoveAndRotate(this.socket,this.tank,speed,this.r), moveTime);
		}
		
		public MoveAndRotate(AgentClientSocket socket,int tank, int speed, Random r){
			super(socket,tank,r);
			this.speed = speed;
		}
	}	
	
	public static class Shoot extends Command{
		
		public void run(){
			socket.sendShootCommand(this.tank);
			socket.getResponse();
			this.timer.schedule(new Shoot(this.socket,this.tank,r), (long)(1500 + this.r.nextFloat()*1000));
		}
		
		public Shoot(AgentClientSocket socket, int tank, Random r){
			super(socket,tank,r);
		}		
	}
}