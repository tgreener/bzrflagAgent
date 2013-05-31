
package state;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class BayesGrid {

	private static final double O_OCCUPIED_GIVEN_OCCUPIED = 0.97d;
	private static final double O_NOT_OCC_GIVEN_NOT_OCC = 0.9d;
	private static final double OCCUPIED_LIMIT = 0.999;
	private static final double EMPTY_LIMIT = 0.001;

	public static enum Occupation {OCCUPIED, EMPTY, UNKNOWN}

	private Semaphore sem;
	private double[][] stateProbabilityGrid;

	public BayesGrid(int size, double s) {
		sem = new Semaphore(1, true);
		size++;
		stateProbabilityGrid = new double[size][size];

		for(int i = 0; i < stateProbabilityGrid.length; i++) {
			Arrays.fill(stateProbabilityGrid[i], s);
		}
	}

	public double getProbability(int x, int y) {
		try {
			sem.acquire();
			double result = stateProbabilityGrid[x][y];
			sem.release();
	
			return result;
		}
		catch(InterruptedException e) {
			System.out.println(e);
		}

		return -1;		
	}

	public void updateWithObservation(int x, int y, int obs) {
		try {
			sem.acquire();
			double s = stateProbabilityGrid[x][y];
			
			double numerator = 0;
			double denominator = 0;

			if(obs == 1) {
				numerator = O_OCCUPIED_GIVEN_OCCUPIED * s;
				denominator = O_OCCUPIED_GIVEN_OCCUPIED * s;
				denominator += (1d - O_NOT_OCC_GIVEN_NOT_OCC) * (1d - s);
			}
			else if(obs == 0) {
				numerator = (1d - O_OCCUPIED_GIVEN_OCCUPIED) * s;
				denominator = (1d - O_OCCUPIED_GIVEN_OCCUPIED) * s;
				denominator += O_NOT_OCC_GIVEN_NOT_OCC * (1d - s);
			}
	
			double result = numerator/denominator;

			stateProbabilityGrid[x][y] = result;
		
			sem.release();
		}
		catch(InterruptedException e) {
			System.out.println(e);
		}
		
	}

	public Occupation isOccupied(int x, int y) {
		double p = stateProbabilityGrid[x][y];
		if(p > OCCUPIED_LIMIT) {
			return Occupation.OCCUPIED;
		}
		else if(p < EMPTY_LIMIT) {
			return Occupation.EMPTY;
		}
		else {
			return Occupation.UNKNOWN;
		}
	}
	
	/*public double[][] getGrid(){
		return stateProbabilityGrid;
	}*/

	public void writeGNUPlotFile(String filename) {
		try {
			PrintWriter p = new PrintWriter(filename, "UTF-8");
			p.println("plot \"-\" with points");
			for (int i = 0; i < stateProbabilityGrid.length; i++) {
				for (int j = 0; j < stateProbabilityGrid[i].length; j++) {
					if(isOccupied(i, j) == Occupation.OCCUPIED) {
						p.println(i + " " + j);
					}
				}
			}
			p.println("e");
			p.close();
		}
		catch(FileNotFoundException e) {
			System.out.println(e);
		}
		catch(UnsupportedEncodingException e) {
			System.out.println(e);
		}
	}
}


