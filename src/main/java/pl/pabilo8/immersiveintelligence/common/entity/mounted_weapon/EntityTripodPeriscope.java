package pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ICameraEntity;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ZoomSettings;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoom;
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
public class EntityTripodPeriscope extends EntityMountedWeapon implements ICameraEntity
{
	private static final ZoomSettings ZOOM = new ZoomSettings(tripodZoomSteps, IIReference.RES_TEXTURES_GUI.with("item/binoculars.png"))
			.withZoomEnabled(true);

	public EntityTripodPeriscope(World world)
	{
		super(world);
		this.setSize(0.77f, 2.4375f);
		this.aim.withYawLimit(-180.0f, 180f)
				.withPitchLimit(22.5f, -22.5f)
				.withAimSpeed(TripodPeriscope.turnSpeed, 1f);

		this.maxSetupTime = TripodPeriscope.setupTime;
		this.setOriginStack(new ItemStack(IIContent.itemTripodPeriscope));
	}

	@Override
	@Nonnull
	protected Vec3d getPassengerPosition(Entity passenger)
	{
		return IIMath.offsetPosDirectionXZ(-0.5, -0.03125f, this.aim.getYaw(0), 0);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if(isSetupComplete()&&!getPassengers().isEmpty())
		{
			Entity user = getPassengers().get(0);
			aim.setTarget(user.getRotationYawHead(), user.rotationPitch);
		}
	}

	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		entityToUpdate.setRenderYawOffset(this.aim.getYaw(0));
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	//--- ICameraEntity ---//

	@Override
	public boolean isCameraEnabled(EntityPlayer player)
	{
		return true;
	}

	@Override
	public float getCameraPitch(EntityPlayer cameraPlayer, float partialTicks)
	{
		return aim.getPitch(partialTicks);
	}

	@Override
	public float getCameraYaw(EntityPlayer cameraPlayer, float partialTicks)
	{
		return aim.getYaw(partialTicks);
	}

	@Override
	public Vec3d getCameraPos(EntityPlayer cameraPlayer, float partialTicks)
	{
		float cameraYaw = getCameraYaw(cameraPlayer, partialTicks);
		float cameraPitch = getCameraPitch(cameraPlayer, partialTicks);
		return getPositionVector()
				.add(IIMath.offsetPosDirectionXYZ(new Vec3d(0, 2.5f, -0.5f), cameraYaw, cameraPitch, 0));
	}

	@Override
	public IAdvancedZoom getZoom()
	{
		return ZOOM;
	}
}
