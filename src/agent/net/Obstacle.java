package agent.net;

public class Obstacle {
	
	double[][] corners;

	public Obstacle(String[] obstacleResponse){
		corners = new double[(obstacleResponse.length-1)/2][1];
		
		for(int i = 1; i < obstacleResponse.length;){
			double[] coords = new double[1];
			coords[0] = Double.parseDouble(obstacleResponse[i++]);
			coords[1] = Double.parseDouble(obstacleResponse[i++]);
			corners[i/2] = coords;
		}

	}

	public double[][] getCorners() {
		return corners;
	}

	public void setCorners(double[][] corners) {
		this.corners = corners;
	}
	
	public double[] getCenter(){
		double x = 0;
		double y = 0;
		int total = 0;
		for(double[] d : corners){
			total++;
			x += d[0];
			y += d[1];
		}
		x = x/total;
		y = y/total;
		double coords[] = {x,y};
		return coords;
	}
	
}
