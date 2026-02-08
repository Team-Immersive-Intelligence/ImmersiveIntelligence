package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeLogisticTag;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a transportable item tag used by the II logistics system.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 26.01.2026
 */
public class LogisticTag implements INBTSerializable<NBTTagCompound>, Cloneable
{
	public static final String NBT_KEY = "ii_logi_tag";
	private static final String TRANSLATION_KEY = IIReference.DESCRIPTION_KEY+"logistic_tag.info.";

	//--- Properties ---//
	private String name = "";
	private String description = "";
	private OwnerIdentity owner = DiplomacyUtils.NEUTRAL;
	private String origin = "";
	private String destination = "";
	private EnumDyeColor color = null;
	private int batchNumber = 0;

	public LogisticTag()
	{

	}

	public LogisticTag(NBTTagCompound logisticsTag)
	{
		this();
		deserializeNBT(logisticsTag);
	}

	public LogisticTag(DataPacket packet)
	{
		this();
		LogisticTag value;
		//Logistics tag data type
		if(packet.has('l'))
		{
			DataTypeLogisticTag variable = packet.getVarInType(DataTypeLogisticTag.class, packet.get('l'));
			value = variable.value;
		}
		//ItemStack of a logistics tag
		else if(packet.has('s'))
		{
			ItemStack stack = packet.getVarInType(DataTypeItemStack.class, packet.get('s')).value;
			//Only allow logistics tag items in this context
			if(stack.getItem()!=IIContent.itemLogisticTag)
				return;
			value = getLogisticsTagFromStack(stack);
		}
		//Packet with logistics tag made by individual variables
		else
		{
			//Name/Description
			IIDataHandlingUtils.optionalString('n', packet)
					.ifPresent(string -> this.name = string);
			IIDataHandlingUtils.optionalString('d', packet)
					.ifPresent(string -> this.description = string);
			//Batch number
			IIDataHandlingUtils.optionalInt('b', packet)
					.ifPresent(integer -> this.batchNumber = integer);
			//From/To
			IIDataHandlingUtils.optionalString('f', packet)
					.ifPresent(string -> this.origin = string);
			IIDataHandlingUtils.optionalString('t', packet)
					.ifPresent(string -> this.destination = string);
			//Owner
			IIDataHandlingUtils.optionalString('o', packet)
					.map(DiplomacyUtils::getIdentityByName)
					.ifPresent(identity -> this.owner = identity);
			//Color (Paint)
			IIDataHandlingUtils.optionalColor('p', packet)
					.ifPresent(color -> this.color = color.getDyeColor());
			return;
		}

		if(value==null)
			return;
		this.name = value.name;
		this.description = value.description;
		this.owner = value.owner;
		this.origin = value.origin;
		this.destination = value.destination;
		this.color = value.color;
	}

	//--- Setters ---//

	public LogisticTag withName(String name)
	{
		this.name = name;
		return this;
	}

	public LogisticTag withDescription(String description)
	{
		this.description = description;
		return this;
	}

	public LogisticTag withOwner(OwnerIdentity owner)
	{
		this.owner = owner;
		return this;
	}

	public LogisticTag withOrigin(String origin)
	{
		this.origin = origin;
		return this;
	}

	public LogisticTag withDestination(String destination)
	{
		this.destination = destination;
		return this;
	}

	public LogisticTag withColor(EnumDyeColor color)
	{
		this.color = color;
		return this;
	}

	public LogisticTag withBatchNumber(int batchNumber)
	{
		this.batchNumber = batchNumber;
		return this;
	}

	//--- Getters ---//

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public OwnerIdentity getOwner()
	{
		return owner;
	}

	public String getOrigin()
	{
		return origin;
	}

	public String getDestination()
	{
		return destination;
	}

	public EnumDyeColor getColor()
	{
		return color;
	}

	public int getBatchNumber()
	{
		return batchNumber;
	}

	//--- NBT Serialization ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withString("name", name)
				.withString("description", description)
				.withString("origin", origin)
				.withString("destination", destination)
				.withColor("color", IIColor.fromDye(color))
				.withString("owner", owner.getDisplayName())
				.withInt("batch_number", batchNumber)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		EasyNBT enbt = EasyNBT.wrapNBT(nbt);
		name = enbt.getString("name");
		description = enbt.getString("description");
		origin = enbt.getString("origin");
		destination = enbt.getString("destination");
		batchNumber = enbt.getInt("batch_number");
		//Color
		color = null;
		enbt.checkSetColor("color", found -> color = found.getDyeColor());
		//Owner
		owner = DiplomacyUtils.NEUTRAL;
		enbt.checkSetString("owner", DiplomacyUtils::getIdentityByName);
	}

	//--- Utils ---//

	/**
	 * @return true if the stack is the logistic tag carrier item and has a serialized tag payload.
	 */
	public static boolean hasLogisticsTag(ItemStack stack)
	{
		return ItemNBTHelper.hasKey(stack, NBT_KEY);
	}

	@Nullable
	public static LogisticTag getLogisticsTagFromStack(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_KEY))
			return null;
		NBTTagCompound tagCompound = ItemNBTHelper.getTagCompound(stack, NBT_KEY);
		if(tagCompound==null)
			return null;
		return new LogisticTag(tagCompound);
	}

	@SideOnly(Side.CLIENT)
	public List<String> addLogisticsTooltip(List<String> tooltip)
	{
		if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT, TRANSLATION_KEY+"hold", tooltip))
		{
			tooltip.add(I18n.format(TRANSLATION_KEY+"main"));
			//Name
			if(name.isEmpty())
				tooltip.add(I18n.format(TRANSLATION_KEY+"name.none"));
			else
				tooltip.add(I18n.format(TRANSLATION_KEY+"name", TextFormatting.WHITE+name));
			//Description
			if(description.isEmpty())
				tooltip.add(I18n.format(TRANSLATION_KEY+"description.none"));
			else
				tooltip.add(I18n.format(TRANSLATION_KEY+"description", TextFormatting.WHITE+description));
			//Batch Number
			if(batchNumber!=0)
				tooltip.add(I18n.format(TRANSLATION_KEY+"batch_number", TextFormatting.WHITE+""+batchNumber));

			//Owner
			if(owner!=DiplomacyUtils.NEUTRAL)
				tooltip.add(I18n.format(TRANSLATION_KEY+"owner", TextFormatting.WHITE+owner.getDisplayName()));
			//To and From
			if(!origin.isEmpty())
				tooltip.add(I18n.format(TRANSLATION_KEY+"origin", TextFormatting.WHITE+origin));
			if(!destination.isEmpty())
				tooltip.add(I18n.format(TRANSLATION_KEY+"destination", TextFormatting.WHITE+destination));

			//Color
			if(color!=null)
				tooltip.add(I18n.format(TRANSLATION_KEY+"color",
						color.chatColor+I18n.format("item.fireworksCharge."+color.getUnlocalizedName())));
			else
				tooltip.add(IIStringUtil.getItalicString(I18n.format(TRANSLATION_KEY+"color.none")));
		}


		return tooltip;
	}

	/**
	 * Applies this tag to the given ItemStack and returns it.
	 */
	public ItemStack applyToStack(ItemStack stack)
	{
		//Set name
		if(this.name.isEmpty())
			stack.clearCustomName();
		else
			stack.setStackDisplayName(this.name);
		//Set logistics tag
		ItemNBTHelper.setTagCompound(stack, NBT_KEY, this.serializeNBT());
		return stack;
	}

	/**
	 * Checks whether the given ItemStack has this tag applied.
	 */
	public boolean itemMatches(ItemStack stack)
	{
		NBTTagCompound tagCompound = ItemNBTHelper.getTagCompound(stack, NBT_KEY);
		if(tagCompound==null)
			return false;
		LogisticTag tag = new LogisticTag();
		tag.deserializeNBT(tagCompound);
		return this.equals(tag);
	}

	//--- Equals and Hashcode ---//

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public LogisticTag clone()
	{
		return new LogisticTag(this.serializeNBT());
	}

	@Override
	public final boolean equals(Object object)
	{
		if(!(object instanceof LogisticTag))
			return false;

		LogisticTag that = (LogisticTag)object;
		return Objects.equals(name, that.name)&&
				Objects.equals(description, that.description)&&
				Objects.equals(owner, that.owner)&&
				Objects.equals(origin, that.origin)&&
				Objects.equals(destination, that.destination)&&
				color==that.color;
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hashCode(name);
		result = 31*result+Objects.hashCode(description);
		result = 31*result+Objects.hashCode(owner);
		result = 31*result+Objects.hashCode(origin);
		result = 31*result+Objects.hashCode(destination);
		result = 31*result+Objects.hashCode(color);
		return result;
	}

	@Override
	public String toString()
	{
		return "LogisticsTag{"+
				"name='"+name+'\''+
				", description='"+description+'\''+
				(owner==DiplomacyUtils.NEUTRAL?"": ", owner="+owner)+
				", origin='"+origin+'\''+
				", destination='"+destination+'\''+
				(color==null?"": ", color="+color)+
				'}';
	}
}
