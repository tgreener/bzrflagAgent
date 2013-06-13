
package agent;

public class TargetTest {

	public static void main(String[] args) {
		Targeter t = new Targeter();

		t.setMyPosition(0, 0);
		t.setTargetPosition(1, 0);
		t.setTargetVelocity(0, 1);
		t.setProjectileSpeed(Math.sqrt(2));

		System.out.println(t.getIntersectTime());
	}

}
