package myAgents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ReceiveBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = 1L;
    
    // Informações do Experimento
    private int benchmark         = 1;
    private int numberOfHosts     = 1;
    private int numberOfSenders   = 1;
    private int numberOfReceivers = 1;
    private int numberOfMessages  = 0;
    private int messageSize       = 1;
    private boolean henrique      = false;

    private int nReceivers   = 0;
    private int messageIndex = 0;
    private int countSB      = 0;
    private int maxcountSB   = 0;
    private int countRB      = 0;

    public Writer writer;

    public ReceiveBehaviour(Agent a, Object[] args) {
        super(a);
        this.benchmark         = (int)args[0];
        this.numberOfHosts     = (int)args[1];
        this.numberOfSenders   = (int)args[2];
        this.numberOfReceivers = (int)args[3];
        this.numberOfMessages  = (int)args[4];
        this.messageSize       = (int)args[5];
        this.nReceivers        = (int)args[6];
        this.henrique          = (Boolean)args[8];

        if(myAgent.getLocalName().equals("S0")){
            try {
                this.writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(
                    "results/"+
                    "Benchmark"+this.benchmark+"/"+
                    myAgent.getLocalName()+
                    "_"+this.benchmark+
                    "_"+this.numberOfHosts+
                    "_"+this.numberOfSenders+
                    "_"+this.numberOfReceivers+
                    "_"+this.messageSize+
                    "_"+this.numberOfMessages+
                    "_"+this.henrique+
                    "_"+System.currentTimeMillis()+
                    ".csv"))); 
                this.writer.write("RTT\n");
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void action(){
        MessageTemplate mc = MessageTemplate.MatchConversationId(myAgent.getLocalName());
        MessageTemplate mp = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mt = MessageTemplate.and(mp, mc);

        ACLMessage msg = myAgent.receive(mt);
        if(msg != null) {
            long end = System.currentTimeMillis(); 
            String[] mysplit = msg.getContent().split("-", 2);
            long start = Long.parseLong(mysplit[0]);
            long result = end-start;
            if(myAgent.getLocalName().equals("S0")){
                try {
                    this.writer.write(result+"\n");
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.countRB +=1;
            System.out.println("CountRB: " + this.countRB + 
                               "Agent:" + myAgent.getLocalName() + 
                               "Content: " + msg.getContent()+
                               "Received from: " + msg.getSender());

            if(this.countRB == this.numberOfMessages*this.nReceivers/2){
                System.out.println("Metade do Experimento: " + myAgent.getLocalName());
            }
            if(this.countRB >= this.numberOfMessages*this.nReceivers){
                System.out.println("Fim do Experimento: " + myAgent.getLocalName());
                if(myAgent.getLocalName().equals("S0")){
                    try {
                        this.writer.close();
                    } 
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //myAgent.doDelete();
            }
        }
        else{
            block();
        }
    }
}