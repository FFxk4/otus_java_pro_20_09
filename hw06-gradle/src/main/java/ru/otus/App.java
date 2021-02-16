package ru.otus;

import ru.otus.example.Test;
import ru.otus.test.framework.service.TestHandler;

public class App {
	public static void main(String[] args) {
		new TestHandler().start(Test.class);
		new TestHandler().start("ru.otus.example.Test");
	}
}