
package agent;

import agent.net.AgentClientSocket;
import agent.net.ResponseParser;
import agent.net.Base;
import java.util.List;

public class Agent {
	
	public static void main(String[] args) {
		AgentClientSocket sock = new AgentClientSocket(args[0],Integer.parseInt(args[1]));
		ResponseParser parser = new ResponseParser();
		sock.sendIntroduction();

		String response = sock.getResponse();
		System.out.println(response);					
		
		sock.sendBasesQuery();
		response = sock.getResponse();
		System.out.println(response);	
		
		List<Base> bases = parser.parseBases(response);
		System.out.println(bases);
	}
}


