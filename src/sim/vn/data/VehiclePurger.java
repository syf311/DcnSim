package sim.vn.data;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.common.ExceptionHelper;
import sim.vn.entity.Vehicle;
import sim.vn.simulator.VehiclePurgeSettings;

public class VehiclePurger {

	private VehiclePurger() {
	}
	
	public static void purgeVehicles(RawDataProcessor rawDataProcessor){
		Logger.getLogger(VehiclePurger.class.getName()).log(
				Level.INFO, 
				"Start purging vehicles");
		
		String[] dates = rawDataProcessor.getDatesSortedAsc();
		List<Vehicle> allVehicles = rawDataProcessor.getVehicles();
		HashSet<Vehicle> vehiclesPurged = new HashSet<Vehicle>();

		for(int i = 0; i < allVehicles.size(); i++) {
			for(int j = 0; j < dates.length; j++) {
				File root = new File(rawDataProcessor.getRootFullPath(), dates[j]);
				File vehicleFile = new File(root.getAbsolutePath(), allVehicles.get(i).toFolderName());
				if(!vehicleFile.exists()){
					vehiclesPurged.add(allVehicles.get(i));
				}
			}
		}
		
		for(Vehicle v : vehiclesPurged) {
			for(int j = 0; j < dates.length; j++) {
				File root = new File(rawDataProcessor.getRootFullPath(), dates[j]);
				File vehicleFile = new File(root.getAbsolutePath(), v.toFolderName());
				if(vehicleFile.exists()){
					Logger.getLogger(VehiclePurger.class.getName()).log(
							Level.INFO, 
							String.format("Purging %s", vehicleFile.getAbsolutePath()));
					vehicleFile.delete();
				}
			}
		}
		
		VehiclePurger.validateVehiclesPurged(rawDataProcessor, vehiclesPurged);
	}
	
	private static void validateVehiclesPurged(RawDataProcessor rawDataProcessor, HashSet<Vehicle> vehiclesPurged){
		String[] dates = rawDataProcessor.getDatesSortedAsc();
		for(Vehicle v : vehiclesPurged) {
			for(int j = 0; j < dates.length; j++) {
				File root = new File(rawDataProcessor.getRootFullPath(), dates[j]);
				File vehicleFile = new File(root.getAbsolutePath(), v.toFolderName());
				if(vehicleFile.exists()){
					throw new IllegalStateException(String.format("%s was not deleted", vehicleFile.getAbsolutePath()));
				}
			}
		}	
		
		Logger.getLogger(VehiclePurger.class.getName()).log(
				Level.INFO, 
				"Vadilation on purging done with no issue found");
	}

	public static void main(String[] args) {
		try {
			VehiclePurgeSettings settings = VehiclePurgeSettings.getSettingsFromArgs(args);
			RawDataProcessor rawDataProcessor = new RawDataProcessor(settings.getRootFullPath());
			VehiclePurger.purgeVehicles(rawDataProcessor);
		} catch (Exception ex) {
			Logger.getLogger(VehiclePurger.class.getName()).log(Level.SEVERE, ExceptionHelper.toString(ex));
		}
	}
}
