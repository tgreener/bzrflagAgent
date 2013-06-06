
package state.kalman;

public class KalmanTest {
	public static void main(String[] args) {
		KalmanFilter kal = new KalmanFilter(0, 0);

		kal.setDelta(1);
		for(int i = 0; i < 50; i++) {
			kal.update(5 + i,5);
			kal.getMu().print();
		}

		System.out.println(kal.predict(100));
	}
}
