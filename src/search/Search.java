
package search;

import state.Occgrid;
import java.awt.Point;

public abstract class Search {
	
	private Occgrid grid;

	public Search(Occgrid grid){
		this.grid = grid;
	}

	public abstract Path search(Point start);
}