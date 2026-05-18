package pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ZoomSettings;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.TripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.TripodPeriscope.tripodZoomSteps;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.05.2026
 * @ii-approved 0.3.1
 * @since 21.01.2021
 */
public class EntityTripodPeriscope extends EntityMountedWeapon implements IEntityZoomProvider
{
	private static final ZoomSettings ZOOM = new ZoomSettings(tripodZoomSteps, IIReference.RES_TEXTURES_GUI.with("item/binoculars.png"))
			.withZoomEnabled(true);

	public EntityTripodPeriscope(World world)
	{
		super(world);
		this.baseAabb = new AxisAlignedBB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);
		this.aim.withYawLimit(-180f, 180f)
				.withPitchLimit(0f, 0f)
				.withAimSpeed(TripodPeriscope.turnSpeed, 1f);

		this.maxSetupTime = TripodPeriscope.setupTime;
		this.setOriginStack(new ItemStack(IIContent.itemTripodPeriscope));
	}

	@Override
	@Nonnull
	protected Vec3d getPassengerPosition(Entity passenger)
	{
		return IIMath.offsetPosDirectionXZ(-0.5, -0.0625f/2f, this.aim.getYaw(0), 0);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if(isSetupComplete()&&!getPassengers().isEmpty())
		{
			Entity user = getPassengers().get(0);
			aim.setTarget(user.getRotationYawHead(), 0);
		}
	}

	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		entityToUpdate.setRenderYawOffset(this.aim.getCurrentYaw());
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	public IAdvancedZoomTool getZoom()
	{
		return ZOOM;
	}
}
