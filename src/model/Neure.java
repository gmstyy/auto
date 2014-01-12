package model;

import java.util.Map;
import java.util.Set;


public abstract class Neure {
	
	public abstract void stimulated(Neure neure,boolean flag);
	public abstract void feedback(Neure neure);
	public abstract void complete();
	protected Set<Neure> frontSet;
	protected Map<Neure,Double> feedSet;
	
	public Set<Neure> getFrontSet() {
		return frontSet;
	}
	
	public void setFrontSet(Set<Neure> frontSet) {
		this.frontSet = frontSet;
	}
	
	public Map<Neure, Double> getFeedSet() {
		return feedSet;
	}
	
	public void setFeedSet(Map<Neure, Double> feedSet) {
		this.feedSet = feedSet;
	}
	
}
