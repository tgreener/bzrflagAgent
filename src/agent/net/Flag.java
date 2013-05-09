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
}
