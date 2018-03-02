package net.dfl.statsdownloader.model;

public class PlayerStats {
	
	private String name;
	private String kicks;
	private String handballs;
	private String disposals;
	private String marks;
	private String hitouts;
	private String freesFor;
	private String freesAgainst;
	private String tackles;
	private String goals;
	private String behinds;
	
	public String getName() {
		return name;
	}
	public String getKicks() {
		return kicks;
	}
	public String getHandballs() {
		return handballs;
	}
	public String getDisposals() {
		return disposals;
	}
	public String getMarks() {
		return marks;
	}
	public String getHitouts() {
		return hitouts;
	}
	public String getFreesFor() {
		return freesFor;
	}
	public String getFreesAgainst() {
		return freesAgainst;
	}
	public String getTackles() {
		return tackles;
	}
	public String getGoals() {
		return goals;
	}
	public String getBehinds() {
		return behinds;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setKicks(String kicks) {
		this.kicks = kicks;
	}
	public void setHandballs(String handballs) {
		this.handballs = handballs;
	}
	public void setDisposals(String disposals) {
		this.disposals = disposals;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}
	public void setHitouts(String hitouts) {
		this.hitouts = hitouts;
	}
	public void setFreesFor(String freesFor) {
		this.freesFor = freesFor;
	}
	public void setFreesAgainst(String freesAgainst) {
		this.freesAgainst = freesAgainst;
	}
	public void setTackles(String tackles) {
		this.tackles = tackles;
	}
	public void setGoals(String goals) {
		this.goals = goals;
	}
	public void setBehinds(String behinds) {
		this.behinds = behinds;
	}
	@Override
	public String toString() {
		return "PlayerStats [name=" + name + ", kicks=" + kicks + ", handballs=" + handballs + ", disposals="
				+ disposals + ", marks=" + marks + ", hitouts=" + hitouts + ", freesFor=" + freesFor + ", freesAgainst="
				+ freesAgainst + ", tackles=" + tackles + ", goals=" + goals + ", behinds=" + behinds + "]";
	}
}
