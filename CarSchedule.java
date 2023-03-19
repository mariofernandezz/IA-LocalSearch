// IMPLEMENTACIÓN DEL ESTADO

import java.util.ArrayList;
import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

public class CarSchedule {

    /* ATRIBUTOS */

    private Usuarios users;
    private ArrayList<ArrayList<Integer>> schedule;
    int M;
    int N;

    // Nose si esto se lee de la terminal al ejecutar el codigo
    // y luego se crea Usuarios o lo que se leen son los Usuarios

    /*public int[] getSchedule(){
        return schedule;
    }*/

    /* CONSTRUCTOR */

    public CarSchedule(int N, int M, int seed){
        users = new Usuarios(N, M, seed);
        schedule = new ArrayList<>();
    }

    public void solucionInicial1() {

    }

    //public void solucionInicial2() {}

    /* CONDICIONES DE APLICABILIDAD */

    // Verificar M Conductores
    public boolean notMaxDrivers(){
        boolean retVal = true;
        if (schedule.size() == M){
            retVal = false;
        }
        return retVal;
    }

    private int distance(Usuario A, boolean Aorig, Usuario B, boolean Borig){
        if (Aorig){
            int Ax = A.getCoordOrigenX();
            int Ay = A.getCoordOrigenY();
        } else{
            int Ax = A.getCoordDestinoX();
            int Ay = A.getCoordDestinoY();
        }

        if (Borig){
            int Bx = B.getCoordOrigenX();
            int By = B.getCoordOrigenY();
        } else{
            int Bx = B.getCoordDestinoX();
            int By = B.getCoordDestinoY();
        }

        return Math.abs(Ax - Bx) + Math.abs(Ay - By);
    }

    // Verificar Kilometraje (max 30km = 300 manzanas)
    public boolean notMaxKilometers(ArrayList C){
        boolean retVal = true;
        int dist = 0;
        boolean[] Visited = new boolean[N];
        for(int i = 0; i < C.size(); i++){

        }
        return retVal;
    }

    // Verificar 2 conductores
    public boolean twoPassengers(ArrayList C){
        boolean retVal = true;

        return retVal;
    }
}