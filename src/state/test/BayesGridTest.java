
package state.test;

import state.BayesGrid;

public class BayesGridTest {
	
	public static void main(String[] args) {
		BayesGrid bg = new BayesGrid(1, 0.05);

		bg.updateWithObservation(0, 0, 1);
		System.out.println(bg.getProbability(0,0));

		bg = new BayesGrid(1, 0.05);
		bg.updateWithObservation(0, 0, 0);
		System.out.println(bg.getProbability(0,0));
	}
}