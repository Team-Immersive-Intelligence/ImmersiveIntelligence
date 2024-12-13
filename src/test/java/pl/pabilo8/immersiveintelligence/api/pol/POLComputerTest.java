package pl.pabilo8.immersiveintelligence.api.pol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLComputerMemory;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLProcess;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLTerminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test of the POL Programming Language working in a mock computer
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 13.12.2024
 * @since 23.02.2021
 */
public class POLComputerTest
{
	static POLComputerMemory MEMORY;
	static POLTerminal TERMINAL;

	@BeforeEach
	public void init()
	{
		IIDataOperationUtils.registerDataOperations();
		IIDataTypeUtils.registerDataTypes();

		MEMORY = new POLComputerMemory(8);
		TERMINAL = new POLMockupTerminal("POL");
	}

	@Test
	public void gcdTest()
	{
		execute(readFile("gcd"));
		compareOutputs("gcd");
	}

	@Test
	public void trigonometryTest()
	{
		execute(readFile("trigonometry"));
		compareOutputs("trigonometry");
	}

	@Test
	public void loadingBarTest()
	{
		execute(readFile("loading_bar"));
		compareOutputs("loading_bar");
	}


	/**
	 * load a POL file into scripts
	 */
	private static String readFile(String name)
	{
		try
		{
			InputStream inputStream = POLComputerTest.class.getClassLoader().getResourceAsStream("computer_tests/"+name+".pol");
			assert inputStream!=null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			ArrayList<String> c = reader.lines().collect(Collectors.toCollection(ArrayList::new));

			MEMORY.putScript(name, POLScript.prepareScript(c));
			reader.close();
			return name;
		} catch(IOException ignored)
		{

		}
		return "";
	}

	private static void execute(String id)
	{
		System.out.println("Starting program: "+id);
		POLProcess process = new POLProcess(MEMORY.getScript(id));
//		final String log = "["+id+"]";
		do
		{
			process.run(MEMORY, TERMINAL);
//			System.out.print(process.isRunning()?"[R]": "[H]");
//			System.out.print(log);
//			System.out.println(MEMORY);
		}
		while(process.isRunning());
	}

	private static class POLMockupTerminal extends POLTerminal
	{
		private ArrayList<String> output = new ArrayList<>();
		private final String name;

		public POLMockupTerminal(String name)
		{
			this.name = "["+name+"]";
		}

		@Override
		public void error(String text)
		{
			System.out.println(name+" "+text);
		}

		@Override
		public void type(String text)
		{
			output.add(name+" "+text);
			System.out.println(name+" "+text);
		}

		@Override
		public void lamp(int lamp, int color, boolean state)
		{
			// TODO: 17.04.2022 lamp display
		}

		@Override
		public IDataDevice getDeviceAt(int section, int id)
		{
			return null;
		}

		@Override
		public void sleep(int value)
		{
			//Do nothing

			/*try
			{
				Thread.sleep(value*20L);
			} catch(InterruptedException ignored)
			{
			}*/
		}
	}

	private static void compareOutputs(String name)
	{
		try
		{
			InputStream inputStream = POLComputerTest.class.getClassLoader().getResourceAsStream("computer_tests/"+name+".out");
			assert inputStream!=null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			ArrayList<String> c = reader.lines().collect(Collectors.toCollection(ArrayList::new));
			POLMockupTerminal mockupTerminal = (POLMockupTerminal)TERMINAL;

			for(int i = 0; i < c.size(); i++)
			{
				assertTrue(mockupTerminal.output.size() > i);
				assertEquals(c.get(i), mockupTerminal.output.get(i));
			}
			reader.close();
		} catch(IOException ignored)
		{

		}
	}
}
