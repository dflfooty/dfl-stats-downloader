package net.dfl.statsdownloader.model;

import java.util.List;

public class TeamStats {

	private String teamId;
	private List<PlayerStats> teamStats;
	
	public String getTeamId() {
		return teamId;
	}
	public List<PlayerStats> getTeamStats() {
		return teamStats;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public void setTeamStats(List<PlayerStats> teamStats) {
		this.teamStats = teamStats;
	}
	@Override
	public String toString() {
		return "TeamStats [teamId=" + teamId + ", teamStats=" + teamStats + "]";
	}
}
