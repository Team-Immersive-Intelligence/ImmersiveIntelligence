package pl.pabilo8.immersiveintelligence.common.item.data;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Wires;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIISmallWireCoil.SmallWires;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.immersiveintelligence.common.wire.IISmallWireType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class ItemIISmallWireCoil extends ItemIISubItemsBase<SmallWires> implements IWireCoil
{
	public ItemIISmallWireCoil()
	{
		super("small_wirecoil", 64, SmallWires.values());
	}

	public enum SmallWires implements IIItemEnum
	{
		REDSTONE(Wires.smallRedstoneWireColouration, Wires.smallRedstoneWireLength),
		DATA(Wires.smallDataWireColouration, Wires.smallDataWireLength);

		public final int colour;
		public final int length;
		private final IISmallWireType type;

		SmallWires(int colour, int length)
		{
			this.colour = colour;
			this.length = length;
			this.type = new IISmallWireType(this);
		}
	}

	@Override
	public WireType getWireType(ItemStack stack)
	{
		return stackToSub(stack).type;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"small_wirecoil"));
		if(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("linkingPos"))
		{
			int[] link = ItemNBTHelper.getIntArray(stack, "linkingPos");
			if(link.length > 3)
				tooltip.add(I18n.format(Lib.DESC_INFO+"attachedToDim", link[1], link[2], link[3], link[0]));
		}
	}

	@Nonnull
	@Override
	@ParametersAreNonnullByDefault
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		return EnumActionResult.FAIL;
	}
}
