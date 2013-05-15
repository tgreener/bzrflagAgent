
package search;

import state.Occgrid;
import state.SearchSpace;
import java.awt.Point;

public abstract class Search {
	
	protected SearchSpace space;

	public Search(SearchSpace s){
		space = s;
	}

	public abstract Path search(Point start);
}