// GENERADOR DE SUCESORES PARA HILL CLIMBING

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class Lab1SuccessorFunction implements SuccessorFunction {
    
    public List getSuccessors(Object s){
        List retVal = new ArrayList(); 
        Estado estado  = (Estado) s;

        // 1. Intercambio de eventos dentro de un mismo conductor
        for (int i=0; i<estado.M; i++){
            for (int j=1; j<estado.getEventos().get(i).size(); j++){ // El primero siempre debe ser el conductor (no se puede cambiar) --> empezamos j en 1
                for (int k=1; k<j; k++){
                    if (estado.getEventos().get(i).get(j) != estado.getEventos().get(i).get(k)) {
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());
                        estadoNuevo.intercambiarEventos(i, j, k);
                        if (estadoNuevo.kilometrajeValido(i) && estadoNuevo.dosPasajeros(estadoNuevo.getEventos().get(i))){ // Cond. aplic.
                            String S = estadoNuevo.INTERCAMBIAR_ORDEN + " del conductor " + i + " posiciones " + j + " <--> " + k + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                        }

                    }
                }
            }
        }

        // 2. Cambiar pasajero de conductor
        for (int i=0; i<estado.M; i++){
            HashSet<Integer> set = new HashSet<>(); // Cada pasajero tiene dos eventos, solo hacemos nuevo sucesor para uno de ellos
            for (int j=1; j<estado.getEventos().get(i).size(); j++){ // El primero siempre debe ser el conductor (no se puede cambiar) --> empezamos j en 1
                int p = estado.getEventos().get(i).get(j);
                if (!set.contains(p)){ // Únicamente si es el primer evento
                    set.add(p);
                    
                    /*
                    // 4. Añadir conductor
                    if (p<estado.M){
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias(), estado.distInicial);
                        estadoNuevo.eliminarPasajero(p, i);
                        estadoNuevo.anadirConductor(p);
                        double h = Lab1HF.getHeuristicValue(estadoNuevo);
                        int dist = estadoNuevo.kilometrajeEstado();
                        int ncond = estadoNuevo.numeroConductores();
                        String S = estadoNuevo.ANADIR_CONDUCTOR + " " + p + ", eliminandolo de conductor " + i + " con coste: " + h + " ncond: " + ncond + " distancia: " + dist + " estado: " + estadoNuevo.conversionString();
                        retVal.add(new Successor(S, estadoNuevo));
                    } 
                    */

                    for (int k=0; k<estado.M; k++){ 
                        if (estado.getEventos().get(k).size()>0 && i!=k) {  // Conductores donde puedo ponerlo
                            Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());
                            estadoNuevo.cambiarConductor(p, i, k);
                            if (estadoNuevo.kilometrajeValido(k)){ // Cond. aplic.
                                String S = estadoNuevo.CAMBIAR_PASAJERO + " " + p + " --> de conductor " + i + " a conductor " + k + " estado: " + estadoNuevo.conversionString();
                                retVal.add(new Successor(S, estadoNuevo));
                            }

                        }
                    }
                }
            }
        }

        // 3. Si un conductor solo se lleva a él mismo, ponerlo a otro conductor y eliminarlo
        for (int i=0; i<estado.M; i++){
            if (estado.getEventos().get(i).size() == 1) { // Solo se conduce a él mismo
                for (int j=0; j<estado.M; j++){ 
                    if (estado.getEventos().get(j).size()>0 && i!=j) { // Conductores donde puedo ponerlo 
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());                    
                        estadoNuevo.eliminarConductor(i, j);
                        if (estadoNuevo.kilometrajeValido(j)){ // Cond. aplic.
                            String S = estadoNuevo.ELIMINAR_CONDUCTOR + " " + i + " --> a conductor " + j + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                        }
                    }
                }
            }
        }

        return retVal;
    }
}


