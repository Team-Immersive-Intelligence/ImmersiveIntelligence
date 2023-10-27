package pl.pabilo8.immersiveintelligence.common.item;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment.ElectricSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Wires;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class ItemIITripWireCoil extends ItemIIBase implements IWireCoil
{
	public ItemIITripWireCoil()
	{
		super("trip_wire", 64);
	}

	public static IITripWireType TRIPWIRE = new IITripWireType();
	public static final String TRIPWIRE_CATEGORY = "TRIPWIRE";

	@Override
	public WireType getWireType(ItemStack stack)
	{
		return TRIPWIRE;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(stack.getTagCompound()!=null&&stack.getTagCompound().hasKey("linkingPos"))
		{
			int[] link = stack.getTagCompound().getIntArray("linkingPos");
			if(link!=null&&link.length > 3)
			{
				tooltip.add(I18n.format(Lib.DESC_INFO+"attachedToDim", link[1], link[2], link[3], link[0]));
			}
		}
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		return ApiUtils.doCoilUse(this, player, world, pos, hand, side, hitX, hitY, hitZ);
	}

	public static class IITripWireType extends WireType
	{
		public IITripWireType()
		{
			super();
		}

		/**
		 * In this case, this does not return the loss RATIO but the loss PER BLOCK
		 */
		@Override
		public double getLossRatio()
		{
			return 0;
		}

		@Override
		public int getTransferRate()
		{
			return 1;
		}

		@Override
		public int getColour(Connection connection)
		{
			return Wires.tripwireColouration;
		}

		@Override
		public double getSlack()
		{
			return 1.002;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public TextureAtlasSprite getIcon(Connection connection)
		{
			return iconDefaultWire;
		}

		@Override
		public int getMaxLength()
		{
			return Wires.tripwireLength;
		}

		@Override
		public ItemStack getWireCoil()
		{
			return new ItemStack(IIContent.itemTripWireCoil, 1, 0);
		}

		@Override
		public String getUniqueName()
		{
			return TRIPWIRE_CATEGORY;
		}

		@Override
		public double getRenderDiameter()
		{
			return 0.0125f;
		}

		@Override
		public boolean isEnergyWire()
		{
			return true;
		}

		@Override
		public String getCategory()
		{
			return TRIPWIRE_CATEGORY;
		}

		@Override
		public double getDamageRadius()
		{
			return 0;
		}

		@Override
		public boolean canCauseDamage()
		{
			return true;
		}

		@Override
		public ElectricSource getElectricSource()
		{
			return super.getElectricSource();
		}
	}
}
