package net.dfl.statsdownloader.model;

import java.util.List;

public class RoundStats {
	private List<TeamStats> roundStats;

	public List<TeamStats> getRoundStats() {
		return roundStats;
	}

	public void setRoundStats(List<TeamStats> roundStats) {
		this.roundStats = roundStats;
	}

	@Override
	public String toString() {
		return "RoundStats [roundStats=" + roundStats + "]";
	}
}
