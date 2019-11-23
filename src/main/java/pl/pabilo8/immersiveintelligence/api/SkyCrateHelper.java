package pl.pabilo8.immersiveintelligence.api;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;

/**
 * Created by Pabilo8 on 07-06-2019.
 */
public class SkyCrateHelper
{
	public static void spawnSkyCrateTest(EntityLivingBase player, TileEntity start, Connection connection, EnumHand hand)
	{

		if(!player.world.isRemote)
		{
			BlockPos cc0 = connection.end==Utils.toCC(start)?connection.start: connection.end;
			BlockPos cc1 = connection.end==Utils.toCC(start)?connection.end: connection.start;
			IImmersiveConnectable iicStart = ApiUtils.toIIC(cc1, player.world);
			IImmersiveConnectable iicEnd = ApiUtils.toIIC(cc0, player.world);
			Vec3d vStart = new Vec3d(cc1);
			Vec3d vEnd = new Vec3d(cc0);

			if(iicStart!=null)
				vStart = Utils.addVectors(vStart, iicStart.getConnectionOffset(connection));
			if(iicEnd!=null)
				vEnd = Utils.addVectors(vEnd, iicEnd.getConnectionOffset(connection));

			Vec3d pos = player.getPositionEyes(0);
			Vec3d across = new Vec3d(vEnd.x-vStart.x, vEnd.y-vStart.y, vEnd.z-vStart.z);
			double linePos = Utils.getCoeffForMinDistance(pos, vStart, across);
			connection.getSubVertices(player.world);

			Vec3d playerMovement = new Vec3d(player.motionX, player.motionY,
					player.motionZ);
			double slopeAtPos = connection.getSlopeAt(linePos);
			Vec3d extendedWire;
			if(connection.vertical)
				extendedWire = new Vec3d(0, connection.horizontalLength, 0);
			else
				extendedWire = new Vec3d(connection.across.x, slopeAtPos*connection.horizontalLength, connection.across.z);
			extendedWire = extendedWire.normalize();

			double totalSpeed = playerMovement.dotProduct(extendedWire);
			double horSpeed = totalSpeed/Math.sqrt(1+slopeAtPos*slopeAtPos);

			EntitySkyCrate hook = new EntitySkyCrate(player.world, connection, linePos, IEContent.blockWoodenDevice0.getStateFromMeta(BlockTypes_WoodenDevice0.CRATE.getMeta()));

			ImmersiveIntelligence.logger.info("Speed keeping: Player {}, wire {}, Pos: {}", playerMovement, extendedWire,
					hook.getPositionVector());
			if(hook.isValidPosition(hook.posX, hook.posY, hook.posZ, hook.getControllingPassenger()))
			{
				player.world.spawnEntity(hook);
			}
		}
	}

	public void spawnSkycrate(World world, IImmersiveConnectable start, Connection connection, EnumHand hand, IBlockState block)
	{
		EntitySkyCrate hook = new EntitySkyCrate(world, connection, 0f, block);
		world.spawnEntity(hook);
	}
}
