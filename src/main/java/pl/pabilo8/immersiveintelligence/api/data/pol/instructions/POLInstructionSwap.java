package pl.pabilo8.immersiveintelligence.api.data.pol.instructions;

import pl.pabilo8.immersiveintelligence.api.data.pol.POLComputerMemory;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLKeywords;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLProcess;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.DataTypeWrapper;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.POLInstruction;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLTerminal;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 22.04.2022
 */
public class POLInstructionSwap extends POLInstruction
{
	private final char from, to;
	@Nullable
	private final DataTypeWrapper page;

	public POLInstructionSwap(char from, char to, @Nullable DataType page)
	{
		super(1);
		this.from = from;
		this.to = to;
		this.page = page==null?null: new DataTypeWrapper(page);
	}

	@Override
	public void execute(POLComputerMemory memory, POLTerminal terminal, POLProcess polProcess, int executionTime)
	{
		int pageID = (page!=null&&page.get(memory.packet) instanceof DataTypeInteger)?((DataTypeInteger)page.get(memory.packet)).value: memory.page;
		DataType f = memory.packet.getPacketVariable(from);
		DataType t = memory.pages[pageID].getPacketVariable(to);

		memory.pages[pageID].setVariable(to, f);
		memory.packet.setVariable(from, t);

	}

	@Override
	public POLKeywords getKeyword()
	{
		return POLKeywords.SWAP;
	}
}
