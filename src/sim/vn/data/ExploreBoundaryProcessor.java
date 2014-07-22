package sim.vn.data;

import sim.vn.entity.Plane;

public class ExploreBoundaryProcessor implements ILineProcessor {

	private Plane plane;
	
	public ExploreBoundaryProcessor() {
	}
	
	@Override
	public void processALine(String line) {
		String[] columns = line.split(",");
		double longitude = LineProcessorHelper.getLongitude(columns);
		double latitude = LineProcessorHelper.getLatitude(columns);
		this.updatePlane(longitude, latitude);
	}
	
	public Plane getPlane() {
		return this.plane;
	}
	
	private void updatePlane(double longtitude, double latitude) {
		if (this.plane == null) {
			this.plane = new Plane(longtitude, longtitude, latitude, latitude);
		} else {
			this.plane.update(longtitude, latitude);
		}
	}
}
