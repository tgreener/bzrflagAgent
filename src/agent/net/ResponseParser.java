
package agent.net;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class ResponseParser {
	
	Scanner scan;

	public ResponseParser() {
	}
	
	private boolean checkAcknowledged() {
		return scan.hasNext("ack");
	}

	public boolean checkOk(String response) {
		scan = new Scanner(response);
		if(checkAcknowledged()) {
			scan.nextLine();
		}

		return scan.hasNext("ok");
	}

	public List<Base> parseBases(String response) {
		List<Base> bases = new ArrayList<Base>();
		scan = new Scanner(response);

		if(checkAcknowledged()) {
			scan.nextLine();
		}
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();

			if(!line.contains("begin") && !line.contains("end")) {
				bases.add(new Base(line.split("\\s\\s*")));
			}
		}
		
		return bases;
	}

	public List<Obstacle> parseObstacles(String response) {
		List<Obstacle> obs = new ArrayList<Obstacle>();
		scan = new Scanner(response);

		if(checkAcknowledged()) {
			scan.nextLine();
		}
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();

			if(!line.contains("begin") && !line.contains("end")) {
				obs.add(new Obstacle(line.split("\\s\\s*")));
			}
		}
		
		return obs;
	}

	public List<OtherTank> parseOtherTanks(String response) {
		List<OtherTank> ots = new ArrayList<OtherTank>();
		scan = new Scanner(response);

		if(checkAcknowledged()) {
			scan.nextLine();
		}
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();

			if(!line.contains("begin") && !line.contains("end")) {
				ots.add(new OtherTank(line.split("\\s\\s*")));
			}
		}
		
		return ots;
	}

	public List<Shot> parseShots(String response) {
		List<Shot> shots = new ArrayList<Shot>();
		scan = new Scanner(response);

		if(checkAcknowledged()) {
			scan.nextLine();
		}
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();

			if(!line.contains("begin") && !line.contains("end")) {
				shots.add(new Shot(line.split("\\s\\s*")));
			}
		}
		
		return shots;
	}
	
	public List<Tank> parseMyTanks(String response) {
		List<Tank> tanks = new ArrayList<Tank>();
		scan = new Scanner(response);

		if(checkAcknowledged()) {
			scan.nextLine();
		}
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();

			if(!line.contains("begin") && !line.contains("end")) {
				tanks.add(new Tank(line.split("\\s\\s*")));
			}
		}
		
		return tanks;
	}

	public List<Team> parseTeams(String response) {
		List<Team> teams = new ArrayList<Team>();
		scan = new Scanner(response);

		if(checkAcknowledged()) {
			scan.nextLine();
		}
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();

			if(!line.contains("begin") && !line.contains("end")) {
				teams.add(new Team(line.split("\\s\\s*")));
			}
		}
		
		return teams;
	}
}



