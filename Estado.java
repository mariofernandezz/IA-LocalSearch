// IMPLEMENTACIÓN DEL ESTADO

import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.*;

import static java.lang.Math.abs;


public class Estado {

   /* ATRIBUTOS */
    private Usuarios u;
    private ArrayList<Usuario> usuarios;
    private ArrayList<ArrayList<Integer>> eventos;
    int m;
    int n;

    /*CONSTRUCTOR*/
    public Estado(int N, int M, int seed){
        u = new Usuarios(N, M, seed);
        eventos = new ArrayList<>(m);
        for(int i = 0; i < m; ++i) eventos.add(new ArrayList<>());
        usuarios = new ArrayList<>();
        m = M;
        n = N;
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
        ArrayList<Integer> usados = new ArrayList<>(n);
        for (int i = 100; i < n; ++i) usados.add(i);        //Lista con N-M ids
        Collections.shuffle(usados);                        //Randomizar la lista
        //System.out.print();
        System.out.println(eventos);
        for(int i = 0; i < m; ++i) {
            // falta comprobar que le da tiempo
            ArrayList<Integer> aux = new ArrayList<>();
            aux.add(i);
            aux.add(usados.get(i));
            aux.add(usados.get(i));
            //if(kilometrajeValido(aux)) eventos.add(aux);
            //else System.exit(0);
            eventos.add(aux);
        }
        System.out.println(eventos);
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

    private int distancia(Usuario A, boolean Aorig, Usuario B, boolean Borig){
        int Ax, Ay, Bx, By;
        if (Aorig){
             Ax = A.getCoordOrigenX();
             Ay = A.getCoordOrigenY();
        } else{
             Ax = A.getCoordDestinoX();
             Ay = A.getCoordDestinoY();
        }

        if (Borig){
             Bx = B.getCoordOrigenX();
             By = B.getCoordOrigenY();
        } else{
             Bx = B.getCoordDestinoX();
             By = B.getCoordDestinoY();
        }
        return abs(Ax - Bx) + abs(Ay - By);
    }

    // Verificar Kilometraje (max 30km = 300 manzanas)
    public boolean kilometrajeValido(ArrayList<Integer> eventosConductor){
        int dist = 0;
        dist += distancia(usuarios.get(eventosConductor.get(0)), false, usuarios.get(eventosConductor.size()-1),false);

        for(int i = 0; i < eventosConductor.size()-1 && dist < 300; i++) {
            dist += distancia(usuarios.get(eventosConductor.get(i)), esOrigen(eventosConductor, i), usuarios.get(eventosConductor.get(i+1)), esOrigen(eventosConductor, i));
        }
        return dist <= 300;
    }

    /*private boolean esOrigen(ArrayList<Integer> eventosConductor, int id) {
        //boolean esta2Veces = false;
        //for (int i = 1; i < eventosConductor.size(); ++i) if(id == eventosConductor.get(id)) esta2Veces = true;
    }*/

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