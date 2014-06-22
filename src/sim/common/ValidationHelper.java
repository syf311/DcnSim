package sim.common;

import java.util.Collection;

public class ValidationHelper {
	
	private ValidationHelper() {
		
	}
	
	public static void notNull(Object object, String name)
	{
		if (object == null)
		{
			throw new IllegalArgumentException(name); 
		}
	}
	
	public static void notNullOrEmpty(Collection<?> collection, String name)
	{
		if (collection == null || collection.isEmpty())
		{
			throw new IllegalArgumentException(name);
		}
	}
	
	public static void largerThanZero(int value, String name)
	{
		if (value <= 0)
		{
			throw new IllegalArgumentException(name);
		}
	}
	
	public static void largerThanZero(double value, String name)
	{
		if (value <= 0)
		{
			throw new IllegalArgumentException(name);
		}
	}
}
