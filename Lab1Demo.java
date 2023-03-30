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

        

        System.out.println("\nSolución inicial  -->");
        Estado estado = new Estado(N, M, seed);
        estado.solucionInicial5();
        mostrarMetricas(estado);
        
        long startTime = System.currentTimeMillis();
        Lab1HillClimbingSearch(estado);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Tiempo de ejecución: " + elapsedTime + " milisegundos");
        
        startTime = System.currentTimeMillis();
        Lab1SimulatedAnnealingSearch(estado);
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("Tiempo de ejecución: " + elapsedTime + " milisegundos");

        startTime = System.currentTimeMillis();
        Lab1SimulatedAnnealingIterationSearch(estado);
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("Tiempo de ejecución: " + elapsedTime + " milisegundos");
    }
        
        private static void mostrarMetricas(Estado estado){
            int ncond = estado.numeroConductores();
            int dist = estado.kilometrajeSolucion();
            System.out.println("Eventos: " + estado.getEventos());
            System.out.println("Distancia recorrida: " + dist);
            System.out.println("Conductores usados: " + ncond);
        }

        private static void Lab1HillClimbingSearch(Estado estado) {
            System.out.println("\nLab1 HillClimbing  -->");
            try {
                Problem problem =  new Problem(estado,new Lab1SuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction2());
                Search search =  new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(problem,search);
                
                //printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
                
                Estado estadoSolucion = (Estado) search.getGoalState();
                mostrarMetricas(estadoSolucion);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private static void Lab1SimulatedAnnealingSearch(Estado estado) {
            System.out.println("\nLab1 Simulated Annealing  -->");
            try {
                Problem problem =  new Problem(estado,new Lab1SASuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction2());
                SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(100000,100,10,0.001);
                //search.traceOn();
                SearchAgent agent = new SearchAgent(problem,search);
                //printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
                
                Estado estadoSolucion = (Estado) search.getGoalState();
                mostrarMetricas(estadoSolucion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static void Lab1SimulatedAnnealingIterationSearch(Estado estado) {
            System.out.println("\nLab1 Simulated Annealing  Iterations-->");
            try {
                Problem problem =  new Problem(estado,new Lab1SAIterationsSuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction2());
                SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(100000,100,10,0.001);
                //search.traceOn();
                SearchAgent agent = new SearchAgent(problem,search);
                //printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());
                
                Estado estadoSolucion = (Estado) search.getGoalState();
                mostrarMetricas(estadoSolucion);
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
