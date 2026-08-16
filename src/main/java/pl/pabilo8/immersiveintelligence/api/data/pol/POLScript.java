package pl.pabilo8.immersiveintelligence.api.data.pol;

import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.pol.instructions.*;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Compiles and stores executable POL programs.
 *
 * @author Pabilo8(pabilo@iiteam.net)
 * @updated 09.08.2026
 * @since 16.04.2022
 */
public class POLScript
{
	private final HashMap<String, Tuple<Integer, Integer>> markers;
	private final POLInstruction[] instructions;

	private POLScript(HashMap<String, Tuple<Integer, Integer>> markers, POLInstruction[] instructions)
	{
		this.markers = markers;
		this.instructions = instructions;
	}

	// TODO: 17.04.2022 parsing errors

	public static POLScript prepareScript(ArrayList<String> lines)
	{
		ArrayList<Tuple<POLKeywords, String>> text = processText(lines);
		return compile(text);
	}

	/**
	 * @return text processed into POL keywords and arguments
	 */
	protected static ArrayList<Tuple<POLKeywords, String>> processText(ArrayList<String> text)
	{
		int level = 0;
		ArrayList<Tuple<POLKeywords, String>> output = new ArrayList<>();

		for(String line : text)
		{
			String next = stripComment(line);
			if(next.trim().isEmpty())
				continue;

			int indents = getIndentLevel(next);
			next = stripIndent(next).replaceFirst("\\s+$", "");

			// Add BEGIN or END if the level differs.
			for(int j = level-indents; j > 0; j--)
				output.add(new Tuple<>(POLKeywords.END, ""));
			for(int j = indents-level; j > 0; j--)
				output.add(new Tuple<>(POLKeywords.BEGIN, ""));

			level = indents;

			String[] split = next.split("\\s+", 2);
			String element = split[0];
			POLKeywords keyword = POLKeywords.v(element);

			if(IIDataTypeUtils.metaTypesByName.containsKey(element))
				output.add(new Tuple<>(POLKeywords.SET, next));
			else if(keyword!=null)
				output.add(new Tuple<>(keyword, split.length > 1?split[1]: ""));
		}

		for(int i = 0; i < level; i++)
			output.add(new Tuple<>(POLKeywords.END, ""));

		return output;
	}

	private static String stripComment(String line)
	{
		char quote = 0;
		boolean escaped = false;

		for(int i = 0; i < line.length(); i++)
		{
			char c = line.charAt(i);
			if(quote!=0)
			{
				if(escaped)
				{
					escaped = false;
					continue;
				}
				if(c=='\\')
				{
					escaped = true;
					continue;
				}
				if(c==quote)
					quote = 0;
				continue;
			}

			if(c=='\''||c=='"')
				quote = c;
			else if(c==';')
				return line.substring(0, i);
		}

		return line;
	}

	private static int getIndentLevel(String line)
	{
		int columns = 0;
		for(int i = 0; i < line.length(); i++)
		{
			char c = line.charAt(i);
			if(c==' ')
				columns++;
			else if(c=='\t')
				columns += 4;
			else
				break;
		}
		return columns/4;
	}

	private static String stripIndent(String line)
	{
		int index = 0;
		while(index < line.length()&&(line.charAt(index)==' '||line.charAt(index)=='\t'))
			index++;
		return line.substring(index);
	}

	/**
	 * @return a compiled {@link POLScript}
	 */
	protected static POLScript compile(ArrayList<Tuple<POLKeywords, String>> script)
	{
		//operations declared by USE keyword
		ArrayList<DataOperation> operations = new ArrayList<>();
		//markers and their line IDs
		HashMap<String, Tuple<Integer, Integer>> markers = new HashMap<>();

		//set for instructions
		ArrayList<POLInstruction> mainSet = new ArrayList<>();
		ArrayList<ArrayList<POLInstruction>> subSets = new ArrayList<>();

		//parse lines
		for(Tuple<POLKeywords, String> tuple : script)
		{
			POLKeywords keyword = tuple.getFirst();
			String rest = tuple.getSecond();

			POLInstruction in = null;
			switch(keyword)
			{
				// TODO: 17.04.2022 devices
				case USE: //add a circuit
				{
					Circuits circuit = Arrays.stream(Circuits.values())
							.filter(e -> e.getName().equals(rest.toLowerCase()))
							.findFirst().orElse(Circuits.ARITHMETIC);
					for(String function : circuit.getFunctions())
						operations.add(IIDataOperationUtils.getOperationInstance(function));
				}
				break;
				case MARK: //add a marker
				{
					markers.put(rest,
							new Tuple<>(mainSet.size(),
									subSets.stream()
											.map(ArrayList::size)
											.mapToInt(Integer::intValue).sum()-1
							));
					//no need for a marker instruction
				}
				break;
				case BEGIN: //start new subset
					subSets.add(new ArrayList<>());
					break;
				case GOTO: //go to a marker
				{
					in = new POLInstructionGoto(beginParseExpression(operations, rest));
				}
				break;
				case EXEC: //go to a marker and return
				{
					in = new POLInstructionExec(beginParseExpression(operations, rest));
				}
				break;
				case IF: //perform if condition is met
				{
					in = new POLInstructionIf(beginParseExpression(operations, rest));
				}
				break;
				case WAIT: //wait ticks
				{
					in = new POLInstructionWait(beginParseExpression(operations, rest));
				}
				break;
				case END: //end a group or the program
				{
					if(!finishInstructionGroup(subSets, mainSet))
						in = new POLInstructionEnd();
				}
				break;
				case TYPE: //type text
					in = new POLInstructionType(beginParseExpression(operations, rest));
					break;
				case SWAP: //swap variables
				{
					String[] words = rest.split("\\s+", 4);
					in = new POLInstructionSwap(
							words[0].charAt(0),
							words.length > 1?words[1].charAt(0): words[0].charAt(0),
							words.length > 2?beginParseExpression(operations, words[2]): null
					);
				}
				break;
				case MOVE: //move a variable
				{
					String[] words = rest.split("\\s+", 4);
					in = new POLInstructionMove(
							words[0].charAt(0),
							words.length > 1?words[1].charAt(0): words[0].charAt(0),
							words.length > 2?beginParseExpression(operations, words[2]): null
					);
				}
				break;
				case COPY: //copy a variable
				{
					String[] words = rest.split("\\s+", 4);
					in = new POLInstructionCopy(
							words[0].charAt(0),
							words.length > 1?words[1].charAt(0): words[0].charAt(0),
							words.length > 2?beginParseExpression(operations, words[2]): null
					);
				}
				break;
				case SET: //set variable value
				{
					String[] words = rest.split("\\s+", 4);
					char letter = words[1].charAt(0);

					in = new POLInstructionSet(letter, beginParseExpression(operations, words[3]), IIDataTypeUtils.getVarInstance(words[0]).getClass());
				}
				break;
				case PAGE: //change memory page
				{
					in = new POLInstructionPage(beginParseExpression(operations, rest));
				}
				break;
				case WIPE: //remove variables from page
				{
					in = new POLInstructionWipe(beginParseExpression(operations, rest));
				}
				break;
			}

			if(in!=null)
				addInstruction(subSets, mainSet, in);
		}

		return new POLScript(markers, mainSet.toArray(new POLInstruction[0]));
	}

	private static boolean finishInstructionGroup(ArrayList<ArrayList<POLInstruction>> subSets, ArrayList<POLInstruction> mainSet)
	{
		ArrayList<POLInstruction> group = null;
		if(subSets.size() > 0) //get last or none
			group = subSets.get(subSets.size()-1);

		if(group!=null) //it's end of a group
		{
			if(subSets.size()-2 < 0)
				mainSet.add(new POLInstructionGroup(group));
			else
				subSets.get(subSets.size()-2).add(new POLInstructionGroup(group));
			subSets.remove(subSets.size()-1);
			return true;
		}
		return false; //it's end of program
	}

	private static void addInstruction(ArrayList<ArrayList<POLInstruction>> subSets, ArrayList<POLInstruction> mainSet, POLInstruction instruction)
	{
		ArrayList<POLInstruction> group = null;
		if(subSets.size() > 0) //get last or none
			group = subSets.get(subSets.size()-1);

		if(group!=null)
			group.add(instruction);
		else
			mainSet.add(instruction);
	}

	private static DataType beginParseExpression(ArrayList<DataOperation> operations, String text)
	{
		text = text.trim();
		if(!isExpressionBalanced(text))
			return new DataTypeNull();
		return parseExpression(operations, text);
	}

	/**
	 * Parses a POL expression in prefix notation.
	 */
	private static DataType parseExpression(ArrayList<DataOperation> operations, String text)
	{
		text = text.trim();
		if(text.isEmpty())
			return new DataTypeNull();

		if(text.charAt(0)=='(')
		{
			int closing = findClosingBracket(text);
			if(closing==text.length()-1)
				return parseExpression(operations, text.substring(1, closing));
		}

		String keyword = firstToken(text);
		DataOperation operation = findOperation(operations, keyword);
		if(operation==null)
			return parseValue(text).getFirst();

		ArrayList<DataType> arguments = new ArrayList<>();
		String remaining = text.substring(keyword.length()).trim();
		while(!remaining.isEmpty())
		{
			Tuple<DataType, String> tuple;
			if(remaining.charAt(0)=='(')
			{
				int closing = findClosingBracket(remaining);
				if(closing < 0)
					return new DataTypeNull();
				tuple = new Tuple<>(
						parseExpression(operations, remaining.substring(1, closing)),
						remaining.substring(closing+1)
				);
			}
			else
				tuple = parseValue(remaining);

			arguments.add(tuple.getFirst());
			String next = tuple.getSecond().trim();
			if(next.equals(remaining))
				break;
			remaining = next;
		}

		return new DataTypeExpression(arguments.toArray(new DataType[0]), operation, ' ');
	}

	private static DataOperation findOperation(ArrayList<DataOperation> operations, String keyword)
	{
		for(DataOperation operation : operations)
		{
			if(operation.getMeta().name().equals(keyword)||operation.getMeta().expression().equals(keyword))
				return operation;
		}
		return null;
	}

	private static int findClosingBracket(String text)
	{
		int depth = 0;
		char quote = 0;
		boolean escaped = false;

		for(int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if(quote!=0)
			{
				if(escaped)
				{
					escaped = false;
					continue;
				}
				if(c=='\\')
				{
					escaped = true;
					continue;
				}
				if(c==quote)
					quote = 0;
				continue;
			}

			if(c=='\''||c=='"')
				quote = c;
			else if(c=='(')
				depth++;
			else if(c==')'&&--depth==0)
				return i;
		}

		return -1;
	}

	private static boolean isExpressionBalanced(String text)
	{
		int depth = 0;
		char quote = 0;
		boolean escaped = false;

		for(int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if(quote!=0)
			{
				if(escaped)
				{
					escaped = false;
					continue;
				}
				if(c=='\\')
				{
					escaped = true;
					continue;
				}
				if(c==quote)
					quote = 0;
				continue;
			}

			if(c=='\''||c=='"')
				quote = c;
			else if(c=='(')
				depth++;
			else if(c==')'&&--depth < 0)
				return false;
		}

		return depth==0&&quote==0;
	}

	private static Tuple<DataType, String> parseValue(String text)
	{
		text = text.trim();
		if(text.isEmpty())
			return new Tuple<>(new DataTypeNull(), "");

		char first = text.charAt(0);
		if(first=='"'||first=='\'')
		{
			int closing = findClosingQuote(text, first);
			if(closing < 0)
				return new Tuple<>(new DataTypeNull(), "");
			return new Tuple<>(
					new DataTypeString(unescapeString(text.substring(1, closing), first)),
					text.substring(closing+1)
			);
		}

		if(first=='@')
		{
			if(text.length() < 2)
				return new Tuple<>(new DataTypeNull(), "");
			return new Tuple<>(new DataTypeAccessor(text.charAt(1)), text.substring(2));
		}

		String token = firstToken(text);
		if(isNumericToken(token))
		{
			try
			{
				DataType value = token.indexOf('.') >= 0||token.indexOf('e') >= 0||token.indexOf('E') >= 0?
						new DataTypeFloat(Float.parseFloat(token)):
						new DataTypeInteger(Integer.parseInt(token));
				return new Tuple<>(value, text.substring(token.length()));
			} catch(NumberFormatException ignored)
			{
				return new Tuple<>(new DataTypeInteger(0), text.substring(token.length()));
			}
		}

		if(token.equals("true"))
			return new Tuple<>(new DataTypeBoolean(true), text.substring(token.length()));
		if(token.equals("false"))
			return new Tuple<>(new DataTypeBoolean(false), text.substring(token.length()));
		if(token.equals("null"))
			return new Tuple<>(new DataTypeNull(), text.substring(token.length()));

		return new Tuple<>(new DataTypeNull(), text.substring(token.length()));
	}

	private static int findClosingQuote(String text, char quote)
	{
		boolean escaped = false;
		for(int i = 1; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if(escaped)
			{
				escaped = false;
				continue;
			}
			if(c=='\\')
			{
				escaped = true;
				continue;
			}
			if(c==quote)
				return i;
		}
		return -1;
	}

	private static String unescapeString(String text, char quote)
	{
		StringBuilder result = new StringBuilder(text.length());
		for(int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			if(c=='\\'&&i+1 < text.length())
			{
				char next = text.charAt(i+1);
				if(next==quote||next=='\\')
				{
					result.append(next);
					i++;
					continue;
				}
			}
			result.append(c);
		}
		return result.toString();
	}

	private static String firstToken(String text)
	{
		int end = 0;
		while(end < text.length()&&!Character.isWhitespace(text.charAt(end)))
			end++;
		return text.substring(0, end);
	}

	private static boolean isNumericToken(String token)
	{
		if(token.isEmpty())
			return false;
		char first = token.charAt(0);
		return Character.isDigit(first)||((first=='-'||first=='+')&&token.length() > 1&&
				(Character.isDigit(token.charAt(1))||token.charAt(1)=='.'));
	}

	public POLInstruction[] getInstructions()
	{
		return instructions;
	}

	public HashMap<String, Tuple<Integer, Integer>> getMarkers()
	{
		return markers;
	}

	public static class DataTypeWrapper
	{
		final DataType wrapped;

		public DataTypeWrapper(DataType wrapped)
		{
			this.wrapped = wrapped;
		}

		public DataType get(DataPacket packet)
		{
			if(wrapped instanceof DataTypeExpression)
				return ((DataTypeExpression)wrapped).getValue(packet);
			if(wrapped instanceof DataTypeAccessor)
				return ((DataTypeAccessor)wrapped).getRealValue(packet);

			return packet.getVarInType(DataType.class, wrapped);
		}

		public String getString(DataPacket packet)
		{
			return get(packet).toString();
		}
	}

	/**
	 * A compiled, executable POL instruction, can contain various statements
	 */
	public static abstract class POLInstruction
	{
		private final int executionTime;

		public POLInstruction(int executionTime)
		{
			this.executionTime = executionTime;
		}

		public abstract void execute(POLComputerMemory memory, POLTerminal terminal, POLProcess polProcess, int executionTime);

		public abstract POLKeywords getKeyword();

		public int getExecutionTime()
		{
			return executionTime;
		}
	}

}
