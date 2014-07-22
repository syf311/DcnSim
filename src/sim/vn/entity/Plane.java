package sim.vn.entity;

import sim.common.ValidationHelper;

public class Plane {
	private static final double SmallestGrain = 0.000001;
	
	private double minLongitude;
	
	private double maxLongitude;
	
	private double minLatitude;
	
	private double maxLatitude;
	
	private double cellSize;
	
	private long horizontalCells;
	
	private long verticalCells;
	
	public Plane(
			double minLongitude, 
			double maxLongitude, 
			double minLatitude,
			double maxLatitude) {
		this.minLongitude = minLongitude;
		this.maxLongitude = maxLongitude;
		this.minLatitude = minLatitude;
		this.maxLatitude = maxLatitude;
		this.cellSize = -1;
		this.horizontalCells = -1;
		this.verticalCells = -1;
	}
	
	public void splitToCells(double cellSize) {
		ValidationHelper.largerThanZero(cellSize, "cellSize");
		this.cellSize = cellSize;
		
		this.horizontalCells = (int)(Math.ceil((this.maxLongitude - this.minLongitude) / cellSize));
		this.verticalCells = (int)(Math.ceil((this.maxLatitude - this.minLatitude) / cellSize));
	}
	
	public long getCell(double longitude, double latitude) {
		ValidationHelper.largerThanZero(this.cellSize, "cellSize");
		this.shouldBeInPlane(longitude, latitude);
		
		// cell is numbered from left to right, bottom to top starting from 0
		return (long)((latitude - this.minLatitude) / this.cellSize) * this.horizontalCells 
				+ (long)((longitude - this.minLongitude) / this.cellSize) ;
	}
	
	@Override
	public String toString() {
		return String.format(
				"minLongitude %f maxLongitude %f minLatitude %f maxLatitude %f cellSize %f horizontalCells %d verticalCells %d",
				this.minLongitude,
				this.maxLongitude,
				this.minLatitude,
				this.maxLatitude,
				this.cellSize,
				this.horizontalCells,
				this.verticalCells);
	}
	
	public void update(double longitude, double latitude) {
		this.updateLongitude(longitude);
		this.updateLatitude(latitude);
	}
	
	public void updateLongitude(double longitude) {
		// Adding extra to reduce complexity to handle points on the boundary
		if (this.minLongitude > longitude) {
			this.minLongitude = longitude - Plane.SmallestGrain;
		}
		
		if (this.maxLongitude < longitude) {
			this.maxLongitude = longitude + Plane.SmallestGrain;
		}
	}
	
	public void updateLatitude(double latitude) {
		// Adding extra to reduce complexity to handle points on the boundary
		if (this.minLatitude > latitude) {
			this.minLatitude = latitude - Plane.SmallestGrain;
		}
		
		if (this.maxLatitude < latitude) {
			this.maxLatitude = latitude + Plane.SmallestGrain;
		}
	}
	
	private void shouldBeInPlane(double longitude, double latitude) {
		if (longitude < this.minLongitude 
				|| longitude > this.maxLongitude 
				|| latitude < this.minLatitude
				|| latitude > this.maxLatitude) {
			throw new IllegalStateException(String.format("longitude %f latitude %f out of boundary", longitude, latitude));
		}
	}
}
