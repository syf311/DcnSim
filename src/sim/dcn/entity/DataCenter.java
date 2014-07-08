package sim.dcn.entity;

import java.util.ArrayList;
import java.util.List;

import sim.common.ValidationHelper;

public class DataCenter {
	protected List<Server> servers;
	
	protected List<Switch> switches;
	
	protected List<Link> links;
	
	protected List<Tenant> tenants;
	
	public DataCenter() {
	}
	
	public void addTenant(Tenant tenant) {
		ValidationHelper.notNull(tenant, "tenant");
		
		if (this.tenants == null) {
			this.tenants = new ArrayList<Tenant>();
		}
		
		this.tenants.add(tenant);
	}
	
	public List<Tenant> getTenants() {
		return this.tenants;
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
		return Entity.getEntity(this.servers, id, Server.class.getSimpleName());
	}
	
	public Switch getSwitch(int id) {
		return Entity.getEntity(this.switches, id, Switch.class.getSimpleName());
	}
	
	public Link getLink(int id) {
		return Entity.getEntity(this.links, id, Link.class.getSimpleName());
	}
}
