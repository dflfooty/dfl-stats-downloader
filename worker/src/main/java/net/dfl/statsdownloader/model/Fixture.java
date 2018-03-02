package net.dfl.statsdownloader.model;

public class Fixture {

	private String homeTeam;
	private String awayTeam;
	
	public String getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	public String getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}
	@Override
	public String toString() {
		return "Fixture [homeTeam=" + homeTeam + ", awayTeam=" + awayTeam + "]";
	}
}
