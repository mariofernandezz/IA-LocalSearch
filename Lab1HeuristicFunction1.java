import aima.search.framework.HeuristicFunction;
import java.util.ArrayList;

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
        for (int i = 0; i< estado.getM(); i++){
          sum += estado.kilometrajeConductor(eventos.get(i));
        }
        return (sum);
    }
}

