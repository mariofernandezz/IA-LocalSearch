// GENERADOR DE SOLUCIÓN ALEATORIA PARA SIMULATED ANNEALING (con cambio de operador si no se puede generar sucesor)

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

// Hay muchos + posibles sucesores de swap y cambiar pasajero de conductor -> individualmente, es + probable que se escoja uno de remove o add condutor
// Escoger operador + 50 intentos de crear sucesor + escoger nuevo operador->
// Escoger operador + 1 intento de crear sucesor+ escoger nuevo operador ->

public class Lab1SASuccessorFunction implements SuccessorFunction {
    
    private int generateRandom(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    public List getSuccessors(Object s){
        List retVal = new ArrayList();
        Estado estado  = (Estado) s;
        Lab1HeuristicFunction1 Lab1HF  = new Lab1HeuristicFunction1();

        // Escogemos de forma aleatoria el operador a usar para crear el sucesor
        Random random = new Random();
        int Operador = 0;
        while (true) {
            Operador = generateRandom(random, 1, 4);
            if (Operador == 1) {
                // 1. Swap eventos dentro de un mismo conductor
                
                // Escoger un conductor aleatorio
                int Conductor = generateRandom(random, 0, estado.M - 1);
                if (estado.getEventos().get(Conductor).size() > 3){ // Comprobar que lleva más de un pasajero
                    // Escoger dos posiciones aleatorias
                    int evento1 = generateRandom(random, 1, estado.getEventos().get(Conductor).size() - 1);
                    int evento2 = generateRandom(random, 1, estado.getEventos().get(Conductor).size() - 1);
                    if (estado.getEventos().get(Conductor).get(evento1) != estado.getEventos().get(Conductor).get(evento2)) { // Eventos diferentes
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());
                        estadoNuevo.distInicial = estado.get_distInicial();
                        estadoNuevo.cambiarOrden(Conductor, evento1, evento2);
                        estadoNuevo.actualizar_distancias(Conductor, null);
                        if (estadoNuevo.kilometrajeValido(Conductor) && estadoNuevo.dosPasajeros(estadoNuevo.getEventos().get(Conductor))){
                            double h = Lab1HF.getHeuristicValue(estadoNuevo);
                            int ncond = estadoNuevo.numeroConductores();
                            int dist = estadoNuevo.kilometrajeSolucion();
                            String S = estadoNuevo.INTERCAMBIAR_ORDEN + " del conductor " + Conductor + " posiciones " + evento1 + " <--> " + evento2+ " con coste: " + h + " ncond: " + ncond + " distancia: " + dist + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                            return retVal;
                        }
                    }
                }
            } else if (Operador == 2) {
                // 2. Cambiar pasajero de conductor
                
                // Escoger un conductor aleatorio
                int Conductor1 = generateRandom(random, 0, estado.M - 1);
                if (estado.getEventos().get(Conductor1).size() > 1){ // Comprobar que lleva almenos un pasajero
                    // Escoger un pasajero aleatorio
                    int evento = generateRandom(random, 1, estado.getEventos().get(Conductor1).size() - 1);
                    int Pasajero = estado.getEventos().get(Conductor1).get(evento);
                    // Escoger otro conductor aleatorio
                    int Conductor2 = generateRandom(random, 0, estado.M - 1);
                    if (Conductor1 != Conductor2 && estado.getEventos().get(Conductor2).size() > 0) { // Conductores diferentes
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());
                        estadoNuevo.distInicial = estado.get_distInicial();
                        estadoNuevo.cambiarConductor(Pasajero, Conductor1, Conductor2);
                        estadoNuevo.actualizar_distancias(Conductor1, Conductor2);
                        if (estadoNuevo.kilometrajeValido(Conductor2)){
                            double h = Lab1HF.getHeuristicValue(estadoNuevo);
                            int ncond = estadoNuevo.numeroConductores();
                            int dist = estadoNuevo.kilometrajeSolucion();
                            String S = estadoNuevo.CAMBIAR_PASAJERO + " " + Pasajero + " --> de conductor " + Conductor1 + " a conductor " + Conductor2 + " con coste: " + h + " ncond: " + ncond + " distancia: " + dist + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                            return retVal;
                        }
                    }
                }
            } else if (Operador == 3) {
                // 3. Si un conductor solo se lleva a él mismo, ponerlo a otro conductor y eliminarlo
    
                // Escoger un conductor aleatorio
                int Conductor1 = generateRandom(random, 0, estado.M - 1);
                if (estado.getEventos().get(Conductor1).size() == 1){ // Comprobar que no tiene pasajeros, solo él mismo
                    // Escoger otro conductor aleatorio al que ponerle de pasajero
                    int Conductor2 = generateRandom(random, 0, estado.M - 1);
                    if (Conductor1 != Conductor2 && estado.getEventos().get(Conductor2).size() > 0) { // Conductores diferentes
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());
                        estadoNuevo.distInicial = estado.get_distInicial();
                        estadoNuevo.eliminarConductor(Conductor1, Conductor2);
                        estadoNuevo.actualizar_distancias(Conductor1, Conductor2);
                        if (estadoNuevo.kilometrajeValido(Conductor2)){
                            double h = Lab1HF.getHeuristicValue(estadoNuevo);
                            int ncond = estadoNuevo.numeroConductores();
                            int dist = estadoNuevo.kilometrajeSolucion();
                            String S = estadoNuevo.ELIMINAR_CONDUCTOR + " " + Conductor1 + " --> a conductor " + Conductor2 + " con coste: " + h + " ncond: " + ncond + " distancia: " + dist + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                            return retVal;
                        }
                    }
                }
            } else {
                // 4. Añadir un pasajero que puede conducir como conductor
    
                // Escoger un conductor aleatorio
                int NuevoConductor = generateRandom(random, 0, estado.M - 1);
                if (estado.getEventos().get(NuevoConductor).size() == 0){ // Comprobar que no conduce
                    int ConductorDelNuevoConductor = estado.obtenerConductor(NuevoConductor);
                    Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());
                    estadoNuevo.distInicial = estado.get_distInicial();
                    estadoNuevo.eliminarPasajero(NuevoConductor, ConductorDelNuevoConductor);
                    estadoNuevo.anadirConductor(NuevoConductor);
                    estadoNuevo.actualizar_distancias(ConductorDelNuevoConductor, NuevoConductor);
                    double h = Lab1HF.getHeuristicValue(estadoNuevo);
                    int ncond = estadoNuevo.numeroConductores();
                    int dist = estadoNuevo.kilometrajeSolucion();
                    String S = estadoNuevo.ANADIR_CONDUCTOR + " " + NuevoConductor + ", eliminandolo de conductor " + ConductorDelNuevoConductor + " con coste: " + h + " ncond: " + ncond + " distancia: " + dist + " estado: " + estadoNuevo.conversionString();
                    retVal.add(new Successor(S, estadoNuevo));
                    return retVal;
                }
            }
        }
    }
}


