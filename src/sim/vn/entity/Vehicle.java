package sim.vn.entity;

public class Vehicle {
	private int id;
	
	public Vehicle(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	@Override
    public boolean equals(Object o) {
		if (o == this) {
            return true;
        }
		
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        
        Vehicle vehicle = (Vehicle)o;
        
        return this.id == vehicle.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
    
    public String toFolderName() {
    	return String.format("t%d.ogd", this.id);
    }
}
