package myAgents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.sql.Timestamp;

public class ReceiverAgent extends Agent {

	private static final long serialVersionUID = 1L;

	protected void setup() {
		String host = getArguments()[0].toString();
		String port = getArguments()[1].toString();
		String myAddress = "http://"+host+":"+port+"/acc";
		getAID().addAddresses(myAddress);
		getAID().removeAddresses(getAID().getAddressesArray()[0]);
		System.out.println("All my info: \n" + getAID());

		addBehaviour(new CyclicBehaviour(this){
			
			private static final long serialVersionUID = 1L;

			public void action() {
				MessageTemplate mp = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage msg = myAgent.receive(mp);
				if(msg != null) {
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println("Me:" + myAgent.getLocalName() +" Sender: " + msg.getSender() + " Received:" + msg.getContent() );
					reply.setContent(msg.getContent());
					reply.setConversationId(msg.getConversationId());
					myAgent.send(reply);
				} 
				else block();
			}
		});
	}
}
