package pl.pabilo8.immersiveintelligence.common.block.mines.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mines;
import pl.pabilo8.immersiveintelligence.common.item.ItemIITripWireCoil;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIITrenchShovel;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IAdvancedBounds;

import java.util.*;

/**
 * Detonates a mine when an entity touches its tripwire.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 04.10.2026
 * @since 02.02.2021
 */
public class TileEntityTripMine extends TileEntityMineBase implements IAdvancedBounds, ITickable
{
	public static final Material[] MATCHING_MATERIALS = new Material[]{Material.GROUND, Material.GRASS, Material.SAND, Material.GOURD};

	private static final Vec3d CONN_OFFSET = new Vec3d(0.5, 0.25, 0.5);
	private static final AxisAlignedBB[] AABB = new AxisAlignedBB[16];

	static
	{
		for(int i = 0; i < 16; i++)
		{
			AABB[i] = new AxisAlignedBB(0.25f, -0.625f*(i/15f), 0.25f,
					0.75f, 0.625f*(1f-(i/15f)), 0.75f);
		}
	}

	public boolean grass = false;
	public int digLevel = 0;

	@Override
	public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		grass = nbtTagCompound.getBoolean("grass");
		digLevel = MathHelper.clamp(nbtTagCompound.getInteger("digLevel"), 0, 15);
		super.readCustomNBT(nbtTagCompound, b);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		nbtTagCompound.setBoolean("grass", grass);
		nbtTagCompound.setInteger("digLevel", digLevel);
		super.writeCustomNBT(nbtTagCompound, b);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return CONN_OFFSET;
	}

	@Override
	public boolean isRelay()
	{
		return true;
	}

	@Override
	public boolean acceptsWireType(WireType cableType)
	{
		return ItemIITripWireCoil.TRIPWIRE_CATEGORY.equals(cableType.getCategory());
	}

	/**
	 * Checks connected tripwires for entity contact on the server.
	 */
	@Override
	public void update()
	{
		if(world==null||world.isRemote||!armed||isInvalid())
			return;
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, pos);
		if(connections==null||connections.isEmpty())
			return;

		for(Connection connection : connections)
		{
			if(!acceptsWireType(connection.cableType)||!world.isBlockLoaded(connection.end))
				continue;
			Vec3d[] vertices = connection.getSubVertices(world);
			double radius = connection.cableType.getRenderDiameter()/2;
			AxisAlignedBB wireBounds = new AxisAlignedBB(vertices[0], vertices[0]);
			for(int i = 1; i < vertices.length; i++)
				wireBounds = wireBounds.union(new AxisAlignedBB(vertices[i-1], vertices[i]));
			wireBounds = wireBounds.grow(radius).offset(connection.start);

			for(Entity entity : world.getEntitiesWithinAABBExcludingEntity(null, wireBounds))
			{
				if(!canTrigger(entity))
					continue;
				AxisAlignedBB entityBounds = entity.getEntityBoundingBox().grow(radius)
						.offset(-connection.start.getX(), -connection.start.getY(), -connection.start.getZ());
				for(int i = 1; i < vertices.length; i++)
					if(entityBounds.contains(vertices[i-1])||entityBounds.contains(vertices[i])
							||entityBounds.calculateIntercept(vertices[i-1], vertices[i])!=null)
					{
						triggerConnection(connection);
						return;
					}
			}
		}
	}

	private boolean canTrigger(Entity entity)
	{
		Entity trigger = entity instanceof MultiPartEntityPart?(Entity)((MultiPartEntityPart)entity).parent: entity;
		if(!trigger.isEntityAlive()||trigger.noClip||trigger.doesEntityNotTriggerPressurePlate())
			return false;
		ResourceLocation key = EntityList.getKey(trigger);
		return key==null||!Arrays.asList(Mines.tripmineBlacklist).contains(key.toString());
	}

	private void triggerConnection(Connection connection)
	{
		TileEntity tileStart = world.getTileEntity(connection.start);
		TileEntity tileEnd = world.getTileEntity(connection.end);
		if(tileStart instanceof TileEntityTripMine)
			((TileEntityTripMine)tileStart).explode();
		if(tileEnd instanceof TileEntityTripMine)
			((TileEntityTripMine)tileEnd).explode();
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(super.interact(side, player, hand, heldItem, hitX, hitY, hitZ))
			return true;
		//Digging the mine in
		if(this.digLevel < 15&&heldItem.getItem().getToolClasses(heldItem).contains("shovel"))
		{
			if(world.isRemote)
				return true;
			heldItem.damageItem(1, player);
			Material material = world.getBlockState(pos.down()).getMaterial();
			if(Arrays.stream(MATCHING_MATERIALS).noneMatch(material1 -> material1==material))
				return true;
			digLevel = MathHelper.clamp(digLevel+(heldItem.getItem() instanceof ItemIITrenchShovel?5: 1), 0, 15);
			markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			world.playSound(pos.getX(), pos.getY()+1, pos.getZ(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1f, 1f, false);
			return true;
		}
		//Placing grass on top
		else if(digLevel==15&&heldItem.getItem() instanceof ItemBlock&&((ItemBlock)heldItem.getItem()).getBlock()==Blocks.TALLGRASS)
		{
			if(world.isRemote)
				return true;
			grass = true;
			markDirty();
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			if(!player.isCreative())
				heldItem.shrink(1);
			world.playSound(pos.getX(), pos.getY()+1, pos.getZ(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1f, 1f, false);
			return true;
		}
		return false;
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		if(collision)
			return new ArrayList<>(Collections.singleton(AABB[digLevel].offset(getPos())));
		return Collections.emptyList();
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0.25f, -0.625f*(digLevel/15f), 0.25f,
				0.75f, 0.625f*(1f-(digLevel/15f)), 0.75f};
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		super.onEntityCollision(world, entity);
		if(canTrigger(entity)&&(digLevel > 6||entity.posY > this.getPos().getY()))
			this.explode();
	}
}
