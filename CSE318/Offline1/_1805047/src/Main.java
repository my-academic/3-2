import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class IntIntArray {
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

class Board {
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

class BoardComparator implements Comparator<Board> {
    public int compare(Board board1, Board board2) {
        if (board1.total_cost < board2.total_cost)
            return -1;
        else if (board1.total_cost > board2.total_cost)
            return 1;
        else if (board1.huristic_cost > board2.huristic_cost)
            return 1;
        else if (board1.huristic_cost < board2.huristic_cost)
            return -1;
        else if (board1.previous_cost > board2.previous_cost)
            return 1;
        else if (board1.previous_cost < board2.previous_cost)
            return -1;

        return 0;
    }
}


public class Main {

    static int explored = 0;
    static int expanded = 0;

    static int getNumberOfInversions(int[] board) {
        int inversions = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = i + 1; j < board.length; j++) {
                if (board[i] != 0 && board[j] != 0 && board[i] > board[j]) {
                    inversions++;
                }
            }
        }
        return inversions;
    }

    static boolean solveableOrNot(Board board, int size) {

        int[] board1D = new int[size * size];
        int k = 0;
        int blank_row = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board1D[k++] = board.arr[i][j];
                if (board.arr[i][j] == 0) {
                    blank_row = i;
                }
            }
        }

        int blank_row_from_bottom = size - blank_row;

        int inversions = getNumberOfInversions(board1D);
        if ((size & 1) == 0) {
            // even
            if ((blank_row_from_bottom & 1) == 0 && (inversions & 1) != 0) {
                return true;
            } else if ((blank_row_from_bottom & 1) != 0 && (inversions & 1) == 0) {
                return true;
            }
            return false;
        } else {
            // odd
            if ((inversions & 1) == 0) {
                return true;
            } else return false;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input.txt");
        Scanner input = new Scanner(file);
//        int iter = input.nextInt();
        int size = input.nextInt();
//        int size = 4;
        Board board = new Board(size);
//        for (int x = 1; x <= iter; x++) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String str = input.next();
                board.arr[i][j] = str.equals("*") ? 0 : Integer.parseInt(str);
            }
        }
        if (solveableOrNot(board, size)) {
            explored = expanded = 0;
            solvePuzzleUsingHammingDistance(board);
            System.out.println("expanded : " + expanded + "\texplored : " + explored);
            explored = expanded = 0;
            boardHashMap.clear();
            boardHashMapDone.clear();
            System.out.println("\n");
            solvePuzzleUsingManhattanDistance(board);
            System.out.println("expanded : " + expanded + "\texplored : " + explored);
            boardHashMap.clear();
            boardHashMapDone.clear();
        } else {
            System.out.println("unsolvable");
        }
//        }
    }

    private static int step = 0;


    private static void printPath(Board board) {
        if(board == null) return;
        printPath(board.parent);
        System.out.println("=============================");
        System.out.println("step : " + step);
        step++;
        System.out.println(board);
        System.out.println("=============================");
    }


    private static HashMap<IntIntArray, Board> boardHashMap = new HashMap<IntIntArray, Board>();
    private static HashMap<IntIntArray, Board> boardHashMapDone = new HashMap<IntIntArray, Board>();

    private static void solvePuzzleUsingManhattanDistance(Board board) {
        System.out.println("starting Manhattan");

        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(new BoardComparator());

        board.parent = null;
        setManhattanDistance(board);
        priorityQueue.add(board);
        boardHashMap.put(new IntIntArray(board.arr), board);
        while (board.huristic_cost != 0) {
            board = priorityQueue.poll();
            makeAMove(board, priorityQueue, true, false);
        }
        System.out.println("optimal cost : " + board.total_cost);
        System.out.println("printing optimal path:");
        step = 0;
        printPath(board);
    }



    private static void solvePuzzleUsingHammingDistance(Board board) {
        System.out.println("starting Hamming");
        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(new BoardComparator());
        setHammingDistance(board);
        priorityQueue.add(board);
        boardHashMap.put(new IntIntArray(board.arr), board);
        while (board.huristic_cost != 0) {
            board = priorityQueue.poll();
            makeAMove(board, priorityQueue, false, true);
        }
        System.out.println("optimal cost : " + board.total_cost);
        System.out.println("printing optimal path:");
        step = 0;
        printPath(board);
    }

    private static int[][] pairs = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

    private static void makeAMove(Board b, PriorityQueue<Board> priorityQueue, boolean manattan, boolean hamming) {
//        priorityQueue.clear();
        int blank_row = 0, blank_column = 0;
        for (int i = 0; i < b.arr.length; i++) {
            for (int j = 0; j < b.arr[i].length; j++) {
                if (b.arr[i][j] == 0) {
                    blank_row = i;
                    blank_column = j;
                }
            }
        }
        explored++;
        for (int i = 0; i < pairs.length; i++) {
//            System.out.println(b);
            int[][] arr = new int[b.arr.length][b.arr.length];
            for (int j = 0; j < b.arr.length; j++) {
                for (int k = 0; k < b.arr.length; k++) {
                    arr[j][k] = b.arr[j][k];
                }
            }
            int swap_row = blank_row + pairs[i][0];
            int swap_column = blank_column + pairs[i][1];
            if (swap_row < 0 || swap_row >= b.arr.length || swap_column < 0 || swap_column >= b.arr.length) continue;
            arr[blank_row][blank_column] = arr[swap_row][swap_column];
            arr[swap_row][swap_column] = 0;
            if (boardHashMapDone.containsKey(new IntIntArray(arr))) {
                continue;
            }
            Board board = new Board(arr, b.previous_cost);

            if (hamming) setHammingDistance(board);
            if (manattan) setManhattanDistance(board);
            if (!boardHashMap.containsKey(new IntIntArray(arr))) {
                board.parent = b;
                priorityQueue.add(board);

                expanded++;
                boardHashMap.put(new IntIntArray(arr), board);
            }
            else if (hamming && boardHashMap.get(new IntIntArray(arr)).total_cost > board.total_cost) {
                priorityQueue.remove(boardHashMap.get(new IntIntArray(arr)));
                board.parent = b;
                priorityQueue.add(board);
                boardHashMap.replace(new IntIntArray(arr), board);
            }

        }
        boardHashMapDone.put(new IntIntArray(b.arr), b);
    }

    private static void setManhattanDistance(Board b) {
        b.huristic_cost = 0;
        for (int i = 0; i < b.arr.length; i++) {
            for (int j = 0; j < b.arr[i].length; j++) {
                int exact_column = (b.arr[i][j] - 1) % b.arr.length;
                int exact_row = (b.arr[i][j] - 1) / b.arr.length;
                if (b.arr[i][j] != 0) b.huristic_cost += Math.abs(exact_column - j) + Math.abs(exact_row - i);
            }
        }
        b.total_cost = b.previous_cost + b.huristic_cost;
    }

    private static void setHammingDistance(Board b) {
        b.huristic_cost = 0;
//        System.out.println(b.arr.length);
        for (int i = 0; i < b.arr.length; i++) {
            for (int j = 0; j < b.arr[i].length; j++) {
                if (b.arr[i][j] != 0 && b.arr[i][j] != b.arr.length * i + j + 1) b.huristic_cost++;
            }
        }
        b.total_cost = b.previous_cost + b.huristic_cost;
    }
}