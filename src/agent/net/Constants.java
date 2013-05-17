package agent.net;

public class Constants {
	
	String team,alive,dead;
	double worldsize,tankangvel,tanklength,tankradius,tankspeed,linearaccel,
		angularaccel, tankwidth,shotraidus,shotrange,shotspeed,flagradius,explodetime,
		truepositive,truenegative;

	public Constants(String[][] response){
		team = response[1][2];
		alive = response[7][2];
		dead = response[8][2];
		worldsize = Double.parseDouble(response[2][2]);
		tankangvel = Double.parseDouble(response[3][2]);
		tanklength = Double.parseDouble(response[4][2]);
		tankradius = Double.parseDouble(response[5][2]);
		tankspeed = Double.parseDouble(response[6][2]);
		linearaccel = Double.parseDouble(response[9][2]);
		angularaccel = Double.parseDouble(response[10][2]);
		tankwidth = Double.parseDouble(response[11][2]);
		shotraidus = Double.parseDouble(response[12][2]);
		shotspeed = Double.parseDouble(response[13][2]);
		flagradius = Double.parseDouble(response[14][2]);
		explodetime = Double.parseDouble(response[15][2]);
		truepositive = Double.parseDouble(response[16][2]);
		truenegative = Double.parseDouble(response[17][2]);
	}

	public String getTeam() {
		return team;
	}

	public String getAlive() {
		return alive;
	}

	public String getDead() {
		return dead;
	}

	public double getWorldsize() {
		return worldsize;
	}

	public double getTankangvel() {
		return tankangvel;
	}

	public double getTanklength() {
		return tanklength;
	}

	public double getTankradius() {
		return tankradius;
	}

	public double getTankspeed() {
		return tankspeed;
	}

	public double getLinearaccel() {
		return linearaccel;
	}

	public double getAngularaccel() {
		return angularaccel;
	}

	public double getTankwidth() {
		return tankwidth;
	}

	public double getShotraidus() {
		return shotraidus;
	}

	public double getShotrange() {
		return shotrange;
	}

	public double getShotspeed() {
		return shotspeed;
	}

	public double getFlagradius() {
		return flagradius;
	}

	public double getExplodetime() {
		return explodetime;
	}

	public double getTruepositive() {
		return truepositive;
	}

	public double getTruenegative() {
		return truenegative;
	}
	
}
