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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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

	public int getShotsAvailable() {
		return shotsAvailable;
	}

	public void setShotsAvailable(int shotsAvailable) {
		this.shotsAvailable = shotsAvailable;
	}

	public double getTimeToTeload() {
		return timeToTeload;
	}

	public void setTimeToTeload(double timeToTeload) {
		this.timeToTeload = timeToTeload;
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

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getAngvel() {
		return angvel;
	}

	public void setAngvel(double angvel) {
		this.angvel = angvel;
	}
	
}
