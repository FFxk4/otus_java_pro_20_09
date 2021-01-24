package ru.otus;

import org.junit.jupiter.api.Test;
import ru.otus.appcontainer.AppComponentsContainerImpl;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.config.AppConfig;
import ru.otus.config.AppConfig1;
import ru.otus.config.AppConfig2;
import ru.otus.services.GameProcessor;
import ru.otus.services.GameProcessorImpl;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

	@Test
	void main1() throws ReflectiveOperationException {
		AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);
		GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);

		Field io = gameProcessor.getClass().getDeclaredField("ioService");
		Field preparer = gameProcessor.getClass().getDeclaredField("equationPreparer");
		Field player = gameProcessor.getClass().getDeclaredField("playerService");
		io.setAccessible(true);
		preparer.setAccessible(true);
		player.setAccessible(true);

		assertNotNull(io.get(gameProcessor));
		assertNotNull(preparer.get(gameProcessor));
		assertNotNull(player.get(gameProcessor));
	}

	@Test
	void main2() throws ReflectiveOperationException {
		AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);
		GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);

		Field io = gameProcessor.getClass().getDeclaredField("ioService");
		Field preparer = gameProcessor.getClass().getDeclaredField("equationPreparer");
		Field player = gameProcessor.getClass().getDeclaredField("playerService");
		io.setAccessible(true);
		preparer.setAccessible(true);
		player.setAccessible(true);

		assertNotNull(io.get(gameProcessor));
		assertNotNull(preparer.get(gameProcessor));
		assertNotNull(player.get(gameProcessor));
	}

	@Test
	void main3() throws ReflectiveOperationException {
		AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);
		GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

		Field io = gameProcessor.getClass().getDeclaredField("ioService");
		Field preparer = gameProcessor.getClass().getDeclaredField("equationPreparer");
		Field player = gameProcessor.getClass().getDeclaredField("playerService");
		io.setAccessible(true);
		preparer.setAccessible(true);
		player.setAccessible(true);

		assertNotNull(io.get(gameProcessor));
		assertNotNull(preparer.get(gameProcessor));
		assertNotNull(player.get(gameProcessor));
	}

	@Test
	void main4() throws ReflectiveOperationException {
		AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);
		GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);

		Field io = gameProcessor.getClass().getDeclaredField("ioService");
		Field preparer = gameProcessor.getClass().getDeclaredField("equationPreparer");
		Field player = gameProcessor.getClass().getDeclaredField("playerService");
		io.setAccessible(true);
		preparer.setAccessible(true);
		player.setAccessible(true);

		assertNotNull(io.get(gameProcessor));
		assertNotNull(preparer.get(gameProcessor));
		assertNotNull(player.get(gameProcessor));
	}

	@Test
	void main5() throws ReflectiveOperationException {
		AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.config");
		GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);

		Field io = gameProcessor.getClass().getDeclaredField("ioService");
		Field preparer = gameProcessor.getClass().getDeclaredField("equationPreparer");
		Field player = gameProcessor.getClass().getDeclaredField("playerService");
		io.setAccessible(true);
		preparer.setAccessible(true);
		player.setAccessible(true);

		assertNotNull(io.get(gameProcessor));
		assertNotNull(preparer.get(gameProcessor));
		assertNotNull(player.get(gameProcessor));
	}
}