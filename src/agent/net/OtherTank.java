package agent.net;

public class OtherTank {

	String callsign;
	String status; //isAlive
	String flag; //dash if not flag
	double x;
	double y;
	double angle; //radians between +/- Pi
	
	public OtherTank(String[] otherTankResponse){
		this.callsign = otherTankResponse[1];
		this.status = otherTankResponse[2];
		this.flag = otherTankResponse[3];
		this.x = Double.parseDouble(otherTankResponse[4]);
		this.y = Double.parseDouble(otherTankResponse[5]);
		this.angle = Double.parseDouble(otherTankResponse[6]);
	}
	
}
