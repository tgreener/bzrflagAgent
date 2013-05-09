
package agent;

import agent.net.AgentClientSocket;
import agent.net.ResponseParser;
import agent.net.Base;
import agent.net.Obstacle;
import agent.net.OtherTank;
import agent.net.Shot;
import agent.net.Tank;
import agent.net.Team;
import java.util.List;

public class SocketTestAgent {
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		ResponseParser parser = new ResponseParser();
		sock.sendIntroduction();

		String response = sock.getResponse();
		System.out.println(response);					
		
		sock.sendBasesQuery();
		response = sock.getResponse();
		List<Base> bases = parser.parseBases(response);
		System.out.println(bases);

		sock.sendObstaclesQuery();
		response = sock.getResponse();
		List<Obstacle> obs = parser.parseObstacles(response);
		System.out.println(obs);

		sock.sendOtherTanksQuery();
		response = sock.getResponse();
		List<OtherTank> ots = parser.parseOtherTanks(response);
		System.out.println(ots);

		sock.sendShootCommand(0);
		response = sock.getResponse();
		System.out.println(response);

		sock.sendShotsQuery();
		response = sock.getResponse();
		List<Shot> shots = parser.parseShots(response);
		System.out.println(shots);

		sock.sendMyTanksQuery();
		response = sock.getResponse();
		List<Tank> tanks = parser.parseMyTanks(response);
		System.out.println(tanks);

		sock.sendTeamsQuery();
		response = sock.getResponse();
		List<Team> teams = parser.parseTeams(response);
		System.out.println(teams);	
	}
}


