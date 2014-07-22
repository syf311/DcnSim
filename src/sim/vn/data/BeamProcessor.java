package sim.vn.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sim.common.ValidationHelper;
import sim.vn.entity.Beam;

public class BeamProcessor implements ILineProcessor {
	private List<Beam> beams;
	
	private Calendar fromFilter;
	
	private Calendar toFilter;
	
	public BeamProcessor(Calendar fromFilter, Calendar toFilter) {
		ValidationHelper.notNull(fromFilter, "fromFilter");
		ValidationHelper.notNull(toFilter, "toFilter");
		this.beams = new ArrayList<Beam>();
		this.fromFilter = fromFilter;
		this.toFilter = toFilter;
	}
	
	public List<Beam> getBeams() {
		return this.beams;
	}
	
	@Override
	public void processALine(String line) throws ParseException {
		String[] columns = line.split(",");
		int vehicleId = LineProcessorHelper.getVehicleId(columns);
		String dateTimeString = LineProcessorHelper.getDateTimeString(columns);
		double longitude = LineProcessorHelper.getLongitude(columns);
		double latitude = LineProcessorHelper.getLatitude(columns);
		int velocity = LineProcessorHelper.getVelocity(columns);
		int direction = LineProcessorHelper.getDirection(columns);
		boolean passengerOnRide = LineProcessorHelper.getPassengerOnRide(columns);
		Date dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeString);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		
		if (cal.compareTo(this.fromFilter) >= 0 && cal.compareTo(this.toFilter) <= 0) {
			int secondOfPeriod = (int)(cal.getTimeInMillis() - this.fromFilter.getTimeInMillis()) / 1000 + 1;
			this.beams.add(new Beam(
					vehicleId, 
					secondOfPeriod,
					longitude,
					latitude,
					velocity,
					direction,
					passengerOnRide,
					cal));
		}
	}
}
