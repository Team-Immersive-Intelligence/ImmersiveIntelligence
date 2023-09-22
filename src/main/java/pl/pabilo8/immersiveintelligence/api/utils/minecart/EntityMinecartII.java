package pl.pabilo8.immersiveintelligence.api.utils.minecart;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @since 07.11.2021
 */
public abstract class EntityMinecartII extends EntityMinecart implements IMinecartBlockPickable
{
	public EntityMinecartII(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartII(World worldIn, Vec3d vv)
	{
		super(worldIn, vv.x, vv.y, vv.z);
	}

	protected abstract Block getCarriedBlock();

	protected abstract int getBlockMetaID();

	abstract void writeNBTToStack(NBTTagCompound nbt);

	abstract void readFromStack(ItemStack stack);

	@Override
	public Type getType()
	{
		return Type.CHEST;
	}

	@Override
	public IBlockState getDefaultDisplayTile()
	{
		return getCarriedBlock().getStateFromMeta(getBlockMetaID());
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
	public void killMinecart(DamageSource source)
	{
		if(!world.isRemote&&this.world.getGameRules().getBoolean("doEntityDrops"))
		{
			ItemStack cart = new ItemStack(Items.MINECART, 1);
			ItemStack block = new ItemStack(getCarriedBlock(), 1, getBlockMetaID());
			NBTTagCompound nbt = new NBTTagCompound();

			if(this.hasCustomName())
				nbt.setString("name", getCustomNameTag());

			writeNBTToStack(nbt);
			block.setTagCompound(nbt);

			entityDropItem(block, 1);
			entityDropItem(cart, 1);
			this.setDead();
		}
	}

	@Override
	public Tuple<ItemStack, EntityMinecart> getBlockForPickup()
	{
		ItemStack stack = new ItemStack(getCarriedBlock(), 1, getBlockMetaID());
		NBTTagCompound nbt = new NBTTagCompound();

		writeNBTToStack(nbt);
		stack.setTagCompound(nbt);

		if(this.hasCustomName())
			nbt.setString("name", getCustomNameTag());

		NBTTagCompound nbt2 = new NBTTagCompound();
		this.writeEntityToNBT(nbt2);

		this.setDead();

		EntityMinecart ent = new EntityMinecartEmpty(this.world);
		ent.readFromNBT(nbt2);
		world.spawnEntity(ent);
		ent.readFromNBT(nbt2);

		return new Tuple<>(stack, ent);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		ItemStack stack = new ItemStack(getCarriedBlock(), 1, getBlockMetaID());
		NBTTagCompound nbt = new NBTTagCompound();
		writeNBTToStack(nbt);
		stack.setTagCompound(nbt);

		if(this.hasCustomName())
			nbt.setString("name", getCustomNameTag());

		return stack;

	}

	@Override
	public void setMinecartBlock(ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			assert stack.getTagCompound()!=null;
			readFromStack(stack);
		}
	}
}
