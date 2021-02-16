package ru.otus.test.framework.service;

import ru.otus.test.framework.annotations.After;
import ru.otus.test.framework.annotations.Before;
import ru.otus.test.framework.annotations.TestIt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс выполняющий тестирование переданного в него класса с тестами,
 * которые помечены аннотацией {@link TestIt}
 */
public class TestHandler {

	private final ArrayList<Method> methodsToStart = new ArrayList<>();
	private final ArrayList<Method> beforeMethods = new ArrayList<>();
	private final ArrayList<Method> afterMethods = new ArrayList<>();
	private int all = 0;
	private int passed = 0;
	private int failed = 0;

	/**
	 * Запуск тестирования класса
	 * @param testClass класс с тестами
	 */
	public void start(Class<?> testClass) {
		sortMethods(testClass.getDeclaredMethods());
		startTests(testClass);
		printResult();
	}

	/**
	 * Запуск тестирования класса
	 * @param testClass полное наименование класса с тестами
	 */
	public void start(String testClass) {
		if (testClass == null || testClass.isBlank()) {
			printWarning();
		}
		try {
			Class<?> clazz = Class.forName(testClass);
			start(clazz);
		} catch (ClassNotFoundException e) {
			printWarning();
		}
	}

	private void sortMethods(Method[] methods) {
		Arrays.stream(methods).forEach(method -> {
			if (method.isAnnotationPresent(TestIt.class)) {
				methodsToStart.add(method);
			}
			if (method.isAnnotationPresent(Before.class)) {
				beforeMethods.add(method);
			}
			if (method.isAnnotationPresent(After.class)) {
				afterMethods.add(method);
			}
		});
	}

	private void startTests(Class<?> testClass) {
		all = methodsToStart.size();

		methodsToStart.forEach(testMethod -> {
			try {
				Object o = testClass.getConstructors()[0].newInstance();
				String methodName = testMethod.getName();

				List<Method> beforeMatched
					= beforeMethods.stream()
						.filter(before -> methodName.equalsIgnoreCase(
												before.getAnnotation(Before.class).target()))
						.collect(Collectors.toList());
				invoke(o, beforeMatched);

				testMethod.invoke(o);

				List<Method> afterMatched
					= afterMethods.stream()
						.filter(before -> methodName.equalsIgnoreCase(
												before.getAnnotation(After.class).target()))
						.collect(Collectors.toList());
				invoke(o, afterMatched);

				passed++;
			} catch (Exception e) {
				failed++;
			}
		});

	}

	private void invoke(Object o, List<Method> methods) throws IllegalAccessException, InvocationTargetException {
		for (Method before : methods) {
			before.invoke(o);
		}
	}

	private void printResult() {
		System.out.printf("\nТестов всего: %d, пройдено: %d, с ошибкой: %d\n", all, passed, failed);
	}

	private void printWarning() {
		System.out.println("Внимание! Тестовый класс отсутствует или неправильно набрано его имя!");
	}
}