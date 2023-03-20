import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Cuantos participantes hay? (N)");
        int N = scan.nextInt();
        System.out.println("Cuantos son conductores? (M)");
        int M = scan.nextInt();
        System.out.println("Introduce una seed");
        int seed = scan.nextInt();

        Estado estado = new Estado(N, M, seed);

        estado.solucionInicial1();
        // estado.solucionInicial2();

        //**************** Estado Inicial *********************
        // print del estado inicial

        //Problem problem = new Problem(estado, new SuccessorFunction(), new GoalTest(), new heuristicFunction());
        //Search search = new ...;
        //Search agent = new SearchAgent(problem, search);

        //**************** Estado Final ***********************
        // print del estado final
    }
}
