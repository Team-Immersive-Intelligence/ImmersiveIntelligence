package pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mortar;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls.MouseBinding;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.05.2026
 * @ii-approved 0.3.1
 * @since 21.01.2021
 */
public class EntityMortar extends EntityMountedWeapon implements IEntityZoomProvider
{
	private static final MortarSights SIGHTS = new MortarSights();
	private final AmmoFactory<EntityAmmoArtilleryProjectile> ammoFactory = new AmmoFactory<>(this);

	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public float shootingProgress = 0f;

	public EntityMortar(World world)
	{
		super(world);
		this.baseAabb = new AxisAlignedBB(-0.5, 0, -0.5, 0.5, 1.5, 0.5);
		this.maxSetupTime = Mortar.setupTime;

		this.aim.withYawLimit(-180f, 180f)
				.withPitchLimit(45f, 85f)
				.withCurrentAngles(0f, 85f);

		this.controls = new VehicleControls().withStates("fire", "pitchUp", "pitchDown");
		if(world.isRemote)
			this.controls
					.withMouseBinding(MouseBinding.MOUSE_RIGHT, "fire")
					.withKeyBinding(ClientUtils.mc().gameSettings.keyBindForward, "pitchUp")
					.withKeyBinding(ClientUtils.mc().gameSettings.keyBindBack, "pitchDown");

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
			else if(controls.getKey("pitchUp"))
				aim.setTarget(aim.getTargetYaw(), aim.getTargetPitch()+0.5f);
			else if(controls.getKey("pitchDown"))
				aim.setTarget(aim.getTargetYaw(), aim.getTargetPitch()-0.5f);
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
	public IAdvancedZoomTool getZoom()
	{
		return SIGHTS;
	}

	private static class MortarSights implements IAdvancedZoomTool
	{
		private static final ResourceLocation SIGHTS_TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/item/mortar.png");

		@Override
		@SideOnly(Side.CLIENT)
		public ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
		{
			return SIGHTS_TEXTURE;
		}

		@Override
		public boolean shouldZoom(ItemStack stack, EntityPlayer player)
		{
			Entity ridingEntity = player.getRidingEntity();
			return ridingEntity instanceof EntityMortar&&((EntityMortar)ridingEntity).shootingProgress==0;
		}

		@Override
		public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
		{
			Entity ridingEntity = player.getRidingEntity();
			if(ridingEntity instanceof EntityMortar)
				return new float[]{1f-Math.min(0.75f/(ridingEntity.rotationPitch/-90f), 0.975f)};
			return new float[]{0};
		}
	}
}
