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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An executable script of the POL Programming Language
 *
 * @author Pabilo8
 * @since 16.04.2022
 */
public class POLScript
{
	private static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");

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
		//remove comments and empty lines
		text = (ArrayList<String>)text.stream()
				.map(s -> SPECIAL_REGEX_CHARS.matcher(s).replaceAll("\\\\$0")) //replace regex chars
				.map(s -> s.replaceAll(";(?<=;).*$", "")) //remove comments
				.map(s -> s.replaceAll("\\\\", "")) //revert regex chars
				.map(s -> s.replaceAll("\\s+$", "")) //trim end whitespace
				.map(s -> s.replaceAll(" {4}", "\t")) //turn 4x string into tabulations
				.filter(s -> !s.isEmpty()) //remove empty lines
				.collect(Collectors.toList());

		int level = 0;

		ArrayList<Tuple<POLKeywords, String>> output = new ArrayList<>();

		for(String next : text)
		{
			int indents = countChars(next, '\t');
			next = next.replaceAll("\t", "");

			//add BEGIN or END if the level differs
			for(int j = level-indents; j > 0; j--)
				output.add(new Tuple<>(POLKeywords.END, "")); //add end keywords
			for(int j = indents-level; j > 0; j--)
				output.add(new Tuple<>(POLKeywords.BEGIN, "")); //add begin keywords

			level = indents;

			String element = next.split(" ")[0];
			POLKeywords keyword = POLKeywords.v(element);

			if(element.length()==0) //letter
				output.add(new Tuple<>(POLKeywords.SET, "generic "+next));
			else if(IIDataTypeUtils.metaTypesByName.containsKey(element)) //type name
				output.add(new Tuple<>(POLKeywords.SET, next));
			else if(keyword!=null)
			{
				output.add(new Tuple<>(keyword,
						next.split(" ").length > 1?next.split(" ", 2)[1]: ""
				));
			}
		}

		//add END if there is no finish marker
		for(int i = 0; i < level; i++)
			output.add(new Tuple<>(POLKeywords.END, ""));

		return output;

		/*return (ArrayList<Tuple<POLKeywords, String>>)
				text.stream().map(s -> {
					String[] split = s.split(" ", 2);
					return new Tuple<>(
							Optional.ofNullable(POLKeywords.v(split[0])).orElse(POLKeywords.WAIT), //shouldn't happen
							split.length>1?split[1]:""
					);
				}).collect(Collectors.toList());*/
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
					String[] words = rest.split(" ", 4);
					in = new POLInstructionSwap(
							words[0].charAt(0),
							words.length > 1?words[1].charAt(0): words[0].charAt(0),
							words.length > 2?beginParseExpression(operations, words[2]): null
					);
				}
				break;
				case MOVE: //move a variable
				{
					String[] words = rest.split(" ", 4);
					in = new POLInstructionMove(
							words[0].charAt(0),
							words.length > 1?words[1].charAt(0): words[0].charAt(0),
							words.length > 2?beginParseExpression(operations, words[2]): null
					);
				}
				break;
				case COPY: //copy a variable
				{
					String[] words = rest.split(" ", 4);
					in = new POLInstructionCopy(
							words[0].charAt(0),
							words.length > 1?words[1].charAt(0): words[0].charAt(0),
							words.length > 2?beginParseExpression(operations, words[2]): null
					);
				}
				break;
				case SET: //set variable value
				{
					String[] words = rest.split(" ", 4);
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
		if(countChars(text, '(')!=countChars(text, ')'))
			return new DataTypeNull(); //unbalanced brackets

		if((countChars(text, '\'')+countChars(text, '\"'))%2!=0)
			return new DataTypeNull(); //non even number of apostrophes

		return parseExpression(operations, text);
	}

	// TODO: 19.04.2022 somehow add escaping special characters

	/**
	 * Polish Notation for the win!
	 * (what else would you expect in a language named POL) :D
	 */
	private static DataType parseExpression(ArrayList<DataOperation> operations, String text)
	{
		if(text.length()==0)
			return new DataTypeNull();

		if(text.startsWith("("))
		{
			return parseExpression(operations, text.substring(1, Math.max(text.length()-2, 1)));
		}

		DataOperation op = null;
		ArrayList<DataType> arguments = new ArrayList<>();
		String keyword = text.split(" ")[0];

		for(DataOperation operation : operations)
			if(operation.getMeta().name().equals(keyword))
			{
				op = operation;
				break;
			}
		if(op==null)
			for(DataOperation operation : operations)
			{
				String expression = operation.getMeta().expression();
				if(!expression.isEmpty()&&expression.equals(keyword))
				{
					op = operation;
					keyword = SPECIAL_REGEX_CHARS.matcher(keyword).replaceAll("\\\\$0"); //replace special chars, like +, -, etc.
					break;
				}
			}

		if(op!=null)
		{
			String remaining = text.replaceFirst(keyword, "").trim();
			while(remaining.length() > 0)
			{
				Tuple<DataType, String> tuple;
				if(remaining.startsWith("("))
				{
					String exp = findFullBracket(remaining);
					tuple = new Tuple<>(
							parseExpression(operations, exp),
							remaining
									.replaceFirst("\\(", "")
									.replaceFirst(SPECIAL_REGEX_CHARS.matcher(exp).replaceAll("\\\\$0"), "") //replace special chars, like +, -, etc.
									.replaceFirst("\\)", "")
					);
				}
				else
					tuple = parseValue(remaining);

				arguments.add(tuple.getFirst());
				remaining = tuple.getSecond().trim();
			}

			return new DataTypeExpression(arguments.toArray(new DataType[0]), op, ' ');
		}
		else
		{
			return parseValue(text).getFirst();
		}


		//parse into operation
		/*for(DataOperation op : operations)
		{
			if(op.expression!=null)
			{
				Pattern pattern = Pattern.compile(op.expression.replaceAll("%s", "\\w"));
			}
		}*/
	}

	private static String findFullBracket(String remaining)
	{
		int opening = 1, closing = 0, c = 1;
		final int l = remaining.length();
		while(opening!=closing&&c < l)
		{
			switch(remaining.charAt(c))
			{
				case '(':
					opening++;
					break;
				case ')':
					closing++;
					break;
				default:
					break;
			}
			c++;
		}

		return remaining.substring(1, c-1);
	}

	private static Tuple<DataType, String> parseValue(String text)
	{
		DataType data;

		if(text.charAt(0)=='\"')
		{
			data = new DataTypeString(text.substring(1, text.indexOf('\"', 1)));
			text = text.replaceFirst("\".+\"", "");
		}
		else if(text.charAt(0)=='\'')
		{
			data = new DataTypeString(text.substring(1, text.indexOf('\'', 1)));
			text = text.replaceFirst("'.+'", "");
		}
		else if(text.charAt(0)=='@')
		{
			data = new DataTypeAccessor(text.charAt(1));
			text = text.replaceFirst("@.", "");
		}
		else if(Character.isDigit(text.charAt(0)))
		{
			String num = text.split(" ")[0];
			try
			{
				data = countChars(num, '.') > 0?new DataTypeFloat(Float.parseFloat(num)): new DataTypeInteger(Integer.parseInt(num));
			} catch(Exception i)
			{
				try
				{
					data = new DataTypeInteger(Integer.parseInt(num));
				} catch(Exception i2)
				{
					data = new DataTypeInteger(0);
				}
			}

			text = text.replaceFirst(num, "");
		}
		else if(text.split(" ")[0].equals("true"))
		{
			return new Tuple<>(new DataTypeBoolean(true), text.replaceFirst("true", ""));
		}
		else if(text.split(" ")[0].equals("false"))
		{
			return new Tuple<>(new DataTypeBoolean(false), text.replaceFirst("false", ""));
		}
		else if(text.split(" ")[0].equals("null"))
		{
			return new Tuple<>(new DataTypeNull(), text.replaceFirst("null", ""));
		}
		else
		{
			data = new DataTypeNull();
			text = "";
		}

		return new Tuple<>(data, text);
	}

	private static int countChars(String s, char c)
	{
		return (int)s.chars().filter(v -> v==c).count();
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