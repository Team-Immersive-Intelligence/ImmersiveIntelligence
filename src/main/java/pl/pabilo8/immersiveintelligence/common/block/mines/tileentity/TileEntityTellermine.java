package pl.pabilo8.immersiveintelligence.common.block.mines.tileentity;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIITrenchShovel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 06.02.2021
 */
public class TileEntityTellermine extends TileEntityIEBase implements IBlockBounds, IAdvancedCollisionBounds, ITileDrop, IPlayerInteraction
{
	public static final Material[] MATCHING_MATERIALS = new Material[]{Material.GROUND, Material.GRASS, Material.SAND, Material.GOURD};

	public int coreColor = 0xffffff;
	public ItemStack mineStack = ItemStack.EMPTY;
	private static final ArrayList<AxisAlignedBB> AABB = new ArrayList<>();
	public int digLevel = 0;

	static
	{
		AABB.add(new AxisAlignedBB(0.25f, 0, 0.25f, 0.75f, 0.125f, 0.75f));
	}

	private boolean armed = true;
	public boolean grass = false;

	@Override
	public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		digLevel = nbtTagCompound.getInteger("digLevel");
		armed = nbtTagCompound.getBoolean("armed");
		grass = nbtTagCompound.getBoolean("grass");
		this.readOnPlacement(null, new ItemStack(nbtTagCompound.getCompoundTag("mineStack")));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		nbtTagCompound.setInteger("digLevel", digLevel);
		nbtTagCompound.setBoolean("armed", armed);
		nbtTagCompound.setBoolean("grass", grass);
		nbtTagCompound.setTag("mineStack", mineStack.serializeNBT());
	}

	public void explode()
	{
		if(!armed)
			return;

		if(!world.isRemote&&mineStack.getItem() instanceof IAmmo)
		{
			EntityBullet bullet = AmmoUtils.createBullet(world, mineStack, new Vec3d(pos).addVector(0.5, 0.5, 0.5), new Vec3d(0, 0, 0), 1f);
			bullet.fuse = 1;
			world.spawnEntity(bullet);
		}
		world.setBlockToAir(this.getPos());
	}

	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return AABB;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0.125f, -0.0625f*digLevel, 0.125f,
				0.875f, 0.1875f-0.0625f*digLevel, 0.875f};
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(this.digLevel < 3&&heldItem.getItem().getToolClasses(heldItem).contains("shovel"))
		{
			heldItem.damageItem(1, player);
			Material material = world.getBlockState(pos.down()).getMaterial();
			if(Arrays.stream(MATCHING_MATERIALS).noneMatch(material1 -> material1==material))
				return true;
			digLevel += heldItem.getItem() instanceof ItemIITrenchShovel?3: 1;
			world.playSound(pos.getX(), pos.getY()+1, pos.getZ(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1f, 1f, false);

			return true;
		}
		else if(digLevel==3&&heldItem.getItem() instanceof ItemBlock&&((ItemBlock)heldItem.getItem()).getBlock()==Blocks.TALLGRASS)
		{
			grass = true;
			if(!player.isCreative())
				heldItem.shrink(1);
			world.playSound(pos.getX(), pos.getY()+1, pos.getZ(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1f, 1f, false);
			return true;
		}
		else if(armed&&heldItem.getItem().getToolClasses(heldItem).contains(Lib.TOOL_WIRECUTTER))
		{
			heldItem.damageItem(8, player);
			world.playSound(pos.getX(), pos.getY()+1, pos.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f, false);
			armed = false;
			grass = false;
		}
		return false;
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		super.onEntityCollision(world, entity);
		this.explode();
	}

	@Override
	public void readOnPlacement(EntityLivingBase placer, ItemStack stack)
	{
		Item item = stack.getItem();
		if(item instanceof IAmmo)
		{
			this.mineStack = stack;
			this.coreColor = ((IAmmo)item).getCore(stack).getColour();
		}
	}

	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		return mineStack;
	}

	@Override
	public NonNullList<ItemStack> getTileDrops(@Nullable EntityPlayer player, IBlockState state)
	{
		explode();
		return NonNullList.from(armed?ItemStack.EMPTY: mineStack);
	}
}
