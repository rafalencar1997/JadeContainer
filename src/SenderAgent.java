import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;
import java.sql.Timestamp;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SenderAgent extends Agent {

	private static final long serialVersionUID = 1L;
	public Writer writer;
	int count = 0;
	int rcv = 1;

	private class InformBehav extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public InformBehav(Agent a) {
		  super(a);
		  myAgent.doWait(5000);
		}

		public void action(){
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			AID receiver = new AID("R@Platform2", AID.ISGUID);
			receiver.addAddresses("http://192.168.15.110:8080/acc");
			msg.addReceiver(receiver);
			msg.setContent("Msg" + count);
			System.out.println(getAID().getLocalName() + 
								") Mensagem " + count + " enviada");

			// set a template with receiver and performative
			MessageTemplate mp = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			MessageTemplate ms = MessageTemplate.MatchSender(receiver);
			MessageTemplate mt = MessageTemplate.and(ms, mp);
	  
			// number of messagens sent
			count += 1;
	  
			// get the initial time
			long start = System.currentTimeMillis();
	  
			// send message
			send(msg);

			ACLMessage reply = myAgent.receive(mt);
			while(reply == null){
				reply = myAgent.receive();
			}
			if (reply != null) {
				System.out.println(getAID().getLocalName() + 
								") Resposta Recebida por "+
								reply.getSender().getLocalName() + 
								" às " + new Timestamp(reply.getPostTimeStamp()));
				long end = System.currentTimeMillis();    
				long result = end-start;
				try {writer.write(Long.toString(result).concat(" " + getLocalName()+"\n"));} 
				catch (IOException e) {e.printStackTrace();}
			}
			if(count >= 10){
				try {writer.close();}
				catch (IOException e) {e.printStackTrace();}
				doDelete();
			}
		}
	}

	protected void setup() {
		getAID().addAddresses("http://"+System.getenv("HOST_IP")+":8081/acc");
		getAID().removeAddresses(getAID().getAddressesArray()[0]);
		System.out.println("Hello World");
		System.out.println("All my info: \n" + getAID());
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(getLocalName().concat(".txt"))));
		} 
		catch (FileNotFoundException e) {
		  e.printStackTrace();
		}

		addBehaviour(new InformBehav(this));		
	}
}
