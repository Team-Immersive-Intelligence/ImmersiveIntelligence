package pl.pabilo8.immersiveintelligence.api.data.pol.instructions;

import pl.pabilo8.immersiveintelligence.api.data.pol.POLComputerMemory;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLKeywords;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLProcess;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.POLInstruction;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLTerminal;

/**
 * @author Pabilo8
 * @since 22.04.2022
 */
public class POLInstructionEnd extends POLInstruction
{
	public POLInstructionEnd() {super(1);}

	@Override
	public void execute(POLComputerMemory memory, POLTerminal terminal, POLProcess polProcess, int executionTime)
	{
		//simple as that
		polProcess.returnStack();
	}

	@Override
	public POLKeywords getKeyword()
	{
		return POLKeywords.END;
	}
}
