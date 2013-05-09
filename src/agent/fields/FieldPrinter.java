
package agent.fields;

import com.panayotis.gnuplot.JavaPlot;

public class FieldPrinter {


	public static void main(String[] args) {
		JavaPlot p = new JavaPlot();
       		p.addPlot("sin(x)");
        	p.plot();
	}

}



