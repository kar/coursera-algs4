import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A CLI client that takes a command-line integer k; reads in a sequence of N strings from standard input using
 * StdIn.readString(); and prints out exactly k of them, uniformly at random.
 */
public class Subset {

  public static void main(String[] args) {
    int k;
    try {
      k = Integer.valueOf(args[0]);
      if (k < 1) {
        throw new IllegalArgumentException("k < 1");
      }
    } catch (IllegalArgumentException e) {
      StdOut.println("Invalid arguments: " + e.getMessage());
      return;
    } catch (ArrayIndexOutOfBoundsException e) {
      StdOut.println("No arguments");
      return;
    }

    RandomizedQueue<String> q = new RandomizedQueue<>();
    while (!StdIn.isEmpty()) {
      q.enqueue(StdIn.readString());
    }

    if (q.isEmpty()) {
      StdOut.println("No input");
      return;
    }

    Iterator<String> it = q.iterator();
    while (it.hasNext()) {
      StdOut.println(it.next());
      if (--k == 0) {
        return;
      }
    }

    StdOut.println("Not enough input");
  }

}
