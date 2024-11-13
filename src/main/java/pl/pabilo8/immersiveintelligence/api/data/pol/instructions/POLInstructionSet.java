package pl.pabilo8.immersiveintelligence.api.data.pol.instructions;

import pl.pabilo8.immersiveintelligence.api.data.pol.POLComputerMemory;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLKeywords;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLProcess;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.DataTypeWrapper;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.POLInstruction;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLTerminal;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

/**
 * @author Pabilo8
 * @since 22.04.2022
 */
public class POLInstructionSet extends POLInstruction
{
	private final char letter;
	private final Class<? extends DataType> type;
	private final DataTypeWrapper text;

	public POLInstructionSet(char letter, DataType rest, Class<? extends DataType> type)
	{
		super(1);
		this.letter = letter;
		this.text = new DataTypeWrapper(rest);
		this.type = type;
	}

	@Override
	public void execute(POLComputerMemory memory, POLTerminal terminal, POLProcess polProcess, int executionTime)
	{
		memory.packet.setVariable(letter, memory.packet.getVarInType(type, text.get(memory.packet)));
	}

	@Override
	public POLKeywords getKeyword()
	{
		return POLKeywords.SET;
	}
}
