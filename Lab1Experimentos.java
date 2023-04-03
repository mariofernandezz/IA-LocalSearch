import java.util.Scanner;

import java.util.Iterator;
import java.util.Properties;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Lab1Experimentos {
    public static void main(String[] args){

        int N, M, seed, alg, h;

        if (args.length != 5) {
            System.out.println("Debes ingresar 5 valores separados por espacios");
            return;
        }
        
        N = Integer.parseInt(args[0]);
        M = Integer.parseInt(args[1]);
        seed = Integer.parseInt(args[2]);
        alg = Integer.parseInt(args[3]);
        h = Integer.parseInt(args[4]);

        // Creacion y print del estado inicial
        System.out.println("\nSolución inicial  -->");
        Estado estado = new Estado(N, M, seed);
        estado.solucionInicial5b(seed);
        mostrarMetricas(estado);
        
        long startTime = System.currentTimeMillis();
        if (alg==1) Lab1HillClimbingSearch(estado, h); // Se realiza la busqueda local usando Hill Climbing
        else Lab1SimulatedAnnealingSearch(estado, h); // Se realiza la busqueda local usando Simulated Annealing
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Tiempo de ejecución: " + elapsedTime + " milisegundos");
    }


        // Muestra por pantalla la lista de eventos, la distancia recorrida y el número de usuarios que hacen de conductor del (estado)
        private static void mostrarMetricas(Estado estado){
            int ncond = estado.numeroConductores();
            int dist = estado.kilometrajeEstado();
            System.out.println("Eventos: " + estado.getEventos());
            System.out.println("Distancia recorrida: " + dist);
            System.out.println("Conductores usados: " + ncond);
        }


        // Búsqueda utilizando el algoritmo de Hill-Climbing
        private static void Lab1HillClimbingSearch(Estado estado, int h) {
            System.out.println("\nLab1 HillClimbing  -->");
            try {
                Problem problem;
                if (h==1) problem = new Problem(estado,new Lab1SuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction1());
                else problem = new Problem(estado,new Lab1SuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction2());
                Search search =  new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(problem,search);
                
                Estado estadoSolucion = (Estado) search.getGoalState();
                mostrarMetricas(estadoSolucion);
                printInstrumentation(agent.getInstrumentation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        

        // Búsqueda utilizando el algoritmo de Simulated Annealing
        private static void Lab1SimulatedAnnealingSearch(Estado estado, int h) {
            System.out.println("\nLab1 Simulated Annealing  -->");
            try {
                // Definición de los parámetros del algoritmo
                int it = 10000000;
                int itpc = 100;
                int k = 10;
                double lambda = 0.001;

                Problem problem;
                if (h==1) problem = new Problem(estado,new Lab1SAIterationsSuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction1());
                else problem = new Problem(estado,new Lab1SAIterationsSuccessorFunction(), new Lab1GoalTest(),new Lab1HeuristicFunction2());
                SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(it, itpc, k, lambda); 
                //search.traceOn();
                SearchAgent agent = new SearchAgent(problem,search);
                
                Estado estadoSolucion = (Estado) search.getGoalState();
                mostrarMetricas(estadoSolucion);
                System.out.println("It: " + it + ". It por cambio de temperatura: " + itpc + ". k: " + k + ". lambda: " + lambda);
                printInstrumentation(agent.getInstrumentation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //*********************************************************************************************
        //******************************* FUNCIONES AUXILIARES ****************************************
        //*********************************************************************************************

        private static void printInstrumentation(Properties properties) {
            Iterator keys = properties.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String property = properties.getProperty(key);
                System.out.println(key + " : " + property);
            }
            
        }
    
}
