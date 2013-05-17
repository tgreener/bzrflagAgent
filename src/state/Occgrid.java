package state;

import java.awt.Point;
import java.util.ArrayList;

public class Occgrid {
	
	int[][] grid;
	Point at;
	int[] dimension;
	
	
	public Occgrid(ArrayList<String> response, Point at, int[] dimension) {
		grid = new int[response.size()][response.get(0).length()];
		int i = 0;
		for(String s : response){
			//System.out.println("STRING:" + s);
			String[] split = s.split("(?!^)");
			for(int j = 0; j < split.length; j++){
				grid[i][j] = Integer.parseInt(split[j]);
			}
			i++;
		}

		this.at = at;
		this.dimension = dimension;
	}
	
	public int getValueAt(int x, int y){
		return grid[x][y];
	}
	
	public void setValueAt(int newValue, int x, int y){
		grid[x][y] = newValue;
	}

	public Point at() {
		return at;
	}

	public int[] getDimension() {
		return dimension;
	}
}
