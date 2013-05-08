
package agent.net;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;

public class AgentClientSocket {

	Socket agentSocket;
	BufferedReader input;
	PrintStream output;
	

	public AgentClientSocket(String machineName, int port) {
		try {
			agentSocket = new Socket(machineName, port);
			input = new BufferedReader(new InputStreamReader(agentSocket.getInputStream()));
			output = new PrintStream(agentSocket.getOutputStream());
		}
		catch(UnknownHostException e) {
			System.out.println(e);
		}
		catch(IOException e) {
			System.out.println(e);			
		}
	}

	public void close() {
		try {
			input.close();
			output.close();
			agentSocket.close();
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}

	public void sendIntroduction() {
		sendCommand("agent 1");
	}

	public void sendCommand(String command) {
		output.println(command);
	}

	public void sendShootCommand(int agentNumber) {
		sendCommand("shoot " + agentNumber);
	}

	public void sendDriveCommand(int agentNumber, float speed) {
		sendCommand("speed " + agentNumber + " " + speed);
	}

	public void sendRotateCommand(int agentNumber, float rotationSpeed) {
		sendCommand("angvel " + agentNumber + " " + rotationSpeed);
	}

	public void sendStopDrivingCommand(int agentNumber) {
		sendCommand("speed " + agentNumber + " 0.0");
	}

	public void sendStopRotatingCommand(int agentNumber) {
		sendCommand("angvel " + agentNumber + " 0.0");
	}

	public void sendTeamsQuery() {
		sendCommand("teams");
	}

	public void sendObstaclesQuery() {
		sendCommand("obstacles");
	}

	public void sendBasesQuery() {
		sendCommand("bases");
	}

	public void sendFlagsQuery() {
		sendCommand("flags");
	}

	public void sendShotsQuery() {
		sendCommand("shots");
	}
	
	public void sendMyTanksQuery() {
		sendCommand("mytanks");
	}

	public void sendOtherTanksQuery() {
		sendCommand("othertanks");
	}

	public void sendConstantsQuery() {
		sendCommand("constants");
	}

	public void sendOccGridQuery(int tankIndex) {
		sendCommand("occgrid " + tankIndex);
	}

	public boolean confirmCommandResponse() {
		throw new UnsupportedOperationException();
	}

	public String getResponse() {
		String response = "";
		try {
			while(input.ready()) {
				response += input.readLine();
			}
		}
		catch(IOException e) {
			System.out.println(e);
		}
		
		return response;
	}
}


