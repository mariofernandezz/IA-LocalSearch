// IMPLEMENTACIÓN DEL ESTADO

import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.lang.Math.abs;


public class Estado {
    
    //*********************************************************************************************
    //************************************** ATRIBUTOS ********************************************
    //*********************************************************************************************
    
    // Ids de los participantes y conductores: 
    // Primero se encuentran los candidatos a conductores [0,...,M]
    // A continuación estas los participantes que no pueden ser conductores [M,...,N]
    private final ArrayList<Usuario> usuarios;
    
    // Eventos de los posibles conductores
    // Si no actua como conductor, no tiene ningun evento asociado
    private final ArrayList<ArrayList<Integer>> eventos;
    
    //Numero de posibles conductores
    private final int M;
    
    //Numero de participantes totales
    private final int N;

    public static String INTERCAMBIAR_ORDEN = "cambio de orden";
    public static String CAMBIAR_PASAJERO = "cambio pasajero";
    public static String ANADIR_CONDUCTOR = "añadir conductor";
    public static String ELIMINAR_CONDUCTOR = "eliminar conductor";

    
    //**********************************************************************************************
    //*************************************** CONSTRUCTORES ****************************************
    //**********************************************************************************************
    
    //Crea un estado dado un numero de participantes (n), un numero de conductores (m) y una seed aletoria
    public Estado(int n, int m, int seed){
        N = n;
        M = m;
        Usuarios u = new Usuarios(N, M, seed); 
        eventos = new ArrayList<>(M);
        usuarios = new ArrayList<>();
        ordenar(u);
    }

    //Crea un estado dado los participantes y sus ids (u) y los eventos para cada conductor (e)
    public Estado(ArrayList<Usuario> u, ArrayList<ArrayList<Integer>> e){
        N = u.size();
        M = e.size();
        usuarios = new ArrayList<>(u);
        eventos = new ArrayList<>(M);
        for (ArrayList<Integer> integers : e) eventos.add(new ArrayList<>(integers));
    }

    
    //**********************************************************************************************
    //********************************** SOLUCIONES INICIALES **************************************
    //**********************************************************************************************

    //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje y
    // cuando un conductor no puede hacer más viajes pasamos al siguiente conductor.
    public void solucionInicial1() {
        

        // Añadimos todos los conductores a los eventos
        for(int i = 0; i < M; i++) {
            anadirConductor(i);
        }
        int c = 0;
        int p = M;
        repartirPasajeros(c, p);
    }

    //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje.
    //El conductor escoge pasajeros que esten cerca de su posición.
    //Si sobran se hace otra pasada y se colocan los restantes donde se pueda
    public void solucionInicial2() {
        //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje.
        //El conductor escoge pasajeros que esten cerca de su posición.

        // Añadimos todos los conductores a los eventos
        for(int i = 0; i < M; i++) {
            anadirConductor(i);
        }

        // Asignamos conductor a los pasajeros
        // Asignamos conductor a los pasajeros
        int c = 0;
        int p = M;
        int count = M;
        boolean esValido = true;
        while(p<N && c<M){
            while(p<N && pasajeroCercano(eventos.get(c).get(eventos.get(c).size()-1), p) && esValido){
                anadirPasajero(p, c);
                p++;
                esValido = kilometrajeValido(eventos.get(c));
                count++;
            }
            if(!esValido) {
                p--;
                eliminarPasajero(p, null);
                esValido = true;
                count--;
            }
            c++;
            //System.out.println();
        }

        System.out.println(c + " * " + p + " * " + count);
        if(count < N) repartirPasajeros(0, count);

        if(p<N) System.out.println("No es solucion");
        else System.out.println("Solucion inicial generada");
        //System.out.println(eventos);
    }

    // Hay 10.000 posiciones. Decir que un punto está en la zona de un conductor si está en un radio con centro la mitad entre su origen y destino.
    // Por cada cuatro conductores, el 'radio' por cada eje se restringe a un cuarto.
    public void solucionInicial3() {
        // Sería interesante que condujeran aquellos con mayor distancia entre orig. y dest. ya que pueden haber más pasajeros dentro

        List<List<Integer>> centroConductores = new ArrayList<List<Integer>>();

        // Definimos el radio de la zona considerada cercana para los conductores, según el número que haya
        // X conductores --> r = 100/raiz(X) * 2 (x lado)
        int a = 1;
        while (a*a <= M) a++;
        a--;
        int radio = (int) Math.ceil(100/(a));

        // Añadimos nuevos conductores a menos que estén en la zona de uno ya añadido
        for(int i = 0; i < M; i++) {
            if ( 1000 < distancia(usuarios.get(i).getCoordOrigenX(), usuarios.get(i).getCoordOrigenY(), usuarios.get(i).getCoordDestinoX(), usuarios.get(i).getCoordDestinoY())){
                anadirConductor(i);
                List<Integer> driverList = new ArrayList<>();
                driverList.add(mitadCamino(usuarios.get(i).getCoordOrigenX(), usuarios.get(i).getCoordDestinoX())); // Centro en X
                driverList.add(mitadCamino(usuarios.get(i).getCoordOrigenY(), usuarios.get(i).getCoordDestinoY())); // Centro en Y
                centroConductores.add(driverList);
            } else{
                boolean asignado = false;
                for(int j = 0; j < i && !asignado; j++){
                    if (centroConductores.get(j).size() > 0){
                        if (dentroZona(centroConductores.get(j).get(0), centroConductores.get(j).get(1), i, radio, true)){
                            anadirPasajero(i, j); // Conductor dentro de la zona de otro
                            if (kilometrajeValido(eventos.get(j))) {
                                asignado = true;
                                centroConductores.add(new ArrayList<>());
                                eventos.add(new ArrayList<>());
                            } else eliminarPasajero(i, j);
                        }
                    }
                }
                if(!asignado){
                    anadirConductor(i);
                    List<Integer> driverList = new ArrayList<>();
                    driverList.add(mitadCamino(usuarios.get(i).getCoordOrigenX(), usuarios.get(i).getCoordDestinoX())); // Centro en X
                    driverList.add(mitadCamino(usuarios.get(i).getCoordOrigenY(), usuarios.get(i).getCoordDestinoY())); // Centro en Y
                    centroConductores.add(driverList);
                }
            }
        }

        System.out.println("b");
        System.out.println(conversionString());

        // Asignamos conductor a los pasajeros, intentando primero que estén dentro de la zona del conductor
        int pasajero = M;
        while(pasajero<N) {
            boolean asignado = false;
            int conductor = 0;
            int primer_conductor_disponible = -1;
            while(!asignado && conductor<M) {
                if (centroConductores.get(conductor).size() > 0){
                    anadirPasajero(pasajero, conductor);
                    if (kilometrajeValido(eventos.get(conductor))) {
                        if (primer_conductor_disponible == -1) {
                            primer_conductor_disponible = conductor;
                        }
                        if (dentroZona(centroConductores.get(conductor).get(0), centroConductores.get(conductor).get(1), pasajero, radio, false)) {
                            asignado = true;
                        } else eliminarPasajero(pasajero, conductor);
                    } else {
                        eliminarPasajero(pasajero, conductor);
                        //if (pasajero== 187) System.out.println("no cabe " + conductor);

                    }
                }
                conductor++;
            }
            if(!asignado && primer_conductor_disponible != -1) anadirPasajero(pasajero, primer_conductor_disponible); else if (!asignado){
                System.out.println("no disponible");
                break;
            }
            pasajero++;
        }
        if(pasajero<N) System.out.println("No es solucion " + pasajero);
        else System.out.println("Solucion inicial generada");
    }
    
    //
    public void solucionInicial5(){

        // Añadimos nuevos conductores a menos que estén en la zona de uno ya añadido
        for(int i = 0; i < M; i++) {
            if ( 100 < distancia(usuarios.get(i).getCoordOrigenX(), usuarios.get(i).getCoordOrigenY(), usuarios.get(i).getCoordDestinoX(), usuarios.get(i).getCoordDestinoY())){
                anadirConductor(i);
            } else{
                boolean asignado = false;
                for(int j = 0; j < i && !asignado; j++){
                    if (eventos.get(j).size() > 0){
                        if (enZona(j, i)){
                            anadirPasajero(i, j); // Conductor dentro de la zona de otro
                            if (kilometrajeValido(eventos.get(j))) {
                                asignado = true;
                                eventos.add(new ArrayList<>());
                            } else eliminarPasajero(i, j);
                        }
                    }
                }
                if(!asignado){
                    anadirConductor(i);
                }
            }
        }

        int pasajero = M;
        while(pasajero<N) {
            boolean asignado = false;
            int conductor = 0;
            int primer_conductor_disponible = -1;
            while(!asignado && conductor<M) {
                anadirPasajero(pasajero, conductor);
                if (kilometrajeValido(eventos.get(conductor))) {
                    if (primer_conductor_disponible == -1) {
                        primer_conductor_disponible = conductor;
                    }
                    if (enZona(conductor, pasajero)) {
                        asignado = true;
                    } else eliminarPasajero(pasajero, conductor);
                } else {
                    eliminarPasajero(pasajero, conductor);
                    //if (pasajero== 187) System.out.println("no cabe " + conductor);
                }
                conductor++;
            }
            if(!asignado && primer_conductor_disponible != -1) anadirPasajero(pasajero, primer_conductor_disponible); else if (!asignado){
                System.out.println("no disponible");
                break;
            }
            pasajero++;
        }
        if(pasajero<N) System.out.println("No es solucion " + pasajero);
        else System.out.println("Solucion inicial generada");
    }

    
    //**********************************************************************************************
    //******************* FUNCIONES AUXILIARES PARA CREAR O MODIFICAR ESTADO ***********************
    //**********************************************************************************************
    
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

    public void anadirConductor(int c){
        if (eventos.size() < M) eventos.add(new ArrayList<>());
        eventos.get(c).add(c);
    }

    public void eliminarConductor(int c, int c2){
        eventos.get(c).remove(Integer.valueOf(c));
        eventos.get(c2).add(c);
        eventos.get(c2).add(c);
    }

    public void anadirPasajero(int p, int c){
        eventos.get(c).add(p);
        eventos.get(c).add(p);
    }

    public void eliminarPasajero(int p, Integer c){
        if (c == null) c = obtenerConductor(p);
        eventos.get(c).remove(Integer.valueOf(p));
        eventos.get(c).remove(Integer.valueOf(p));
    }

    public void cambiarConductor(int p, Integer c, int c2){
        eliminarPasajero(p, c);
        anadirPasajero(p, c2);
    }

    public void cambiarOrden(int c, int id1, int id2){
        int a = eventos.get(c).get(id1);
        int b = eventos.get(c).get(id2);
        eventos.get(c).set(id1, b);
        eventos.get(c).set(id2, a);
    }

    private void repartirPasajeros(int c, int p) {
        boolean esValido = true;
        while(p<N && c<M){
            while(p<N && esValido){
                anadirPasajero(p, c);
                p++;
                esValido = kilometrajeValido(eventos.get(c));
            }
            if(!esValido) {
                p--;
                eliminarPasajero(p, null);
                esValido = true;
            }
            c++;
            //System.out.println();
        }
        if(p<N) System.out.println("No es solucion");
        else System.out.println("Solucion inicial generada");
    }
    
    private boolean pasajeroCercano(int actual, int nuevo) {
        int Ax = usuarios.get(actual).getCoordDestinoX();
        int Ay = usuarios.get(actual).getCoordDestinoY();
        int Bx = usuarios.get(nuevo).getCoordOrigenX();
        int By = usuarios.get(nuevo).getCoordOrigenY();
        return 50 > distancia(Ax, Ay, Bx, By);
    }
    
    
    //**********************************************************************************************
    //******************************* CONDICIONES DE APLICABILIDAD *********************************
    //**********************************************************************************************

    private int distancia(int Ax, int Ay, int Bx, int By){
        return abs(Ax - Bx) + abs(Ay - By);
    }

    public int kilometrajeSolucion(){
        int sum = 0;
        for (int i=0; i<M; i++){
            sum += kilometrajeConductor(eventos.get(i));
        }
        return sum;
    }

    public int kilometrajeConductor(ArrayList<Integer> eventosConductor){
        if (eventosConductor.size() == 0) return 0;
        int dist = 0;
        int c = eventosConductor.get(0);
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
        return dist; // Manzanas
    }

    // Verificar Kilometraje (max 30km = 300 manzanas)
    public boolean kilometrajeValido(ArrayList<Integer> eventosConductor){
        return kilometrajeConductor(eventosConductor) <= 300;
    }

    // Verificar 2 conductores
    public boolean dosPasajeros(ArrayList<Integer> eventosConductor){
        HashSet<Integer> set = new HashSet<>();
        for(int i = 1; i < eventosConductor.size(); ++i) {
            if(set.contains(eventosConductor.get(i))) set.remove(eventosConductor.get(i));
            else if (set.size() == 2) return false;
            else set.add(eventosConductor.get(i));
        }
        return true;
    }

    private int mitadCamino (int a, int b){
        int retVal;
        // Redondeamos a lo que esté más cerca del origen.
        if ((double) a >= (double) b) {
            retVal = (int) Math.ceil(((double) a - (double) b)/2);
        } else {
            retVal = (int) Math.floor(((double) b - (double) a)/2);
        }
        return retVal;
    }

    private boolean dentroZona (int CondX, int CondY, int Pasajero, int rad, boolean conductor) {
        int minX = CondX - rad;
        int maxX = CondX + rad;
        int minY = CondY - rad;
        int maxY = CondY + rad;
        int PorigX = usuarios.get(Pasajero).getCoordOrigenX();
        int PorigY = usuarios.get(Pasajero).getCoordOrigenY();
        int PdestX = usuarios.get(Pasajero).getCoordDestinoX();
        int PdestY = usuarios.get(Pasajero).getCoordDestinoY();
        if (conductor){
            if (PorigX > minX && PorigX < maxX && PorigY > minY && PorigY < maxY){
                return PdestX > minX && PdestX < maxX && PdestY > minY && PdestY < maxY;
            }
        } else {
            if (PorigX > minX && PorigX < maxX && PorigY > minY && PorigY < maxY) return true;
            return PdestX > minX && PdestX < maxX && PdestY > minY && PdestY < maxY;
        }
        return false;
    }


    // ampliar radio
    // que solo orig o dest esté en la zona

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
        if (AOx <= ADx) return BOx > AOx && BOx <= ADx && BOy <= AOy && BOy > ADy && BDx > AOx && BDx <= ADx && BDy <= AOy && BDy > ADy;
        if (AOy <= ADy) return BOx <= AOx && BOx > ADx && BOy > AOy && BOy <= ADy && BDx <= AOx && BDx > ADx && BDy > AOy && BDy <= ADy;
        return BOx <= AOx && BOx > ADx && BOy <= AOy && BOy > ADy && BDx <= AOx && BDx > ADx && BDy < AOy && BDy > ADy;
    }
    
    //¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿ ESTO SE PUEDE QUITAR NO????????????????????
    
    // HEURÍSTICA 2 DE HÉCTOR
    public int kilometrajeConductor_manzanasLibres (ArrayList<Integer> eventosConductor) {
        int dist = 0;
        int distTotal = 0;
        int distLibre = 0; // De momento solo tiene en cuenta la dist libre hasta el fin del trayecto. Para penalizar coches con trayecto muy corto se puede penalizar hasta 300.
        int plazasLibres = 2;
        int c = eventosConductor.get(0);
        int Ax, Ay, Bx, By;
        Ax = usuarios.get(c).getCoordOrigenX();
        Ay = usuarios.get(c).getCoordOrigenY();
        HashSet<Integer> set = new HashSet<>();
        for(int i = 1; i < eventosConductor.size() && distTotal <= 300; i++) {
            int id2 = eventosConductor.get(i);
            int plazasLibresActual = plazasLibres;
            if(set.contains(id2)) {
                plazasLibres ++; // Se libera un asiento
                Bx = usuarios.get(id2).getCoordDestinoX();
                By = usuarios.get(id2).getCoordDestinoY();
            } else {
                plazasLibres --; // Se ocupa un asiento
                set.add(id2);
                Bx = usuarios.get(id2).getCoordOrigenX();
                By = usuarios.get(id2).getCoordOrigenY();
            }
            dist = distancia(Ax, Ay, Bx, By);
            if (plazasLibresActual == 2) distLibre += 2*dist; // La dist se recorre con dos asientos libres
            else if (plazasLibresActual == 1) distLibre += dist; // La dist se recorre con un asiento libre
            distTotal += dist;
            Ax = Bx;
            Ay = By;
        }
        distTotal += distancia(Ax, Ay, usuarios.get(c).getCoordDestinoX(), usuarios.get(c).getCoordDestinoY());
        int distVacia = 300 - distTotal;
        distLibre += distVacia * 2;
        return distTotal + distLibre; // Manzanas
    }

    
    //**********************************************************************************************
    //***************************************** GETTERS ********************************************
    //**********************************************************************************************
    public ArrayList<Usuario> getUsuarios(){
        return usuarios;
    }
    
    public ArrayList<ArrayList<Integer>> getEventos(){
        return eventos;
    }
    
    public int getM(){
        return M;
    }
    
    
    //**********************************************************************************************
    //*********************************** FUNCIONES PRIVADAS O OTRAS *******************************
    //**********************************************************************************************
    
    //Ordena un conjunto de usuarios poniendo los posibles conductores primero y despues los participantes restantes
    private void ordenar(Usuarios u) {
        for (Usuario usuario : u) {
            if (usuario.isConductor()) usuarios.add(usuario);
        }
        for (Usuario usuario : u) {
            if (!usuario.isConductor()) usuarios.add(usuario);
        }
    }

    public String conversionString(){
        return eventos.toString();
    }
}