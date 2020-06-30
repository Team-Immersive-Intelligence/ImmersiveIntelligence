package pl.pabilo8.immersiveintelligence.common.entity.minecarts;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.EntityMinecartFluidContainer;
import pl.pabilo8.immersiveintelligence.api.utils.IMinecartBlockPickable;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class EntityMinecartBarrelWooden extends EntityMinecartFluidContainer implements IMinecartBlockPickable
{
	public EntityMinecartBarrelWooden(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartBarrelWooden(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	@Override
	public boolean isGasAllowed()
	{
		return false;
	}

	@Override
	public int getMaxTemperature()
	{
		return TileEntityWoodenBarrel.IGNITION_TEMPERATURE;
	}

	@Override
	public int getTankCapacity()
	{
		return 12000;
	}

	@Override
	public void killMinecart(DamageSource source)
	{
		if(!world.isRemote&&this.world.getGameRules().getBoolean("doEntityDrops"))
		{
			ItemStack cart = new ItemStack(Items.MINECART, 1);
			ItemStack drop2 = new ItemStack(IEContent.blockWoodenDevice0, 1, BlockTypes_WoodenDevice0.BARREL.getMeta());
			NBTTagCompound nbt = new NBTTagCompound();


			if(this.hasCustomName())
			{
				nbt.setString("name", getCustomNameTag());
			}
			writeTank(nbt, true);
			drop2.setTagCompound(nbt);

			entityDropItem(drop2, 1);
			entityDropItem(cart, 1);
			this.setDead();
		}
	}

	@Override
	public Type getType()
	{
		return Type.CHEST;
	}

	@Override
	public IBlockState getDefaultDisplayTile()
	{
		return IEContent.blockWoodenDevice0.getStateFromMeta(BlockTypes_WoodenDevice0.BARREL.getMeta());
	}

	@Override
	public int getDefaultDisplayTileOffset()
	{
		return 8;
	}

	@Override
	public void setDead()
	{
		this.isDead = true;
	}

	@Override
	public Tuple<ItemStack, EntityMinecart> getBlockForPickup()
	{
		Item drop = Item.getItemFromBlock(IEContent.blockWoodenDevice0);
		ItemStack drop2 = new ItemStack(drop, 1, BlockTypes_WoodenDevice0.BARREL.getMeta());
		NBTTagCompound nbt = new NBTTagCompound();

		writeTank(nbt, true);

		drop2.setTagCompound(nbt);

		NBTTagCompound nbt2 = new NBTTagCompound();
		this.writeEntityToNBT(nbt2);

		this.setDead();

		EntityMinecart ent = new EntityMinecartEmpty(this.world);
		ent.readFromNBT(nbt2);
		world.spawnEntity(ent);
		ent.readFromNBT(nbt2);

		return new Tuple<>(drop2, ent);
	}

	@Override
	public void setMinecartBlock(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemBlock&&((ItemBlock)stack.getItem()).getBlock()==IEContent.blockMetalDevice0)
		{
			if(stack.hasTagCompound())
			{
				readTank(stack.getTagCompound());
				updateTank(false);
			}
		}
	}
}
