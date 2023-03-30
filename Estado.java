// IMPLEMENTACIÓN DEL ESTADO

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

   /* ATRIBUTOS */
    private ArrayList<Usuario> usuarios;
    private ArrayList<ArrayList<Integer>> eventos;
    int M;
    int N;
    int distInicial;
    ArrayList<Integer> distancias;
    

    public static String INTERCAMBIAR_ORDEN = "cambio de orden";
    public static String CAMBIAR_PASAJERO = "cambio pasajero";
    public static String ANADIR_CONDUCTOR = "añadir conductor";
    public static String ELIMINAR_CONDUCTOR = "eliminar conductor";

    /*CONSTRUCTOR*/
    public Estado(int n, int m, int seed){
        N = n;
        M = m;
        Usuarios u = new Usuarios(N, M, seed); 
        eventos = new ArrayList<ArrayList<Integer>>(M);
        usuarios = new ArrayList<>();
        distancias = new ArrayList<>();
        ordenar(u);
        distInicial = 300*M;
    }

     
    public Estado(ArrayList<Usuario> u, ArrayList<ArrayList<Integer>> e, ArrayList<Integer> ds, int d){
        N = u.size();
        M = e.size();
        distancias = new ArrayList<> (ds);
        distInicial = d;
        usuarios = new ArrayList<>(u);
        eventos = new ArrayList<>(M);
        for(int i = 0; i<e.size(); ++i) {
            eventos.add(new ArrayList<>(e.get(i)));
        }
    }
    

    private void ordenar(Usuarios u) {
        for(int i = 0; i < u.size(); ++i) {
            if(u.get(i).isConductor()) usuarios.add(u.get(i));
        }
        for(int i = 0; i < u.size(); ++i) {
            if(!u.get(i).isConductor()) usuarios.add(u.get(i));
        }

    }

    public ArrayList<Usuario> getUsuarios(){ return usuarios;}
    public ArrayList<ArrayList<Integer>> getEventos(){ return eventos;}
    public int getM(){ return M;}
    public int getN(){ return N;}
    public int getDistInicial() {return distInicial;}
    public ArrayList<Integer> getDistancias(){return distancias;}

    /* FUNCIONES AUXILIARES */
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

    /* OPERADORES */
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

    public void cambiarConductor(int p, Integer c, int c2){
        eliminarPasajero(p, c);
        anadirPasajero(p, c2);
        distancias.set(c, kilometrajeConductor(c));
        distancias.set(c2, kilometrajeConductor(c2));
    }

    public void cambiarOrden(int c, int id1, int id2){
        int a = eventos.get(c).get(id1);
        int b = eventos.get(c).get(id2);
        eventos.get(c).set(id1, b);
        eventos.get(c).set(id2, a);
        distancias.set(c, kilometrajeConductor(c));
    }

    public int numeroPasajeros(int c){
        return (eventos.get(c).size()-1)/2;
    }

    
    public void solucionInicial4() {
        //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje y 
        // cuando un conductor no puede hacer más viajes pasamos al siguiente conductor.
        ArrayList<Integer> permutation = new ArrayList<>();
        for (int p=M; p<N; p++){
            permutation.add(p);
        }
        Collections.shuffle(permutation);

        // Añadimos todos los conductores a los eventos
        for(int i = 0; i < M; i++) {
            anadirConductor(i);
        }

        // Asignamos conductor a los pasajeros
        int c = 0;
        int p = 0;
        boolean esValido = true;
        while(p<(N-M) && c<M){
            while(p<(N-M) && esValido){
                anadirPasajero(permutation.get(p), c);
                p++;
                c++;
                if (c==M) c=0;
                else esValido = kilometrajeValido(c);
            }
            if (! esValido){
                p--;
                eliminarPasajero(permutation.get(p), c);
                esValido = true;
            }
            c++;
        }
    }

    /*
    public void solucionInicial4b() {
        //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje y 
        // cuando un conductor no puede hacer más viajes pasamos al siguiente conductor.
        ArrayList<Integer> permutation = new ArrayList<>();
        for (int p=M; p<N; p++){
            permutation.add(p);
        }
        Collections.shuffle(permutation);

        // Añadimos todos los conductores a los eventos
        for(int i = 0; i < M; i++) {
            anadirConductor(i);
        }

        // Asignamos conductor a los pasajeros
        int c = 0;
        int p = 0;
        boolean esValido = true;
        while(p<(N-M) && c<M){
            while(p<(N-M) && esValido){
                anadirPasajero(permutation.get(p), c);
                p++;
                esValido = kilometrajeValido(c);
            }
            if (! esValido){
                p--;
                eliminarPasajero(permutation.get(p), c);
                esValido = true;
            }
            c++;
        }

    }
    
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

        public void solucionInicial1() {
            //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje y 
            // cuando un conductor no puede hacer más viajes pasamos al siguiente conductor.

            // Añadimos todos los conductores a los eventos
            for(int i = 0; i < M; i++) {
                anadirConductor(i);
            }
            int c = 0;
            int p = M;
            repartirPasajeros(c, p);
        }
        
        private void repartirPasajeros(int c, int p) {
            // Asignamos conductor a los pasajeros

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
            System.out.println(eventos);
        }

        public void solucionInicial2() {
            //Todos los conductores conducen. Llenamos los coches hasta el límite de kilometraje.
            //El conductor escoge pasajeros que esten cerca de su posición.

            // Añadimos todos los conductores a los eventos
            for(int i = 0; i < M; i++) {
                anadirConductor(i);
            }

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
  
        private boolean pasajeroCercano(int actual, int nuevo) {
            int Ax = usuarios.get(actual).getCoordDestinoX();
            int Ay = usuarios.get(actual).getCoordDestinoY();
            int Bx = usuarios.get(nuevo).getCoordOrigenX();
            int By = usuarios.get(nuevo).getCoordOrigenY();
            return 50 > distancia(Ax, Ay, Bx, By);
        }
        */

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

    public int kilometrajeEstado(){
        int sum = 0;
        for (int i=0; i<M; i++){
            sum += distancias.get(i);
        }
        return sum;
    }


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
        return dist; // Manzanas
    }

    // Verificar Kilometraje (max 30km = 300 manzanas)
    public boolean kilometrajeValido(int i){
        return distancias.get(i) <= 300;
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

    public String conversionString(){
        return eventos.toString();
    }
    
    /*
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
        int distSinUsar = 300 - distTotal;
        return distLibre + distSinUsar; // Manzanas
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
    public void solucionInicial3() {
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
    // ampliar radio
    // que solo orig o dest esté en la zona
*/
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

    /*
    public void solucionInicial5(){
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

        //Asignamos pasajeros que no han sido asignados por la zona
        if(pasajerosAsignados.size()!= N-M){
            int c = 0;
            for(int p=M; p<N; p++){
                while(!pasajerosAsignados.contains(p)){
                    anadirPasajero(p, c);
                    if (kilometrajeValido(c)) pasajerosAsignados.add(p);
                    else eliminarPasajero(p, c);
                    c++;
                }
                c=0;
            }
        }
    }
    


        public void solucionInicial5b(int seed){
            HashSet<Integer> pasajerosAsignados = new HashSet<>();
            Random rndm = new Random(seed);
    
            //Añadimos todos los conductores a los eventos
            for(int i=0; i<M; i++) {
                anadirConductor(i);
            }
    
            //Para cada conductor hacemos una lista de los conductores que estan dentro de la "zona"
            for(int c=0; c<M; c++){
                HashSet<Integer> pasajerosProbados = new HashSet<>();
                while(pasajerosProbados.size() + pasajerosAsignados.size()<N-M){
                    int p = rndm.nextInt(M, N);
                    if (!pasajerosAsignados.contains(p) && !pasajerosProbados.contains(p)){
                        pasajerosProbados.add(p);
                        if(enZona(c, p)){
                            anadirPasajero(p, c);
                            if (kilometrajeValido(c)) pasajerosAsignados.add(p);
                            else eliminarPasajero(p, c);
                        }
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
    */

    public void solucionInicial5b(int seed){
        HashSet<Integer> pasajerosAsignados = new HashSet<>();
        Random rndm = new Random(seed);

        //Añadimos todos los conductores a los eventos
        for(int i=0; i<M; i++) {
            anadirConductor(i);
        }

        //Para cada conductor hacemos una lista de los conductores que estan dentro de la "zona"
        for(int c=0; c<M; c++){
            HashSet<Integer> pasajerosProbados = new HashSet<>();
            while(pasajerosProbados.size() + pasajerosAsignados.size()<N-M){
                int p = rndm.nextInt(M, N);
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
            int p = rndm.nextInt(M, N);
            while(!pasajerosAsignados.contains(p)){
                int c = rndm.nextInt(0, M);
                anadirPasajero(p, c);
                if (kilometrajeValido(c)) pasajerosAsignados.add(p);
                else eliminarPasajero(p, c);
            }
        }

        distInicial = kilometrajeEstado();
    }
    

    

}