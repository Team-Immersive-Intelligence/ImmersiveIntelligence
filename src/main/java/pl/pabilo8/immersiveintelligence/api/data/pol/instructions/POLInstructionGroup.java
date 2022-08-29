package pl.pabilo8.immersiveintelligence.api.data.pol.instructions;

import pl.pabilo8.immersiveintelligence.api.data.pol.POLComputerMemory;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLKeywords;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLProcess;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.POLInstruction;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLTerminal;

import java.util.ArrayList;

/**
 * A group of instructions, can contain another group
 */
public class POLInstructionGroup extends POLInstruction
{
	private final ArrayList<POLInstruction> instructionSet;

	public POLInstructionGroup(ArrayList<POLInstruction> set)
	{
		super(
				set.stream()
						.map(POLInstruction::getExecutionTime)
						.mapToInt(Integer::intValue)
						.sum()
		);
		instructionSet = set;
	}

	// TODO: 18.04.2022 improve nesting
	@Override
	public void execute(POLComputerMemory memory, POLTerminal terminal, POLProcess polProcess, int executionTime)
	{
		if(executionTime < 0||executionTime > instructionSet.size()-1)
			return;

		int time = 0;
		for(POLInstruction instruction : instructionSet)
		{
			if(time >= executionTime)
			{
				instruction.execute(memory, terminal, polProcess, executionTime);
				break;
			}
			time += instruction.getExecutionTime();
		}

	}

	@Override
	public POLKeywords getKeyword()
	{
		return POLKeywords.BEGIN;
	}
}
