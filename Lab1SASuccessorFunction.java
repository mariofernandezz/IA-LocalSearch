// GENERADOR DE SUCESOR ALEATORIO PARA SIMULATED ANNEALING (con cambio de operador si no se puede generar sucesor)

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


public class Lab1SASuccessorFunction implements SuccessorFunction {
    
    // Función que devuelve un entero aleatorio entre min y max.
    private int generateRandom(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    public List getSuccessors(Object s){
        List retVal = new ArrayList();
        Estado estado  = (Estado) s;

        // Escogemos de forma aleatoria el operador a usar para crear el sucesor
        Random random = new Random();
        int Operador = 0;
        while (true) {
            Operador = generateRandom(random, 1, 4);
            if (Operador == 1) {
                // 1. Intercambio eventos dentro de un mismo conductor
                
                // Escoger un conductor aleatorio
                int Conductor = generateRandom(random, 0, estado.getM() - 1);
                if (estado.getEventos().get(Conductor).size() > 3){ // Comprobar que lleva más de un pasajero
                    // Escoger dos posiciones aleatorias
                    int evento1 = generateRandom(random, 1, estado.getEventos().get(Conductor).size() - 1);
                    int evento2 = generateRandom(random, 1, estado.getEventos().get(Conductor).size() - 1);
                    if (estado.getEventos().get(Conductor).get(evento1) != estado.getEventos().get(Conductor).get(evento2)) { // Eventos diferentes
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());
                        estadoNuevo.intercambiarEventos(Conductor, evento1, evento2);
                        if (estadoNuevo.kilometrajeValido(Conductor) && estadoNuevo.dosPasajeros(estadoNuevo.getEventos().get(Conductor))){ // Cond. aplic.
                            String S = estadoNuevo.INTERCAMBIAR_ORDEN + " del conductor " + Conductor + " posiciones " + evento1 + " <--> " + evento2 + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                            return retVal;
                        }
                    }
                }
            } else if (Operador == 2) {
                // 2. Cambiar pasajero de conductor
                
                // Escoger un conductor aleatorio
                int Conductor1 = generateRandom(random, 0, estado.getM() - 1);
                if (estado.getEventos().get(Conductor1).size() > 1){ // Comprobar que lleva almenos un pasajero
                    // Escoger un pasajero aleatorio
                    int evento = generateRandom(random, 1, estado.getEventos().get(Conductor1).size() - 1);
                    int Pasajero = estado.getEventos().get(Conductor1).get(evento);
                    // Escoger otro conductor aleatorio
                    int Conductor2 = generateRandom(random, 0, estado.getM() - 1);
                    if (Conductor1 != Conductor2) { // Conductores diferentes
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());
                        estadoNuevo.cambiarConductor(Pasajero, Conductor1, Conductor2);
                        if (estadoNuevo.kilometrajeValido(Conductor2)){ // Cond. aplic.
                            String S = estadoNuevo.CAMBIAR_PASAJERO + " " + Pasajero + " --> de conductor " + Conductor1 + " a conductor " + Conductor2 + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                            return retVal;
                        }
                    }
                }
            } else if (Operador == 3) {
                // 3. Si un conductor solo se lleva a él mismo, ponerlo a otro conductor y eliminarlo
    
                // Escoger un conductor aleatorio
                int Conductor1 = generateRandom(random, 0, estado.getM() - 1);
                if (estado.getEventos().get(Conductor1).size() == 1){ // Comprobar que no tiene pasajeros, solo él mismo
                    // Escoger otro conductor aleatorio al que ponerle de pasajero
                    int Conductor2 = generateRandom(random, 0, estado.getM() - 1);
                    if (Conductor1 != Conductor2 && estado.getEventos().get(Conductor2).size() > 0) { // Conductores diferentes
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());
                        estadoNuevo.eliminarConductor(Conductor1, Conductor2);
                        if (estadoNuevo.kilometrajeValido(Conductor2)){ // Cond. aplic.
                            String S = estadoNuevo.ELIMINAR_CONDUCTOR + " " + Conductor1 + " --> a conductor " + Conductor2 + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                            return retVal;
                        }
                    }
                }
            } else {
                // 4. Añadir un pasajero que puede conducir como conductor
                
                /*
                // Escoger un conductor aleatorio
                int NuevoConductor = generateRandom(random, 0, estado.getM() - 1);
                if (estado.getEventos().get(NuevoConductor).size() == 0){ // Comprobar que no conduce
                    int ConductorDelNuevoConductor = estado.obtenerConductor(NuevoConductor);
                    Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());
                    estadoNuevo.eliminarPasajero(NuevoConductor, ConductorDelNuevoConductor);
                    estadoNuevo.anadirConductor(NuevoConductor);
                    String S = estadoNuevo.ANADIR_CONDUCTOR + " " + NuevoConductor + ", eliminandolo de conductor " + ConductorDelNuevoConductor + " estado: " + estadoNuevo.conversionString();
                    retVal.add(new Successor(S, estadoNuevo));
                    return retVal;  
                }
                */
            }
        }
    }
}


