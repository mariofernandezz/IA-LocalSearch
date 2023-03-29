import java.util.ArrayList;

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
    ArrayList<ArrayList<Integer>> eventos = estado.getEventos();
    int sum = 0;
    int ncond = 0;
    for (int i=0; i<estado.getM(); i++){
      int dist = estado.kilometrajeConductor(eventos.get(i));
      sum += dist;
      if (estado.getEventos().get(i).size() > 0) ncond += 1;
    }
    return sum;
  }  
}

