package pl.pabilo8.immersiveintelligence.api.data.pol.instructions;

import pl.pabilo8.immersiveintelligence.api.data.pol.POLComputerMemory;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLKeywords;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLProcess;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.DataTypeWrapper;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.POLInstruction;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLTerminal;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;

/**
 * @author Pabilo8
 * @since 22.04.2022
 */
public class POLInstructionType extends POLInstruction
{
	private final DataTypeWrapper text;

	public POLInstructionType(DataType rest)
	{
		super(1);
		this.text = new DataTypeWrapper(rest);
	}

	@Override
	public void execute(POLComputerMemory memory, POLTerminal terminal, POLProcess polProcess, int executionTime)
	{
		terminal.type(text.getString(memory.packet));
	}

	@Override
	public POLKeywords getKeyword()
	{
		return POLKeywords.TYPE;
	}
}
