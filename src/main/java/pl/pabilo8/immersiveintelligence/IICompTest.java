package pl.pabilo8.immersiveintelligence;

import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLComputerMemory;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLProcess;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLTerminal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 23.02.2021
 * <p>
 * A test of the POL Programming Language
 */
@Deprecated
public class IICompTest
{
	// TODO: remove or move into a dedicated "emulator"
	static boolean DEBUG = true;
	static POLComputerMemory MEMORY = new POLComputerMemory(8);
	static POLTerminal TERMINAL = new POLMockupTerminal("POL");

	public static void main(String[] args)
	{
		execute(readFile("trigonometry"));
	}

	/**
	 * load a POL file into scripts
	 */
	private static String readFile(String name)
	{
		try
		{
			FileReader f = new FileReader("src/pol/"+name+".pol");
			BufferedReader reader = new BufferedReader(f);
			ArrayList<String> c = reader.lines().collect(Collectors.toCollection(ArrayList::new));

			MEMORY.putScript(name, POLScript.prepareScript(c));
			reader.close();
			return name;
		}
		catch(IOException ignored)
		{

		}
		return "";
	}

	private static void execute(String id)
	{
		POLProcess process = new POLProcess(MEMORY.getScript(id));
//		final String LOG = "["+id+"]";

		do
		{
			process.run(MEMORY, TERMINAL);
			/*if(DEBUG)
			{
				System.out.print(process.isRunning()?"[R]": "[H]");
				System.out.print(LOG);
				System.out.println(MEMORY);
			}*/
		}
		while(process.isRunning());
	}

	private static class POLMockupTerminal extends POLTerminal
	{
		private final String name;

		public POLMockupTerminal(String name)
		{
			this.name = "["+name+"]";
		}

		@Override
		public void error(String text)
		{
			System.out.println(text);
		}

		@Override
		public void type(String text)
		{
			System.out.println(text);
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
			try
			{
				Thread.sleep(value*20L);
			}
			catch(InterruptedException ignored)
			{
			}
		}
	}
}
