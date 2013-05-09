
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
				bases.add(new Base(line.split(" ")));
			}
		}
		
		return bases;
	}
}

