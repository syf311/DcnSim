package sim.common;

import java.util.Collection;

public class ValidationHelper {
	
	private ValidationHelper() {

	}
	
	public static void notNull(Object object, String name) {
		if (object == null)
		{
			throw new IllegalArgumentException(name); 
		}
	}
	
	public static void notNullOrEmpty(Collection<?> collection, String name) {
		if (ValidationHelper.isNullOrEmpty(collection)) {
			throw new IllegalArgumentException(name);
		}
	}
	
	public static void notNullOrEmpty(String value, String name) {
		if (ValidationHelper.isNullOrEmpty(value)) {
			throw new IllegalArgumentException(name);
		}
	}
	
	public static void largerThanZero(int value, String name) {
		if (value <= 0) {
			throw new IllegalArgumentException(name);
		}
	}
	
	public static void largerThanZero(double value, String name) {
		if (value <= 0) {
			throw new IllegalArgumentException(name);
		}
	}
	
	public static void powerOfTwo(int number, String name) {
		if (!ValidationHelper.isPowerOfTwo(number)) {
			throw new IllegalArgumentException(name);
		}
	}
	
	public static boolean isNullOrEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
	
	public static boolean isNullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}
	
	public static boolean isPowerOfTwo(int number) {
		while (number > 0 && number % 2 == 0) {
			number = number / 2;
		}
		
		return number == 1;
	}
}
