package sim.vn.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import sim.common.ValidationHelper;
import sim.vn.entity.Beam;
import sim.vn.entity.Plane;
import sim.vn.entity.Vehicle;

public class PositionsCalculator {

	private PositionsCalculator() {
		
	};
	
	public static void discretizeBeams(
			int windowSizeUnitInSeconds,
			String rootFolder, 
			Calendar from,
			Calendar to,
			Plane plane,
			List<Vehicle> vehicles,
			RawDataProcessor rawDataProcessor,
			int unitsPerPartition) throws IOException, ParseException {
		ValidationHelper.notNullOrEmpty(vehicles, "vehicles");
		ValidationHelper.notNull(rawDataProcessor, "rawDataProcessor");
		ValidationHelper.largerThanZero(unitsPerPartition, "unitsPerPartition");
		ValidationHelper.largerThanZero(windowSizeUnitInSeconds, "windowSizeUnitInSeconds");
		
		File root = new File(rootFolder);
		if (!root.exists()) {
			root.mkdir();
		} else if (!ValidationHelper.isNullOrEmpty(root.listFiles())) {
			throw new IllegalStateException(String.format("%s not empty", rootFolder));
		}
		
		for (Vehicle vehicle : vehicles) {
			PositionsCalculator.discretizeBeams(
					windowSizeUnitInSeconds, 
					rootFolder, 
					from, 
					to, 
					plane, 
					vehicle, 
					rawDataProcessor, 
					unitsPerPartition);
		}
	}
	
	private static void discretizeBeams(
			int windowSizeUnitInSeconds,
			String rootFolder, 
			Calendar from,
			Calendar to,
			Plane plane,
			Vehicle vehicle,
			RawDataProcessor rawDataProcessor,
			int unitsPerPartition) throws IOException, ParseException {
		List<Beam> beams = rawDataProcessor.getBeams(vehicle, from, to);
		int startUnit = -unitsPerPartition - 1;
		int currentUnit = 0;
		long theSecondTo = (long)(to.getTimeInMillis() - from.getTimeInMillis()) / 1000;
		long theSecond;
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			while ((theSecond = currentUnit * windowSizeUnitInSeconds) <= theSecondTo) {
				if (currentUnit > startUnit + unitsPerPartition) {
					startUnit = currentUnit;
					
					if (bw != null) {
						bw.close();
						bw = null;
					}
					
					if (fw != null) {
						fw.close();
						fw = null;
					}
					
					File output = new File(
							rootFolder, 
							String.format("%d_%d_%d_%d.beams", 
									vehicle.getId(), 
									windowSizeUnitInSeconds,
									startUnit, 
									startUnit + unitsPerPartition));
					
					fw = new FileWriter(output, true);
					bw = new BufferedWriter(fw);
				}
				
				Beam beam = DataProcessingHelper.getBeam(beams, theSecond);
				
				bw.write(String.format("%d\t%d", currentUnit, plane.getCell(beam.getLongitude(), beam.getLatitude())));
				bw.newLine();
				
				
				currentUnit++;
			}
		} finally {
			if (bw != null) {
				bw.close();
				bw = null;
			}
			
			if (fw != null) {
				fw.close();
				fw = null;
			}
		}
	}
	
	public static void CalculatePositionsAndOutput(
			int windowSizeUnitInSeconds,
			int windowSizeInUnits,
			String rootFolder, 
			Calendar from,
			Calendar to,
			Plane plane,
			Vehicle vehicle,
			RawDataProcessor rawDataProcessor,
			int partitionFactor) throws IOException, ParseException {
	}
}
