import jade.core.Agent;
import jade.core.AID;
import java.util.Iterator;

public class HelloAgent extends Agent {
	protected void setup() {
		System.out.println("Hello World");
		System.out.println("All my info: \n" + getAID());
		System.out.println("My local name is " + getAID().getLocalName());
		System.out.println("My global name is " + getAID().getName());
		System.out.println("My adresses are: ");
		Iterator it = getAID().getAllAddresses();
		while(it.hasNext()){
			System.out.println("- " + it.next());
		}
	}
}
