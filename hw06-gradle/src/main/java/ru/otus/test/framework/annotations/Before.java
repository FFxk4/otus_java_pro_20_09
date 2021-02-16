package ru.otus.test.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Маркирование утилитного метода, который надо запустить <b><i>перед</i></b>
 * методом с аннотацией {@link TestIt}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Before {
	/**
	 * @return Имя метода перед которым надо запустить Before
	 */
	String target();
}