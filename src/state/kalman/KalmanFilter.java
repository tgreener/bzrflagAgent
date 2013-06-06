
package state.kalman;

import java.awt.geom.Point2D;
import org.jblas.DoubleMatrix;
import static org.jblas.Solve.*;

public class KalmanFilter {
	private static final int MATRIX_SIZE = 6;

	private DoubleMatrix identity;	
	private DoubleMatrix mu;
	private DoubleMatrix F;
	private DoubleMatrix FT;
	private DoubleMatrix H;
	private DoubleMatrix HT;
	private DoubleMatrix sigT;
	private DoubleMatrix sigX;
	private DoubleMatrix sigZ;

	private DoubleMatrix glob;

	public KalmanFilter(double x, double y) {
		identity = DoubleMatrix.eye(MATRIX_SIZE);
		mu = new DoubleMatrix(new double[]{x, 0, 0, y, 0, 0});

		F = DoubleMatrix.eye(MATRIX_SIZE);
		FT = F.transpose();

		H = new DoubleMatrix(2, MATRIX_SIZE);
		H.put(0, 0, 1);
		H.put(1, (MATRIX_SIZE / 2), 1);
		HT = H.transpose();

		sigT = DoubleMatrix.diag(new DoubleMatrix(new double[]{10, 0.1, 0.1, 10, 0.1, 0.1}));
		sigX = DoubleMatrix.diag(new DoubleMatrix(new double[]{0.1, 0.1, 10, 0.1, 0.1, 10}));
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

		FT = F.transpose();
	}

	public void update(double xObs, double yObs, double dt) {
		setDelta(dt);

		update(xObs, yObs);
	}

	public void update(double xObs, double yObs) {
		generateGlob();
		DoubleMatrix k = k();
		updateMu(new DoubleMatrix(new double[]{xObs, yObs}), k);
		updateSigT(k);
	}

	public DoubleMatrix getMu() {
		return mu;
	}

	public Point2D.Double predict(double dt) {
		DoubleMatrix predictionMatrix = DoubleMatrix.eye(MATRIX_SIZE);
		DoubleMatrix H = DoubleMatrix.zeros(2, MATRIX_SIZE);
		
		predictionMatrix.put(0, 1, dt);
		predictionMatrix.put(0, 2, Math.pow(dt, 2) / 2);
		predictionMatrix.put(1, 2, dt);

		predictionMatrix.put(3, 4, dt);
		predictionMatrix.put(3, 5, Math.pow(dt, 2) / 2);
		predictionMatrix.put(4, 5, dt);

		H.put(0, 0, 1);
		H.put(1, 3, 1);

		DoubleMatrix result = H.mmul(predictionMatrix);
		result = result.mmul(mu);

		return new Point2D.Double(result.get(0), result.get(1));
	}

	private void generateGlob() {
		glob = DoubleMatrix.zeros(MATRIX_SIZE, MATRIX_SIZE);

		F.mmuli(sigT, glob);
		glob.mmuli(FT, glob);
		glob.addi(sigX, glob);
	}

	private void updateSigT(DoubleMatrix k) {
		DoubleMatrix temp = k.mmul(H);
		temp = DoubleMatrix.eye(MATRIX_SIZE).sub(temp);
	
		sigT = temp.mmul(glob);
		
	}

	private void updateMu(DoubleMatrix z, DoubleMatrix k) {
		DoubleMatrix zMinusHFMu = H.mmul(F);
		zMinusHFMu = zMinusHFMu.mmul(mu);
		zMinusHFMu = z.sub(zMinusHFMu);

		DoubleMatrix temp = k.mmul(zMinusHFMu);
		mu = F.mmul(mu);
		mu = mu.add(temp);
	}

	private DoubleMatrix k() {
		DoubleMatrix k = DoubleMatrix.zeros(MATRIX_SIZE, MATRIX_SIZE);
		DoubleMatrix temp = DoubleMatrix.zeros(MATRIX_SIZE, MATRIX_SIZE);
		
		glob.mmuli(HT, k);

		H.mmuli(glob, temp);
		temp = temp.mmul(HT);
		temp.addi(sigZ, temp);
		temp = solve(temp, DoubleMatrix.eye(temp.rows));

		k.mmuli(temp, k);

		return k;
	}
}

