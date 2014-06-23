package sim.dcn.entity;

import java.util.List;

import sim.common.*;

public abstract class NetworkComponent {
	
	protected int id;
	
	protected List<Link> links;
	
	abstract public String getType();
	
	public NetworkComponent (int id, List<Link> links)
	{
		this.id = id;
		
		ValidationHelper.notNullOrEmpty(links, "links");
		
		this.links = links;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s %s", this.getType(), this.id);
	}
	
	public int getId() {
		return this.id;
	}
	
	public List<Link> getLinks() {
		return this.links;
	}
	
	public void AddLink(Link link) {
		this.links.add(link);
	}
}
