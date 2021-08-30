package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTellermine;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 05.02.2021
 */
public class BlockIIRadioExplosives extends BlockIIMine
{
	public BlockIIRadioExplosives()
	{
		super("radio_explosives", ItemBlockRadioExplosives.class, IEProperties.FACING_ALL);
	}

	@Override
	protected TileEntity getMineTileEntity()
	{
		return new TileEntityRadioExplosives();
	}

	public static class ItemBlockRadioExplosives extends ItemBlockMineBase
	{
		public ItemBlockRadioExplosives(Block b)
		{
			super(b);
		}

		@Override
		public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipFlag)
		{
			super.addInformation(stack, world, tooltip, tooltipFlag);
			if(ItemNBTHelper.hasKey(stack,"programmed_data"))
				tooltip.add(I18n.format(CommonProxy.DESCRIPTION_KEY+"explosives_programmed"));
		}

		@Override
		public String getName()
		{
			return "radio_explosives";
		}

		@Override
		public float getComponentMultiplier()
		{
			return 0.45f;
		}

		@Override
		public int getCoreMaterialNeeded()
		{
			return 1;
		}

		@Override
		public float getInitialMass()
		{
			return 0.75f;
		}

		@Override
		public float getCaliber()
		{
			return 10f;
		}

		@Override
		public @Nonnull
		Class<? extends IBulletModel> getModel()
		{
			return ModelTellermine.class;
		}

		@Override
		public float getDamage()
		{
			return 0;
		}

		@Override
		public ItemStack getCasingStack(int amount)
		{
			return ItemStack.EMPTY;
		}
	}
}
