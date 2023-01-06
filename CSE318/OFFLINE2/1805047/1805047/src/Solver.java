import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Solver {

    NextVariable variableOrderHeuristic;

    public long totalNode = 0;
    public long totalBacktrack = 0;
    public long runtime = 0;

    public Solver(NextVariable nv) {
        this.variableOrderHeuristic = nv;
    }

    CSP backtrackingSearch(CSP csp) throws InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println(java.time.LocalTime.now());
        var x = backtrack(csp);
        long end = System.currentTimeMillis();
        runtime = (int) (end - start);
        return x;
    }

    CSP backtrack(CSP csp) throws InterruptedException {
        totalNode++;
        // System.out.println(totalNode);
        if (csp.isComplete())
            return csp;
        Variable variable = variableOrderHeuristic.getNextVariable(csp);
        var domains = valueOrder(csp.variables, variable);
        for (int i = 0; i < domains.size(); i++) {
            var domain = domains.get(i);
            csp.setValue(variable.row, variable.column, domain.intValue());
            // TimeUnit.MILLISECONDS.sleep(0);
            CSP result = backtrack(csp);
            if (result != null) {
                return result;
            }
            csp.removeValue(variable.row, variable.column, domain.intValue());
        }
        totalBacktrack++;
        return null;
    }

    private ArrayList<Integer> valueOrder(Variable[][] variables, Variable variable) {
        ArrayList<Integer> domains = new ArrayList<>();
        // HashMap<Integer, Integer> count = new HashMap<>();
        // for (int i = 0; i < variables.length; i++) {
        // var ds = variables[i][variable.column].domains;
        // if (i != variable.row)
        // for (Integer x : ds) {
        // if (count.containsKey(x)) {
        // count.put(x, count.get(x) + 1);
        // } else {
        // count.put(x, 1);
        // }
        // }
        // ds = variables[variable.row][i].domains;
        // if (i != variable.column)
        // for (Integer x : ds) {
        // if (count.containsKey(x)) {
        // count.put(x, count.get(x) + 1);
        // } else {
        // count.put(x, 1);
        // }
        // }
        // }
        // List<Map.Entry<Integer, Integer> > list =
        // new LinkedList<Map.Entry<Integer, Integer> >(count.entrySet());

        // // Sort the list
        // Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() {
        // public int compare(Map.Entry<Integer, Integer> o1,
        // Map.Entry<Integer, Integer> o2)
        // {
        // return (o1.getValue()).compareTo(o2.getValue());
        // }
        // });
        // for (Map.Entry<Integer, Integer> aa : list) {
        // if(variable.domains.contains(aa.getKey())) {
        // domains.add(aa.getKey());
        // }
        // }
        for (int i = 0; i < variable.domains.size(); i++) {
            domains.add(variable.domains.get(i));
        }
        Collections.shuffle(domains);
        return domains;
    }

    @Override
    public String toString() {
        return "Solver [totalNode = " + totalNode + ", totalBacktrack = " + totalBacktrack + ", runtime = " + runtime
                + " ms]";
    }

    CSP forwardCheckingSearch(CSP csp) throws InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println(java.time.LocalTime.now());
        var x = forwardChecking(csp);
        long end = System.currentTimeMillis();
        runtime = (int) (end - start);
        return x;
    }

    CSP forwardChecking(CSP csp) throws InterruptedException {
        totalNode++;
        // System.out.println(totalNode);
        if (csp.isComplete())
            return csp;
        Variable variable = variableOrderHeuristic.getNextVariable(csp);
        var domains = valueOrder(csp.variables, variable);
        for (int i = 0; i < domains.size(); i++) {
            var domain = domains.get(i);
            csp.setValue(variable.row, variable.column, domain.intValue());
            // TimeUnit.MILLISECONDS.sleep(0);
            if (checkDomains(csp.variables)) {
                CSP result = forwardChecking(csp);
                if (result != null) {
                    return result;
                }
            }
            csp.removeValue(variable.row, variable.column, domain.intValue());
        }
        totalBacktrack++;
        return null;
    }

    private boolean checkDomains(Variable[][] variables) {
        for (int i = 0; i < variables.length; i++) {
            for (int j = 0; j < variables[i].length; j++) {
                if (variables[i][j].domains.size() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
