package pl.pabilo8.immersiveintelligence.common.util.block;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect_Potion;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler.IAcidProtectionEquipment;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @author Kingcavespider1
 * @since 10-07-2019.
 * @since 07-05-2020
 */
public class BlockIIFluid extends BlockFluidClassic
{
	private int flammability = 0;
	private int fireSpread = 0;
	public boolean isAcid;
	private PotionEffect[] potionEffects;

	public BlockIIFluid(String name, Fluid fluid, Material material)
	{
		super(fluid, material);
		this.setUnlocalizedName(ImmersiveIntelligence.MODID+"."+name);
		this.setCreativeTab(IIContent.II_CREATIVE_TAB);
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
				for(ItemStack stack1 : entity.getArmorInventoryList())
				{
					if(!(stack1.getItem() instanceof IAcidProtectionEquipment)||
							!((IAcidProtectionEquipment)stack1.getItem()).protectsFromAcid(stack1))
					{
						entity.attackEntityFrom(IEDamageSources.acid, 2);
						break;
					}
				}
			}
		}

	}

	public void addToChemthrower()
	{
		if(potionEffects!=null)
			ChemthrowerHandler.registerEffect(this.definedFluid,
					isAcid?new ChemthrowerEffect_Acid(potionEffects): new ChemthrowerEffect_Potion(null, 0, potionEffects));
		if(flammability > 0)
			ChemthrowerHandler.registerFlammable(this.definedFluid);
	}

	public static class ChemthrowerEffect_Acid extends ChemthrowerEffect_Potion
	{
		public ChemthrowerEffect_Acid(PotionEffect... effects)
		{
			super(IEDamageSources.acid, 2, effects);
		}

		@Override
		public void applyToEntity(EntityLivingBase target, @Nullable EntityPlayer shooter, ItemStack thrower, Fluid fluid)
		{
			for(ItemStack stack1 : target.getArmorInventoryList())
			{
				if(!(stack1.getItem() instanceof IAcidProtectionEquipment)||
						!((IAcidProtectionEquipment)stack1.getItem()).protectsFromAcid(stack1))
				{
					super.applyToEntity(target, shooter, thrower, fluid);
					return;
				}
			}
		}
	}

}
