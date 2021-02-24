package pl.pabilo8.immersiveintelligence;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 23.02.2021
 * <p>
 * A test of the POL Programming Language
 */
@Deprecated
public class IICompTest
{
	// TODO: 24.02.2021 Finish, then remove
	static final ArrayList<String> KEYWORDS = new ArrayList<>();
	static final DataPacket packet = new DataPacket();

	static
	{
		KEYWORDS.add("using");
		KEYWORDS.add("swap");
		KEYWORDS.add("execute");
		KEYWORDS.add("return");
	}

	static final ArrayList<String> list = new ArrayList<>();
	static final ArrayList<String> list2 = new ArrayList<>();
	static final HashMap<String,ArrayList<String>> scripts = new HashMap<>();

	static
	{
		list.add("using ARITMETIC_CIRCUIT from CIRCUIT0;");
		list.add("integer a = 25;");
		list.add("integer b = 10;");
		list.add("integer c = @a + @b;");
		list.add("execute subtract;");
		//list.add("integer c = @a+@b;");

		list2.add("integer c -= 1;");

		scripts.put("main",list);
		scripts.put("subtract",list2);
	}

	public static void main(String[] args)
	{
		execute("main");
	}

	private static void execute(String id)
	{
		ArrayList<String> script = scripts.get(id);
		boolean running = true;
		int lineID = 0;
		final String LOG = "["+id+"]";
		while(running)
		{
			if(!executeLine(reformatCode(script.get(lineID))))
				running = false;
			lineID++;
			if(lineID >= script.size())
				running = false;
			System.out.print(running?"[R]": "[S]");
			System.out.print(LOG);
			System.out.println(packet.toString());
		}
	}

	private static boolean executeLine(String[] reformatCode)
	{
		if(reformatCode.length > 0)
		{
			if(KEYWORDS.contains(reformatCode[0]))
			{
				switch(reformatCode[0])
				{
					case "using":
					{
						return true;
					}
					case "swap":
					{
						return true;
					}
					case "execute":
					{
						execute(reformatCode[1]);
						return true;
					}
					case "return":
					{
						return false;
					}

				}
			}
			else
			{
				if(reformatCode[0].equals("integer"))
				{
					int result = 0;
					char c = reformatCode[1].charAt(0);
					switch(reformatCode[2])
					{
						case "=":
							result = parseExpression(Arrays.copyOfRange(reformatCode, 3, reformatCode.length));
							break;
						case "+=":
							result = ((DataPacketTypeInteger)packet.getPacketVariable(c)).value+parseExpression(Arrays.copyOfRange(reformatCode, 3, reformatCode.length));
							break;
						case "-=":
							result = ((DataPacketTypeInteger)packet.getPacketVariable(c)).value-parseExpression(Arrays.copyOfRange(reformatCode, 3, reformatCode.length));
							break;
						case "*=":
							result = ((DataPacketTypeInteger)packet.getPacketVariable(c)).value*parseExpression(Arrays.copyOfRange(reformatCode, 3, reformatCode.length));
							break;
						case "/=":
							result = ((DataPacketTypeInteger)packet.getPacketVariable(c)).value/parseExpression(Arrays.copyOfRange(reformatCode, 3, reformatCode.length));
							break;
						case "%=":
							result = ((DataPacketTypeInteger)packet.getPacketVariable(c)).value%parseExpression(Arrays.copyOfRange(reformatCode, 3, reformatCode.length));
							break;
					}
					packet.setVariable(c, new DataPacketTypeInteger(result));
				}
			}
		}
		return true;
	}

	private static String[] reformatCode(String s)
	{
		return s.replaceAll(";", "").split(" ");
	}

	static int parseExpression(String[] exp)
	{
		/*
		for(int i = 0; i < exp.length; i++)
		if(exp[i].contains("("))
		 */
		String[] finExp = Arrays.stream(exp).map(s -> s.startsWith("@")?
				String.valueOf(((DataPacketTypeInteger)packet.getPacketVariable(s.substring(1).charAt(0))).value)
				: s).toArray(String[]::new);
		int g = 0;
		char op = 0;
		for(int i = 0; i < finExp.length; i++)
		{
			//System.out.println(finExp[i]+" / "+op+" / "+g);
			if(i==0)
				g = Integer.parseInt(finExp[0]);
			else if(op!=0)
			{
				int parsed = Integer.parseInt(finExp[i]);
				switch(op)
				{
					case '+':
						g += parsed;
						break;
					case '-':
						g -= parsed;
						break;
					case '*':
						g *= parsed;
						break;
					case '/':
						g /= parsed;
						break;
					case '%':
						g %= parsed;
						break;
				}
				op = 0;
			}
			else
				op = finExp[i].charAt(0);
		}
		return g;
	}
}
