package pl.pabilo8.immersiveintelligence.api.style;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints.PaintStyleConstraint;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 07.09.2025
 */
public class StyleCustomization implements INBTSerializable<NBTTagCompound>
{
	private final StyleConstraints constraints;
	private final List<String> decorations;
	private String style;
	private IIColor color;

	public StyleCustomization(StyleConstraints constraints)
	{
		this.constraints = constraints;
		this.style = constraints.getDefaultStyle();
		this.color = IIColor.WHITE;
		this.decorations = new ArrayList<>();
	}

	//--- Getters ---//

	public String getStyle()
	{
		return style;
	}

	public IIColor getColor()
	{
		return color;
	}

	public List<String> getDecorations()
	{
		return decorations;
	}

	public StyleConstraints getConstraints()
	{
		return constraints;
	}

	//--- Setters ---//

	public StyleCustomization withStyle(String style)
	{
		this.style = style;
		return this;
	}

	public StyleCustomization withColor(IIColor color)
	{
		if(constraints.getColorCustomization()==PaintStyleConstraint.PAINTS_COLOR_ONLY)
			color = color.constraintToPaintSystem();
		this.color = color;
		return this;
	}

	//--- INBTSerializable ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withString("style", style)
				.withColor("color", color)
				.withList("decorations", decorations)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		EasyNBT enbt = EasyNBT.wrapNBT(nbt);
		style = enbt.getString("style");
		color = enbt.getColor("color");
		decorations.clear();
		enbt.streamList(NBTTagString.class, "decorations")
				.map(NBTTagString::getString)
				.forEach(decorations::add);
	}

	//--- Equals and HashCode ---//

	@Override
	public final boolean equals(Object o)
	{
		if(!(o instanceof StyleCustomization))
			return false;
		StyleCustomization that = (StyleCustomization)o;
		return Objects.equals(constraints, that.constraints)
				&&decorations.equals(that.decorations)
				&&style.equals(that.style)
				&&color.equals(that.color);
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hashCode(constraints);
		result = 31*result+decorations.hashCode();
		result = 31*result+style.hashCode();
		result = 31*result+color.hashCode();
		return result;
	}
}
