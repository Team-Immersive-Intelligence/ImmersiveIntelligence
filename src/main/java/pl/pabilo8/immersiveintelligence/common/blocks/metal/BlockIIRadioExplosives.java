package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTellermine;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 05.02.2021
 */
public class BlockIIRadioExplosives extends BlockIIMine
{
	public BlockIIRadioExplosives()
	{
		super("radio_explosives", ItemBlockRadioExplosives.class, IEProperties.FACING_ALL, IOBJModelCallback.PROPERTY, IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1]);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState ext = (IExtendedBlockState)state;
			TileEntity te = world.getTileEntity(pos);
			if(!(te instanceof TileEntityImmersiveConnectable))
				return state;
			state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityImmersiveConnectable)te).genConnBlockstate());
		}
		return state;
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
			return Utils.getStackWithMetaName(IIContent.itemAmmoCasing,"radio_explosives",amount);
		}
	}
}
