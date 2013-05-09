package agent.net;

public class Obstacle {
	
	double[][] corners;

	public Obstacle(String[] obstacleResponse){
		corners = new double[(obstacleResponse.length-1)/2][2];
		
		for(int i = 1; i < obstacleResponse.length;){
			double[] coords = new double[2];
			coords[0] = Double.parseDouble(obstacleResponse[i++]);
			coords[1] = Double.parseDouble(obstacleResponse[i++]);
			corners[(i/2) - 1] = coords;
		}

	}

	@Override
	public String toString() {
		String result = "\nobstacle \n";
		for(int i = 0; i < corners.length; i++) {
			result += " " + corners[i][0] + ", ";
			result += corners[i][1] + ";";
			
			if(i != corners.length - 1) {
				result += "\n";
			}
		}

		return result;
	}
	
}
