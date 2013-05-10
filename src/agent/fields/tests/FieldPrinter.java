
package agent.fields.tests;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.ArrayDataSet;
import agent.fields.*;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class FieldPrinter {

	private static final int BOARD_SIZE = 15;
	private static final int FIELD_X = 7;
	private static final int FIELD_Y = 7;

	public static void main(String[] args) {
		/*JavaPlot p = new JavaPlot();
		p.addPlot(getAttractiveFieldData());
		p.plot();*/
		
		Field arf = new AttractiveRadialField(.2, .1, 6, FIELD_X, FIELD_Y);
		printFieldData(arf, "attract.dat");

		Field rrf = new RepulsiveRadialField(.2, .1, 6, FIELD_X, FIELD_Y);
		printFieldData(rrf, "repulse.dat");

		Field trf = new TangentialRadialField(.2, .1, 6, FIELD_X, FIELD_Y);
		printFieldData(trf, "tangent.dat");
	}

	private static void printFieldData(Field field, String fileName) {
		double[][] data = new double[BOARD_SIZE * BOARD_SIZE][4];
		int dataIndex = 0;

		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				Vector2d fieldAtPoint = field.fieldAtPoint(j, i);
				if(fieldAtPoint.length() > 1) {
					fieldAtPoint = fieldAtPoint.normalize();
				}
				
				data[dataIndex][0] = j;
				data[dataIndex][1] = i;
				
				data[dataIndex][2] = fieldAtPoint.getX();
				data[dataIndex][3] = fieldAtPoint.getY();

				dataIndex++;
			}
		}

		writeFile(data, fileName);
	}

	

	private static void writeFile(double[][] data, String fileName) {
		try {
			PrintWriter p = new PrintWriter(fileName, "UTF-8");
			//System.out.println(BOARD_SIZE * BOARD_SIZE * 2);

			for(int i = 0; i < data.length; i++) {
				String printThis = ""; 
				printThis += data[i][0];
				printThis += " " + data[i][1];
				printThis += " " + data[i][2];
				printThis += " " + data[i][3];

				p.println(printThis);
			}

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



