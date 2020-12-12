package myAgents;

import jade.core.Agent;
import jade.core.AID;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;

public class SenderAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private CircularLinkedList cll = new CircularLinkedList();

	protected void setup() {
		String myAddress = "http://"+System.getenv("HOST_IP")+":8080/acc";
		getAID().addAddresses(myAddress);
		getAID().removeAddresses(getAID().getAddressesArray()[0]);
		System.out.println("Hello World");
		System.out.println("All my info: \n" + getAID());
		
		CircularLinkedList cll = new CircularLinkedList();
		Object[] args = getArguments();
		int messageSize = 1;
		int messageNumber = 0;

		try {
			File myObj = new File("parameters"+args[0]+".txt");
			Scanner myReader = new Scanner(myObj);
			String[] data = myReader.nextLine().split(" ");
			messageSize =  Integer.parseInt(data[1]);
			data = myReader.nextLine().split(" ");
			messageNumber = Integer.parseInt(data[1]);
			while (myReader.hasNextLine()) {
			  	data = myReader.nextLine().split(" ");
				cll.addNode(data[0], data[1]);
			}
			myReader.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		Node n = cll.getHead();
		for(int i = 0; i < cll.lenght(); i++){
			n = n.nextNode;
		}

		Object[] behavArgs = {messageNumber, messageSize, cll.lenght(), cll.getHead()};
		addBehaviour(new SendBehaviour(this, behavArgs));		
	}
}
