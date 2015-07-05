/**
 * PercolationStats performs a series of computational experiments.
 */
public class PercolationStats {

    private final double mMean;
    private final double mStddev;
    private final double[] mConfidence;

    /**
     * Perform T independent experiments on an N-by-N grid
     */
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("n <= 0 || t <= 0");
        }

        final double sites = (double) n * n;
        final double[] experiments = new double[t];
        for (int i = 0; i < t; i++) {
            final int opened = monteCarloSimulation(n);
            experiments[i] = opened / sites;
        }

        mMean = StdStats.mean(experiments);
        mStddev = StdStats.stddev(experiments);
        mConfidence = confidence(mMean, mStddev, t);
    }

    private double[] confidence(double mean, double stddev, int t) {
        double low = mean - ((1.96 * stddev) / Math.sqrt(t));
        double high = mean + ((1.96 * stddev) / Math.sqrt(t));
        return new double[] {low, high};
    }

    /**
     * Sample mMean of percolation threshold
     */
    public double mean() {
        return mMean;
    }

    /**
     * Sample standard deviation of percolation threshold
     */
    public double stddev() {
        return mStddev;
    }

    /**
     * Low endpoint of 95% mConfidence interval
     */
    public double confidenceLo() {
        return mConfidence[0];
    }

    /**
     * Low endpoint of 95% mConfidence interval
     */
    public double confidenceHi() {
        return mConfidence[1];
    }

    private int monteCarloSimulation(int n) {
        final Percolation p = new Percolation(n);
        int opened = 0;
        do {
            final int i = StdRandom.uniform(1, n + 1);
            final int j = StdRandom.uniform(1, n + 1);
            if (p.isOpen(i, j)) {
                continue;
            }
            p.open(i, j);
            opened++;
        } while (!p.percolates() || opened >= n * n);
        return opened;
    }

    /**
     * Test client
     *
     * a main() method that takes two command-line arguments N and T, performs T independent computational experiments
     * (discussed above) on an N-by-N grid, and prints out the mMean, standard deviation, and the 95% mConfidence interval
     * for the percolation threshold. Use standard random from our standard libraries to generate random numbers; use
     * standard statistics to compute the sample mMean and standard deviation.
     */
    public static void main(String[] args) {
        int t, n;
        try {
            t = Integer.valueOf(args[0]);
            n = Integer.valueOf(args[1]);
            final PercolationStats ps = new PercolationStats(t, n);
            StdOut.println(String.format("%-20s = %.10f", "mean", ps.mean()));
            StdOut.println(String.format("%-20s = %.10f", "stddev", ps.stddev()));
            StdOut.println(String.format("%-20s = %.10f, %.10f", "95% confidence inter", ps.confidenceLo(), ps.confidenceHi()));
        } catch (IllegalArgumentException e) {
            StdOut.println("Invalid arguments: " + e.getMessage());
            return;
        }
    }
}
