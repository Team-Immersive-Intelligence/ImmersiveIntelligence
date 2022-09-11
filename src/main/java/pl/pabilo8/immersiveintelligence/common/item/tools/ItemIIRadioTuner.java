package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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
import pl.pabilo8.immersiveintelligence.Config.IIConfig;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.utils.IItemScrollable;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIRadioTuner.RadioTuners;
import pl.pabilo8.immersiveintelligence.common.util.IILib;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24-06-2019
 */
public class ItemIIRadioTuner extends ItemIISubItemsBase<RadioTuners> implements IItemScrollable
{
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

		list.add(TextFormatting.ITALIC+I18n.format(IILib.DESCRIPTION_KEY+"radio_configurator_"+tuner.getName()));
		list.add(I18n.format(IILib.DESCRIPTION_KEY+"radio_configurator_max_frequency",
				TextFormatting.GOLD.toString()+tuner.maxFrequency+TextFormatting.RESET));
		list.add(I18n.format(IILib.DESCRIPTION_KEY+"radio_configurator_frequency",
				TextFormatting.GOLD.toString()+ItemNBTHelper.getInt(stack, "Frequency")+tuner.maxFrequency+TextFormatting.RESET));
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
			int frequency = ItemNBTHelper.getInt(player.getHeldItem(hand), "Frequency");
			IRadioDevice device = (IRadioDevice)tile;
			int maxFrequency = device.isBasicRadio()?IIConfig.radioBasicMaxFrequency: IIConfig.radioAdvancedMaxFrequency;

			if(!player.isSneaking()) //set frequency
			{
				if(frequency > maxFrequency)
					ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(IILib.INFO_KEY+"frequency_invalid", maxFrequency)), (EntityPlayerMP)player);
				else
				{
					device.setFrequency(frequency);
					ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(IILib.INFO_KEY+"frequency_set", device.getFrequency())), (EntityPlayerMP)player);
				}
			}
			else //get frequency
			{
				// TODO: 06.09.2022 make shift+click copy frequency to device ; add frequency display, like with goggles
				ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(IILib.INFO_KEY+"current_frequency", String.valueOf(device.getFrequency()))), (EntityPlayerMP)player);
			}

			//sync
			ImmersiveEngineering.packetHandler.sendToAll(new MessageTileSync((TileEntityIEBase)tile, tile.serializeNBT()));
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
		int frequency = IIUtils.cycleInt(forward, ItemNBTHelper.getInt(stack, "Frequency"), 0, tuner.maxFrequency);

		//set
		ItemNBTHelper.setInt(stack, "Frequency", frequency);

		//send current frequency message
		SPacketTitle packet = new SPacketTitle(Type.ACTIONBAR,
				new TextComponentTranslation(IILib.DESCRIPTION_KEY+"radio_configurator_frequency", frequency), 0, 20, 0);
		player.connection.sendPacket(packet);
	}
}
