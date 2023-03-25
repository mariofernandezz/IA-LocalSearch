// IMPLEMENTACIÓN DEL ESTADO

import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import java.util.List;
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
    public Estado(int n, int m, int seed){
        N = n;
        M = m;
        u = new Usuarios(N, M, seed);
        eventos = new ArrayList<ArrayList<Integer>>(M);
        usuarios = new ArrayList<>();
        ordenar();
    }

    public Estado(ArrayList<Usuario> usuarios, ArrayList<ArrayList<Integer>> eventos){
        eventos = new ArrayList<ArrayList<Integer>>(M);
        usuarios = new ArrayList<>();
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
    public ArrayList<ArrayList<Integer>> getEventos(){ return eventos;}

    /* FUNCIONES AUXILIARES */
    private int obtenerConductor(int p){
        for (int i = 0; i < eventos.size(); i++) {
			for (int j = 0; j < eventos.get(i).size(); j++){
                if (eventos.get(i).get(j) == p) return i;
            }
        }
        return -1;
    }

    /* OPERADORES */
    public void anadirConductor(int c){
        eventos.add(new ArrayList<>());
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

    public int numeroPasajeros(int c){
        return (eventos.get(c).size()-1)/2;
    }


    public void solucionInicial1() {
        //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje y 
        // cuando un conductor no puede hacer más viajes pasamos al siguiente conductor.

        // Añadimos todos los conductores a los eventos
        for(int i = 0; i < M; i++) {
            anadirConductor(i);
        }

        // Asignamos conductor a los pasajeros
        int c = 0;
        int p = M;
        boolean esValido = true;
        while(p<N && c<M){
            while(p<N && esValido){
                anadirPasajero(p, c);
                p++;
                esValido = kilometrajeValido(eventos.get(c));
            }
            if (! esValido){
                p--;
                eliminarPasajero(p, c);
                esValido = true;
            }
            c++;
        }
        if(p<N) System.out.println("No es solucion");
        else System.out.println("Solucion inicial generada");

    }

    private int mitadCamino (int a, int b){
        double orig = (double) a;
        double dest = (double) b;
        int retVal;
        // Redondeamos a lo que esté más cerca del origen.
        if (orig >= dest) {
            retVal = (int) Math.ceil((orig-dest)/2);
        } else {
            retVal = (int) Math.floor((dest-orig)/2);
        }
        return retVal;
    }

    private boolean dentroZona (int CondX, int CondY, int Pasajero, int rad) {
        int minX = CondX - rad;
        int maxX = CondX + rad;
        int minY = CondY - rad;
        int maxY = CondY + rad;
        int PorigX = usuarios.get(Pasajero).getCoordOrigenX();
        int PorigY = usuarios.get(Pasajero).getCoordOrigenY();
        int PdestX = usuarios.get(Pasajero).getCoordDestinoX();
        int PdestY = usuarios.get(Pasajero).getCoordDestinoY();

        if (PorigX > minX && PorigX < maxX && PorigY > minY && PorigY < maxY) {
            if (PdestX > minX && PdestX < maxX && PdestY > minY && PdestY < maxY) return true;
        }
        return false;
    }

    // Hay 10.000 posiciones. Decir que un punto está en la zona de un conductor si está en un radio con centro la mitad entre su origen y destino.
    // Por cada cuatro conductores, el 'radio' por cada eje se restringe a un cuarto.
    public void solucionInicial2() {
        // Sería interesante que condujeran aquellos con mayor distancia entre orig. y dest. ya que pueden haber más pasajeros dentro

        List<List<Integer>> centroConductores = new ArrayList<List<Integer>>();

        // Añadimos todos los conductores a los eventos y calculamos el centro de sus trayectos al trabajo
        for(int i = 0; i < M; i++) {
            anadirConductor(i);
            List<Integer> driverList = new ArrayList<>();
            driverList.add(mitadCamino(usuarios.get(i).getCoordOrigenX(), usuarios.get(i).getCoordDestinoX())); // Centro en X
            driverList.add(mitadCamino(usuarios.get(i).getCoordOrigenY(), usuarios.get(i).getCoordDestinoY())); // Centro en Y
            centroConductores.add(driverList);
        }

        // Definimos el radio de la zona considerada cercana para los conductores, según el número que haya
        // X conductores --> r = 100/raiz(X) * 2 (x lado)
        int a = 1;
        while (a*a <= M) a++;
        a--;
        int radio = (int) Math.ceil(M/(a*2));

        // Asignamos conductor a los pasajeros, intentando primero que estén dentro de la zona del conductor
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
                    if (dentroZona(centroConductores.get(conductor).get(0), centroConductores.get(conductor).get(0), pasajero, radio)) {
                        asignado = true;
                    } else eliminarPasajero(pasajero, conductor);                    
                } else eliminarPasajero(pasajero, conductor);
                conductor++;
            }
            if(!asignado && primer_conductor_disponible != -1) anadirPasajero(pasajero, primer_conductor_disponible); else break;
            pasajero++;
        }
        if(pasajero<N) System.out.println("No es solucion");
        else System.out.println("Solucion inicial generada");

    }
    

    /*
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
    */

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

    private int distancia(int Ax, int Ay, int Bx, int By){
        return abs(Ax - Bx) + abs(Ay - By);
    }

    public int kilometrajeConductor(ArrayList<Integer> eventosConductor){
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
            System.out.println(Ax + ", " + Ay + ", " + Bx + ", " + By);
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
    public boolean twoPassengers(ArrayList<Integer> eventosConductor){
        HashSet<Integer> set = new HashSet<>();
        for(int i = 1; i < eventosConductor.size(); ++i) {
            if(set.contains(eventosConductor.get(i))) set.remove(eventosConductor.get(i));
            else if (set.size() == 2) return false;
            else set.add(eventosConductor.get(i));
        }
        return true;
    }

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
            System.out.println(Ax + ", " + Ay + ", " + Bx + ", " + By);  
            dist = distancia(Ax, Ay, Bx, By);
            if (plazasLibresActual == 2) distLibre += 2*dist; // La dist se recorre con dos asientos libres
            else if (plazasLibresActual == 1) distLibre += dist; // La dist se recorre con un asiento libre
            distTotal += dist;
            Ax = Bx;
            Ay = By;
        }
        distTotal += distancia(Ax, Ay, usuarios.get(c).getCoordDestinoX(), usuarios.get(c).getCoordDestinoY());
        return distTotal + distLibre; // Manzanas
    }
}