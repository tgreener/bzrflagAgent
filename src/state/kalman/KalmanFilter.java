
package state.kalman;

import org.jblas.DoubleMatrix;

public class KalmanFilter {
	private static final int MATRIX_SIZE = 6;

	private DoubleMatrix identity;	
	private DoubleMatrix mu;
	private DoubleMatrix F;
	private DoubleMatrix H;
	private DoubleMatrix sigT;
	private DoubleMatrix sigX;
	private DoubleMatrix sigZ;

	public KalmanFilter(double x, double y) {
		identity = DoubleMatrix.eye(MATRIX_SIZE);
		mu = new DoubleMatrix(new double[]{x, 0, 0, y, 0, 0});

		F = DoubleMatrix.eye(MATRIX_SIZE);
		H = new DoubleMatrix(2, MATRIX_SIZE);
		H.put(0, 0, 1);
		H.put(1, (MATRIX_SIZE / 2), 1);

		sigT = DoubleMatrix.diag(new DoubleMatrix(new double[]{100, 0.1, 0.1, 100, 0.1, 0.1}));
		sigX = DoubleMatrix.diag(new DoubleMatrix(new double[]{0.1, 0.1, 100, 0.1, 0.1, 100}));
		sigZ = new DoubleMatrix(2, 2);
		updateSigmaZ(5);
	}

	public void updateSigmaZ(double noise) {
		sigZ.put(0, 0, Math.pow(noise, 2));
		sigZ.put(1, 1, Math.pow(noise, 2));
	} 

	public void setDelta(double dt) {
		F.put(0, 1, dt);
		F.put(0, 2, Math.pow(dt, 2) / 2);
		F.put(1, 2, dt);

		F.put(3, 4, dt);
		F.put(3, 5, Math.pow(dt, 2) / 2);
		F.put(4, 5, dt);
	}

	public DoubleMatrix update(double xObs, double yObs, double dt) {
		setDelta(dt);

		return update(xObs, yObs);
	}

	public DoubleMatrix update(double xObs, double yObs) {
		return mu;
	}

	public DoubleMatrix getMu() {
		return mu;
	}
}