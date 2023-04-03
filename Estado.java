// IMPLEMENTACIÓN DEL ESTADO DEL PROBLEMA

import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collections;
import java.util.HashSet;


import static java.lang.Math.abs;


public class Estado {

    //*********************************************************************************************
    //************************************** ATRIBUTOS ********************************************
    //*********************************************************************************************
    
    // Ids de los participantes y conductores: 
    // Primero se encuentran los candidatos a conductores [0,...,M]
    // A continuación están los participantes que no pueden ser conductores [M,...,N]
    private ArrayList<Usuario> usuarios;

    // Eventos de los posibles conductores
    // Si no actua como conductor, no tiene ningun evento asociado
    // El primer elemento de la lista de cada conductor es su id, solo si este conduce
    private ArrayList<ArrayList<Integer>> eventos;
    
    // Numero de posibles conductores
    private int M;

    // Numero de participantes totales
    private int N;

    // Distancia que recorre cada conductor
    ArrayList<Integer> distancias;
    

    public static String INTERCAMBIAR_ORDEN = "cambio de orden";
    public static String CAMBIAR_PASAJERO = "cambio pasajero";
    public static String ANADIR_CONDUCTOR = "añadir conductor";
    public static String ELIMINAR_CONDUCTOR = "eliminar conductor";


    //**********************************************************************************************
    //*************************************** CONSTRUCTORES ****************************************
    //**********************************************************************************************
    
    // Crea un estado dado un numero de participantes (n), un numero de conductores (m) y una seed aletoria
    public Estado(int n, int m, int seed){
        N = n;
        M = m;
        Usuarios u = new Usuarios(N, M, seed); 
        eventos = new ArrayList<ArrayList<Integer>>(M);
        usuarios = new ArrayList<>();
        distancias = new ArrayList<>();
        ordenar(u);
    }

    // Crea un estado dado los participantes y sus ids (u), los eventos para cada conductor (e), las distancias 
    // que recorren cada conductor (ds)
    public Estado(ArrayList<Usuario> u, ArrayList<ArrayList<Integer>> e, ArrayList<Integer> ds){
        N = u.size();
        M = e.size();
        usuarios = new ArrayList<>(u);
        eventos = new ArrayList<>(M);
        for (ArrayList<Integer> integers : e) eventos.add(new ArrayList<>(integers));
        distancias = new ArrayList<> (ds);
    }


    //**********************************************************************************************
    //***************************************** GETTERS ********************************************
    //**********************************************************************************************

    public ArrayList<Usuario> getUsuarios(){ return usuarios;}
    public ArrayList<ArrayList<Integer>> getEventos(){ return eventos;}
    public int getM(){ return M;}
    public int getN(){ return N;}
    public ArrayList<Integer> getDistancias(){return distancias;}

    
    //**********************************************************************************************
    //********************************** SOLUCIONES INICIALES **************************************
    //**********************************************************************************************
    
    // Método 1 para la generación de la solución inicial. Método simple.
    public boolean solucionInicial1() {
        //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje y 
        // cuando un conductor no puede hacer más viajes pasamos al siguiente conductor.
	    
        // Añadimos todos los conductores a los eventos.
        for(int i = 0; i < M; i++) {
            anadirConductor(i);
        }
        int c = 0;
        int p = M;
        if(repartirPasajeros(c, p)) return true; else return false; // Devuelve true si se ha podido generar una solución inicial
    }

    private boolean repartirPasajeros(int c, int p) {
        // Asignamos conductor a los pasajeros
        boolean esValido = true;
        while(p<N && c<M){
            while(p<N && esValido){
                anadirPasajero(p, c);
                p++;
                esValido = kilometrajeValido(c);
            }
            if(!esValido) {
                p--;
                eliminarPasajero(p, null);
                esValido = true;
            }
            c++;
        }
        if (p<N) return false; else return true;
    }

    // Método 2 para la generación de la solución inicial. Método complejo.
    public boolean solucionInicial2(){
        HashSet<Integer> pasajerosAsignados = new HashSet<>();

        //Añadimos todos los conductores a los eventos
        for(int i=0; i<M; i++) {
            anadirConductor(i);
        }

        //Para cada conductor hacemos una lista de los conductores que estan dentro de la "zona"
        for(int c=0; c<M; c++){
            for(int p=M; p<N; p++){
                if(!pasajerosAsignados.contains(p) && enZona(c, p)){
                    anadirPasajero(p, c);
                    if (kilometrajeValido(c)) pasajerosAsignados.add(p);
                    else eliminarPasajero(p, c);
                }
            }
        }

        //Asignamos pasajeros que no han sido asignados por la "zona
        if(pasajerosAsignados.size()!= N-M){
            int c = 0;
            for(int p=M; p<N; p++){
                while(!pasajerosAsignados.contains(p)){
                    if (c == M) return false;
                    anadirPasajero(p, c);
                    if (kilometrajeValido(c)) pasajerosAsignados.add(p);
                    else eliminarPasajero(p, c);
                    c++;
                }
                c=0;
            }
        }
        return true; // Devuelve true si se ha podido generar una solución inicial
    }
		
    // Método 3 para la generación de la solución inicial. Método complejo con aleatoriedad.
    public boolean solucionInicial3(int seed){
        HashSet<Integer> pasajerosAsignados = new HashSet<>();
        Random rndm = new Random(seed);

        //Añadimos todos los conductores a los eventos
        for(int i=0; i<M; i++) {
            anadirConductor(i);
        }

        //Para cada conductor hacemos una lista de los conductores que están dentro de la "zona"
        for(int c=0; c<M; c++){
            HashSet<Integer> pasajerosProbados = new HashSet<>();
            while(pasajerosProbados.size() + pasajerosAsignados.size()<N-M){
                int p = rndm.nextInt(M, N); // El orden de los pasajeros es aleatorio
                if (!pasajerosAsignados.contains(p) && !pasajerosProbados.contains(p)){
                    pasajerosProbados.add(p);
                    if(enZona(c, p)){
                        anadirPasajero(p, c);
                        if (kilometrajeValido(c)) pasajerosAsignados.add(p);
                        else eliminarPasajero(p, c);
                    }
                }
            }
        }

        //Asignamos pasajeros que no han sido asignados por la zona
        while(pasajerosAsignados.size()!= N-M){
            int p = rndm.nextInt(M, N); // Miramos los pasajeros en orden aleatorio
            HashSet<Integer> conductoresProbados = new HashSet<>();
            while(!pasajerosAsignados.contains(p)){
                if (conductoresProbados.size() == M) return false; // El pasajero p no cabe en ninguno de los conductores
                int c = rndm.nextInt(0, M); // Se le intenta asignar conductor de forma aleatoria
                conductoresProbados.add(c);
                anadirPasajero(p, c);
                if (kilometrajeValido(c)) pasajerosAsignados.add(p);
                else eliminarPasajero(p, c);
            }
        }
        return true; // Devuelve true si se ha podido generar una solución inicial
    }

    

    //**********************************************************************************************
    //**************************************** OPERADORES ******************************************
    //**********************************************************************************************
    
    public void anadirConductor(int c){
        if (eventos.size() < M) {
            eventos.add(new ArrayList<>());
            distancias.add(0);
        }
        eventos.get(c).add(c);
        distancias.set(c, distancia(usuarios.get(c).getCoordOrigenX(), usuarios.get(c).getCoordOrigenY(), usuarios.get(c).getCoordDestinoX(), usuarios.get(c).getCoordDestinoY())); 
    }

    public void eliminarConductor(int c, int c2){
        eventos.get(c).remove(Integer.valueOf(c));
        eventos.get(c2).add(c);
        eventos.get(c2).add(c);
        distancias.set(c, kilometrajeConductor(c));
        distancias.set(c2, kilometrajeConductor(c2));
    }

    public void cambiarConductor(int p, Integer c, int c2){
        eliminarPasajero(p, c);
        anadirPasajero(p, c2);
        distancias.set(c, kilometrajeConductor(c));
        distancias.set(c2, kilometrajeConductor(c2));
    }

    public void intercambiarEventos(int c, int id1, int id2){
        int a = eventos.get(c).get(id1);
        int b = eventos.get(c).get(id2);
        eventos.get(c).set(id1, b);
        eventos.get(c).set(id2, a);
        distancias.set(c, kilometrajeConductor(c));
    }

    //**********************************************************************************************
    //******************* FUNCIONES AUXILIARES PARA CREAR O MODIFICAR ESTADO ***********************
    //**********************************************************************************************

    // Método que devuelve true si los dos puntos de un pasajero se encuentran dentro de la 'zona' de un conductor.
    public boolean enZona(int c, int p){
        int AOx = usuarios.get(c).getCoordOrigenX();
        int AOy = usuarios.get(c).getCoordOrigenY();
        int ADx = usuarios.get(c).getCoordDestinoX();
        int ADy = usuarios.get(c).getCoordDestinoY();
        int BOx = usuarios.get(p).getCoordOrigenX();
        int BOy = usuarios.get(p).getCoordOrigenY();
        int BDx = usuarios.get(p).getCoordDestinoX();
        int BDy = usuarios.get(p).getCoordDestinoY();
        if (AOx <= ADx && AOy <= ADy) return BOx > AOx && BOx <= ADx && BOy > AOy && BOy <= ADy && BDx > AOx && BDx <= ADx && BDy > AOy && BDy <= ADy;
        if (AOx <= ADx && AOy > ADy) return BOx > AOx && BOx <= ADx && BOy <= AOy && BOy > ADy && BDx > AOx && BDx <= ADx && BDy <= AOy && BDy > ADy;
        if (AOx > ADx && AOy <= ADy) return BOx <= AOx && BOx > ADx && BOy > AOy && BOy <= ADy && BDx <= AOx && BDx > ADx && BDy > AOy && BDy <= ADy;
        if (AOx > ADx && AOy > ADy) return BOx <= AOx && BOx > ADx && BOy <= AOy && BOy > ADy && BDx <= AOx && BDx > ADx && BDy < AOy && BDy > ADy;
        return false;
    }

    public int obtenerConductor(int p){
        for (int i = 0; i < eventos.size(); i++) {
			for (int j = 0; j < eventos.get(i).size(); j++){
                if (eventos.get(i).get(j) == p) return i;
            }
        }
        return -1;
    }

    public int numeroConductores(){
        int ncond = 0;
        for (int i=0; i<M; i++){
            if (eventos.get(i).size()>0) ncond += 1;
        }
        return ncond;
    }

    public void anadirPasajero(int p, int c){
        eventos.get(c).add(p);
        eventos.get(c).add(p);
        distancias.set(c, kilometrajeConductor(c));
    }

    public void eliminarPasajero(int p, Integer c){
        if (c == null) c = obtenerConductor(p);
        eventos.get(c).remove(Integer.valueOf(p));
        eventos.get(c).remove(Integer.valueOf(p));
        distancias.set(c, kilometrajeConductor(c));
    }    
         
        

    //**********************************************************************************************
    //******************************* CONDICIONES DE APLICABILIDAD *********************************
    //**********************************************************************************************

    // Devuelve la distancia que separa las ubicaciones (Ax, Ay) y (Bx, By)
    private int distancia(int Ax, int Ay, int Bx, int By){
        return abs(Ax - Bx) + abs(Ay - By);
    }

    // Devuelve la distancia total recorrida en manzanas por el conductor (c)
    public int kilometrajeConductor(int c){
        ArrayList<Integer> eventosConductor = eventos.get(c);
        if (eventosConductor.size() == 0) return 0;
        int dist = 0;
        int Ax, Ay, Bx, By;
        Ax = usuarios.get(c).getCoordOrigenX();
        Ay = usuarios.get(c).getCoordOrigenY();
        HashSet<Integer> set = new HashSet<>();
        for(int i = 1; i < eventosConductor.size() && dist <= 300; i++) {
            int id2 = eventosConductor.get(i);
            if(set.contains(id2)) {
                Bx = usuarios.get(id2).getCoordDestinoX();
                By = usuarios.get(id2).getCoordDestinoY();
            } else {
                set.add(id2);
                Bx = usuarios.get(id2).getCoordOrigenX();
                By = usuarios.get(id2).getCoordOrigenY();
            }
            dist += distancia(Ax, Ay, Bx, By);
            Ax = Bx;
            Ay = By;
        }
        dist += distancia(Ax, Ay, usuarios.get(c).getCoordDestinoX(), usuarios.get(c).getCoordDestinoY());
        return dist; 
    }

    // Devuelve la distancia total recorrida en manzanas por todos los conductores en el estado definido
    public int kilometrajeEstado(){
        int sum = 0;
        for (int i=0; i<M; i++){
            sum += distancias.get(i);
        }
        return sum;
    }

    // Verifica que el kilometraje del conductor (c) esté permitido, es decir, que sea de < 300 manzanas
    public boolean kilometrajeValido(int c){
        return distancias.get(c) <= 300;
    }

    // Verifica que el orden de eventos del conductor (c) esté permitido, es decir, que nunca hayan asignados
    // más de 2 pasajeros a su coche a la vez
    public boolean dosPasajeros(ArrayList<Integer> eventosConductor){
        HashSet<Integer> set = new HashSet<>();
        for(int i = 1; i < eventosConductor.size(); ++i) {
            if(set.contains(eventosConductor.get(i))) set.remove(eventosConductor.get(i));
            else if (set.size() == 2) return false;
            else set.add(eventosConductor.get(i));
        }
        return true;
    }


    //**********************************************************************************************
    //************************************** OTRAS FUNCIONES ***************************************
    //**********************************************************************************************

    // Ordena un conjunto de usuarios poniendo los posibles conductores primero y despues los participantes restantes
    private void ordenar(Usuarios u) {
        for(int i = 0; i < u.size(); ++i) {
            if(u.get(i).isConductor()) usuarios.add(u.get(i));
        }
        for(int i = 0; i < u.size(); ++i) {
            if(!u.get(i).isConductor()) usuarios.add(u.get(i));
        }
    }

    // Devuelve un String con la representación de los eventos del estado
    public String conversionString(){
        return eventos.toString();
    }
}
