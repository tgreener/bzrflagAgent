package agent.net;

public class Base {
	
	String color;
	double[][] corners;

	public Base(String[] baseResponse){
		this.color = baseResponse[1];
		corners = new double[(baseResponse.length-2)/2][2];
		for(int i = 2; i < baseResponse.length;){
			double[] coords = new double[2];
			coords[0] = Double.parseDouble(baseResponse[i++]);
			coords[1] = Double.parseDouble(baseResponse[i++]);
			corners[(i/2) - 2] = coords;
		}
	}
	
	public String getColor(){
		return color;
	}
	
	public double[][] getCorners(){
		return corners;
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
