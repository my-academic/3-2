import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

class Board {
    public int[][] arr;
    public int previous_cost;
    public int huristic_cost;

    public Board(int[][] arr, int previous_cost) {
        this.arr = arr;
        this.previous_cost = previous_cost + 1;
        huristic_cost = 0;
        total_cost = -1;
    }

    public int total_cost;


    @Override
    public String toString() {

        String str = "";
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                str += this.arr[i][j] == 0 ? "* " : this.arr[i][j] + " ";
            }
            str += "\n";
        }
        str += "previous cost = " + previous_cost + " :: huristic cost = " + huristic_cost + " :: total cost = " + total_cost + "\n";
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
        int size = input.nextInt();
        Board board = new Board(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String str = input.next();
                board.arr[i][j] = str.equals("*") ? 0 : Integer.parseInt(str);
            }
        }
        if (solveableOrNot(board, size)) {
            System.out.println("solveable\n=========");
//            System.out.println(board);
            solvePuzzleUsingHammingDistance(board);
            System.out.println("expanded : " + expanded + "\nexplored : " + explored);
            explored = expanded = 0;
            solvePuzzleUsingManhattanDistance(board);
            System.out.println("expanded : " + expanded + "\nexplored : " + explored);

        } else {
            System.out.println("not solveable");
        }
    }

    private static void solvePuzzleUsingManhattanDistance(Board board) {

        System.out.println("starting Manhattan");
        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(new BoardComparator());
        setManhattanDistance(board);
        priorityQueue.add(board);
        while (board.huristic_cost != 0) {
            board = priorityQueue.peek();
            System.out.println(board);
            makeAMove(board, priorityQueue, true, false);

//            long start = System.currentTimeMillis();
//            long end = start + 100;
//            while(true) {
//                //do your code
//                //
//                if(System.currentTimeMillis() > end) {
//                    break;
//                }
//            }
        }
    }

    private static void solvePuzzleUsingHammingDistance(Board board) {
        System.out.println("starting Hamming");
        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(new BoardComparator());
        setManhattanDistance(board);
        priorityQueue.add(board);
        while (board.huristic_cost != 0) {
            board = priorityQueue.peek();
            System.out.println(board);
            makeAMove(board, priorityQueue, false, true);

//            long start = System.currentTimeMillis();
//            long end = start + 100;
//            while(true) {
//                if(System.currentTimeMillis() > end) {
//                    break;
//                }
//            }
        }
    }

    private static int[][] pairs = { {0, -1 }, { 0, 1}, { -1, 0 },  { 1, 0} };

    private static void makeAMove(Board b, PriorityQueue<Board> priorityQueue, boolean manattan, boolean hamming) {
        priorityQueue.clear();
        int blank_row = 0, blank_column = 0;
        for (int i = 0; i < b.arr.length; i++) {
            for (int j = 0; j < b.arr[i].length; j++) {
                if (b.arr[i][j] == 0) {
                    blank_row = i;
                    blank_column = j;
                }
            }
        }
        explored ++;
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
            if(swap_row < 0 || swap_row >= b.arr.length || swap_column < 0 || swap_column >= b.arr.length) continue;
            arr[blank_row][blank_column] = arr[swap_row][swap_column];
            arr[swap_row][swap_column] = 0;
            Board board = new Board(arr, b.previous_cost);
            expanded ++;

            if(hamming) setHammingDistance(board);
            if(manattan) setManhattanDistance(board);
            priorityQueue.add(board);

        }
    }

    private static void setManhattanDistance(Board b) {
        b.huristic_cost = 0;
        for (int i = 0; i < b.arr.length; i++) {
            for (int j = 0; j < b.arr[i].length; j++) {
                int exact_column = (b.arr[i][j] - 1)% b.arr.length;
                int exact_row = (b.arr[i][j] - 1) / b.arr.length;
                if (b.arr[i][j] != 0) b.huristic_cost += Math.abs(exact_column - j) + Math.abs(exact_row - i) ;
            }
        }
        b.total_cost = b.previous_cost + b.huristic_cost;
    }

    private static void setHammingDistance(Board b) {
        b.huristic_cost = 0;
        for (int i = 0; i < b.arr.length; i++) {
            for (int j = 0; j < b.arr[i].length; j++) {
                if (b.arr[i][j] != 0 && b.arr[i][j] != b.arr.length * i + j + 1) b.huristic_cost++;
            }
        }
        b.total_cost = b.previous_cost + b.huristic_cost;
    }
}