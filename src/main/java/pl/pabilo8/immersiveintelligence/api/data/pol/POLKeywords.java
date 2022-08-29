package pl.pabilo8.immersiveintelligence.api.data.pol;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Keywords used for declaring statements in a POL script
 *
 * @author Pabilo8
 * @since 15.04.2022
 */
public enum POLKeywords implements IStringSerializable
{
	//--- Setup ---//
	USE,    //use a circuit or device

	BEGIN(false), //beginning of a marker
	END,    //end the script or marker

	WAIT,    //wait for [ticks / device input]

	//--- Control Statements ---//
	SET(false),    //set a variable
	EXT,    //assign device to variable
	MARK,    //mark a code fragment
	GOTO,    //go to a marker
	EXEC,    //execute a script or marker
	IF,      //test a statement
	ELIF,    //else-if
	ELSE,    //opposite

	//--- Memory Functions ---//
	PAGE,    //set the memory [page]
	WIPE,    //clean the memory <page>
	COPY,    //copy a variable to [another page] <letters...>
	MOVE,    //move a variable to [another page] <letters...>
	SWAP,    //swap variables [letter1] [letter2] <page>

	//--- Terminal Functions ---//
	SIGN,    //display signal on console
	TYPE,    //print text on teletype
	READ;    //get text from terminal keyboard

	/**
	 * Whether this keyword is visible to the compiler<br>
	 * f.e. the {@link POLKeywords#SET} isn't visible, as variables are declared using their type names<br>
	 */
	final boolean visible;

	POLKeywords(boolean visible)
	{
		this.visible = visible;
	}

	POLKeywords()
	{
		visible = true;
	}

	public boolean isVisible()
	{
		return visible;
	}

	@Nullable
	public static POLKeywords v(String ss)
	{
		return Arrays.stream(values())
				.filter(POLKeywords::isVisible)
				.filter(e -> e.getName().equalsIgnoreCase(ss))
				.findFirst().orElse(null);
	}

	@Override
	public String getName()
	{
		return this.toString().toLowerCase();
	}
}
