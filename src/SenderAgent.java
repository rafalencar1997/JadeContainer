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
		String name = getName().split("@")[0];
		String platform = getName().split("@")[1];

		String indexName = name.substring(name.length() - 1);
		String indexPlat = platform.substring(platform.length() - 1);

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

		if(benchmark == 1){
			for(int i = 0; i < numberOfAgents; i++){
				if(!String.valueOf(i).equals(indexPlat)){	
					String receiver_host = String.valueOf(10+i);
					String receiver_address = "http://10.0.1."+receiver_host+":"+port+"/acc";
					String receiver_name = "R"+i+"@Platform"+i;
					cll.addNode(receiver_name, receiver_address);
				}
			}
		}
		else{
			if(benchmark == 2){
				String receiver_name = "R0@Platform0";
				String receiver_address = "http://10.0.1.10:"+port+"/acc";
				cll.addNode(receiver_name, receiver_address);
			}
			else{
				if(benchmark == 3){
					String receiver_name = "R"+indexName+"@Platform0";
					String receiver_address = "http://10.0.1.10:"+port+"/acc";
					cll.addNode(receiver_name, receiver_address);
				}
				else{
					String receiver_name = "R"+indexName+"@Platform0";
					String receiver_address = "http://10.0.1.10:"+port+"/acc";
					cll.addNode(receiver_name, receiver_address);
				}
			}	
		}

		// Node n = cll.getHead();
		// for(int i = 0; i < cll.lenght(); i++){
		// 	System.out.println(n.AID + " " +n.Address);
		// 	n = n.nextNode;
		// }
		
		Object[] behavArgs = {
			benchmark,
			numberOfAgents,
			numberOfMessages,
			messageSize,
			cll.lenght(), 
			cll.getHead()
		};
		
		addBehaviour(new SendBehaviour(this, behavArgs));		
	}
}
