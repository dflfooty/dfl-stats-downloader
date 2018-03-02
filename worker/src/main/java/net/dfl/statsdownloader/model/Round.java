package net.dfl.statsdownloader.model;

import java.util.List;

public class Round {

	private List<Fixture> games;

	public List<Fixture> getGames() {
		return games;
	}

	public void setGames(List<Fixture> games) {
		this.games = games;
	}
	
	public void addGame(Fixture fixture) {		
		this.games.add(fixture);
	}

	@Override
	public String toString() {
		return "Round [games=" + games + "]";
	}
}
