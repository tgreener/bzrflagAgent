package agent.net;

public class Team {
	
	String color;
	int playerCount;

	public Team(String[] teamResponse){
		this.color = teamResponse[1];
		this.playerCount = Integer.parseInt(teamResponse[2]);
	}
}
