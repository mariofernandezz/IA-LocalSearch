// IMPLEMENTACIÓN DEL ESTADO

import IA.Comparticion.Usuario;
import IA.Comparticion.Usuarios;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Math.abs;


public class Estado {

   /* ATRIBUTOS */
    private ArrayList<Usuario> usuarios;
    private ArrayList<ArrayList<Integer>> eventos;
    int M;
    int N;

    /*CONSTRUCTOR*/
    public Estado(int n, int m, int seed){
        N = n;
        M = m;
        Usuarios u = new Usuarios(N, M, seed);
        eventos = new ArrayList<ArrayList<Integer>>(M);
        usuarios = new ArrayList<>();
        ordenar(u);
    }

    public Estado(ArrayList<Usuario> u, ArrayList<ArrayList<Integer>> e){
        N = u.size();
        M = e.size();
        eventos = e;
        usuarios = u;
    }

    private void ordenar(Usuarios u) {
        for(int i = 0; i < u.size(); ++i) {
            if(u.get(i).isConductor()) usuarios.add(u.get(i));
        }
        for (Usuario usuario : u) {
            if (!usuario.isConductor()) usuarios.add(usuario);
        }

    }

    public ArrayList<Usuario> getUsuarios(){ return usuarios;}
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
        int c = 0;
        int p = M;
        repartirPasajeros(c, p);
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
                eliminarPasajero(p);
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
    }

    private boolean pasajeroCercano(int actual, int nuevo) {
        int Ax = usuarios.get(actual).getCoordDestinoX();
        int Ay = usuarios.get(actual).getCoordDestinoY();
        int Bx = usuarios.get(nuevo).getCoordOrigenX();
        int By = usuarios.get(nuevo).getCoordOrigenY();
        //System.out.print(distancia(Ax, Ay, Bx, By) + " ** ");
        return 50 > distancia(Ax, Ay, Bx, By);
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
            //System.out.println(Ax + ", " + Ay + ", " + Bx + ", " + By);
            dist += distancia(Ax, Ay, Bx, By);
            Ax = Bx;
            Ay = By;
        }
        dist += distancia(Ax, Ay, usuarios.get(c).getCoordDestinoX(), usuarios.get(c).getCoordDestinoY());
        return dist;
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

    public String conversionString(){
        return eventos.toString();
    }

    /*
    public Estado clone() throws CloneNotSupportedException {
        Estado copia = (Estado) super.clone();
        copia.N = this.N;
        copia.M = this.M;
        copia.usuarios = this.usuarios;
        copia.eventos = this.eventos;
        return copia;
    }*/

    public Estado deepCopy() {
        return new Estado(this.getUsuarios(), this.getEventos());
    }

    @Override
    public Estado clone() {
        return new Estado(this);
    }

    /* // Guardar el estado padre en una variable
     * Estado estadoAnterior = estado;
     *
     * // Crear sucesor
     * Estado estadoNuevo = estadoAnterior.clone();
     */
}