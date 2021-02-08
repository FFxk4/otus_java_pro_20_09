package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {
	private final List<Object> appComponents = new ArrayList<>();
	private final Map<String, Object> appComponentsByName = new HashMap<>();

	public AppComponentsContainerImpl(Class<?>... configClasses) throws ReflectiveOperationException  {
		processConfig(Arrays.asList(configClasses));
	}

	public AppComponentsContainerImpl(String configLocationPackageName) throws ReflectiveOperationException  {
		processByReflectionsApiConfig(configLocationPackageName);
	}

	@Override
	public <C> C getAppComponent(Class<C> componentClass) {
		for (Object element : appComponents) {
			var elementClass = element.getClass();
			if (elementClass.equals(componentClass) || componentClass.isAssignableFrom(elementClass)) {
				return (C) element;
			}
		}
		return null;
	}

	@Override
	public <C> C getAppComponent(String componentName) {
		return (C) appComponentsByName.get(componentName.toLowerCase());
	}

private void processConfig(List<Class<?>> configClassList) throws ReflectiveOperationException {
	var orderedConfigMethods = new ArrayList<Method>();
	configClassList.sort(Comparator.comparingInt(config -> config.getDeclaredAnnotation(AppComponentsContainerConfig.class).order()));
	configClassList.forEach(config -> {
		checkConfigClass(config);
		orderedConfigMethods
			.addAll(Arrays.stream(config.getDeclaredMethods())
				.filter(method -> method.isAnnotationPresent(AppComponent.class))
				.sorted(Comparator.comparingInt(method -> method.getDeclaredAnnotation(AppComponent.class).order()))
				.collect(Collectors.toList()));
	});
	loadComponentsToContainer(orderedConfigMethods);
}

	private void checkConfigClass(Class<?> configClass) {
		if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
			throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
		}
	}

	private void loadComponentsToContainer(List<Method> configClassMethods) throws ReflectiveOperationException{
		for (Method method : configClassMethods) {
			var preparedComponentParameters = prepareComponentParameters(method.getParameters());
			var methodClassInstance = method.getDeclaringClass().getConstructors()[0].newInstance();
			var result = method.invoke(methodClassInstance, preparedComponentParameters);
			var appComponent = method.getDeclaredAnnotation(AppComponent.class);
			appComponentsByName.put(appComponent.name().toLowerCase(), result);
			appComponents.add(result);
		}
	}

	private Object[] prepareComponentParameters(Parameter[] parameters) {
		var loadedComponentParameters = new ArrayList<>();
		for (Parameter p : parameters) {
			var parameterName = p.getType().getSimpleName();
			var component = getAppComponent(parameterName);
			loadedComponentParameters.add(component);
		}
		return loadedComponentParameters.toArray();
	}

	private void processByReflectionsApiConfig(String packageName) throws ReflectiveOperationException {
		var reflections = new Reflections(
								new ConfigurationBuilder()
									.setUrls(ClasspathHelper.forPackage(packageName))
									.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner()));

		var annotated = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
		processConfig(new ArrayList<>(annotated));
	}
}