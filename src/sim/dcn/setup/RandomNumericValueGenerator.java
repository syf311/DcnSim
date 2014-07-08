package sim.dcn.setup;

import java.util.Random;

public class RandomNumericValueGenerator implements NumericValueGenerator {

	private double min;
	private double max;
	
	private Random generator;
	
	public RandomNumericValueGenerator(double min, double max) {
		this.generator = new Random();
		this.min = min;
		this.max = max;
	}

	@Override
	public double nextDoubleValue() {
		return (this.generator.nextDouble() * (this.max - this.min) + this.min);
	}	
}
