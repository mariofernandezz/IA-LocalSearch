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
        //double ncond = estado.numeroConductores();
        //return 0.75*estado.kilometrajeEstado()/estado.getDistInicial() + 0.25*ncond/estado.getM();
        return 0.75*estado.kilometrajeEstado()/estado.getDistInicial() + 0.25*estado.numeroConductores()/estado.getM();
    }
  
}