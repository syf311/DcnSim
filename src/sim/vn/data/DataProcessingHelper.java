package sim.vn.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sim.vn.entity.Beam;

public class DataProcessingHelper {
	private DataProcessingHelper() {
		
	}
	
	public static Beam getBeam(List<Beam> beams, long theSecond) {
		int indexOfFirstOneGreaterThanOrEqualTo = Collections.binarySearch(
				beams, 
				Beam.getBeamForSearch(theSecond), 
				new Comparator<Beam>() {
					@Override
					public int compare(Beam arg0, Beam arg1) {
						long diff = arg0.getTheSecond() - arg1.getTheSecond();
						if (diff > 0) {
							return 1;
						} else if (diff < 0) {
							return -1;
						} else {
							return 0;
						}
					}
		        });
		
		Beam theBeam = null;
		
		// try to find first one greater than this guy
		if (indexOfFirstOneGreaterThanOrEqualTo < 0) {
			indexOfFirstOneGreaterThanOrEqualTo = ~indexOfFirstOneGreaterThanOrEqualTo;
		}
		
		// if all smaller than this guy, then use the last element
		if (indexOfFirstOneGreaterThanOrEqualTo == beams.size()) {
			indexOfFirstOneGreaterThanOrEqualTo -= 1;
		}

		theBeam = beams.get(indexOfFirstOneGreaterThanOrEqualTo);
		return theBeam;
	}
}
