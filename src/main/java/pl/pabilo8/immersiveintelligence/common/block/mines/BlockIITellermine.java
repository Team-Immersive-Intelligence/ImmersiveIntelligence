package pl.pabilo8.immersiveintelligence.common.block.mines;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTellermine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTellermine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05.02.2021
 */
public class BlockIITellermine extends BlockIIMine
{
	public BlockIITellermine()
	{
		super("tellermine", TileEntityTellermine.class, ItemBlockTellermine::new);
	}

	@Override
	protected TileEntity getMineTileEntity()
	{
		return new TileEntityTellermine();
	}

	public static class ItemBlockTellermine extends ItemBlockMineBase
	{
		public ItemBlockTellermine(BlockIIMine b)
		{
			super(b);
		}

		@Override
		public String getName()
		{
			return "tellermine";
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
			return IIContent.itemAmmoCasing.getStack(Casings.TELLERMINE, amount);
		}
	}
}
