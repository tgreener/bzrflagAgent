package agent.net;

public class Tank {

	int index;
	String callsign;
	String status; //isAlive
	int shotsAvailable; //always 10?
	double timeToTeload; //3 second reload time
	String flag; //dash if not flag
	double x;
	double y;
	double angle; //radians between +/- Pi
	double vx; //Angular velocity in x direction
	double vy; //Angular velocity in y direction
	double angvel; //Angular change/velocity
	
	public Tank(String[] mytanksResponse){
		this.index = Integer.parseInt(mytanksResponse[1]);
		this.callsign = mytanksResponse[2];
		this.status = mytanksResponse[3];
		this.shotsAvailable = Integer.parseInt(mytanksResponse[4]);
		this.timeToTeload = Double.parseDouble(mytanksResponse[5]);
		this.flag = mytanksResponse[6];
		this.x = Double.parseDouble(mytanksResponse[7]);
		this.y = Double.parseDouble(mytanksResponse[8]);
		this.angle = Double.parseDouble(mytanksResponse[9]);
		this.vx = Double.parseDouble(mytanksResponse[10]);
		this.vy = Double.parseDouble(mytanksResponse[11]);
		this.angvel = Double.parseDouble(mytanksResponse[12]);
	}
	
}
