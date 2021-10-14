package myAgents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.Behaviour;
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

public class SendBehaviourOld extends Behaviour {

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
    private int countSend    = 0;
    private int countReceive = 0;
    public Writer writer;

    public SendBehaviourOld(Agent a, Object[] args) {
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

        if(myAgent.getLocalName().equals("S0")){
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
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
                writer.write("RTT\n");
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        myAgent.doWait(10000);
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
            String content = randomString(this.messageSize) + this.countSend;
            msg.setContent(content);

            // set a template with receiver and performative
            MessageTemplate mp = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            MessageTemplate ms = MessageTemplate.MatchSender(receiver);
            MessageTemplate mt = MessageTemplate.and(ms, mp);
            if(this.henrique == false){
                MessageTemplate mc = MessageTemplate.MatchContent(content);
                mt = MessageTemplate.and(mt, mc);
            }
    
            long start = System.currentTimeMillis();

            if (this.countSend < this.numberOfMessages*this.nReceivers){
                myAgent.send(msg);
                System.out.println("Send Agent " + myAgent.getLocalName() + "Message: " + this.countSend);
            
                this.countSend += 1;
                this.actualNode = this.actualNode.nextNode;
            }

        block();
        ACLMessage reply = myAgent.receive(mt);
            
        if (reply != null) {
            System.out.println("Receiver Agent: "+myAgent.getLocalName() + " Response:" + this.countReceive);
            long end = System.currentTimeMillis();    
            long result = end-start;
            if(myAgent.getLocalName().equals("S0")){
                try {
                    writer.write(result+"\n");
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.countReceive+=1;
        }

        if(this.countReceive >= this.numberOfMessages*this.nReceivers){
            if(myAgent.getLocalName().equals("S0")){
                try {
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Fim do Experimento: " + myAgent.getLocalName());
            myAgent.doDelete();
        }
    }

    public boolean done(){
        System.out.println(myAgent.getLocalName()+ " CountSend" + this.countSend);
        return (this.countSend >= this.numberOfMessages*this.nReceivers);
    }
}