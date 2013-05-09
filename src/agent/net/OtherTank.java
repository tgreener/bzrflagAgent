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

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
	
}
