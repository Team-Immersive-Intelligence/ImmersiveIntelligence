package pl.pabilo8.immersiveintelligence.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * Created by Pabilo8 on 10-07-2019.
 */
public class BlockIIFluid extends BlockFluidClassic
{
	private int flammability = 0;
	private int fireSpread = 0;
	private PotionEffect[] potionEffects;

	public BlockIIFluid(String name, Fluid fluid, Material material)
	{
		super(fluid, material);
		this.setTranslationKey(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(ImmersiveIntelligence.creativeTab);
		ImmersiveIntelligence.proxy.blocks.add(this);
	}

	public BlockIIFluid setFlammability(int flammability, int fireSpread)
	{
		this.flammability = flammability;
		this.fireSpread = fireSpread;
		return this;
	}

	public BlockIIFluid setPotionEffects(PotionEffect... potionEffects)
	{
		this.potionEffects = potionEffects;
		return this;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return this.flammability;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return fireSpread;
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return this.flammability > 0;
	}


	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if(potionEffects!=null&&entity instanceof EntityLivingBase)
		{
			for(PotionEffect effect : potionEffects)
				if(effect!=null)
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(effect));
		}
	}

}
