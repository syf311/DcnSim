package sim.vn.data;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.common.ExceptionHelper;
import sim.common.ValidationHelper;
import sim.dcn.simulator.Simulator;
import sim.vn.entity.Beam;
import sim.vn.entity.Plane;
import sim.vn.entity.Vehicle;
import sim.vn.simulator.RawDataProcessorSettings;

public class RawDataProcessor {
	private boolean preprocessed = false;
	private String rootFullPath;
	
	private List<String> dates;
	private HashSet<Vehicle> vehicles;
	
	private Plane plane;
	
	public RawDataProcessor(String rootFullPath) {
		ValidationHelper.notNullOrEmpty(rootFullPath, "rootFullPath");
		this.rootFullPath = rootFullPath;
		this.dates = new ArrayList<String>();
		this.vehicles = new HashSet<Vehicle>();
		this.preprocess();
	}
	
	public static void main(String[] args) throws Exception {
		try {
			long startMs = System.currentTimeMillis();
			RawDataProcessorSettings settings = RawDataProcessorSettings.getSettingsFromArgs(args);
			RawDataProcessor rawDataProcessor = new RawDataProcessor(settings.getRootFullPath());
			Plane plane = rawDataProcessor.getPlane();
			plane.splitToCells(settings.getCellSize());
			
			MovingProbabilityCalculator.calculateProbabilityAndOutput(
					settings.getWindowSizeUnitInSeconds(),
					settings.getwindowSizeInUnits(),
					settings.getOutputRootFullPath(), 
					settings.getFrom(), 
					settings.getTo(), 
					plane, 
					rawDataProcessor.getVehicles(), 
					rawDataProcessor);
			
			long endMs = System.currentTimeMillis();
			Logger.getLogger(Simulator.class.getName()).log(Level.INFO, String.format("Total run time:%d seconds", (endMs - startMs) / 1000));
		}
		catch (Exception ex) {
			Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, ExceptionHelper.toString(ex));
		}
	}
	
	public String getRootFullPath(){
		return this.rootFullPath;
	}
	
	public List<Vehicle> getVehicles() {
		return new ArrayList<Vehicle>(this.vehicles);
	}
	
	public Plane getPlane() throws IOException, ParseException {
		if (this.plane == null) {
			ExploreBoundaryProcessor exploreBondaryProcessor = new ExploreBoundaryProcessor(); 
			for (String date : this.dates) {
				for (Vehicle vehicle : this.vehicles) {
					String rawDataFileFullPath = this.getRawDataFileFullPath(date, vehicle);
					this.processOneVehicle(rawDataFileFullPath, exploreBondaryProcessor);
				}
			}
			
			this.plane = exploreBondaryProcessor.getPlane();
		}
		
		return plane;
	}
	
	public List<Beam> getBeams(Vehicle vehicle, Calendar from, Calendar to) throws IOException, ParseException {
		ValidationHelper.notNull(vehicle, "vehicle");
		ValidationHelper.notNull(from, "from");
		ValidationHelper.notNull(to, "to");
		BeamProcessor beamProcessor = new BeamProcessor(from, to);
		
		String fromDateString = String.format("%02d%02d%02d", from.get(Calendar.YEAR) - 2000, from.get(Calendar.MONTH) + 1, from.get(Calendar.DAY_OF_MONTH));
		String toDateString = String.format("%02d%02d%02d", to.get(Calendar.YEAR) - 2000, to.get(Calendar.MONTH) + 1, to.get(Calendar.DAY_OF_MONTH));
		for (String date : this.dates) {
			if (date.compareTo(fromDateString) >= 0 && date.compareTo(toDateString) <= 0 ) {
				String rawDataFileFullPath = this.getRawDataFileFullPath(date, vehicle);
				this.processOneVehicle(rawDataFileFullPath, beamProcessor);
			}
		}
		
		return beamProcessor.getBeams();
	}
	
	public String[] getDatesSortedAsc() {
		String[] dates = (String[])this.dates.toArray();
		Arrays.sort(dates);
		return dates;
	}
	
	private void preprocess() {
		if (!this.preprocessed) {
			File rootFolder = new File(this.rootFullPath);
			File[] subFolderFullPaths = rootFolder.listFiles();
			ValidationHelper.notNullOrEmpty(subFolderFullPaths, "subFolderFullPaths");
			
			for (File subFolderFullPath : subFolderFullPaths) {
				this.preprocessOneDay(subFolderFullPath.getAbsolutePath());
			}
			
			this.preprocessed = true;
		}
	}
	
	private void preprocessOneDay(String oneDayRootFullPath) {
		File subFolder = new File(oneDayRootFullPath);
		File[] rawDataFileFullPaths = subFolder.listFiles();
		ValidationHelper.notNullOrEmpty(rawDataFileFullPaths, "rawDataFileFullPaths");
		
		this.dates.add(subFolder.getName());
		for (File rawDataFileFullPath : rawDataFileFullPaths) {
			this.preprocessOneVehicle(rawDataFileFullPath.getAbsolutePath());
		}
	}
	
	private void preprocessOneVehicle(String rawDataFileFullPath) {
		Logger.getLogger(RawDataProcessor.class.getName()).log(
				Level.INFO, 
				String.format("Preprocessing one vehicle: %s", rawDataFileFullPath));
		
		File rawDataFile = new File(rawDataFileFullPath);
		String vehicleIdString = rawDataFile.getName();
		if (!vehicleIdString.startsWith("t")) {
			throw new IllegalStateException(String.format("Unexpected format of raw data file name %s", vehicleIdString));
		} else {
			int vehcileId = Integer.parseInt(vehicleIdString.substring(1, vehicleIdString.indexOf('.')));
			Vehicle vehcile = new Vehicle(vehcileId);
			this.vehicles.add(vehcile);
		}
	}
	
	private void processOneVehicle(String rawDataFileFullPath, ILineProcessor lineProcessor) throws IOException, ParseException {
		Logger.getLogger(RawDataProcessor.class.getName()).log(
				Level.INFO, 
				String.format("Processing one vehicle: %s using %s", rawDataFileFullPath, lineProcessor.getClass().getName()));
		
		File rawDataFile = new File(rawDataFileFullPath);
		
		if (rawDataFile.exists()) {
			FileInputStream fstream = null;
			BufferedReader br = null;
			try
			{
				fstream = new FileInputStream(rawDataFileFullPath);
				br = new BufferedReader(new InputStreamReader(fstream));
				String theLine;
				boolean firstLine = true;
				
				while ((theLine = br.readLine()) != null) {
					if (firstLine) {
						firstLine = false;
					} else {
						lineProcessor.processALine(theLine);
					}
				}
			}
			finally
			{
				br.close();
				fstream.close();
			}
		}
	}

	private String getRawDataFileFullPath(String date, Vehicle vehicle) {
		File rootFolder = new File(this.rootFullPath);
		File subFolder = new File(rootFolder, date);
		File rawDataFile = new File(subFolder, vehicle.toFolderName());
		return rawDataFile.getAbsolutePath(); 
	}
}
