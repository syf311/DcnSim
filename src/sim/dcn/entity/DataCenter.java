package sim.dcn.entity;

import java.util.Collection;
import java.util.List;

public class DataCenter {
	protected List<Server> servers;
	
	protected List<Switch> switches;
	
	protected List<Link> links;
	
	public DataCenter() {
	}
	
	public List<Server> getServers() {
		return this.servers;
	}
	
	public List<Switch> getSwitches() {
		return this.switches;
	}
	
	public List<Link> getLinks() {
		return this.links;
	}
	
	public Server getServer(int id) {
		return DataCenter.getNetworkComponent(this.servers, id, Server.class.getSimpleName());
	}
	
	public Switch getSwitch(int id) {
		return DataCenter.getNetworkComponent(this.switches, id, Switch.class.getSimpleName());
	}
	
	public Link getLink(int id) {
		return DataCenter.getNetworkComponent(this.links, id, Link.class.getSimpleName());
	}
	
	private static <T> T getNetworkComponent(Collection<T> collection, int id, String objectName) {
		for (T object : collection) {
			if (((NetworkComponent)object).getId() == id) {
				return object;
			}
		}
		
		throw new IllegalStateException(String.format("Cannot get any %s with id %d", objectName, id));
	}
}
