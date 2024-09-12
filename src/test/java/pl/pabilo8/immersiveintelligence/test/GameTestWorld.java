package pl.pabilo8.immersiveintelligence.test;

import com.builtbroken.mc.testing.junit.TestManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.08.2024
 **/
public class GameTestWorld
{
	protected final TestManager testManager = new TestManager(getClass().getSimpleName(), Assertions::fail);

	@BeforeEach
	public void setUp()
	{
		testManager.initWorld(0);
	}

	@AfterEach
	public void tearDown()
	{
		testManager.cleanupBetweenTests();
	}

}
