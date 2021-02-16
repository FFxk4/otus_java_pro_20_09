package ru.otus.example;

import ru.otus.test.framework.annotations.Before;
import ru.otus.test.framework.annotations.TestIt;
import ru.otus.test.framework.annotations.After;

public class Test {

	@TestIt
	public void test1() {
		System.out.println("Тест №1");
	}

	@After(target = "test1")
	public void after1() {
		System.out.println("После теста №1");
	}

	@After(target = "test1")
	public void secondAfter1() {
		System.out.println("Второй после теста №1");
	}

	@Before(target = "test2")
	public void before2() {
		System.out.println("Перед тестом №2");
	}

	@TestIt
	public void test2() {
		System.out.println("Тест №2");
	}

	@After(target = "test2")
	public void after2() {
		System.out.println("После теста №2");
	}

	@TestIt
	public void test3() {
		System.out.println("Тест №3");
	}

	@TestIt
	public void test4() {
		var a = 5 / 0;
	}
}