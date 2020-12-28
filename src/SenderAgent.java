package myAgents;

import jade.core.Agent;
import jade.core.AID;
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Hashtable;
import java.util.Scanner;

public class SenderAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private CircularLinkedList cll = new CircularLinkedList();

	protected void setup() {
		String name = getName();
		// String host = getProperty("host", "0");
		// String port = getProperty("port", "0");
		String host = getArguments()[0].toString();
		String port = getArguments()[1].toString();
		String myAddress = "http://"+host+":"+port+"/acc";
		getAID().addAddresses(myAddress);
		getAID().removeAddresses(getAID().getAddressesArray()[0]);
		System.out.println("All my info: \n" + getAID());
		CircularLinkedList cll = new CircularLinkedList();

		int benchmark 	     = Integer.parseInt(getArguments()[2].toString());
        int agentType        = Integer.parseInt(getArguments()[3].toString());
        int numberOfAgents   = Integer.parseInt(getArguments()[4].toString());
		int messageSize      = Integer.parseInt(getArguments()[5].toString());
		int numberOfMessages = Integer.parseInt(getArguments()[6].toString());

		try {
			File myObj = new File("receivers/Benchmark"+benchmark+"/receivers.txt");
			Scanner myReader = new Scanner(myObj);
			String[] data;
			
			for(int i = 0; i < numberOfAgents && myReader.hasNextLine(); i++){
				data = myReader.nextLine().split(" ");
				String index = data[0].substring(1);
				if(index.equals(name.substring(1))){ 
					if(benchmark > 1 || !data[1].equals(myAddress)){
						cll.addNode(data[0], data[1]);
					}
				}
			}
			myReader.close();
		} 
		catch (FileNotFoundException e) {
			System.err.println("An error occurred.");
			e.printStackTrace();
		}

		Node n = cll.getHead();
		for(int i = 0; i < cll.lenght(); i++){
			System.out.println(n.AID + " " + n.Address);
			n = n.nextNode;
		}
		
		Object[] behavArgs = {
			benchmark,
			numberOfAgents,
			numberOfMessages,
			messageSize,
			cll.lenght(), 
			cll.getHead()
		};
		
		// addBehaviour(new SendBehaviour(this, behavArgs));		
	}
}
