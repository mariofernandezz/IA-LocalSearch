import IA.Comparticion.Usuario;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Cuantos participantes hay? (N)");
        int N = scan.nextInt();
        System.out.println("Cuantos son conductores? (M)");
        int M = scan.nextInt();
        System.out.println("Introduce una seed");
        int seed = scan.nextInt();

        Estado estado = new Estado(N, M, seed);
 
        //System.out.println(estado.getUsuarios());
        //System.out.println(estado.getUsuarios2());

        // Mostrar lista de usuarios con características
        System.out.println("Usuarios:");
        for(int i = 0; i < estado.getUsuarios().size(); i++) {
            Usuario u1 = estado.getUsuarios().get(i);
            System.out.println("O: (" + u1.getCoordOrigenX() + "," + u1.getCoordOrigenY() + ") D: (" + u1.getCoordDestinoX() + "," + u1.getCoordDestinoY() + ") C: "+ u1.isConductor());
        }
/*
        System.out.println("Planificación inicial: ");
        for(int i = 0; i < estado.getEventos().size(); i++) {
            ArrayList<Integer> e1 = estado.getEventos().get(i);
            System.out.println("Conductor: " + i + " Eventos: " + e1);
        }

        // Añadimos conductores a los eventos
        for(int i = 0; i < estado.getUsuarios().size(); i++) {
            Usuario u1 = estado.getUsuarios().get(i);
            if (u1.isConductor()) estado.anadirConductor(i);
        }

        // Añadimos pasajeros
        estado.anadirPasajero(3,0);
        estado.anadirPasajero(2, 0);
        estado.anadirPasajero(4, 1);
        estado.anadirPasajero(5, 1);

        // Mostrar eventos
        System.out.println("Planificación:");
        for(int i = 0; i < estado.getEventos().size(); i++) {
            ArrayList<Integer> e1 = estado.getEventos().get(i);
            System.out.println("Conductor: " + i + " Eventos: " + e1);
        }

        estado.cambiarConductor(2, null, 1);
        estado.cambiarConductor(4, null, 0);
        System.out.println("Nueva Planificación:");
        for(int i = 0; i < estado.getEventos().size(); i++) {
            ArrayList<Integer> e1 = estado.getEventos().get(i);
            System.out.println("Conductor: " + i + " Eventos: " + e1);
        }

        estado.cambiarOrden(0, 2, 3);
        estado.cambiarOrden(1, 2, 4);
        System.out.println("Nueva Planificación:");
        for(int i = 0; i < estado.getEventos().size(); i++) {
            ArrayList<Integer> e1 = estado.getEventos().get(i);
            System.out.println("Conductor: " + i + " Eventos: " + e1);
        }

        System.out.println(estado.numeroPasajeros(0));
*/

        estado.solucionInicial1();
        //estado.solucionInicial2();

        for(int i = 0; i < estado.getEventos().size(); i++) {
            ArrayList<Integer> e1 = estado.getEventos().get(i);
            System.out.println("Conductor: " + i + " Eventos: " + e1);
        }

        // estado.solucionInicial2();

        //**************** Estado Inicial *********************
        // print del estado inicial

         //Problem problem = new Problem(estado, new SuccessorFunction(), new GoalTest(), new heuristicFunction());
         //Search search = new ...;
        //Search agent = new SearchAgent(problem, search);

        //**************** Estado Final ***********************
        // print del estado final
    }
}
