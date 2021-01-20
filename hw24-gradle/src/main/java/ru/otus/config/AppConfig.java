package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.EquationPreparer;
import ru.otus.services.EquationPreparerImpl;
import ru.otus.services.IOService;
import ru.otus.services.IOServiceConsole;
import ru.otus.services.PlayerService;
import ru.otus.services.PlayerServiceImpl;
import ru.otus.services.GameProcessor;
import ru.otus.services.GameProcessorImpl;

@AppComponentsContainerConfig(order = 1)
public class AppConfig {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 2, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 3, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }

    @AppComponent(order = 1, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }

}