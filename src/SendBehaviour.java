package myAgents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SendBehaviour extends Behaviour {

    private static final long serialVersionUID = 1L;
    
    // Informações do Experimento
    private int benchmark         = 1;
    private int numberOfHosts     = 1;
    private int numberOfSenders   = 1;
    private int numberOfReceivers = 1;
    private int numberOfMessages  = 0;
    private int messageSize       = 1;
    private boolean henrique      = false;

    private Node actualNode  = null;
    private int nReceivers   = 0;
    private int messageIndex = 0;

    public SendBehaviour(Agent a, Object[] args) {
        super(a);
        this.benchmark         = (int)args[0];
        this.numberOfHosts     = (int)args[1];
        this.numberOfSenders   = (int)args[2];
        this.numberOfReceivers = (int)args[3];
        this.numberOfMessages  = (int)args[4];
        this.messageSize       = (int)args[5];
        this.nReceivers        = (int)args[6];
        this.actualNode        = (Node)args[7];
        this.henrique          = (Boolean)args[8];

        myAgent.doWait(5000);
    }

    public static String randomString(int lenght){
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder();
        while(lenght -- != 0){
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public void action(){
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        AID receiver = new AID(actualNode.AID, AID.ISGUID);
        receiver.addAddresses(actualNode.Address);
        msg.addReceiver(receiver);
        long start = System.currentTimeMillis();
        String content =  String.valueOf(start) + "-" + this.messageIndex + "-"+randomString(this.messageSize);
        System.out.println(this.messageIndex+")Receiver: " + receiver.getName() + " Sender:" + myAgent.getName()+ " Content" + content);
        
        msg.setContent(content);
        msg.setConversationId(myAgent.getLocalName());
        myAgent.send(msg);
        this.messageIndex += 1;
        this.actualNode = this.actualNode.nextNode;
    }

    public boolean done(){
        return (this.messageIndex >= this.numberOfMessages*this.nReceivers);
    };
}