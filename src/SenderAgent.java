import jade.core.Agent;
import jade.core.AID;

public class SenderAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private CircularLinkedList cll = new CircularLinkedList();

	protected void setup() {
		// getAID().addAddresses("http://"+System.getenv("HOST_IP")+":8080/acc");
		// getAID().addAddresses("http://192.168.15.110:8080/acc");
		// getAID().removeAddresses(getAID().getAddressesArray()[0]);
		System.out.println("Hello World");
		System.out.println("All my info: \n" + getAID());
		
		// Object[] args = getArguments();

		CircularLinkedList cll = new CircularLinkedList();
		cll.addNode("R1@Platform", "http://user-Vostro-14-5480:7778/acc");
		cll.addNode("R2@Platform", "http://user-Vostro-14-5480:7778/acc");
		cll.addNode("R3@Platform", "http://user-Vostro-14-5480:7778/acc");

		Object[] behavArgs = {5, cll.lenght(), cll.getHead()};
		addBehaviour(new SendBehaviour(this, behavArgs));		
	}
}
