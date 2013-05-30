
package state.test;

import state.BayesGrid;

public class BayesGridTest {
	
	public static void main(String[] args) {
		BayesGrid bg = new BayesGrid(1, 0.05);

		for(int i = 0; bg.isOccupied(0,0) != BayesGrid.Occupation.OCCUPIED; i++){
			bg.updateWithObservation(0, 0, 1);
			System.out.println("Iteration " + i + ": "+ bg.getProbability(0,0));
		}
		System.out.println("Space is Occupied!\n");

		bg = new BayesGrid(1, 0.05);
		for(int i = 0; bg.isOccupied(0,0) != BayesGrid.Occupation.EMPTY; i++){
			bg.updateWithObservation(0, 0, 0);
			System.out.println("Iteration: " + bg.getProbability(0,0));
		}
		System.out.println("Space is Empyt!\n");
	}
}