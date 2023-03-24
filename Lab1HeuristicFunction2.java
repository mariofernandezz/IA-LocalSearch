import java.util.ArrayList;

import aima.search.framework.HeuristicFunction;

public class Lab1HeuristicFunction2 implements HeuristicFunction  {

    public boolean equals(Object obj) {
        boolean retValue;

        retValue = super.equals(obj);
        return retValue;
    }
  
    // Heurística 2: Se considera la suma de la distancia recorrida por todos los conductores y el número de conductores de la solución
    // REDEFINIR QUÉ IMPORTANCIA LE DAMOS A CADA COSA (CONDUCTORES Y KM)!!
    public double getHeuristicValue(Object s) {
        Estado estado = (Estado)s;
        ArrayList<ArrayList<Integer>> eventos = estado.getEventos();
        int sum = 0;
        int ncond = 0;
        for (int i=0; i<estado.M; i++){
            if (eventos.get(i).size()>0) {
                ncond+=1;
                sum += estado.kilometrajeConductor(eventos.get(i));
            }
        }
        return (sum*ncond);
    }
  
}