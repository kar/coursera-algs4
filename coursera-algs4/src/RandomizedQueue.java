import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * RandomizedQueue is similar to a stack or queue, except that the item removed is chosen uniformly at random from items
 * in the data structure.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[] index = (Item[]) new Object[10];

  private int size = 0;

  // construct an empty randomized queue
  public RandomizedQueue() {
  }

  // is the queue empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the queue
  public int size() {
    return size;
  }

  // add the item
  public void enqueue(Item item) {
    ensureNotNull(item);
    index[size++] = item;
    adjustIndexSize();
  }

  // remove and return a random item
  public Item dequeue() {
    ensureNotEmpty();
    int i = StdRandom.uniform(size);
    Item item = index[i];
    index[i] = index[size - 1]; // Put last item at chosen item's place to have no gaps
    index[--size] = null; // Drop reference to the taken item and decrease the size
    adjustIndexSize();
    return item;
  }

  // return (but do not remove) a random item
  public Item sample() {
    ensureNotEmpty();
    int i = StdRandom.uniform(size);
    return index[i];
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomizedIterator<>();
  }

  private class RandomizedIterator<Item> implements Iterator<Item> {
    private final RandomizedQueue<Item> q;

    private RandomizedIterator() {
      q = new RandomizedQueue<>();
      for (int i = 0; i < size; i++) {
        q.enqueue((Item) index[i]);
      }
    }

    @Override
    public boolean hasNext() {
      return !q.isEmpty();
    }

    @Override
    public Item next() {
      return q.dequeue();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("do not remove in iterator");
    }
  };

  private void ensureNotNull(Item item) {
    if (item == null) {
      throw new NullPointerException("null item");
    }
  }

  private void ensureNotEmpty() {
    if (isEmpty()) {
      throw new NoSuchElementException("empty");
    }
  }

  private void adjustIndexSize() {
    if (size > index.length / 2) {
      resizeIndex(index.length * 2);
    } else if (size < index.length / 4) {
      resizeIndex(index.length / 2);
    }
  }

  private void resizeIndex(int newSize) {
    Item[] newIndex = (Item[]) new Object[newSize];
    for (int i = 0; i < size; i++) {
      newIndex[i] = index[i];
    }
    index = newIndex;
  }

  // unit testing
  public static void main(String[] args) {
    final RandomizedQueue<String> d = new RandomizedQueue<>();
    String val;
    int i;
    boolean caught;

    // Simple test of adding and removing one item
    ass(d.isEmpty());
    d.enqueue("A");
    ass(!d.isEmpty());
    val = d.dequeue();
    ass("A".equals(val));
    ass(d.isEmpty());

    // Adding and removing multiple items
    addDEFCBA(d);
    val = "";
    for (i = 0; i < 6; i++) {
      val += d.dequeue();
    }
    ass(val.length() == 6);
    ass(val.contains("D"));
    ass(val.contains("B"));
    ass(!val.contains("G"));
    ass(d.isEmpty());

    // Check for randomness for same input
    addDEFCBA(d);
    String val2 = "";
    for (i = 0; i < 6; i++) {
      val2 += d.dequeue();
    }
    StdOut.println(val + " - " + val2);

    // Removing from empty throws exception
    caught = false;
    try {
      d.dequeue();
    } catch (NoSuchElementException e) {
      caught = true;
    }
    ass(caught);

    // Iterator
    addDEFCBA(d);
    ass(d.size() == 6);
    Iterator<String> it = d.iterator();
    val = "";
    i = 0;
    while (it.hasNext()) {
      val += it.next();
      i++;
    }
    ass(d.size() == 6);
    ass(val.length() == 6);
    ass(i == 6);

    // Next on finished iterator throws exception
    caught = false;
    try {
      it.next();
    } catch (NoSuchElementException e) {
      caught = true;
    }
    ass(caught);

    // Nested iterators do not affect each other
    Iterator<String> it2 = d.iterator();
    i = 0;
    while (it2.hasNext()) {
      it2.next();
      i++;
      Iterator<String> itInner = d.iterator();
      int j = 0;
      while (itInner.hasNext()) {
        itInner.next();
        j++;
      }
      ass(j == 6);
    }
    ass(i == 6);
  }

  private static void addDEFCBA(RandomizedQueue q) {
    q.enqueue("D");
    q.enqueue("E");
    q.enqueue("F");
    q.enqueue("C");
    q.enqueue("B");
    q.enqueue("A");
  }

  private static void ass(boolean exp) {
    if (!exp) {
      throw new RuntimeException("assertion not true");
    }
  }
}
