
package agent.net;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

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

	public List<Base> parseBases(String response) {
		List<Base> bases = new ArrayList();
		Scanner scan = new Scanner(response);
		
		while(scan.hasNextLine()) {
			String line = scan.nextLine();

			if(!line.contains("begin") && !line.contains("end")) {
				bases.add(new Base(line.split(" ")));
			}
		}
		
		return bases;
	}
}

