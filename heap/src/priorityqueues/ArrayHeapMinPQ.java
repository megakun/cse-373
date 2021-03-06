package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    Map<T, Integer> map;
    private int size;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        items.add(new PriorityNode<T>(null, 0.0));
        map = new HashMap<>();
        size = 0;
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);

        map.put(items.get(b).getItem(), a);
        items.set(a, items.get(b));

        map.put(temp.getItem(), b);
        items.set(b, temp);
    }

    /**
     * Adds an item with the given priority value.
     * Runs in O(log N) time (except when resizing).
     * @throws IllegalArgumentException if item is null or is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) {
            throw new IllegalArgumentException();
        }
        items.add(size + 1, new PriorityNode<>(item, priority));
        map.put(item, size + 1);
        percolateUp(size + 1);
        size++;
    }

    private void percolateUp(int root) {
        while (root > 1 &&
            (items.get(root / 2).getPriority() > items.get(root).getPriority())) {
            swap(root, root / 2);
            root = root / 2;
        }
    }

    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /**
     * Returns the item with the least-valued priority.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    /**
     * Removes and returns the item with the least-valued priority.
     * Runs in O(log N) time (except when resizing).
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T min = items.get(START_INDEX).getItem();
        swap(START_INDEX, size);
        items.set(size, null);
        map.remove(min);
        size--;
        percolateDown(START_INDEX);
        return min;
    }

    private void percolateDown(int root) {
        while (2 * root <= size) {
            int j = 2 * root;
            if (j != size && (items.get(j).getPriority() > items.get(j + 1).getPriority())) {
                j++;
            }
            if (items.get(root).getPriority() <= items.get(j).getPriority()) {
                break;
            }
            swap(root, j);
            root = j;
        }
    }


    /** for experiment
    private void down(int startNode) {
        /* I changed the variable to "startNode" because using an
         * argument as a regular variable is kind of tacky. But it's a
         * minor point, and I wanted to document it.


        int child;
        PriorityNode<T> tmp = items.get(startNode);
        int hole;


        for (hole = startNode; hole * 2 <= size; hole = child) {

            child = hole * 2;

            if (child != size &&
                items.get(child + 1).getPriority() < items.get(child).getPriority()) {

                child++;
            }


            if (items.get(child).getPriority() < tmp.getPriority()) {

                swap(hole, child);
            } else {

                break;
            }
        }


        items.set(hole, tmp);
    } **/

    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     *
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int index = map.get(item);
        Double oldPriority = items.get(index).getPriority();
        items.get(index).setPriority(priority);
        if (priority > oldPriority) {
            percolateDown(index);
        } else {
            percolateUp(index);
        }

    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(log N) time.
     */
    @Override
    public int size() {
        return size;
    }
}
