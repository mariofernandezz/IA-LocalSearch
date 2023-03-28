// GENERADOR DE SOLUCIÓN ALEATORIA PARA SIMULATED ANNEALING (con cambio de operador si tras 'iteraciones_max' iteraciones no se puede generar sucesor)

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Hay muchos + posibles sucesores de swap y cambiar pasajero de conductor -> individualmente, es + probable que se escoja uno de remove o add condutor
// Escoger operador + 50 intentos de crear sucesor + escoger nuevo operador->
// Escoger operador + 1 intento de crear sucesor+ escoger nuevo operador ->

public class Lab1SAIterationsSuccessorFunction implements SuccessorFunction {
    
    private int generateRandom(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    public List getSuccessors(Object s){
        List retVal = new ArrayList();
        Estado estado  = (Estado) s;
        Lab1HeuristicFunction1 Lab1HF  = new Lab1HeuristicFunction1();
        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());

        int iteraciones_max = (int) estado.getM() /2; // # de iteraciones max que hace un operador para encontrar un sucesor hasta que se genera otro operador

        // Escogemos de forma aleatoria el operador a usar para crear el sucesor
        Random random = new Random();
        int Operador = 0;
        while (true) {
            Operador = generateRandom(random, 1, 4);
            if (Operador == 1) {
                // 1. Swap eventos dentro de un mismo conductor

                int Conductor;
                int evento1;
                int evento2;
                int i = 0;
                while (i < iteraciones_max) {
                    // Escoger un conductor aleatorio
                    Conductor = generateRandom(random, 0, estado.getM() - 1);
                    if (estado.getEventos().get(Conductor).size() > 3){ // Comprobar que lleva más de un pasajero
                        // Escoger dos posiciones aleatorias
                        evento1 = generateRandom(random, 1, estado.getEventos().get(Conductor).size() - 1);
                        evento2 = generateRandom(random, 1, estado.getEventos().get(Conductor).size() - 1);
                        if (estado.getEventos().get(Conductor).get(evento1) != estado.getEventos().get(Conductor).get(evento2)) { // Eventos diferentes
                            estadoNuevo.cambiarOrden(Conductor, evento1, evento2);
                            if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(Conductor)) && estadoNuevo.dosPasajeros(estadoNuevo.getEventos().get(Conductor))){
                                double h = Lab1HF.getHeuristicValue(estadoNuevo);
                                String S = "heuristic:" + h;
                                retVal.add(new Successor(S, estadoNuevo));
                                return retVal;
                            }
                        }
                    }
                    i++;
                }
            } else if (Operador == 2) {
                // 2. Cambiar pasajero de conductor
                
                int Conductor1;
                int evento;
                int Conductor2;
                int i = 0;
                while (i < iteraciones_max) {
                    // Escoger un conductor aleatorio
                    Conductor1 = generateRandom(random, 0, estado.getM() - 1);
                    if (estado.getEventos().get(Conductor1).size() > 1){ // Comprobar que lleva almenos un pasajero
                        // Escoger un pasajero aleatorio
                        evento = generateRandom(random, 1, estado.getEventos().get(Conductor1).size() - 1);
                        int Pasajero = estado.getEventos().get(Conductor1).get(evento);
                        // Escoger otro conductor aleatorio
                        Conductor2 = generateRandom(random, 0, estado.getM() - 1);
                        if (Conductor1 != Conductor2) { // Conductores diferentes
                            estadoNuevo.cambiarConductor(Pasajero, Conductor1, Conductor2);
                            if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(Conductor2))){
                                double h = Lab1HF.getHeuristicValue(estadoNuevo);
                                String S = "heuristic:" + h;
                                retVal.add(new Successor(S, estadoNuevo));
                                return retVal;
                            }
                        }
                    }
                    i++;
                }
            } else if (Operador == 3) {
                // 3. Si un conductor solo se lleva a él mismo, ponerlo a otro conductor y eliminarlo
                
                int Conductor1;
                int Conductor2;
                int i = 0;
                while (i < iteraciones_max) {
                    // Escoger un conductor aleatorio
                    Conductor1 = generateRandom(random, 0, estado.getM() - 1);
                    if (estado.getEventos().get(Conductor1).size() == 1){ // Comprobar que no tiene pasajeros, solo él mismo
                        // Escoger otro conductor aleatorio al que ponerle de pasajero
                        Conductor2 = generateRandom(random, 0, estado.getM() - 1);
                        if (Conductor1 != Conductor2) { // Conductores diferentes
                            estadoNuevo.eliminarConductor(Conductor1, Conductor2);
                            if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(Conductor2))){
                                double h = Lab1HF.getHeuristicValue(estadoNuevo);
                                String S = "heuristic:" + h;
                                retVal.add(new Successor(S, estadoNuevo));
                                return retVal;
                            }
                        }
                    }
                    i++;
                }
            } else {
                // 4. Añadir un pasajero que puede conducir como conductor
                
                int NuevoConductor;
                int i = 0;
                while (i < iteraciones_max) {
                    // Escoger un conductor aleatorio
                    NuevoConductor = generateRandom(random, 0, estado.getM() - 1);
                    if (estado.getEventos().get(NuevoConductor).size() == 0){ // Comprobar que no conduce
                        int ConductorDelNuevoConductor = estado.obtenerConductor(NuevoConductor);
                        estadoNuevo.eliminarPasajero(NuevoConductor, ConductorDelNuevoConductor);
                        estadoNuevo.anadirConductor(NuevoConductor);
                        double h = Lab1HF.getHeuristicValue(estadoNuevo);
                        String S = "heuristic:" + h;
                        retVal.add(new Successor(S, estadoNuevo));
                        return retVal;
                    }
                    i++;
                }
            }
        }
    }
}


