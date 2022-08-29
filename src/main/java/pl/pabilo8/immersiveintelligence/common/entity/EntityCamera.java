package pl.pabilo8.immersiveintelligence.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Pabilo8
 * @since 10-11-2019
 */
@SideOnly(Side.CLIENT)
public class EntityCamera extends EntityLivingBase
{
	public float rotationRoll = 0;

	public EntityCamera(World worldIn)
	{
		super(worldIn);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{

	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList()
	{
		return NonNullList.withSize(0, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
	{

	}

	@Override
	public EnumHandSide getPrimaryHand()
	{
		return null;
	}
}
