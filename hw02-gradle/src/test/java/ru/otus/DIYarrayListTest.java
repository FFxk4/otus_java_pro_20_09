package ru.otus;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Test for DIYarrayList puplic methods. Disabled test are not realized.
 */
class DIYarrayListTest {

	@Test
	void size() {
		assertEquals(0, new DIYarrayList<>().size());
	}

	@Test
	void isEmpty() {
		assertTrue(new DIYarrayList<>().isEmpty());
	}

	@Test
	void add() {
		DIYarrayList<Integer> list = new DIYarrayList<>();
		list.add(5);
		assertEquals(1, list.size());
	}

	@Test
	void testAdd() {
		DIYarrayList<Integer> list = new DIYarrayList<>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		list.add(7, 555);
		assertEquals(555, list.get(7));
	}

	@Test
	void contains() {
		DIYarrayList<Integer> list = new DIYarrayList<>();
		list.add(5);
		assertTrue(list.contains(5));
		assertFalse(list.contains(6));
	}

	@Test
	void containsAll() {
		DIYarrayList<Integer> testList = new DIYarrayList<>();
		List<Integer> expectedData = IntStream.range(1, 100)
			.boxed()
			.collect(Collectors.toList());

		testList.addAll(expectedData);
		assertTrue(testList.containsAll(expectedData));
	}

	@Test
	void get() {
		DIYarrayList<Integer> list = new DIYarrayList<>();
		list.add(7);
		list.add(9);
		assertEquals(9, list.get(1));
	}

	@Test
	void set() {
		DIYarrayList<Integer> list = new DIYarrayList<>();
		list.add(7);
		list.add(9);
		int old = list.set(1, 5);
		assertEquals(5, list.get(1));
		assertEquals(9, old);
	}

	@Test
	void indexOf() {
		DIYarrayList<Integer> list = new DIYarrayList<>();
		list.add(0);
		list.add(3);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		assertEquals(1, list.indexOf(3));
	}

	@Test
	void lastIndexOf() {
		DIYarrayList<Integer> list = new DIYarrayList<>();
		list.add(0);
		list.add(3);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		assertEquals(3, list.lastIndexOf(3));
	}

	@Test
	void addAll() {
		DIYarrayList<Integer> testList = new DIYarrayList<>();
		List<Integer> expectedData = IntStream.range(1, 100)
			.boxed()
			.collect(Collectors.toList());

		testList.addAll(expectedData);
		assertThat(testList).containsExactlyElementsOf(expectedData);
	}

	@Test
	void subList() {
		DIYarrayList<Integer> testList = new DIYarrayList<>();
		List<Integer> expectedData = IntStream.range(1, 100)
			.boxed()
			.collect(Collectors.toList());

		testList.addAll(expectedData);
		assertThat(testList.subList(20, 90)).containsExactlyElementsOf(expectedData.subList(20, 90));
	}

	@Test
	void clear() {
		DIYarrayList<Integer> testList = new DIYarrayList<>();
		List<Integer> expectedData = IntStream.range(1, 100)
			.boxed()
			.collect(Collectors.toList());

		testList.addAll(expectedData);
		testList.clear();
		expectedData.clear();
		assertThat(testList).containsExactlyElementsOf(expectedData);
	}

	@Test
	@Disabled
	void remove() {
	}

	@Test
	@Disabled
	void removeAll() {
	}
}