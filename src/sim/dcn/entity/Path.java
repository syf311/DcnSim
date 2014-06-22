package sim.dcn.entity;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sim.common.*;

public class Path {
	private List<Link> links;	// links should be in the right order
	
	private NetworkComponent source;
	
	private NetworkComponent destination;
	
	public Path(NetworkComponent source, NetworkComponent destination, List<Link> links)
	{
		ValidationHelper.notNull(source, "source");
		ValidationHelper.notNull(destination, "destination");
		ValidationHelper.notNullOrEmpty(links, "links");
		
		this.source = source;
		this.destination = destination;
		this.links = links;
	}
	
	public NetworkComponent getSource() {
		return this.source;
	}
	
	public NetworkComponent getDestination() {
		return this.destination;
	}
	
	public List<Link> getLinks()
	{
		return this.links;
	}
	
	public void SendReuqest(Request request) {
		Logger.getLogger(Path.class.getName()).log(Level.INFO, String.format("Sending %s over a path", request));
		
		ValidationHelper.notNull(request, "request");
		if (request.getSource() != this.source || request.getDestination() != this.destination) {
			throw new IllegalStateException(
					String.format(
							"request source %s, destination %s don't match path source $s, destination %s", 
							request.getSource(), 
							request.getDestination(), 
							this.source, 
							this.destination));
		}
		
		NetworkComponent currentFrom = this.source;
		for (Link link : this.links) {
			if (link.isEndPoint1(currentFrom)) {
				link.SendRequest1To2(request);
				currentFrom = link.getEndPoint2();
			} 
			else if (link.isEndPoint2(currentFrom)) {
				link.SendRequest2To1(request);
				currentFrom = link.getEndPoint1();
			}
			else {
				throw new IllegalStateException(
						String.format(
								"currentFrom %s does not match %s", 
								currentFrom, 
								link));
			}
		}
	}
}
