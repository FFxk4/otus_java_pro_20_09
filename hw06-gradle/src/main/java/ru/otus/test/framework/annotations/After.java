package ru.otus.test.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Маркирование утилитного метода, который надо запустить <b><i>после</i></b>
 * методом с аннотацией {@link TestIt}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface After {
	/**
	 * @return Имя метода после которым надо запустить After
	 */
	String target();
}