import java.util.Scanner;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Lab1Demo {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("Cuantos participantes hay? (N)");
        int N = scan.nextInt();
        System.out.println("Cuantos son conductores? (M)");
        int M = scan.nextInt();
        System.out.println("Introduce una seed");
        int seed = scan.nextInt();

        Estado estado = new Estado(N, M, seed);
        estado.solucionInicial5();
        System.out.println(estado.conversionString());
        Lab1HillClimbingSearch(estado);
        //Lab1SimulatedAnnealingSearch(estado);
    }
        
        private static void Lab1HillClimbingSearch(Estado estado) {
            System.out.println("\nLab1 HillClimbing  -->");
            try {
                Problem problem =  new Problem(estado,new Lab1SuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction1());
                Search search =  new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(problem,search);
                
                System.out.println();
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
                System.out.println(agent.getActions().get(agent.getActions().size()-1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private static void Lab1SimulatedAnnealingSearch(Estado estado) {
            System.out.println("\nLab1 Simulated Annealing  -->");
            try {
                Problem problem =  new Problem(estado,new Lab1SuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction1());
                SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(2000,100,5,0.001);
                //search.traceOn();
                SearchAgent agent = new SearchAgent(problem,search);
                
                System.out.println();
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private static void printInstrumentation(Properties properties) {
            Iterator keys = properties.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String property = properties.getProperty(key);
                System.out.println(key + " : " + property);
            }
            
        }
        
        private static void printActions(List actions) {
            for (int i = 0; i < actions.size(); i++) {
                String action = (String) actions.get(i);
                System.out.println(action);
            }
        }
    
}
