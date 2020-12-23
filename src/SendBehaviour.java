package myAgents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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

public class SendBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = 1L;
    
    // Informações do Experimento
    private int benchmark = 1;
    private int numberOfAgents = 1;
    private int numberOfMessages = 0;
    private int messageSize = 1;

    private Node actualNode = null;
    private int nReceivers = 0;
    private int count = 0;
    public Writer writer;

    public SendBehaviour(Agent a, Object[] args) {
        super(a);
        this.benchmark        = (int)args[0];
        this.numberOfAgents   = (int)args[1];
        this.numberOfMessages = (int)args[2];
        this.messageSize      = (int)args[3];
        this.nReceivers       = (int)args[4];
        this.actualNode       = (Node)args[5];

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(
                "results/"+
                "Benchmark"+this.benchmark+"/"+
                myAgent.getLocalName()+
                "_"+this.numberOfAgents+
                "_"+this.messageSize+
                "_"+this.numberOfMessages+
                ".csv")));
        } 
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        myAgent.doWait(10000);
    }

    public static String randomString(int lenght){
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
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
        msg.setContent(randomString(this.messageSize));

        // set a template with receiver and performative
        MessageTemplate mp = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate ms = MessageTemplate.MatchSender(receiver);
        MessageTemplate mt = MessageTemplate.and(ms, mp);
  
        count += 1;
        long start = System.currentTimeMillis();
        myAgent.send(msg);
        ACLMessage reply = myAgent.receive(mt);
        while(reply == null) 
            reply = myAgent.receive();
        if (reply != null) {
            long end = System.currentTimeMillis();    
            long result = end-start;
            try {
                writer.write(actualNode.Address+","+
                             result+","+
                             this.messageSize+","+
                             this.numberOfMessages+","+
                             this.numberOfAgents+"\n");
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(count >= numberOfMessages*nReceivers){
            try {writer.close();}
            catch (IOException e) {e.printStackTrace();}
            myAgent.doDelete();
            System.out.println("Fim do Experimento");
        }
        this.actualNode = this.actualNode.nextNode;
    }
}