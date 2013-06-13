
package agent;

public class Targeter {

	private double[] myPos;
	private double[] targPos;
	private double[] targVel;
	private double projSpeed;

	public Targeter() {
		myPos = new double[2];
		targPos = new double[2];
		targVel = new double[2];
	}

	public double getIntersectTime() {
		double a = getA();
		double b = getB();
		double c = getC();
		double discriminate = Math.pow(b, 2) - (4 * a * c);

		if(discriminate < 0 || (a - 0.001 < 0 && a + 0.001 > 0)) {
			return -1;
		}
		else if (discriminate - 0.001 > 0) {
			double t1 = (-b + Math.sqrt(discriminate)) / (2 * a);
			double t2 = (-b - Math.sqrt(discriminate)) / (2 * a);

			return minNotNegative(t1, t2);
		}
		else {
			double t = (-b + Math.sqrt(discriminate)) / (2 * a);
			System.out.println(t);

			return t;
		}
	}

	private double minNotNegative(double a, double b) {
		if(a > 0 && b > 0) {
			return Math.min(a, b);
		}
		else {
			return Math.max(a, b);
		}
	}

	private double getA() {
		double a = Math.pow(targVel[0], 2);
		a += Math.pow(targVel[1], 2);
		a -= Math.pow(projSpeed, 2);

		return a;
	}

	private double getB() {
		double difX = targPos[0] - myPos[0];
		double difY = targPos[1] - myPos[1];
		double b = (targVel[0] * difX) + (targVel[1] * difY);
		b *= 2;

		return b;
	}

	private double getC() {
		double difX = targPos[0] - myPos[0];
		double difY = targPos[1] - myPos[1];
		double c = Math.pow(difX, 2) + Math.pow(difY, 2);

		return c;
	}

	public void setMyPosition(double x, double y) {
		myPos[0] = x;
		myPos[1] = y;
	}

	public void setTargetPosition(double x, double y) {
		targPos[0] = x;
		targPos[1] = y;
	}

	public void setTargetVelocity(double x, double y) {
		targVel[0] = x;
		targVel[1] = y;
	}

	public void setProjectileSpeed(double s) {
		projSpeed = s;
	}
}


