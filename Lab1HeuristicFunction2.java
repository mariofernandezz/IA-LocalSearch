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
  
    // Heurística 2: Se considera la suma de la distancia recorrida por todos los conductores y el número de conductores de la solución
    public double getHeuristicValue(Object s) {
        Estado estado = (Estado)s;
        return 0.75*estado.kilometrajeEstado()/estado.getDistInicial() + 0.25*estado.numeroConductores()/estado.getM();
    }
  
}