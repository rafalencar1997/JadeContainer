import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.sql.Timestamp;

public class ReceiverAgent extends Agent {

	private static final long serialVersionUID = 1L;

	protected void setup() {
		// getAID().addAddresses("http://"+System.getenv("HOST_IP")+":8080/acc");
		// getAID().addAddresses("http://192.168.15.110:8080/acc");
		// getAID().removeAddresses(getAID().getAddressesArray()[0]);
		System.out.println("Hello World");
		System.out.println("All my info: \n" + getAID());

		addBehaviour(new CyclicBehaviour(this){
			
			private static final long serialVersionUID = 1L;

			public void action() {
				ACLMessage msg = myAgent.receive();
				if(msg != null) {
					ACLMessage reply = msg.createReply();
					String content = msg.getContent();
					System.out.println(getAID().getLocalName() + 
									   ") Mensagem "+ content + " enviada por " +
										msg.getSender().getLocalName() +
										" às:" + 
										new Timestamp(msg.getPostTimeStamp()));
					reply.setPerformative(ACLMessage.AGREE);
					reply.setContent("Recebi sua Mensagem");
					myAgent.send(reply);
				} 
				else block();
			}
		});
	}
}
