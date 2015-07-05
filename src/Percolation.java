/**
 * Percolation
 * <p/>
 * Corner cases.  By convention, the row and column indices i and j are integers between 1 and N, where (1, 1) is the
 * upper-left site: Throw a java.lang.IndexOutOfBoundsException if any argument to open(), isOpen(), or isFull() is
 * outside its prescribed range.
 */
public class Percolation {

    private static final byte CLOSED = 0;
    private static final byte OPEN   = 1;

    private int n;

    private final WeightedQuickUnionUF qu;
    private final byte[][] grid;

    /**
     * Create N-by-N grid, with all sites blocked.
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n < 0");
        }
        this.n = n;
        grid = new byte[n][n];
        qu = new WeightedQuickUnionUF(n * n + 2); // 2 for top and bottom node
    }

    /**
     * Open site (row i, column j) if it is not open already.
     *
     * @param i
     * @param j
     */
    public void open(int i, int j) {
        throwIfBagArguments(i, j);
        grid[c(i)][c(j)] = OPEN;
        connectNeighborIfExists(i, j, i + 1, j);
        connectNeighborIfExists(i, j, i, j + 1);
        connectNeighborIfExists(i, j, i - 1, j);
        connectNeighborIfExists(i, j, i, j - 1);
    }

    private void connectNeighborIfExists(int i, int j, int i2, int j2) {
        try {
            byte neighbor = grid[c(i2)][c(j2)];
            if (neighbor == OPEN) {
                qu.union(ijTo1D(i, j), ijTo1D(i2, j2));
            }
        } catch (IndexOutOfBoundsException e) {
            if (i2 == 0 || i2 == n + 1) {
                // Top or bottom row, connect to special node
                int edgeNode = 0;
                if (i2 == n + 1) {
                    edgeNode = 1;
                }
                qu.union(ijTo1D(i, j), edgeNode);
            }
        }
    }

    /**
     * Is site (row i, column j) open?
     *
     * @param i
     * @param j
     */
    public boolean isOpen(int i, int j) {
        throwIfBagArguments(i, j);
        return grid[c(i)][c(j)] == OPEN;
    }

    /**
     * Is site (row i, column j) full?
     *
     * @param i
     * @param j
     */
    public boolean isFull(int i, int j) {
        throwIfBagArguments(i, j);
        return grid[c(i)][c(j)] == OPEN && qu.connected(0, ijTo1D(i, j));
    }

    /**
     * Does the system percolate?
     */
    public boolean percolates() {
        return qu.connected(0, 1);
    }

    private void throwIfBagArguments(int i, int j) {
        if (i < 1 || j < 1) {
            throw new IndexOutOfBoundsException("Should be one-indexed");
        } else if (i > n || j > n) {
            throw new IndexOutOfBoundsException("Should not be greater than n");
        }
    }

    private int c(int i) {
        return i - 1;
    }

    private int ijTo1D(int i, int j) {
        return (i - 1) * n + (j - 1) + 2;
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(1, 3);
        p.open(1, 2);
        p.open(1, 1);
        p.open(3, 1);
        p.open(3, 2);
        p.open(3, 1);
    }
}
