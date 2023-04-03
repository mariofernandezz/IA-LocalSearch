// DEFINICIÓN DE LA FUNCIÓN HEURISTICA 1

import aima.search.framework.HeuristicFunction;

public class Lab1HeuristicFunction1 implements HeuristicFunction  {

  public boolean equals(Object obj) {
    boolean retValue;

    retValue = super.equals(obj);
    return retValue;
  }
  
  // Heurística 1: Se considera únicamente la suma de distancia recorrida por todos los conductores
  public double getHeuristicValue(Object s) {
      Estado estado = (Estado)s;
      return estado.kilometrajeEstado();
  }  
}

