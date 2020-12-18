package myAgents;

import java.util.ArrayList;
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

    public static ContainerController createPlatform(String host, String port){ 
        // Retrieve the singleton instance of the JADE Runtime
        Runtime rt = Runtime.instance();
        // Create Main Container to host the agents
        Profile p = new ProfileImpl();

        p.setParameter(Profile.MAIN_HOST, host);
        p.setParameter(Profile.MAIN_PORT, port);
        p.setParameter(Profile.MAIN_PORT, port);
        return rt.createMainContainer(p);
    }

    public static List<AgentController> startAgents(ContainerController cc, int agentType, int numberOfAgents) {
        if (cc != null) {
            // Create the pair of agent and start it
            try {
                List<AgentController> agents = new ArrayList<AgentController>();
                // TY
                switch (agentType){
                    //comparing value of variable against each case
                    case 0:
                        for (int i = 0; i < numberOfAgents; i++){
                            agents.add(cc.createNewAgent("S"+i, "myAgents.SenderAgent", null));
                            agents.add(cc.createNewAgent("R"+i, "myAgents.ReceiverAgent", null));
                        } 
                    break;
                    case 1:
                        for (int i = 0; i < numberOfAgents; i++) 
                            agents.add(cc.createNewAgent("S"+i, "myAgents.SenderAgent", null));
                    break;
                    case 2:
                        for (int i = 0; i < numberOfAgents; i++) 
                            agents.add(cc.createNewAgent("R"+i, "myAgents.ReceiverAgent", null));
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
        ContainerController cc = createPlatform("192.168.15.16", "8080");
        startAgents(cc, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}