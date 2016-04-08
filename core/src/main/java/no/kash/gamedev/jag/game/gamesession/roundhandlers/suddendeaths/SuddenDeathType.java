package no.kash.gamedev.jag.game.gamesession.roundhandlers.suddendeaths;

public enum SuddenDeathType {
	none("off"), ONE_HP("1 HP"), PISTOLS_ONLY("Pistol only"), GOLDEN_GUN("Golden gun");

	String displayName;

	private SuddenDeathType(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
