import java.util.Arrays;

public class IntIntArray {
    private int[][] value;


    public IntIntArray(int[][] value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntIntArray that = (IntIntArray) o;
        return Arrays.deepEquals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(value);
    }
}
