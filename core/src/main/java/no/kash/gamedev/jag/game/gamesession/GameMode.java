package no.kash.gamedev.jag.game.gamesession;

public enum GameMode {
	STANDARD_FFA("Free for all", false), STANDARD_TEAM("Team", true), SMASH_FFA("Smash FFA", false), SMASH_TEAM("Smash Team", true);

	public String displayName;
	public boolean teamBased = false;

	private GameMode(String displayName, boolean teamBased) {
		this.displayName = displayName;
		this.teamBased = teamBased;
	}
}
