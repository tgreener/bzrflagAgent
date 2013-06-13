
package state.kalman;
import java.util.Random;

public class KalmanTest {
	public static void main(String[] args) {
		KalmanFilter kal = new KalmanFilter(0, 0);
		double initX = 500;
		double initY = 500;

		kal.setDelta(1);
		for(int i = 0; i < 500; i++) {
			double x = makeNoise((initX + i), 5);
			double y = makeNoise(initY, 5);
			System.out.println("Input: " + x + ", " + y);

			kal.update(x, y);
			kal.getMu().print();
		}

		System.out.println(kal.predict(1));
	}

	private static double makeNoise(double mean, double sd) {
		Random r = new Random();

		return (r.nextGaussian() * sd) + mean;
	}
}
