package sim.vn.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import sim.common.ValidationHelper;
import sim.vn.entity.Beam;
import sim.vn.entity.Plane;
import sim.vn.entity.Vehicle;

public class MovingProbabilityCalculator {
	private MovingProbabilityCalculator() {
		
	}
	
	public static void calculateProbabilityAndOutput(
			int windowSizeUnitInSeconds,
			int windowSizeInUnits,
			String rootFolder, 
			Calendar from,
			Calendar to,
			Plane plane,
			List<Vehicle> vehicles,
			RawDataProcessor rawDataProcessor) throws IOException, ParseException {
		ValidationHelper.notNull(plane, "plane");
		ValidationHelper.notNullOrEmpty(vehicles, "vehicles");
		ValidationHelper.notNull(rawDataProcessor, "rawDataProcessor");
		
		File root = new File(rootFolder);
		if (!root.exists()) {
			root.mkdir();
		} else if (!ValidationHelper.isNullOrEmpty(root.listFiles())) {
			throw new IllegalStateException(String.format("%s not empty", rootFolder));
		}
		
		for (Vehicle vehicle : vehicles) {
			MovingProbabilityCalculator.calculateProbabilityAndOutput(
					windowSizeUnitInSeconds, 
					windowSizeInUnits,
					rootFolder,
					from,
					to,
					plane,
					vehicle,
					rawDataProcessor);
		}
	}
	
	private static void calculateProbabilityAndOutput(
			int windowSizeUnitInSeconds,
			int windowSizeInUnits,
			String rootFolder, 
			Calendar from,
			Calendar to,
			Plane plane,
			Vehicle vehicle,
			RawDataProcessor rawDataProcessor) throws IOException, ParseException {
		List<Beam> beams = rawDataProcessor.getBeams(vehicle, from, to);
		
		// we will do data preprocessing to delete those vehicles that have partial data (missing days across all the time range)
		ValidationHelper.notNullOrEmpty(beams, "beams");
		
		long theSecondTo = (long)(to.getTimeInMillis() - from.getTimeInMillis()) / 1000 - windowSizeInUnits * windowSizeUnitInSeconds - windowSizeUnitInSeconds;
		int windowStart = 0;
		
		HashMap<String, HashMap<Long, Integer>> paths = new HashMap<String, HashMap<Long, Integer>>(); 
		
		while (windowStart <= theSecondTo) {
			StringBuffer givenPath = new StringBuffer();
			long nextCell = -1; 
			for (int i = 0; i <= windowSizeInUnits; i++) {
				int theSecond = windowStart + i * windowSizeUnitInSeconds;
				Beam beam = DataProcessingHelper.getBeam(beams, theSecond);
				long cell = plane.getCell(beam.getLongitude(), beam.getLatitude());
				if (i < windowSizeInUnits - 1) {
					givenPath.append(String.format("%d,", cell));
				} else if (i == (windowSizeInUnits - 1)) {
					givenPath.append(String.format("%d", cell));
				} else {
					nextCell = cell;
				}
			}
			
			String givenPathString = givenPath.toString();
			if (paths.containsKey(givenPathString)) {
				HashMap<Long, Integer> nextHopStats = paths.get(givenPathString);
				if (nextHopStats.containsKey(nextCell)) {
					nextHopStats.put(nextCell, nextHopStats.get(nextCell) + 1);
				} else {
					nextHopStats.put(nextCell, 1);
				}
			} else {
				HashMap<Long, Integer> nextHopStats = new HashMap<Long, Integer>();
				nextHopStats.put(nextCell, 1);
				paths.put(givenPathString, nextHopStats);
			}
			
			windowStart += windowSizeUnitInSeconds;
		}

		FileWriter fw = null;
		BufferedWriter bw = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		File output = new File(
				rootFolder, 
				String.format("%d_%d_%d_%s_%s.txt", 
						vehicle.getId(), 
						windowSizeUnitInSeconds,
						windowSizeInUnits, 
						formatter.format(from.getTime()),
						formatter.format(to.getTime())));
		try {
			fw = new FileWriter(output, true);
			bw = new BufferedWriter(fw);

		    Iterator<Entry<String, HashMap<Long, Integer>>> itPaths = paths.entrySet().iterator();
		    while (itPaths.hasNext()) {
		    	Entry<String, HashMap<Long, Integer>> pairs = (Entry<String, HashMap<Long, Integer>>)itPaths.next();
		        String givenPathString = pairs.getKey();
		        Iterator<Entry<Long, Integer>> itNextCells = pairs.getValue().entrySet().iterator();
		        
		        int sum = 0;
		        while (itNextCells.hasNext()) {
		        	Entry<Long, Integer> nextCellStats = (Entry<Long, Integer>)itNextCells.next();
		        	sum += nextCellStats.getValue();
		        }
		        
		        itNextCells = pairs.getValue().entrySet().iterator();
		        
		        while (itNextCells.hasNext()) {
		        	Entry<Long, Integer> nextCellStats = (Entry<Long, Integer>)itNextCells.next();
		        	int timesThisCell = nextCellStats.getValue();
		        	
					bw.write(String.format("%s->%d %f", givenPathString, nextCellStats.getKey(), timesThisCell/(double)sum));
					bw.newLine();
		        }
		    }	
		} finally {
			bw.close();
			fw.close();
		}
	}
}
