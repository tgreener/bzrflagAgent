package agent.net;

public class Shot {

	double x,y,vx,vy;
	
	public Shot(String[] shotResponse){
		this.x = Double.parseDouble(shotResponse[1]);
		this.y = Double.parseDouble(shotResponse[2]);
		this.vx = Double.parseDouble(shotResponse[3]);
		this.vy = Double.parseDouble(shotResponse[4]);
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
	
}
