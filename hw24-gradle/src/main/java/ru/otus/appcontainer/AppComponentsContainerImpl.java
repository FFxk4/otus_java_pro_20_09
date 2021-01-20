package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.AppConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.asList;

public class AppComponentsContainerImpl implements AppComponentsContainer {
	private final List<Object> appComponents = new ArrayList<>();
	private final Map<String, Object> appComponentsByName = new HashMap<>();

	public AppComponentsContainerImpl(Class<?> initialConfigClass) {
		processConfig(initialConfigClass);
	}

	public AppComponentsContainerImpl(Class<?>... configClasses) {
		processSeveralConfigs(asList(configClasses));
	}

	public AppComponentsContainerImpl(String packageName) {
		//processConfig(initialConfigClass); //todo
	}

	@Override
	public <C> C getAppComponent(Class<C> componentClass) {
		for (Object element : appComponents) {
			Class<?> elementClass = element.getClass();
			List<Class<?>> elementInterfaces = asList(elementClass.getInterfaces());
			if (elementClass.equals(componentClass) || elementInterfaces.contains(componentClass)) {
				return (C) element;
			}
		}
		return null;
	}

	@Override
	public <C> C getAppComponent(String componentName) {
		for (Map.Entry<String, Object> entry : appComponentsByName.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(componentName)) {
				return (C) entry.getValue();
			}
		}
		return null;
	}

	private void processConfig(Class<?> configClass) {
		checkConfigClass(configClass);
		// You code here...
		var configClassMethods = getFilteredAndOrderedMethods(configClass);
		var methodsToClassBinding = matchMethodsToConfigClass(configClass);
		loadComponentsToContainer(configClassMethods, methodsToClassBinding);
	}

	private void checkConfigClass(Class<?> configClass) {
		if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
			throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
		}
	}

	private ArrayList<Method> getFilteredAndOrderedMethods(Class<?> configClass) {
		var unSortedMethods = configClass.getDeclaredMethods();
		var orderedMethods = new TreeMap<Integer, Method>();
		for (Method m : unSortedMethods) {
			if (!m.isAnnotationPresent(AppComponent.class)) {
				continue;
			}
			var annotation = m.getDeclaredAnnotation(AppComponent.class);
			orderedMethods.put(annotation.order(), m);
		}
		return new ArrayList<>(orderedMethods.values());
	}

	private void loadComponentsToContainer(ArrayList<Method> configClassMethods, HashMap<Method, Class<?>> methodsToClassBinding) {
		for (Method method : configClassMethods) {
			var parameters = method.getParameters();
			var loadedParameters = new ArrayList<>();
			if (parameters.length > 0) {
				for (Parameter p : parameters) {
					String parameterName = p.getType().getSimpleName();
					Object component = getAppComponent(parameterName);
					loadedParameters.add(component);
				}
			}
			Object result = new Object();
			Class<?> configClass = methodsToClassBinding.get(method);

			try {
				Constructor<?>[] constructors = configClass.getConstructors();
				//result = m.invoke(new AppConfig(), loadedParameters.toArray());
				result = method.invoke(constructors[0].newInstance(), loadedParameters.toArray());
			} catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
			var appComponent = method.getDeclaredAnnotation(AppComponent.class);
			appComponentsByName.put(appComponent.name(), result);
			appComponents.add(result);
		}
	}

	private void processSeveralConfigs(List<Class<?>> configClassList) {
		var fulfilledConfigMethods = new ArrayList<Method>();
		var orderedConfigClasses = new TreeMap<Integer, Class<?>>();
		var methodsToClassBinding = new HashMap<Method, Class<?>>();
		for (Class<?> configClass : configClassList) {
			checkConfigClass(configClass);
			var annotation = configClass.getDeclaredAnnotation(AppComponentsContainerConfig.class);
			orderedConfigClasses.put(annotation.order(), configClass);
			methodsToClassBinding.putAll(matchMethodsToConfigClass(configClass));
		}
		for (Class<?> configClass : orderedConfigClasses.values()) {
			var partConfigClassMethods = getFilteredAndOrderedMethods(configClass);
			fulfilledConfigMethods.addAll(partConfigClassMethods);
		}
		loadComponentsToContainer(fulfilledConfigMethods, methodsToClassBinding);
	}

	private HashMap<Method, Class<?>> matchMethodsToConfigClass(Class<?> configClass) {

		var methods = configClass.getDeclaredMethods();
		var methodsByClass = new HashMap<Method, Class<?>>();
		for (Method method : methods) {
			if (!method.isAnnotationPresent(AppComponent.class)) {
				continue;
			}
			methodsByClass.put(method, configClass);
		}
		return methodsByClass;
	}
}