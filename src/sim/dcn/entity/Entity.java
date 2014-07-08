package sim.dcn.entity;

import java.util.Collection;

public abstract class Entity {
	protected int id;
	
	public Entity(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static <T> T getEntity(Collection<T> collection, int id, String objectName) {
		T entity = null;
		for (T object : collection) {
			if (((Entity)object).getId() == id) {
				entity = object;
				break;
			}
		}
		
		if (entity == null) {
			throw new IllegalStateException(
					String.format("Cannot find entity %s with Id %d", objectName, id));
		}
		
		return entity;
	}
}
