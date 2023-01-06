import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Variable {
    public int value;
    public ArrayList<Integer> domains;
    public int row;
    public int column;
    public HashMap<Integer, Integer> count;
    public int degree;

    public Variable() {
        this.value = 0;
        this.domains = null;
        this.row = -1;
        this.column = -1;
        this.count = null;
        this.degree = 0;
    }

    public void setDomains(int start, int end) {
        this.domains = new ArrayList<>(end - start + 1);
        this.count = new HashMap<>();
        for (int i = start; i < end + 1; i++) {
            this.domains.add(i);
            this.count.put(i, 0);
        }
        this.degree = 2 * (end - start) + 1;
    }

    public void increaseCount(int domain) {
        this.degree --;

        if (value != 0)
            return;
        this.count.put(domain, this.count.get(domain) + 1);
        this.domains.removeAll(Arrays.asList(domain));
        // System.out.println("removing => " + this);
        // Collections.sort(this.domains);
    }

    public void decreaseCount(int domain) {
        this.degree ++;
        if (value != 0)
            return;
        // System.out.println(domain + " " + row + " " + column + " " + "=>" +
        // this.count.get(domain));
        if(this.count.get(domain) == 0){
            System.out.println("here");
        }
        this.count.put(domain, Math.max(0, this.count.get(domain) - 1));
        // System.out.println(domain + " " + row + " " + column + " " + "=>" +
        // this.count.get(domain));
        if (this.count.get(domain) == 0 ) {
            // System.out.println(this);
            this.domains.add(Integer.valueOf(domain));
            // System.out.println(this.domains);
            // Collections.sort(this.domains);
            // System.out.println("here => " + this);
        }
    }

    public void setOnlyOneDomain(int value) {
        this.domains.clear();
        this.domains.add(value);
    }

    @Override
    public String toString() {
        return "" + value;
        // System.out.println(row + " " + column );
        // return "Variable [value=" + value + ", domains=" + domains + ", count=" +
        // count + "]";
        // return "Variable [value=" + value + ", domains=" + domains + "]";
    }

}
