import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

interface NextVariable {
    Variable getNextVariable(CSP csp);
}

class VAH1 implements NextVariable {

    public Variable getNextVariable(CSP csp) {
        var variables = csp.variables;
        Variable variable = null;
        for (Variable[] variables2 : variables) {
            for (Variable variable2 : variables2) {
                if (variable2.value == 0 && (variable == null || variable2.domains.size() < variable.domains.size())) {
                    variable = variable2;
                }
            }
        }
        // var key = variable.row * variables.length + variable.column;
        // if(!count.containsKey(key)) {
        // count.put(key, 0);
        // }
        // count.put(key, count.get(key) + 1);
        return variable;
    }
}

class VAH2 implements NextVariable {

    public Variable getNextVariable(CSP csp) {
        var variables = csp.variables;
        Variable variable = null;
        for (int i = 0; i < variables.length; i++) {
            for (int j = 0; j < variables.length; j++) {
                var var = variables[i][j];
                if (var.value == 0 && (variable == null || var.degree > variable.degree)) {
                    variable = var;
                }
            }
        }
        return variable;
    }
}

class VAH3 implements NextVariable {

    public Variable getNextVariable(CSP csp) {
        var variables = csp.variables;
        Variable variable = null;
        for (Variable[] variables2 : variables) {
            for (Variable variable2 : variables2) {
                if(variable2.value != 0) continue;
                if(variable == null) {
                    variable = variable2; continue;
                }
                if ((variable2.domains.size() > variable.domains.size())) continue;
                else if ((variable2.domains.size() < variable.domains.size()) || variable2.degree > variable.degree) {
                    variable = variable2;
                }
            }
        }
        return variable;
    }
}

class VAH4 implements NextVariable {

    public Variable getNextVariable(CSP csp) {
        var variables = csp.variables;
        Variable variable = null;
        for (Variable[] variables2 : variables) {
            for (Variable variable2 : variables2) {
                if(variable2.value != 0) continue;
                if(variable == null) {
                    variable = variable2;
                    continue;
                }
                double x2 = variable2.degree == 0 ? Integer.MAX_VALUE : variable2.domains.size() / (double) variable2.degree;
                double x = variable.degree == 0 ? Integer.MAX_VALUE : variable.domains.size() / (double) variable.degree;
                if (x2 < x) {
                    variable = variable2;
                }
            }
        }
        return variable;
    }
}

class VAH5 implements NextVariable {

    public Variable getNextVariable(CSP csp) {
        Random random = new Random();
        int i = random.nextInt(csp.unassigneds.size());
        return csp.unassigneds.get(i);
    }
}

public class VariableOrderHeuristic {
    // HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();
    Variable getNextVariableWithVAH1(CSP csp) {
        var variables = csp.variables;
        Variable variable = null;
        for (Variable[] variables2 : variables) {
            for (Variable variable2 : variables2) {
                if (variable2.value == 0 && (variable == null || variable2.domains.size() < variable.domains.size())) {
                    variable = variable2;
                }
            }
        }
        // var key = variable.row * variables.length + variable.column;
        // if(!count.containsKey(key)) {
        // count.put(key, 0);
        // }
        // count.put(key, count.get(key) + 1);
        return variable;
    }

    Variable getNextVariableWithVAH2(CSP csp) {
        var variables = csp.variables;
        Variable variable = null;
        for (int i = 0; i < variables.length; i++) {
            for (int j = 0; j < variables.length; j++) {
                var var = variables[i][j];
                if (var.value == 0 && (variable == null || var.degree > variable.degree)) {
                    variable = var;
                }
            }
        }
        return variable;
    }

    Variable getNextVariableWithVAH3(CSP csp) {
        // getNextVariableWithVAH2(csp);
        var variables = csp.variables;
        Variable variable = null;
        for (Variable[] variables2 : variables) {
            for (Variable variable2 : variables2) {
                if(variable2.value != 0) continue;
                if(variable == null) {
                    variable = variable2; continue;
                }
                if ((variable2.domains.size() > variable.domains.size())) continue;
                else if ((variable2.domains.size() < variable.domains.size()) || variable2.degree > variable.degree) {
                    variable = variable2;
                }
            }
        }
        return variable;
    }

    Variable getNextVariableWithVAH4(CSP csp) {
        // getNextVariableWithVAH2(csp);
        var variables = csp.variables;
        Variable variable = null;
        for (Variable[] variables2 : variables) {
            for (Variable variable2 : variables2) {
                if(variable2.value != 0) continue;
                if(variable == null) {
                    variable = variable2;
                    continue;
                }
                double x2 = variable2.degree == 0 ? Integer.MAX_VALUE : variable2.domains.size() / (double) variable2.degree;
                double x = variable.degree == 0 ? Integer.MAX_VALUE : variable.domains.size() / (double) variable.degree;
                if (x2 < x) {
                    variable = variable2;
                }
            }
        }
        return variable;
    }



    Variable getNextVariableWithVAH5(CSP csp) {
        Random random = new Random();
        int i = random.nextInt(csp.unassigneds.size());
        return csp.unassigneds.get(i);
    }

    Variable getNextVariable(CSP csp) {
        return getNextVariableWithVAH4(csp);
    }

    public void print() {
        // ArrayList<Integer> sortedKeys = new ArrayList<>(count.keySet());

        // Collections.sort(sortedKeys);

        // // Display the TreeMap which is naturally sorted
        // for (var x : sortedKeys)
        // System.out.println("Key = " + x
        // + ", Value = " + count.get(x));
    }
}
