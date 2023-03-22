// GENERADOR DE SOLUCIÓN ALEATORIA PARA SIMULATED ANNEALING

// !!!!!!!!!!!!!!!!! hasta que punto es necesario el operador de eliminar conductor?
// SI ESCOJO PRIMERO ALEATORIAMENTE EL OPERADOR, HAY SUCESORES CON MAS PROBABILIDAD, AQUELLOS QUE ES ELIMINAR UN CONDUCTOR (LO CUAL PUEDE SER BUENO?)
// AUNQ LO MISMO PASA CON AÑADIR UN CONDUCTOR

// si no creamos arraylist para todos los conductores con el segundo metodo de inicio puede ser que nos de erros al obtener la size de algun 'conductor'

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Random;



import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class Lab1SASuccessorFunction implements SuccessorFunction {
    
    public List getSuccessors(Object s){
        List retVal = new ArrayList();
        Estado estado  = (Estado) s;
        Lab1HeuristicFunction1 Lab1HF  = new Lab1HeuristicFunction1();
        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());

        // Miramos si los operadores de añadir y/o eliminar conductor son viables
        boolean no3 = false;
        boolean no4 = false;
        if (!estado.conductoresSinPasajeros()) no3 = true; // No se puede eliminar ningun conductor
        if (estado.todosConduciendo()) no4 = true; // No se puede añadir un conductor

        // Escogemos de forma aleatoria el operador a usar para crear el sucesor
        Random random = new Random();
        int min = 1; // Valor mínimo del rango
        int max = 4; // Valor máximo del rango
        int Operador = 0;
        boolean valido = false;
        while (!valido) {
            valido = true;
            Operador = random.nextInt(max - min + 1) + min;
            if ((Operador == 3 && no3) || (Operador == 4 && no4)) valido = false;
        }

        if (Operador == 1) {
            // 1. Swap eventos dentro de un mismo conductor

            // Escoger un conductor aleatorio
            min = 0;
            max = estado.M - 1;
            int Conductor = 0;
            valido = false;
            while (!valido) {
                Conductor = random.nextInt(max - min + 1) + min;
                if (estado.getEventos().get(Conductor).size() > 3) valido = true; // Lleva a más de un pasajero
            }

            // Escoger dos posiciones aleatorias
            min = 1;
            max = estado.getEventos().get(Conductor).size() - 1;
            int evento1 = random.nextInt(max - min + 1) + min;
            int evento2;
            while (true) { // el intercambio ha de ser de pasajeros diferentes y cumplir con las condiciones de aplicabilidad del operador
                evento2 = random.nextInt(max - min + 1) + min;
                if (estado.getEventos().get(Conductor).get(evento1) != estado.getEventos().get(Conductor).get(evento2)) {
                    estadoNuevo.cambiarOrden(Conductor, evento1, evento2);
                    if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(Conductor)) && estadoNuevo.twoPassengers(estadoNuevo.getEventos().get(Conductor))){
                        double h = Lab1HF.getHeuristicValue(estadoNuevo);
                        String S = "heuristic:" + h;
                        retVal.add(new Successor(S, estadoNuevo));
                        return retVal;
                    }
                }
            }
        } else if (Operador == 2) {
            // 2. Cambiar pasajero de conductor

            // Escoger un conductor que lleve pasajeros aleatoriamente
            min = 0;
            max = estado.M - 1;
            int Conductor1 = 0;
            valido = false;
            while (!valido) {
                Conductor1 = random.nextInt(max - min + 1) + min;
                if (estado.getEventos().get(Conductor1).size() > 1) valido = true; // Lleva a un pasajero mínimo
            }

            // Escoger un pasajero aleatorio
            min = 1;
            max = estado.getEventos().get(Conductor1).size() - 1;
            int evento = random.nextInt(max - min + 1) + min;
            int Pasajero = estado.getEventos().get(Conductor1).get(evento);

            // Escoger otro conductor aleatorio
            min = 0;
            max = estado.M - 1;
            int Conductor2;
            while (true) { // el movimiento ha de ser a un conductor diferente y cumplir con las condiciones de aplicabilidad del operador
                Conductor2 = random.nextInt(max - min + 1) + min;
                if (Conductor1 != Conductor2) {
                    estadoNuevo.cambiarConductor(Pasajero, Conductor1, Conductor2);
                    if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(Conductor2))){
                        double h = Lab1HF.getHeuristicValue(estadoNuevo);
                        String S = "heuristic:" + h;
                        retVal.add(new Successor(S, estadoNuevo));
                        return retVal;
                    }
                }
            }
        } else if (Operador == 3) {
            // 3. Si un conductor solo se lleva a él mismo, ponerlo a otro conductor y eliminarlo

            // Escoger un conductor que solo se lleve a si mismo aleatoriamente
            min = 0;
            max = estado.M - 1;
            int Conductor1 = 0;
            valido = false;
            while (!valido) {
                Conductor1 = random.nextInt(max - min + 1) + min;
                if (estado.getEventos().get(Conductor1).size() == 1) valido = true; // Es un conductor sin pasajeros
            }

            // Escoger otro conductor aleatorio al que ponerle de pasajero
            int Conductor2;
            while (true) {
                Conductor2 = random.nextInt(max - min + 1) + min;
                if (Conductor1 != Conductor2) { // el movimiento ha de cumplir con las condiciones de aplicabilidad del operador
                    estadoNuevo.eliminarConductor(Conductor1, Conductor2);
                    if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(Conductor2))){
                        double h = Lab1HF.getHeuristicValue(estadoNuevo);
                        String S = "heuristic:" + h;
                        retVal.add(new Successor(S, estadoNuevo));
                        return retVal;
                    }
                }
            }
        } else {
            // 4. Añadir un pasajero que puede conducir como conductor

            // Escoger un pasajero que pueda ejercer de conductor aleatoriamente
            min = 0;
            max = estado.M - 1;
            int NuevoConductor;
            valido = false;
            while (true) {
                NuevoConductor = random.nextInt(max - min + 1) + min;
                if (estado.getEventos().get(NuevoConductor).size() == 0) { // Es un possible conductor que no conduce
                    int ConductorDelNuevoConductor = estado.obtenerConductor(NuevoConductor);
                    estadoNuevo.eliminarPasajero(NuevoConductor, ConductorDelNuevoConductor);
                    estadoNuevo.anadirConductor(NuevoConductor); // REVISAR LO DE QUE SE AÑADE UN ARRAYLIST A OTRO
                    double h = Lab1HF.getHeuristicValue(estadoNuevo);
                    String S = "heuristic:" + h;
                    retVal.add(new Successor(S, estadoNuevo));
                    return retVal;
                }
            }
        }
    }
}


