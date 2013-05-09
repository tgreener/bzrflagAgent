package agent.net;

public class Shot {

	double x,y,vx,vy;
	
	public Shot(String[] shotResponse){
		this.x = Double.parseDouble(shotResponse[1]);
		this.y = Double.parseDouble(shotResponse[2]);
		this.vx = Double.parseDouble(shotResponse[3]);
		this.vy = Double.parseDouble(shotResponse[4]);
	}
	
}
