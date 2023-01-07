import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CSP {
    public Variable[][] variables;
    public ArrayList<Variable> unassigneds;

    public CSP(int n) {
        variables = new Variable[n][];
        this.unassigneds = new ArrayList<Variable>();
        for (int i = 0; i < n; i++) {
            variables[i] = new Variable[n];
            for (int j = 0; j < n; j++) {
                variables[i][j] = new Variable();
                variables[i][j].value = 0;
                variables[i][j].setDomains(1, n);
                variables[i][j].row = i;
                variables[i][j].column = j;
                unassigneds.add(variables[i][j]);
            }
        }

    }

    public void setValue(int r, int c, int val) {
        this.variables[r][c].value = val;
        unassigneds.remove(this.variables[r][c]);
        for (int i = 0; i < variables.length; i++) {
            if(i == c) continue;
            variables[r][i].increaseCount(val);
        }
        for (int i = 0; i < variables.length; i++) {
            if(i == r) continue;
            variables[i][c].increaseCount(val);
        }
        // this.variables[r][c].setOnlyOneDomain(val);
    }

    public void removeValue(int r, int c, int val) {
        this.variables[r][c].value = 0;
        unassigneds.add(this.variables[r][c]);
        for (int i = 0; i < variables.length; i++) {
            if(i == c) continue;
            variables[r][i].decreaseCount(val);
        }
        for (int i = 0; i < variables.length; i++) {
            if(i == r) continue;
            variables[i][c].decreaseCount(val);
        }
    }

    @Override
    public String toString() {
        var str = "";
        for (Variable[] variable : variables) {
            System.out.print('[');
            for (Variable v : variable) {
                // System.out.print(v + "\t\t ");
                System.out.print(v + ", ");
            }
            System.out.println("] ,");
            // System.out.println("\n");
            System.out.println();
        }

        return str;
    }

    public boolean isComplete() throws InterruptedException {
        for (Variable[] var : variables) {
            for (Variable variable : var) {
                if (variable.value == 0) {
                    return false;
                }
            }
        }
        // System.out.println("completed");
        // TimeUnit.MINUTES.sleep(1);
        return true;
    }
}
