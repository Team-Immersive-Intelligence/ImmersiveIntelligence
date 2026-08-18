package pl.pabilo8.immersiveintelligence.api.data.pol;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEntity;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test of the POL Programming Language working in a mock computer
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 09.08.2026
 * @since 23.02.2021
 */
public class POLComputerTest extends GameTestBasic
{
	private static final Logger log = LogManager.getLogger("POLComputerTest");
	private static final String[] CIRCUIT_PROGRAMS = {
			"arithmetic",
			"logic",
			"text",
			"itemstack",
			"array",
			"entity",
			"document",
			"type_conversion",
			"fluidstack",
			"map",
			"vector",
			"cryptographer"
	};

	static POLComputerMemory MEMORY;
	static POLTerminal TERMINAL;

	private static ArrayList<String> readLines(String resource)
	{
		try(InputStream inputStream = POLComputerTest.class.getClassLoader().getResourceAsStream(resource))
		{
			assertNotNull(inputStream, "Missing POL test resource: "+resource);
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
			{
				return reader.lines().collect(Collectors.toCollection(ArrayList::new));
			}
		} catch(IOException e)
		{
			throw new AssertionError("Failed to read POL test resource: "+resource, e);
		}
	}

	private static String readFile(String name)
	{
		MEMORY.putScript(name, POLScript.prepareScript(readLines("computer_tests/"+name+".pol")));
		return name;
	}

	private static void execute(String id)
	{
		log.info("Starting program: "+id);
		POLProcess process = new POLProcess(MEMORY.getScript(id));
		do
		{
			process.run(MEMORY, TERMINAL);
			log.debug(() -> "["+id+"]"+(process.isRunning()?"[R]": "[H]")+", "+MEMORY);
		}
		while(process.isRunning());
		log.info("Program "+id+" finished");
	}

	private static void compareOutputs(String name)
	{
		List<String> expected = readLines("computer_tests/"+name+".out");
		POLMockupTerminal mockupTerminal = (POLMockupTerminal)TERMINAL;
		assertEquals(expected, mockupTerminal.output, "Unexpected output for POL program: "+name);
	}

	private static void runProgram(String name)
	{
		execute(readFile(name));
		compareOutputs(name);
	}

	private static void runCircuitProgram(String program)
	{
		prepareCircuitFixture(program);
		runProgram("circuits/"+program);
	}

	private static void resetComputer()
	{
		MEMORY = new POLComputerMemory(8);
		TERMINAL = new POLMockupTerminal("POL");
	}

	private static void prepareCircuitFixture(String program)
	{
		switch(program)
		{
			case "itemstack":
			{
				Item item = new Item().setRegistryName(new ResourceLocation("minecraft", "pol_test_item"));
				MEMORY.packet.with('i', new DataTypeItemStack(new ItemStack(item, 3, 0)));
			}
			break;
			case "fluidstack":
			{
				MEMORY.packet.with('f', new DataTypeFluidStack(new FluidStack(FluidRegistry.WATER, 1000)));
			}
			break;
			case "entity":
			{
				DataTypeEntity entity = new DataTypeEntity();
				entity.entityID = 42;
				entity.dimensionID = -1;
				entity.entityClass = "minecraft:villager";
				entity.customName = "Psi Syn";
				entity.lastPos = new Vec3d(1.5, 64, -2.25);
				MEMORY.packet.with('e', entity);
			}
			break;
			case "document":
			{
				Item item = new Item().setRegistryName(new ResourceLocation("minecraft", "pol_test_item"));
				ItemStack document = new ItemStack(item);
				NBTTagCompound tag = new NBTTagCompound();
				NBTTagList pages = new NBTTagList();
				pages.appendTag(new NBTTagString("{\"text\":\"First page\"}"));
				pages.appendTag(new NBTTagString("{\"text\":\"Second page\"}"));
				tag.setTag("pages", pages);
				tag.setString("author", "HAZARD PAN NASZ");
				tag.setString("title", "Engineer's Manual");
				document.setTagCompound(tag);
				MEMORY.packet.with('d', new DataTypeItemStack(document));
			}
			break;
		}
	}

	private static boolean containsOperation(String source, String operation)
	{
		return Pattern.compile("(?<![a-z0-9_])"+Pattern.quote(operation)+"(?![a-z0-9_])")
				.matcher(source)
				.find();
	}

	@BeforeEach
	public void init()
	{
		IIDataOperationUtils.registerDataOperations();
		IIDataTypeUtils.registerDataTypes();
		resetComputer();
	}

	@Test
	public void gcdTest()
	{
		runProgram("gcd");
	}

	@Test
	public void trigonometryTest()
	{
		runProgram("trigonometry");
	}

	@Test
	public void loadingBarTest()
	{
		runProgram("loading_bar");
	}

	@Test
	public void memorySerializationTest()
	{
		POLComputerMemory restored = new POLComputerMemory(MEMORY.toNBT());
		assertEquals(MEMORY.pages.length, restored.pages.length);
		assertSame(restored.pages[0], restored.packet);
		for(int i = 0; i < restored.pages.length; i++)
			assertNotNull(restored.pages[i], "Missing restored POL memory page: "+i);

		POLComputerMemory empty = new POLComputerMemory(new NBTTagCompound());
		assertEquals(1, empty.pages.length);
		assertSame(empty.pages[0], empty.packet);
	}

	@Test
	public void arithmeticCircuitTest()
	{
		runCircuitProgram("arithmetic");
	}

	@Test
	public void logicCircuitTest()
	{
		runCircuitProgram("logic");
	}

	@Test
	public void textCircuitTest()
	{
		runCircuitProgram("text");
	}

	@Test
	public void itemStackCircuitTest()
	{
		runCircuitProgram("itemstack");
	}

	@Test
	public void arrayCircuitTest()
	{
		runCircuitProgram("array");
	}

	@Test
	public void entityCircuitTest()
	{
		runCircuitProgram("entity");
	}

	@Test
	public void documentCircuitTest()
	{
		runCircuitProgram("document");
	}

	@Test
	public void typeConversionCircuitTest()
	{
		runCircuitProgram("type_conversion");
	}

	@Test
	public void fluidStackCircuitTest()
	{
		runCircuitProgram("fluidstack");
	}

	@Test
	public void mapCircuitTest()
	{
		runCircuitProgram("map");
	}

	@Test
	public void vectorCircuitTest()
	{
		runCircuitProgram("vector");
	}

	@Test
	public void cryptographerCircuitTest()
	{
		runCircuitProgram("cryptographer");
	}

	@Test
	public void circuitProgramsCoverAllRegisteredOperations()
	{
		String source = Arrays.stream(CIRCUIT_PROGRAMS)
				.flatMap(program -> readLines("computer_tests/circuits/"+program+".pol").stream())
				.map(line -> line.replaceFirst(";.*$", ""))
				.collect(Collectors.joining("\n"));

		List<String> missing = IIDataOperationUtils.getAllOperationNames()
				.stream()
				.filter(operation -> !containsOperation(source, operation))
				.sorted()
				.collect(Collectors.toList());

		assertTrue(missing.isEmpty(), "Missing POL operation coverage: "+missing);
	}

	private static class POLMockupTerminal extends POLTerminal
	{
		private final ArrayList<String> output = new ArrayList<>();
		private final String name;

		private POLMockupTerminal(String name)
		{
			this.name = "["+name+"]";
		}

		@Override
		public void error(String text)
		{
			log.error(name+" "+text);
		}

		@Override
		public void type(String text)
		{
			output.add(name+" "+text);
			log.info(name+" "+text);
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
			// Do nothing.
		}
	}
}
