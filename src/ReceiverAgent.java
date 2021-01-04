package myAgents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.sql.Timestamp;

public class ReceiverAgent extends Agent {

	private static final long serialVersionUID = 1L;

	protected void setup() {
		// String host = getProperty("host", "0");
		// String port = getProperty("port", "0");
		String host = getArguments()[0].toString();
		String port = getArguments()[1].toString();
		String myAddress = "http://"+host+":"+port+"/acc";
		getAID().addAddresses(myAddress);
		getAID().removeAddresses(getAID().getAddressesArray()[0]);
		System.out.println("All my info: \n" + getAID());

		addBehaviour(new CyclicBehaviour(this){
			
			private static final long serialVersionUID = 1L;

			public void action() {
				ACLMessage msg = myAgent.receive();
				if(msg != null) {
					ACLMessage reply = msg.createReply();
					reply.setContent(msg.getContent());
					myAgent.send(reply);
				} 
				else block();
			}
		});
	}
}
