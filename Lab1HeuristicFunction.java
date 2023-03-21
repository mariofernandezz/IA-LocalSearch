import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import java.util.ArrayList;

import aima.search.framework.HeuristicFunction;

public class Lab1HeuristicFunction implements HeuristicFunction  {

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
    for (int i=0; i<estado.M; i++){
        sum += estado.kilometrajeConductor(eventos.get(i));
    }
    return (sum);
  }

  // Heurística 2: Se considera la suma de la distancia recorrida por todos los conductores y el número de conductores de la solución
  
}

