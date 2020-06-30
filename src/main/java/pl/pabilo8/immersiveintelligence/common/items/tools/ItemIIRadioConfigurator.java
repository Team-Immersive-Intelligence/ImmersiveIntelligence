package pl.pabilo8.immersiveintelligence.common.items.tools;

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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.utils.IItemScrollable;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24-06-2019
 */
public class ItemIIRadioConfigurator extends ItemIIBase implements IItemScrollable
{
	public ItemIIRadioConfigurator()
	{
		super("radio_configurator", 1, "basic", "advanced");
	}

	public boolean isBasic(ItemStack stack)
	{
		return stack.getMetadata()==0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		list.add(I18n.format(CommonProxy.description_key+"radio_configurator_"+(isBasic(stack)?"basic": "advanced")));
		list.add(I18n.format(CommonProxy.description_key+"radio_configurator_max_frequency", isBasic(stack)?IIConfig.radioBasicMaxFrequency: IIConfig.radioAdvancedMaxFrequency));
		list.add(I18n.format(CommonProxy.description_key+"radio_configurator_frequency", ItemNBTHelper.getInt(stack, "Frequency")));
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		ItemNBTHelper.setInt(stack, "Frequency", 0);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote&&worldIn.getTileEntity(pos) instanceof IRadioDevice)
		{
			IRadioDevice device = (IRadioDevice)worldIn.getTileEntity(pos);
			TileEntity tile = worldIn.getTileEntity(pos);
			if(!player.isSneaking())
			{

				if(device.isBasicRadio()&&ItemNBTHelper.getInt(player.getHeldItem(hand), "Frequency") <= IIConfig.radioBasicMaxFrequency)
				{
					device.setFrequency(ItemNBTHelper.getInt(player.getHeldItem(hand), "Frequency"));
					ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(CommonProxy.info_key+"frequency_set", device.getFrequency())), (EntityPlayerMP)player);
					return EnumActionResult.SUCCESS;
				}
				else if(!device.isBasicRadio()&&ItemNBTHelper.getInt(player.getHeldItem(hand), "Frequency") <= IIConfig.radioAdvancedMaxFrequency)
				{
					device.setFrequency(ItemNBTHelper.getInt(player.getHeldItem(hand), "Frequency"));
					ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(CommonProxy.info_key+"frequency_set", device.getFrequency())), (EntityPlayerMP)player);
					return EnumActionResult.SUCCESS;
				}
				else
				{
					ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(CommonProxy.info_key+"frequency_invalid", IIConfig.radioBasicMaxFrequency)), (EntityPlayerMP)player);
				}
			}
			else
			{
				ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation(CommonProxy.info_key+"current_frequency", String.valueOf(device.getFrequency()))), (EntityPlayerMP)player);
			}
			ImmersiveEngineering.packetHandler.sendToAll(new MessageTileSync((TileEntityIEBase)worldIn.getTileEntity(pos), worldIn.getTileEntity(pos).serializeNBT()));
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player)
	{
		ItemNBTHelper.setInt(stack, "Frequency", Math.min(Math.max(ItemNBTHelper.getInt(stack, "Frequency")+(forward?1: -1), 0), isBasic(stack)?IIConfig.radioBasicMaxFrequency: IIConfig.radioAdvancedMaxFrequency));
		SPacketTitle packet = new SPacketTitle(Type.ACTIONBAR, new TextComponentTranslation(CommonProxy.description_key+"radio_configurator_frequency", ItemNBTHelper.getInt(stack, "Frequency")), 0, 20, 0);
		player.connection.sendPacket(packet);
	}
}
