package sim.vn.data;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import sim.vn.entity.Beam;
import sim.vn.entity.Plane;
import sim.vn.entity.Vehicle;

public class PositionsCalculator {

	private PositionsCalculator() {
		
	};
	
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
		
		List<Beam> beams = rawDataProcessor.getBeams(vehicle, from, to);
		
		
	}
}
