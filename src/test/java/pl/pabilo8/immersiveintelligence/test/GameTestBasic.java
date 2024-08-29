package pl.pabilo8.immersiveintelligence.test;

import net.minecraft.init.Bootstrap;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.08.2024
 **/
public class GameTestBasic
{
	@BeforeAll
	static void beforeAll()
	{
		Bootstrap.register();
	}
}
