
package agent.net;

import java.util.Scanner;

public class ResponseParser {
	
	public ResponseParser() {
	}
	
	public boolean checkAcknowledged(String response) {
		Scanner scan = new Scanner(response);

		return scan.hasNext("ack");
	}

	public boolean checkOk(String response) {
		Scanner scan = new Scanner(response);
		if(checkAcknowledged(response)) {
			scan.nextLine();
		}

		return scan.hasNext("ok");
	}
}

