
package state.kalman;

public class KalmanTest {
	public static void main(String[] args) {
		KalmanFilter kal = new KalmanFilter(0, 0);

		kal.setDelta(5);
		for(int i = 0; i < 100; i++) {
			kal.update(5,5);
			kal.getMu().print();
		}
	}
}
