package agent.net;

public class Flag {
	
	String flagColor;
	String possesingColor;
	double x,y;

	public Flag(String[] flagResponse){
		
		this.flagColor = flagResponse[1];
		this.possesingColor = flagResponse[2];
		this.x = Double.parseDouble(flagResponse[3]);
		this.y = Double.parseDouble(flagResponse[4]);
		
	}

	public String getFlagColor() {
		return flagColor;
	}

	public void setFlagColor(String flagColor) {
		this.flagColor = flagColor;
	}

	public String getPossesingColor() {
		return possesingColor;
	}

	public void setPossesingColor(String possesingColor) {
		this.possesingColor = possesingColor;
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
	
	
}
