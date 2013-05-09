package agent.net;

public class Team {
	
	String color;
	int playerCount;

	public Team(String[] teamResponse){
		this.color = teamResponse[1];
		this.playerCount = Integer.parseInt(teamResponse[2]);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public String toString() {
		String result = "\nTeam: ";
		result += color + ", " + playerCount;

		return result;
	}
}
