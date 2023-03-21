import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class Lab1SuccessorFunction implements SuccessorFunction {
    
    public List getSuccessors(Object s){
        List retVal = new ArrayList();
        Estado estado  = (Estado) s;
        Lab1HeuristicFunction1 Lab1HF  = new Lab1HeuristicFunction1();

        // 1. Swap eventos dentro de un mismo conductor
        // Mirar que no sea el mismo elemento en las dos posiciones que cambiamos
        for (int i=0; i<estado.M; i++){
            for (int j=1; j<estado.getEventos().get(i).size(); j++){ //el primero siempre debe ser el conductor (no se puede cambiar) --> empezamos j en 1
                for (int k=1; k<estado.getEventos().get(i).size(); k++){
                    if (j!=k) {
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());
                        estadoNuevo.cambiarOrden(i, j, k);
                        if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(i)) && estadoNuevo.twoPassengers(estadoNuevo.getEventos().get(i))){
                            double h = Lab1HF.getHeuristicValue(estadoNuevo);
                            String S = "heuristic:" + h;
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
                for (int k=0; k<estado.M; k++){ //conductores donde puedo ponerlo
                    if (!set.contains(j) && j!=k) { 
                        set.add(j);
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());
                        estadoNuevo.cambiarConductor(j, i, k);
                        if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(i)) && estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(k)) && estadoNuevo.twoPassengers(estadoNuevo.getEventos().get(i)) && estadoNuevo.twoPassengers(estadoNuevo.getEventos().get(k))){
                            double h = Lab1HF.getHeuristicValue(estadoNuevo);
                            String S = "heuristic:" + h;
                            retVal.add(new Successor(S, estadoNuevo));
                        }

                    }
                }
            }
        }

        // 3. Si un conductor solo se lleva a él mismo, ponerlo a otro conductor y eliminarlo
        for (int i=0; i<estado.M; i++){
            if (estado.getEventos().get(i).size() == 1) { //solo se conduce a él mismo
                for (int j=0; j<estado.M; j++){ //conductores donde puedo ponerlo
                    if (i!=j) { 
                        Estado estadoNuevo = new Estado(estado.getUsuarios(), estado.getEventos());
                        estadoNuevo.eliminarConductor(i, j);
                        if (estadoNuevo.kilometrajeValido(estadoNuevo.getEventos().get(j)) && estadoNuevo.twoPassengers(estadoNuevo.getEventos().get(j))){
                            double h = Lab1HF.getHeuristicValue(estadoNuevo);
                            String S = "heuristic:" + h;
                            retVal.add(new Successor(S, estadoNuevo));
                        }

                    }
                }
            }
        }

        return retVal;
    }
}


