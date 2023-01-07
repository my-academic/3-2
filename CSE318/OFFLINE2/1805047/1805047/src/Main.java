import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        Scanner scanner = new Scanner(new File("input.txt"));
//        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        // CSP csp = new CSP(n);
        // Assignment assignment = new Assignment();

        // setValues(csp, assignment);
        int[][] values = new int[n][];


        for (int i = 0; i < n; i++) {
            values[i] = new int[n];
            var line = scanner.nextLine();
            line = line.replaceAll(" |\\|", "");
            String[] temp = line.split(",");
            for (int j = 0; j < temp.length; j++) {
                String str = temp[j];
                var val = Integer.parseInt(str);
                values[i][j] = val;
                // if (val == 0) continue;
                // csp.setValue(i, j, val);
            }
        }
        // System.out.println(csp);
        // return;
        NextVariable[] nextVariables = new NextVariable[5];
        nextVariables[0] = new VAH1();
        nextVariables[1] = new VAH2();
        nextVariables[2] = new VAH3();
        nextVariables[3] = new VAH4();
        nextVariables[4] = new VAH5();
        for (int i = 0; i < 5; i++) {

            if(i == 0 || i == 2 || i == 3 || i == 4) continue;

            CSP csp = new CSP(n);

            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if(values[j][k] == 0) continue;
                    csp.setValue(j, k, values[j][k]);
                }
            }



            System.out.println("vah => " + (i + 1));
            Solver solver = new Solver(nextVariables[i]);
            var x = solver.backtrackingSearch(csp);
            System.out.println("\n================================\n\n");
            System.out.println(x);
            // System.out.println(csp);
            System.out.println(solver);
        }

    }

}
