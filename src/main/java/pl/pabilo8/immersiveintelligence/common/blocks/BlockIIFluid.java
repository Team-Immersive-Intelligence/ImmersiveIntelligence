package pl.pabilo8.immersiveintelligence.common.blocks;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect_Potion;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
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
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * @author Pabilo8
 * @since 10-07-2019.
 * @author Kingcavespider1
 * @since 07-05-2020
 */
public class BlockIIFluid extends BlockFluidClassic
{
	private int flammability = 0;
	private int fireSpread = 0;
	public boolean isAcid = false;
	private PotionEffect[] potionEffects;

	public BlockIIFluid(String name, Fluid fluid, Material material)
	{
		super(fluid, material);
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(ImmersiveIntelligence.creativeTab);
		IIContent.BLOCKS.add(this);
		isAcid = name.endsWith("acid");
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
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
	{
		if(potionEffects!=null&&entity instanceof EntityLivingBase)
		{
			for(PotionEffect effect : potionEffects)
				if(effect!=null)
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(effect));

			if(isAcid)
			{
				entity.attackEntityFrom(IEDamageSources.acid, 2);
			}
		}


	}

	public BlockIIFluid addToChemthrower()
	{
		if(potionEffects!=null)
			ChemthrowerHandler.registerEffect(this.definedFluid,
					new ChemthrowerEffect_Potion(null, 0, potionEffects));
		if(flammability > 0)
			ChemthrowerHandler.registerFlammable(this.definedFluid);
		return this;
	}

}
