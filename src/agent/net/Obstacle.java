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
	
}
