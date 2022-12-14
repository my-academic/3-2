public class Board {
    public int[][] arr;
    public int previous_cost;
    public int huristic_cost;

    Board parent;

    public Board(int[][] arr, int previous_cost) {
        this.arr = arr;
        this.previous_cost = previous_cost + 1;
        huristic_cost = 0;
        total_cost = -1;
    }

    public int total_cost;

    public Board(Board board) {
        this.arr = board.arr;
        this.previous_cost = board.previous_cost;
        this.total_cost = board.total_cost;
        this.huristic_cost = board.huristic_cost;
    }


    @Override
    public String toString() {

        String str = "";
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                str += this.arr[i][j] == 0 ? "* " : this.arr[i][j] + " ";
            }
            str += "\n";
        }
        str += "previous cost = " + previous_cost + " :: huristic cost = " + huristic_cost + " :: total cost = " + total_cost ;
        return str;
    }

    public Board(int size) {
        arr = new int[size][size];
        previous_cost = 0;
        huristic_cost = 0;
        total_cost = 0;
    }


}
