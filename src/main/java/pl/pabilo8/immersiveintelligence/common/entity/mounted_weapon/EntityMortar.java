package pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ICameraEntity;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ZoomSettings;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoom;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mortar;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls.MouseBinding;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.05.2026
 * @ii-approved 0.3.1
 * @since 21.01.2021
 */
public class EntityMortar extends EntityMountedWeapon implements ICameraEntity
{
	private static final ZoomSettings SIGHTS = new ZoomSettings(new float[]{0.5f}, IIReference.RES_TEXTURES_GUI.with("item/mortar.png"));
	private final AmmoFactory<EntityAmmoArtilleryProjectile> ammoFactory = new AmmoFactory<>(this);

	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public float shootingProgress = 0f, rotateYawPrep = 0f;

	public EntityMortar(World world)
	{
		super(world);
		this.setSize(1f, 1.5f);
		this.maxSetupTime = Mortar.setupTime;

		this.aim.withYawLimit(-180f, 180f)
				.withPitchLimit(-89, -45)
				.withCurrentAngles(0f, 89f);

		this.controls = new VehicleControls().withStates("fire", "aim", "up", "down", "left", "right");
		if(world.isRemote)
			this.controls
					.withMouseBinding(MouseBinding.MOUSE_RIGHT, "fire")
					.withKeyBinding(ClientProxy.keybindZoom, "aim")
					.withKeyBinding(ClientUtils.mc().gameSettings.keyBindForward, "up")
					.withKeyBinding(ClientUtils.mc().gameSettings.keyBindBack, "down")
					.withKeyBinding(ClientUtils.mc().gameSettings.keyBindLeft, "left")
					.withKeyBinding(ClientUtils.mc().gameSettings.keyBindRight, "right");


		setOriginStack(new ItemStack(IIContent.itemMortar));
	}

	@Override
	@Nonnull
	protected Vec3d getPassengerPosition(Entity passenger)
	{
		return IIMath.offsetPosDirectionXZ(0.125f, -0.75f, this.rotationYaw, 0)
				.addVector(0, -0.5, 0);
	}

	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		float yy = this.rotationYaw;
		if(isSetupComplete())
		{
			float setupWindow = Math.max(1f, maxSetupTime*0.2f);
			yy += MathHelper.clamp((setupTime/setupWindow), 0, 1)*25;
		}
		if(shootingProgress > 0)
		{
			float ff = shootingProgress/Mortar.shootTime;
			if(ff < 0.3)
				yy += MathHelper.clamp(ff/0.1, 0, 1)*65;
			else if(ff < 0.4)
				yy += (1f-MathHelper.clamp((ff-0.3)/0.1, 0, 1))*65;
		}
		entityToUpdate.setRenderYawOffset(yy);

		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw-(this.rotationYaw));
		float f1 = MathHelper.clamp(f, -75.0F, 75.0F);
		entityToUpdate.prevRotationYaw += f1-f;
		entityToUpdate.rotationYaw += f1-f;

		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if(!isSetupComplete())
			return;

		float prevShootingProgress = shootingProgress;

		if(shootingProgress==0)
		{
			//Handle aiming
			if(controls.getKey("up"))
				aim.setTarget(aim.getTargetYaw(), aim.getTargetPitch()+0.5f);
			else if(controls.getKey("down"))
				aim.setTarget(aim.getTargetYaw(), aim.getTargetPitch()-0.5f);

			//Handle rotation
			if(controls.getKey("left")||controls.getKey("right"))
			{

			}
			else
				rotateYawPrep = Math.max(0, rotateYawPrep-1);

			//Handle firing
			if(controls.getKey("fire")&&!getPassengers().isEmpty())
			{
				Entity entity = getPassengers().get(0);
				if(entity instanceof EntityLivingBase)
				{
					ItemStack heldItem = ((EntityLivingBase)entity).getHeldItem(EnumHand.MAIN_HAND);
					if(heldItem.getItem()==IIContent.itemAmmoMortar)
					{
						shootingProgress = 1;
						controls.setKey("fire", false);
					}
				}
			}

		}
		else if(!getPassengers().isEmpty())
		{
			Entity entity = getPassengers().get(0);
			if(entity instanceof EntityLivingBase)
			{
				ItemStack heldItem = ((EntityLivingBase)entity).getHeldItem(EnumHand.MAIN_HAND);
				if(heldItem.getItem()==IIContent.itemAmmoMortar||shootingProgress > Mortar.shootTime*0.45)
				{
					if(shootingProgress < Mortar.shootTime)
						shootingProgress++;
					else
						shootingProgress = 0;
					if(shootingProgress==Math.round(Mortar.shootTime*0.2f))
						world.playSound(null, posX, posY, posZ, IISounds.mortarLoad, SoundCategory.PLAYERS, 1.25f, 1f);
					if(shootingProgress==Math.round(Mortar.shootTime*0.55f))
						if(!world.isRemote)
						{
							//Play firing sound
							world.playSound(null, posX, posY, posZ, IISounds.mortarShot, SoundCategory.PLAYERS, 1.25f, 1f);

							//Create the ammo piece
							ammoFactory.setPosition(getPositionVector())
									.setPositionAndVelocity(this.getPositionVector(), this.aim, 2f, 1f)
									.setStack(Utils.copyStackWithAmount(heldItem, 1))
									.setShooterAndGun(getPassengers().get(0), this)
									.create();
							heldItem.shrink(1);
						}
				}
				else
					shootingProgress = 0;
			}
		}
		else
			shootingProgress = 0;
		if(!world.isRemote&&shootingProgress!=prevShootingProgress)
			updateEntityForEvent(SyncEvents.ENTITY_CUSTOM1);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return amount > 4;
	}

	@Override
	public IAdvancedZoom getZoom()
	{
		return SIGHTS.withZoomEnabled(controls.getKey("aim"));
	}

	@Override
	public float getCameraPitch(EntityPlayer cameraPlayer, float partialTicks)
	{
		return aim.getPitch(partialTicks)*0.125f;
	}

	@Override
	public float getCameraYaw(EntityPlayer cameraPlayer, float partialTicks)
	{
		return aim.getYaw(partialTicks);
	}

	@Override
	public Vec3d getCameraPos(EntityPlayer cameraPlayer, float partialTicks)
	{
		return getPositionVector().addVector(0, 1, 0);
	}


	@Override
	public boolean isCameraEnabled(EntityPlayer player)
	{
		return isSetupComplete()&&controls.getKey("aim");
	}
}
