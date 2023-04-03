import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class Lab1SuccessorFunction implements SuccessorFunction {
    
    public List getSuccessors(Object s){
        List retVal = new ArrayList();
        Estado estado  = (Estado) s;

        // 1. Swap eventos dentro de un mismo conductor
        for (int i=0; i<estado.M; i++){
            for (int j=1; j<estado.getEventos().get(i).size(); j++){ //el primero siempre debe ser el conductor (no se puede cambiar) --> empezamos j en 1
                for (int k=1; k<j; k++){
                    if (estado.getEventos().get(i).get(j) != estado.getEventos().get(i).get(k)) {
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());
                        estadoNuevo.cambiarOrden(i, j, k);
                        if (estadoNuevo.kilometrajeValido(i) && estadoNuevo.dosPasajeros(estadoNuevo.getEventos().get(i))){
                            String S = estadoNuevo.INTERCAMBIAR_ORDEN + " del conductor " + i + " posiciones " + j + " <--> " + k + " estado: " + estadoNuevo.conversionString();
                            retVal.add(new Successor(S, estadoNuevo));
                        }

                    }
                }
            }
        }

        // 2. Cambiar pasajero de conductor
        for (int i=0; i<estado.M; i++){
            HashSet<Integer> set = new HashSet<>(); //aparecen dos veces cada indice, solo hacemos nuevo sucesor para uno de ellos
            for (int j=1; j<estado.getEventos().get(i).size(); j++){ //el primero siempre debe ser el conductor (no se puede cambiar) --> empezamos j en 1
                int p = estado.getEventos().get(i).get(j);
                if (!set.contains(p)){ //únicamente si es el primer indice
                    set.add(p);
                    
                    /*
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

                    for (int k=0; k<estado.M; k++){ //conductores donde puedo ponerlo
                        if (estado.getEventos().get(k).size()>0 && i!=k) { 
                            Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());
                            estadoNuevo.cambiarConductor(p, i, k);
                            if (estadoNuevo.kilometrajeValido(k)){
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
            if (estado.getEventos().get(i).size() == 1) { //solo se conduce a él mismo
                for (int j=0; j<estado.M; j++){ //conductores donde puedo ponerlo
                    if (estado.getEventos().get(j).size()>0 && i!=j) { 
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos(), estado.getDistancias());                    
                        estadoNuevo.eliminarConductor(i, j);
                        if (estadoNuevo.kilometrajeValido(j)){
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


