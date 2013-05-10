
package agent.fields.tests;

import agent.fields.*;
import java.text.DecimalFormat;

public class FieldTest {

	private static final int BOARD_SIZE = 15;
	private static final int FIELD_X = 7;
	private static final int FIELD_Y = 7;

	private static DecimalFormat posForm = new DecimalFormat("00.0");
	private static DecimalFormat negForm = new DecimalFormat("0.0");

	public static void main(String[] args) {
		Field arf = new AttractiveRadialField(1, 3, 5, FIELD_X, FIELD_Y);
				
		System.out.println("Attractive Field:");
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				Vector2d fieldAtPoint = arf.fieldAtPoint(j, i);
				double x = fieldAtPoint.getX();
				double y = fieldAtPoint.getY();

				printX(x);
				printY(y);
			}
			System.out.println();
		}

		Field rrf = new RepulsiveRadialField(1, 3, 5, FIELD_X, FIELD_Y);
				
		System.out.println("Repulsive Field:");
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				Vector2d fieldAtPoint = rrf.fieldAtPoint(j, i);
				double x = fieldAtPoint.getX();
				double y = fieldAtPoint.getY();

				printX(x);
				printY(y);
			}
			System.out.println();
		}

		Field trf = new TangentialRadialField(1, 3, 5, FIELD_X, FIELD_Y);
	
		System.out.println("Tangential Field:");
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				Vector2d fieldAtPoint = trf.fieldAtPoint(j, i);
				double x = fieldAtPoint.getX();
				double y = fieldAtPoint.getY();

				printX(x);
				printY(y);
			}
			System.out.println();
		}
	}

	private static void printX(double x) {
		if(x < 0) {
			System.out.print(negForm.format(x) + ", ");
		}
		else {
			System.out.print(posForm.format(x) + ", ");
		}
	}

	private static void printY(double y) {
		if(y < 0) {
			System.out.print(negForm.format(y) + "| ");
		}
		else {
			System.out.print(posForm.format(y) + "| ");
		}
	}

}



