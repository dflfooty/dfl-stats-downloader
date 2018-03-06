package net.dfl.statsdownloader.helpers;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.opencsv.CSVWriter;

import net.dfl.statsdownloader.model.Fixture;
import net.dfl.statsdownloader.model.PlayerStats;
import net.dfl.statsdownloader.model.Round;
import net.dfl.statsdownloader.model.RoundStats;
import net.dfl.statsdownloader.model.TeamStats;

public class StatsHelper {
	
	private String round;
	private String year;
	
	private static final String chromeBin = System.getenv("GOOGLE_CHROME_BIN");
	
	private WebDriver webDriver;
	
	
	
	public StatsHelper(String round, String year) {
		this.round = round;
		this.year = year;
	}
	
	private void setupChrome() {
		ChromeOptions options = new ChromeOptions();
		options.setBinary(chromeBin);
		options.addArguments("--headless", "--disable-gpu", "--no-sandbox --incognito");
		
		webDriver = new ChromeDriver(options);
	}
	
	private void setupPhantomjs() {
		webDriver = new PhantomJSDriver();
	}
		
	public Path execute() throws Exception {
		
		String headlessBrowser = System.getenv("HEADLESS_BROWSER");
		
		if(headlessBrowser.equalsIgnoreCase("phantomjs")) {
			setupPhantomjs();
		} else {
			setupChrome();
		}

		
		
		Round roundFixtures = getRound();
		RoundStats roundStats = createStats(roundFixtures);
		Path csvFile = writeStatsCsv(roundStats);
		
		webDriver.quit();
		
		return csvFile;
	}
	
	private Round getRound() {
		
		String paddedRoundNo = "";
		
		if(round.length() < 2) {
			paddedRoundNo = "0" + round;
		} else {
			paddedRoundNo = round;
		}
		
		String fixtureUrl = "http://www.afl.com.au/fixture?roundId=CD_R" + year + "014" + paddedRoundNo + "#tround";
				
		//logger.info("Loading fixture from: {}", fixtureUrl);
		
		webDriver.get(fixtureUrl);
		
		List<WebElement> webFixtures = webDriver.findElement(By.id("tround")).findElement(By.tagName("tbody")).findElements(By.className("team-logos"));
		
		Round roundFixutres = new Round();
		List<Fixture> games = new ArrayList<Fixture>();
		
		for(WebElement webFixture : webFixtures) {
			Fixture fixture = new Fixture();
			fixture.setHomeTeam(webFixture.findElements(By.className("home")).get(0).getText());
			fixture.setAwayTeam(webFixture.findElements(By.className("away")).get(0).getText());
			
			//if(System.getProperty("app.debug").equals("Y")) {
			//	logger.debug("Fixture: {}", fixture);
			//}
			
			games.add(fixture);
		}
		
		roundFixutres.setGames(games);
		
		//if(System.getProperty("app.debug").equals("Y")) {
		//	logger.debug("Round Games: {}", round);
		//}
		
		//logger.info("Fixtures Loaded");
		
		return roundFixutres;
	}
	
	private RoundStats createStats(Round roundFixutres) {
		//logger.info("Handling Stats Download for: year={}, round={}", year, this.round);
		
		RoundStats roundStats = new RoundStats();
		List<TeamStats> stats = new ArrayList<TeamStats>();
				
		String statsUri = "http://www.afl.com.au/match-centre/" + year + "/" + round + "/";
		
		for(Fixture game : roundFixutres.getGames()) {
			String gameStr = game.getHomeTeam().toLowerCase() + "-v-" + game.getAwayTeam().toLowerCase();
			String statsUrl = statsUri + gameStr;
			
			//logger.info("Stats URL: {}", statsUrl);
			
			List<TeamStats> teamStats = downloadStats(game.getHomeTeam().toLowerCase(), game.getHomeTeam().toLowerCase(), statsUrl);
			stats.addAll(teamStats);
		}
		
		roundStats.setRoundStats(stats);
		//if(System.getProperty("app.debug").equals("Y")) {
		//	logger.debug("Round stats: {}", roundStats);
		//}
		
		//logger.info("Writing CSV");
		//writeRobStatsCsv(roundStats);
		
		//logger.info("Stats Download Handler Completed");
		
		return roundStats;
	}
	

	
	private List<TeamStats> downloadStats(String homeTeam, String awayTeam, String url) {
						
		List<TeamStats> stats = new ArrayList<TeamStats>();
					
		try {				
			webDriver.get(url);
			
			//logger.info("Getting home team stats: {}", homeTeam);
			TeamStats homeTeamStats = new TeamStats();
			homeTeamStats.setTeamId(homeTeam);
			homeTeamStats.setTeamStats(getStats("h", webDriver));
			
			//logger.info("Getting away team stats: {}", awayTeam);
			TeamStats awayTeamStats = new TeamStats();
			awayTeamStats.setTeamId(awayTeam);
			awayTeamStats.setTeamStats(getStats("a", webDriver));
						
			stats.add(homeTeamStats);
			stats.add(awayTeamStats);
			
		} catch (Exception e) {} finally {} //ignore errors

		//logger.info("Stats have been downloaded");
		
		return stats;
	}
	
	private List<PlayerStats> getStats(String homeORaway, WebDriver driver) throws Exception {
		
		List<PlayerStats> teamStats = new ArrayList<PlayerStats>();
		
		driver.findElement(By.cssSelector("a[href='#full-time-stats']")).click();
		driver.findElement(By.cssSelector("a[href='#advanced-stats']")).click();

		List<WebElement> statsRecs;
		
		if(homeORaway.equals("h")) {
			statsRecs = driver.findElement(By.id("homeTeam-advanced")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		} else {
			statsRecs = driver.findElement(By.id("awayTeam-advanced")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		}
		
		for(WebElement statsRec : statsRecs) {
			PlayerStats playerStats = new PlayerStats();
			
			List<WebElement> stats = statsRec.findElements(By.tagName("td"));

			playerStats.setName(stats.get(0).findElements(By.tagName("span")).get(1).getText());
			playerStats.setKicks(stats.get(2).getText());
			playerStats.setHandballs(stats.get(3).getText());
			playerStats.setDisposals(stats.get(4).getText());
			playerStats.setMarks(stats.get(9).getText());
			playerStats.setHitouts(stats.get(12).getText());
			playerStats.setFreesFor(stats.get(17).getText());
			playerStats.setFreesAgainst(stats.get(18).getText());
			playerStats.setTackles(stats.get(19).getText());
			playerStats.setGoals(stats.get(23).getText());
			playerStats.setBehinds(stats.get(24).getText());
			teamStats.add(playerStats);
			
			//if(System.getProperty("app.debug").equals("Y")) {
			//	logger.debug("Player stats: {}", playerStats);
			//}
		}
		
		//if(System.getProperty("app.debug").equals("Y")) {
		//	logger.debug("Team stats: {}", teamStats);
		//}
		
		return teamStats;
	}
	
	private Path writeStatsCsv(RoundStats roundStats) throws Exception {
		Path dir = Paths.get("stats");
		if(!Files.exists(dir)) {
			Files.createDirectories(dir);
		}
		
		String roundPadded = "";
		
		if(this.round.length() < 2) {
			roundPadded = "0" + this.round;
		} else {
			roundPadded = this.round;
		}
		
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String now = df.format(new Date());
		
		Path csvFile = dir.resolve("stats-" + this.year + "-" + roundPadded + "_" + now + ".csv");
		
		//logger.info("CSV File: {}", dir + "/" + csvFile);
		
		Files.createDirectories(dir);
		CSVWriter csvFileWriter = new CSVWriter(Files.newBufferedWriter(csvFile, Charset.forName("Cp1252"), new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.WRITE}));
		
		List<String[]> rows = new ArrayList<String[]>();
		
		//if(System.getProperty("app.debug").equals("Y")) {
		//	logger.debug("Round stats to be written: {}", roundStats);
		//}
		
		for(TeamStats teamStats : roundStats.getRoundStats()) {
			for(PlayerStats playerStats : teamStats.getTeamStats()) {
				
				//if(System.getProperty("app.debug").equals("Y")) {
				//	logger.debug("Player to be written: {}", playerStats);
				//}		
				
				rows.add(new String[]{playerStats.getName(),
									  playerStats.getDisposals(), 
									  playerStats.getMarks(),  
									  playerStats.getHitouts(), 
									  playerStats.getFreesFor(), 
									  playerStats.getFreesAgainst(),
									  playerStats.getTackles(),
									  playerStats.getGoals()});
				
				//if(System.getProperty("app.debug").equals("Y")) {
				//	logger.debug("CSV Row: {}", (Object[])rows.get(rows.size()-1));
				//}
			}
		}
		
		csvFileWriter.writeAll(rows);
		csvFileWriter.flush();
		csvFileWriter.close();
		//logger.info("CSV File written");
		
		return csvFile;
	}
}
