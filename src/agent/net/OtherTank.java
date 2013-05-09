package agent.net;

public class OtherTank {

	String callsign;
	String color;
	String status; //isAlive
	String flag; //dash if not flag
	double x;
	double y;
	double angle; //radians between +/- Pi
	
	public OtherTank(String[] otherTankResponse){
		this.callsign = otherTankResponse[1];
		this.color = otherTankResponse[2];
		this.status = otherTankResponse[3];
		this.flag = otherTankResponse[4];
		this.x = Double.parseDouble(otherTankResponse[5]);
		this.y = Double.parseDouble(otherTankResponse[6]);
		this.angle = Double.parseDouble(otherTankResponse[7]);
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

	public String getColor() {
		return color;
	}

	public void setColor(String c) {
		color = c;
	}

	public String toString() {
		String result = "\nOtherTank\n";
		result += callsign + ", ";
		result += color + ", ";
		result += flag + ", ";
		result += x + ", ";
		result += y + ", ";
		result += angle;
		
		
		return result;
	}
	
}



