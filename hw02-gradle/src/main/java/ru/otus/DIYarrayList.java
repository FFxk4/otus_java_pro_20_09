package ru.otus;

import java.util.*;

/**
 * Test implementation of  {@link List} interface with generics.
 * Not realized methods: remove, removeAll, retainAll and overloaded addAll(with index)
 */
public class DIYarrayList<E> implements List<E> {

	private static final int DEFAULT_SIZE = 10;
	private static final int EMPTY_SIZE = 0;
	private int size;
	private transient Object[] array;

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < size; i++) {
			if (array[i].equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean add(E e) {
		if (size == array.length) {
			enlarge();
		}
		array[size] = e;
		size += 1;
		return true;
	}

	@Override
	public void add(int index, E element) {
		checkIndex(index);
		if (size == 0) {
			add(element);
			return;
		}
		if (size == array.length) {
			enlarge();
		}
		Object[] newArray = new Object[array.length];
		for (int i = 0; i < size; i++) {
			if (i < index) {
				newArray[i] = array[i];
			}
			if (i > index) {
				newArray[i] = array[i - 1];
			}
			if (i == index) {
				newArray[i] = element;
			}
		}
		array = newArray;
		size += 1;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E get(int index) {
		checkIndex(index);
		return (E) array[index];
	}

	@Override
	@SuppressWarnings("unchecked")
	public E set(int index, E element) {
		checkIndex(index);
		E oldElement = (E) array[index];
		array[index] = element;
		return oldElement;
	}

	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < size; i++) {
			if (o == null) {
				if (o == array[i]) {
					return i;
				} else {
					continue;
				}
			}
			if (o.equals(array[i])) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		for (int i = size; i >= 0; i--) {
			if (o == null) {
				if (o == array[i]) {
					return i;
				} else {
					continue;
				}
			}
			if (o.equals(array[i])) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			add(e);
		}
		return true;
	}

	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			array[i] = null;
		}
		size = 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new ArrayIterator();
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(array, size);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T1> T1[] toArray(T1[] a) {
		return (T1[]) Arrays.copyOf(array, size);
	}

	@Override
	public ListIterator<E> listIterator() {
		return new LstIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new LstIterator(index);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<E> subList(int fromIndex, int toIndex) {
		DIYarrayList<E> list = new DIYarrayList<>();
		if (toIndex - fromIndex < 1 || fromIndex < 0 || fromIndex > size || toIndex > size) {
			return list;
		}
		for (int i = fromIndex; i < toIndex; i++) {
			list.add((E) array[i]);
		}
		return list;
	}

	private void checkIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("There is no such index in the DIYarrayList");
		}
	}

	private void enlarge() {
		int doubledSize = array.length * 2;
		if (size > EMPTY_SIZE) {
			array = Arrays.copyOf(array,
			                      doubledSize < 0 ? Integer.MAX_VALUE : doubledSize);
		} else {
			array = new Object[DEFAULT_SIZE];
		}
	}

	private class ArrayIterator implements Iterator<E> {

		int currentIdx = 0;

		int lastRet = -1; // index of last element returned; -1 if no such

		@Override
		public boolean hasNext() {
			if (currentIdx < size) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			lastRet = currentIdx;
			return (E) array[currentIdx++];
		}
	}

	private class LstIterator extends ArrayIterator implements ListIterator<E> {

		LstIterator(int index) {
			super();
			currentIdx = index;
		}

		@Override
		public boolean hasPrevious() {
			return currentIdx > 0;
		}

		@Override
		@SuppressWarnings("unchecked")
		public E previous() {
			if (!hasPrevious()) {
				throw new IndexOutOfBoundsException();
			}
			currentIdx -= 1;
			lastRet = currentIdx;
			return (E) array[currentIdx];
		}

		@Override
		public int nextIndex() {
			return currentIdx;
		}

		@Override
		public int previousIndex() {
			return currentIdx - 1;
		}

		@Override
		public void remove() {
			if (lastRet < 0) {
				throw new IllegalStateException();
			}
			DIYarrayList.this.remove(lastRet);
			currentIdx = lastRet;
			lastRet = -1;
		}

		@Override
		public void set(E e) {
			if (lastRet < 0) {
				throw new IllegalStateException();
			}
			DIYarrayList.this.set(lastRet, e);
		}

		@Override
		public void add(E e) {
			DIYarrayList.this.add(currentIdx, e);
			currentIdx += 1;
			lastRet = -1;
		}
	}

	/**
	 * Create list with default size
	 */
	public DIYarrayList() {
		this.array = new Object[DEFAULT_SIZE];
		this.size = 0;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return false;
	}

	@Override
	public E remove(int index) {
		return null;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}
}
