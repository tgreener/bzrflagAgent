
package search;

import state.Occgrid;
import java.awt.Point;

public abstract class Search {
	
	private SearchSpace space;

	public Search(SearchSpace s){
		space = s;
	}

	public abstract Path search(Point start);
}