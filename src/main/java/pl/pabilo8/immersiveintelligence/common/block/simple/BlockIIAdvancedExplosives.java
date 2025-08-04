package pl.pabilo8.immersiveintelligence.common.block.simple;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIAdvancedExplosives.HMX_Explosives;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.12.2021
 */
public class BlockIIAdvancedExplosives extends BlockIIBase<HMX_Explosives>
{
	public BlockIIAdvancedExplosives()
	{
		super("advanced_explosives", PropertyEnum.create("type", HMX_Explosives.class), Material.TNT, ItemBlockIIBase::new);
		this.setHardness(3.0F);
		this.setResistance(25F);
		setCategory(IICategory.WARFARE);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		//TODO: 05.01.2025 II lighter
		if(!world.isRemote)
		{
			ItemStack heldItem = player.getHeldItem(hand);
			if(heldItem.getItem()==Items.FLINT_AND_STEEL||heldItem.getItem()==Items.FIRE_CHARGE)
			{
				world.setBlockToAir(pos); //Remove the block
				explode(world, pos, player); //Trigger the explosion
				return true;
			}
		}
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		//If powered by redstone, ignite the dynamite
		if(world.isBlockPowered(pos))
		{
			explode(world, pos, null);
			world.setBlockToAir(pos);
		}
	}

	public void explode(World world, BlockPos pos, @Nullable EntityLivingBase igniter)
	{
		if(!world.isRemote)
		{
			IBlockState state = world.getBlockState(pos);
			HMX_Explosives type = state.getValue(this.property);
			AmmoComponent component;
			switch(type)
			{
				case RDX:
					component = IIContent.ammoComponentRDX;
					break;
				case HMX:
					component = IIContent.ammoComponentHMX;
					break;
				case WHITE_PHOSPHORUS:
					component = IIContent.ammoComponentWhitePhosphorus;
					break;
				default:
					return; //Nie wolno, nie można, nie potrzeba nam tego
			}
			component.onEffect(world, new Vec3d(pos), Vec3d.ZERO, ComponentEffectShape.ORB, new NBTTagCompound(), 1.0f, 1.0f, igniter);
		}
	}

	public enum HMX_Explosives implements IIBlockEnum
	{
		@IIBlockProperties(oreDict = {"explosiveRDX", "explosiveHexogen"}, needsCustomState = true)
		RDX,
		@IIBlockProperties(oreDict = {"explosiveHMX", "explosiveHexamine"}, needsCustomState = true)
		HMX,
		@IIBlockProperties(oreDict = {"explosiveWhitePhosphorus"}, needsCustomState = true)
		WHITE_PHOSPHORUS
	}
}


