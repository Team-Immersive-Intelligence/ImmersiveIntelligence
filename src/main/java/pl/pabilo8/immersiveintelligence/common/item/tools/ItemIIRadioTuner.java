package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIRadioTuner.RadioTuners;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24-06-2019
 */
@IIItemProperties(category = IICategory.TOOLS)
public class ItemIIRadioTuner extends ItemIISubItemsBase<RadioTuners> implements IItemScrollable
{
	public static final String TAG_PREV_FREQUENCY = "prev_frequency";
	public static final String TAG_FREQUENCY = "frequency";

	public ItemIIRadioTuner()
	{
		super("radio_configurator", 1, RadioTuners.values());
	}

	public enum RadioTuners implements IIItemEnum
	{
		BASIC(IIConfig.radioBasicMaxFrequency),
		ADVANCED(IIConfig.radioAdvancedMaxFrequency);

		final int maxFrequency;

		RadioTuners(int maxFrequency)
		{
			this.maxFrequency = maxFrequency;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag)
	{
		RadioTuners tuner = stackToSub(stack);

		list.add(IIUtils.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"radio_configurator_"+tuner.getName())));
		list.add(I18n.format(IIReference.DESCRIPTION_KEY+"radio_configurator_max_frequency",
				TextFormatting.GOLD.toString()+tuner.maxFrequency+TextFormatting.RESET));
		list.add(I18n.format(IIReference.DESCRIPTION_KEY+"radio_configurator_frequency",
				TextFormatting.GOLD.toString()+ItemNBTHelper.getInt(stack, TAG_FREQUENCY)+TextFormatting.RESET));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		//Frequency for smooth animations
		EasyNBT nbt = EasyNBT.wrapNBT(stack);
		int frequency = nbt.getInt(TAG_FREQUENCY);

		if(nbt.getInt(TAG_PREV_FREQUENCY)!=frequency)
			nbt.withInt(TAG_PREV_FREQUENCY, frequency);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged;
	}

	@Override
	@Nonnull
	public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return EnumActionResult.FAIL;

		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof IRadioDevice)
		{
			ItemStack stack = player.getHeldItem(hand);

			int frequency = ItemNBTHelper.getInt(stack, TAG_FREQUENCY);
			IRadioDevice device = (IRadioDevice)tile;
			int maxFrequency = device.isBasicRadio()?IIConfig.radioBasicMaxFrequency: IIConfig.radioAdvancedMaxFrequency;

			if(!player.isSneaking()) //set frequency
			{
				if(frequency > maxFrequency)
					IIPacketHandler.sendChatTranslation(player, IIReference.INFO_KEY+"frequency_invalid", maxFrequency);
				else
				{
					device.setFrequency(frequency);
					IIPacketHandler.sendChatTranslation(player, IIReference.INFO_KEY+"frequency_set", device.getFrequency());
				}
			}
			else //get frequency
			{
				ItemNBTHelper.setInt(stack, TAG_FREQUENCY, frequency);
				sendSetFrequencyPacket(((EntityPlayerMP)player), frequency);
			}

			//sync
			IIPacketHandler.sendToClient(tile, new MessageIITileSync(((TileEntityIEBase)tile)));
			return EnumActionResult.SUCCESS;
		}
		else


			return EnumActionResult.FAIL;
	}

	@Override
	public void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player)
	{
		RadioTuners tuner = stackToSub(stack);
		//cycle
		int frequency = IIUtils.cycleInt(forward, ItemNBTHelper.getInt(stack, TAG_FREQUENCY), 0, tuner.maxFrequency);

		//set
		ItemNBTHelper.setInt(stack, TAG_FREQUENCY, frequency);

		//send current frequency message
		sendSetFrequencyPacket(player, frequency);
	}

	private static void sendSetFrequencyPacket(EntityPlayerMP player, int frequency)
	{
		player.connection.sendPacket(
				new SPacketTitle(Type.ACTIONBAR,
						new TextComponentTranslation(IIReference.DESCRIPTION_KEY+"radio_configurator_frequency", frequency), 0, 20, 0)
		);
	}
}
