
package agent.fields.tests;

import agent.fields.*;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Iterator;

public class FieldListPrinter {

	private int boardSize;
	private List<Field> fields;
	private int sample;

	public FieldListPrinter(List<Field> fl, int bs, int s) {
		fields = fl;
		boardSize = bs;
		sample = s;
	}

	public void printFieldsFile(String fileName) {
		double[][] data = new double[(boardSize / sample) * (boardSize / sample)][4];
		int dataIndex = 0;

		for(int i = 0; i < boardSize; i += sample) {
			for(int j = 0; j < boardSize; j += sample) {
				Vector2d fieldAtPoint = sumFieldAtPoint(j-400, i-400);
				if(fieldAtPoint.length() > 1) {
					fieldAtPoint = fieldAtPoint.normalize();
				}
				
				data[dataIndex][0] = j;
				data[dataIndex][1] = i;
				
				data[dataIndex][2] = fieldAtPoint.getX()*sample;
				data[dataIndex][3] = fieldAtPoint.getY()*sample;

				dataIndex++;
			}
		}

		writeFile(data, fileName);
	}

	private Vector2d sumFieldAtPoint(double x, double y) {
		Iterator<Field> itr = fields.iterator();
		Vector2d sum = new Vector2d(0d, 0d);		

		while(itr.hasNext()) {
			sum = sum.add(itr.next().fieldAtPoint(x, y));
		}
		
		return sum;
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



