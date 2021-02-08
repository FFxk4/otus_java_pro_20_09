package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.EquationPreparer;
import ru.otus.services.EquationPreparerImpl;
import ru.otus.services.IOService;
import ru.otus.services.IOServiceConsole;

@AppComponentsContainerConfig(order = 0)
public class AppConfig1 {

	@AppComponent(order = 0, name = "equationPreparer")
	public EquationPreparer equationPreparer(){
		return new EquationPreparerImpl();
	}

	@AppComponent(order = 1, name = "ioService")
	public IOService ioService() {
		return new IOServiceConsole(System.out, System.in);
	}
}