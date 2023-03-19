// IMPLEMENTACIÓN DEL ESTADO

import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Math.abs;


public class Estado {

   /* ATRIBUTOS */
    private Usuarios u;
    private ArrayList<Usuario> usuarios;
    private ArrayList<ArrayList<Integer>> eventos;
    int M;
    int N;

    /*CONSTRUCTOR*/
    public Estado(int N, int M, int seed){
        u = new Usuarios(N, M, seed);
        eventos = new ArrayList<>(M);
        usuarios = new ArrayList<>();
        ordenar();
    }

    private void ordenar() {
        for(int i = 0; i < u.size(); ++i) {
            if(u.get(i).isConductor()) usuarios.add(u.get(i));
        }
        for(int i = 0; i < u.size(); ++i) {
            if(!u.get(i).isConductor()) usuarios.add(u.get(i));
        }

    }

    public ArrayList<Usuario> getUsuarios(){ return usuarios;}
    public Usuarios getUsuarios2(){ return u;}


    public void solucionInicial1() {

    }

    //public void solucionInicial2() {}

    /* CONDICIONES DE APLICABILIDAD */

    //Verificar M Conductores
    /*public boolean notMaxDrivers(){
        boolean retVal = true;
        if (eventos.size() == M){
            retVal = false;
        }
        return retVal;
    }*/

    private int distancia(Usuario A, Usuario B){
        int Ax, Bx, Ay, By;

        Ax = A.getCoordOrigenX();
        Ay = A.getCoordOrigenY();
        Bx = B.getCoordOrigenX();
        By = B.getCoordOrigenY();

        return abs(Ax - Bx) + abs(Ay - By);
    }

    // Verificar Kilometraje (max 30km = 300 manzanas)
    public boolean kilometrajeValido(ArrayList<Integer> eventosConductor){
        int dist = 0;
        dist += distancia(usuarios.get(0), usuarios.get(eventos.size()-1));

        for(int i = 0; i < eventosConductor.size()-1 && dist < 300; i++) {
            dist += distancia(usuarios.get(i), usuarios.get(i+1));
        }
        return dist <= 300;
    }

    // Verificar 2 conductores
    public boolean twoPassengers(ArrayList<Integer> eventosConductor){
        HashSet<Integer> set = new HashSet<>();
        for(int i = 1; i < eventosConductor.size(); ++i) {
            if(set.contains(eventosConductor.get(i))) set.remove(eventosConductor.get(i));
            else if (set.size() == 2) return false;
            else set.add(eventosConductor.get(i));
        }
        return true;
    }
}