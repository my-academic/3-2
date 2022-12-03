import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

class Board {
    public int[][] a;
    public int cost;
    public Board(int size) {
        a = new int[size][size];
        cost = 0;
    }

}

class BoardComparator implements Comparator<Board> {

    // Overriding compare()method of Comparator
    // for descending order of cgpa
    public int compare(Board board1, Board board2) {
        if (board1.cost < board2.cost)
            return 1;
        else if (board1.cost > board2.cost)
            return -1;
        return 0;
    }
}


public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(new BoardComparator());
        int size = input.nextInt();
        Board board = new Board(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board.a[i][j] = input.nextInt();
            }
        }
    }
}