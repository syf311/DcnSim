package sim.dcn.setup;

public class ConstantNumericValueGenerator implements NumericValueGenerator {
	private double constantNumericValue;
	
	public ConstantNumericValueGenerator(double constantNumericValue) {
		this.constantNumericValue = constantNumericValue;
	}

	@Override
	public double nextDoubleValue() {
		return this.constantNumericValue;
	}
}
