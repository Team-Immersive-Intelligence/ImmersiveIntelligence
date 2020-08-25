package pl.pabilo8.immersiveintelligence.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.api.utils.IEntityOverlayText;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 09.07.2020
 */
public class EntityVehicleMultiPart extends MultiPartEntityPart implements IEntityOverlayText
{
	IVehicleMultiPart parentExt;

	public EntityVehicleMultiPart(IVehicleMultiPart parent, String partName, float width, float height)
	{
		super(parent, partName, width, height);
		this.parentExt = parent;
	}

	@Override
	public void applyEntityCollision(Entity entityIn)
	{
		//disable collisions for entities of the same multipart
		if(!(entityIn instanceof EntityVehicleSeat)&&entityIn!=parentExt&&Arrays.stream(parentExt.getParts()).noneMatch(entity -> entity==entityIn))
			super.applyEntityCollision(entityIn);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return getEntityBoundingBox();
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		return parentExt.onInteractWithPart(this, player, hand);
	}

	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop, boolean hammer)
	{
		return parentExt.getOverlayTextOnPart(this, player, mop, hammer);
	}

	@Override
	public boolean useNixieFont(EntityPlayer player, RayTraceResult mop)
	{
		return false;
	}

	@Override
	public boolean canRenderOnFire()
	{
		//handled by parent
		return false;
	}
}
