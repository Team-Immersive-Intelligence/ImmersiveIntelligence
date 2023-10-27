package pl.pabilo8.immersiveintelligence.common.block.mines;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTellermine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityRadioExplosives;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

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
		super("radio_explosives", TileEntityRadioExplosives.class,
				ItemBlockRadioExplosives::new, IEProperties.FACING_ALL, IOBJModelCallback.PROPERTY, IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.CONNECTIONS);
	}

	@Override
	protected TileEntity getMineTileEntity()
	{
		return new TileEntityRadioExplosives();
	}

	public static class ItemBlockRadioExplosives extends ItemBlockMineBase
	{
		public ItemBlockRadioExplosives(BlockIIMine b)
		{
			super(b);
		}

		@Override
		public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag tooltipFlag)
		{
			super.addInformation(stack, world, tooltip, tooltipFlag);
			if(ItemNBTHelper.hasKey(stack, "programmed_data"))
				tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"explosives_programmed"));
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
			return 1f;
		}

		@Override
		public float getCaliber()
		{
			return 10f;
		}

		@SideOnly(Side.CLIENT)
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
			return IIContent.itemAmmoCasing.getStack(Casings.RADIO_EXPLOSIVES, amount);
		}
	}
}
