
package agent.fields;

public abstract class RadialField extends Field{

	protected double radius;
	protected double spread;

	public RadialField(double a, double r) {
		super(a);
		radius = r;
		spread = Double.POSITIVE_INFINITY;
	}

	public RadialField(double a, double r, double s) {
		super(a);
		radius = r;
		spread = s;
	}
}

