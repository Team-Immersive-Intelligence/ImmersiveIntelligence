package pl.pabilo8.immersiveintelligence.api.data.pol;

import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.POLInstruction;

import java.util.Stack;

/**
 * @author Pabilo8
 * @since 15.04.2022
 */
public class POLProcess
{
	private final POLScript script;
	private boolean running = true;
	private int lineID = 0, time = 0;
	private final Stack<Tuple<Integer, Integer>> returnStack = new Stack<>();

	public POLProcess(POLScript script)
	{
		this.script = script;
	}

	public void run(POLComputerMemory memory, POLTerminal terminal)
	{
		POLInstruction instruction = script.getInstructions()[lineID];
		instruction.execute(memory, terminal, this, time);
		time++;

		if(instruction.getExecutionTime() <= time)
		{
			lineID++;
			time = 0;
		}
		if(lineID < 0||lineID >= script.getInstructions().length)
			running = false;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setLineID(Tuple<Integer, Integer> marker)
	{
		this.lineID = marker.getFirst();
		time = marker.getSecond();
	}

	public POLScript getScript()
	{
		return script;
	}

	public void setStack()
	{
		returnStack.push(new Tuple<>(lineID, time));
	}

	public void returnStack()
	{
		if(returnStack.isEmpty())
			endProcess();
		else
			setLineID(returnStack.pop());
	}

	private void endProcess()
	{
		this.running = false;
	}

	public void skip()
	{
		POLInstruction instruction = script.getInstructions()[lineID];
		time++;
		if(instruction.getExecutionTime() <= time)
		{
			lineID++;
			time = 0;
		}
	}
}
