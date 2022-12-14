import java.util.Comparator;

public class BoardComparator implements Comparator<Board> {
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
