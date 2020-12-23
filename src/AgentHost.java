package myAgents;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class AgentHost{ 
    
    public static final int BOTH = 0;
    public static final int SENDERS = 1;
    public static final int RECEIVERS = 2;

    public static ContainerController createPlatform(String host, String port, String platform_id){ 
        // Retrieve the singleton instance of the JADE Runtime
        Runtime rt = Runtime.instance();
        // Create Main Container to host the agents
        Profile p = new ProfileImpl();

        p.setParameter(Profile.MAIN_HOST, host);
        p.setParameter(Profile.MAIN_PORT, port);
        p.setParameter(Profile.PLATFORM_ID, platform_id);
        return rt.createMainContainer(p);
    }

    public static List<AgentController> startAgents(
            ContainerController cc, 
            int agentType, 
            int numberOfAgents,
            Object[] arguments) {
        if (cc != null) {
            // Create the pair of agent and start it
            try {
                List<AgentController> agents = new ArrayList<AgentController>();
                switch (agentType){
                    case 0:
                        for (int i = 0; i < numberOfAgents; i++){
                            agents.add(cc.createNewAgent("S"+i, "myAgents.SenderAgent", arguments));
                            agents.add(cc.createNewAgent("R"+i, "myAgents.ReceiverAgent", null));
                        } 
                    break;
                    case 1:
                        for (int i = 0; i < numberOfAgents; i++){ 
                            agents.add(cc.createNewAgent("S"+i, "myAgents.SenderAgent", arguments));
                        }
                    break;
                    case 2:
                        for (int i = 0; i < numberOfAgents; i++){ 
                            agents.add(cc.createNewAgent("R"+i, "myAgents.ReceiverAgent", null));
                        }
                    break;
                }
                
                for (AgentController agent : agents)
                    agent.start(); 

                return agents;
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {

        String ip            = args[0];
        String port          = args[1];
        int benchmark        = Integer.parseInt(args[2]);
        int agentType        = Integer.parseInt(args[3]);
        int numberOfAgents   = Integer.parseInt(args[4]);
        
        if(benchmark <= 3 && agentType != RECEIVERS)
            numberOfAgents = 1;
        else
            numberOfAgents = Integer.parseInt(args[4]);

        int messageSize      = Integer.parseInt(args[5]); 
        int numberOfMessages = Integer.parseInt(args[6]); 

        ContainerController cc = createPlatform(ip, port, "Platform");
        startAgents(cc, agentType, numberOfAgents, args);
    }
}