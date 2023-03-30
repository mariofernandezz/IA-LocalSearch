// DEFINICIÓN DE LA FUNCIÓN HEURISTICA 2

// Penalizar manzanas sin pasajeros completos (antes de acabar la trayectoria?)

// Solución Inicial --> Poner a cada conductor pasajeros dentro del rectangulo de su path.

import java.util.ArrayList;

import aima.search.framework.HeuristicFunction;

public class Lab1HeuristicFunction2 implements HeuristicFunction  {

    public boolean equals(Object obj) {
        boolean retValue;

        retValue = super.equals(obj);
        return retValue;
    }
  
    public double getHeuristicValue(Object s) {
        Estado estado = (Estado)s;
        ArrayList<ArrayList<Integer>> eventos = estado.getEventos();
        int sum = 0;
        double ncond = 0;
        for (int i=0; i<estado.getM(); i++){
            if (eventos.get(i).size()>0) {
                ncond+=1;
                sum += estado.kilometrajeConductor(eventos.get(i));
            }
        }
        //System.out.println(sum/(300*ncond) + (ncond/estado.getM()));
        //System.out.println(estado.getDistInicial());
        return (0.75*sum/estado.getDistInicial() + 0.25*ncond/estado.getM());
        //return sum*ncond;
    }
    
    /*
    // Heurística 2: Se considera la suma de la distancia recorrida por todos los conductores y el número de conductores de la solución
    // REDEFINIR QUÉ IMPORTANCIA LE DAMOS A CADA COSA (CONDUCTORES Y KM)!!
    public double getHeuristicValue(Object s) {
        Estado estado = (Estado)s;
        ArrayList<ArrayList<Integer>> eventos = estado.getEventos();
        int sum = 0;
        for (int i=0; i<estado.M; i++){
            if (eventos.get(i).size()>0) {
                sum += estado.kilometrajeConductor_manzanasLibres(eventos.get(i));
            }
        }
        return sum;
    }
    */
  
}