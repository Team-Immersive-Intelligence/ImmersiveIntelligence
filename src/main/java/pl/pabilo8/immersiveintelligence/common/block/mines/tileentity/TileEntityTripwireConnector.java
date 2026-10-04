package pl.pabilo8.immersiveintelligence.common.block.mines.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.item.ItemIITripWireCoil;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIConnectable;

import javax.annotation.Nullable;

/**
 * Provides an anchor for mechanical tripwire connections.
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 02.02.2021
 * @updated 04.10.2026
 */
public class TileEntityTripwireConnector extends TileEntityIIConnectable implements IPlayerInteraction, IBlockBounds
{
	private static final Vec3d CONN = new Vec3d(0.5, 0.25, 0.5);
	public boolean grass = false;

	@Override
	public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		if(!nbtTagCompound.hasKey("limit_type")&&nbtTagCompound.hasKey("limitType"))
			nbtTagCompound.setString("limit_type", nbtTagCompound.getString("limitType"));
		super.readCustomNBT(nbtTagCompound, b);
		grass = nbtTagCompound.getBoolean("grass");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b)
	{
		super.writeCustomNBT(nbtTagCompound, b);
		nbtTagCompound.setBoolean("grass", grass);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return CONN;
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

	@Override
	public boolean allowEnergyToPass(@Nullable Connection connection)
	{
		return false;
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0.4375f, 0, 0.4375f,
				0.5625f, 0.3125f, 0.5625f};
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		super.onEntityCollision(world, entity);
		// TODO: 09.12.2021 send to all around (?)
	}
}
