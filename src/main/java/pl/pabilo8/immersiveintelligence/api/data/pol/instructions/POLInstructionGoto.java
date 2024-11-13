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
public class POLInstructionGoto extends POLInstruction
{
	private final DataTypeWrapper text;

	public POLInstructionGoto(DataType rest)
	{
		super(1);
		this.text = new DataTypeWrapper(rest);
	}

	@Override
	public void execute(POLComputerMemory memory, POLTerminal terminal, POLProcess polProcess, int executionTime)
	{
		String string = text.getString(memory.packet);
		if(polProcess.getScript().getMarkers().containsKey(string))
			polProcess.setLineID(polProcess.getScript().getMarkers().get(string));
	}

	@Override
	public POLKeywords getKeyword()
	{
		return POLKeywords.GOTO;
	}
}
