import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Deque is a double ended queue, is a generalization of a stack and a queue that supports adding and removing items
 * from either the front or the back of the data structure.
 */
public class Deque<Item> implements Iterable<Item> {

  private static class Node<Item> {
    private final Item item;
    private Node<Item> next;
    private Node<Item> previous;

    public Node(Item item, Node<Item> next, Node<Item> previous) {
      this.item = item;
      this.next = next;
      this.previous = previous;
    }
  }

  private Node<Item> head;
  private Node<Item> tail = head;

  private int size = 0;

  // construct an empty deque
  public Deque() {
    newHead(null);
  }

  // is the deque empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the deque
  public int size() {
    return size;
  }

  // add the item to the front
  public void addFirst(Item item) {
    ensureNotNull(item);
    if (size > 0) {
      Node<Item> node = new Node(item, head, null);
      head.previous = node;
      head = node;
    } else {
      newHead(item);
    }
    size++;
  }

  // add the item to the end
  public void addLast(Item item) {
    ensureNotNull(item);
    if (size > 0) {
      Node<Item> node = new Node(item, null, tail);
      tail.next = node;
      tail = node;
    } else {
      newHead(item);
    }
    size++;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    ensureNotEmpty();
    Node<Item> node = head;
    if (size > 1) {
      head = head.next;
    } else {
      newHead(null);
    }
    size--;
    return node.item;
  }

  // remove and return the item from the end
  public Item removeLast() {
    ensureNotEmpty();
    Node<Item> node = tail;
    if (size > 1) {
      tail = tail.previous;
    } else {
      newHead(null);
    }
    size--;
    return node.item;
  }

  // return an iterator over items in order from front to end
  public Iterator<Item> iterator() {
    return new Iterator<Item>() {
      private Node<Item> node = head;

      @Override
      public boolean hasNext() {
        return node != null;
      }

      @Override
      public Item next() {
        if (node == null) {
          throw new NoSuchElementException("iterator finished");
        }
        Item item = node.item;
        node = node.next;
        return item;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("do not remove in iterator");
      }
    };
  }

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

  private void newHead(Item item) {
    head = new Node<>(item, null, null);
    tail = head;
  }

  // unit testing
  public static void main(String[] args) {
    final Deque<String> d = new Deque<>();
    String val;
    boolean caught;

    // Simple test of adding and removing one item
    ass(d.isEmpty());
    d.addLast("A");
    ass(!d.isEmpty());
    val = d.removeFirst();
    ass("A".equals(val));
    ass(d.isEmpty());

    // Adding and removing multiple items from both sides
    d.addLast("D");
    d.addLast("E");
    d.addLast("F");
    d.addFirst("C");
    d.addFirst("B");
    d.addFirst("A");
    val = "";
    for (int i = 0; i < 6; i++) {
      val += d.removeFirst();
    }
    ass("ABCDEF".equals(val));
    ass(d.isEmpty());

    // Removing from empty throws exception
    caught = false;
    try {
      d.removeFirst();
    } catch (NoSuchElementException e) {
      caught = true;
    }
    ass(caught);

    // Iterator
    d.addFirst("S");
    d.addFirst("G");
    d.addFirst("R");
    d.addFirst("A");
    d.addFirst("K");
    ass(d.size() == 5);
    Iterator<String> it = d.iterator();
    val = "";
    while (it.hasNext()) {
      val += it.next();
    }
    ass("KARGS".equals(val));
    ass(d.size() == 5);

    // Next on finished iterator throws exception
    caught = false;
    try {
      it.next();
    } catch (NoSuchElementException e) {
      caught = true;
    }
    ass(caught);
  }

  private static void ass(boolean exp) {
    if (!exp) {
      throw new RuntimeException("assertion not true");
    }
  }
}
