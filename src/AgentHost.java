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

    public static ContainerController createPlatform(String platform_id){ 
        // Retrieve the singleton instance of the JADE Runtime
        Runtime rt = Runtime.instance();
        // Create Main Container to host the agents
        Profile p = new ProfileImpl();
        p.setParameter(Profile.PLATFORM_ID, platform_id);
        return rt.createMainContainer(p);
    }

    public static List<AgentController> startAgents(
            ContainerController cc, 
            int benchmark,
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
                            agents.add(cc.createNewAgent("R"+i, "myAgents.ReceiverAgent", arguments));
                        } 
                    break;
                    case 1:
                        if(benchmark == 3){
                            String ip = arguments[0].toString();
                            String index = Integer.toString(Integer.parseInt(ip.substring(ip.length()-2))-1);
                            agents.add(cc.createNewAgent("S"+index, "myAgents.SenderAgent", arguments));
                        }
                        else{
                            for (int i = 0; i < numberOfAgents; i++){ 
                                agents.add(cc.createNewAgent("S"+i, "myAgents.SenderAgent", arguments));
                            }
                        }
                    break;
                    case 2:
                        for (int i = 0; i < numberOfAgents; i++){ 
                            agents.add(cc.createNewAgent("R"+i, "myAgents.ReceiverAgent", arguments));
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

        String ip            = System.getenv("HOST_IP");
        String port          = System.getenv("HOST_PORT");
        int index            = Integer.parseInt(ip.substring(ip.length()-2));
        int benchmark        = Integer.parseInt(args[0]);
        int numberOfAgents   = Integer.parseInt(args[1]);
        
        int agentType = 0;
        if(benchmark > 1 && benchmark < 5){
            if(index > 10){
                agentType = 1;
            }
            else{
                agentType = 2;
            }
        }
        
        if(benchmark < 3 || (benchmark == 3 && agentType == SENDERS)){
            numberOfAgents = 1;
        }

        String[] arguments = {ip, port, args[0], Integer.toString(agentType), args[1] , args[2] , args[3]};
        ContainerController cc = createPlatform("Platform"+ip.substring(ip.length()-1));
        startAgents(cc, benchmark, agentType, numberOfAgents, arguments);
    }
}