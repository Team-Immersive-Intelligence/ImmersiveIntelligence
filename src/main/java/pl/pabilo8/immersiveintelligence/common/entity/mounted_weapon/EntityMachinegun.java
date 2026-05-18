package pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.MachinegunCoolantHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ZoomSettings;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessagePlayerAimAnimationSync;
import pl.pabilo8.immersiveintelligence.common.util.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunRecoil;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunShootingHandler;

import javax.annotation.Nonnull;
import java.util.EnumSet;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.05.2026
 * @ii-approved 0.3.1
 * @since 01.11.2019
 */
public class EntityMachinegun extends EntityMountedWeapon implements IAdvancedTextOverlay, IEntitySpecialRepairable, IEntityZoomProvider
{
	private final static ZoomSettings SCOPE = new ZoomSettings(Machinegun.machinegunScopeMaxZoom,
			IIReference.RES_TEXTURES_GUI.with("item/machinegun/scope.png"));
	private final static ZoomSettings SCOPE_IR = new ZoomSettings(Machinegun.machinegunScopeMaxZoom,
			IIReference.RES_TEXTURES_GUI.with("item/machinegun/scope_ir.png"));
	private final AmmoFactory<EntityAmmoProjectile> ammoFactory = new AmmoFactory<>(this);
	public EnumSet<WeaponUpgrade> upgrades = EnumSet.noneOf(WeaponUpgrade.class);

	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public GunRecoil recoil = new GunRecoil();
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public GunShootingHandler shooting = new GunShootingHandler();

	//Second magazine is an upgrade
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public ItemStack magazine1 = ItemStack.EMPTY, magazine2 = ItemStack.EMPTY;

	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public float setYaw = 0;
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public VehicleDurability shield = new VehicleDurability(Machinegun.shieldStrengthInitial, 2);

	@SyncNBT(events = SyncEvents.ENTITY_INTERACT)
	public FilteredFluidTank tank = new FilteredFluidTank(0)
			.withInputFilter(MachinegunCoolantHandler::isValidCoolant);

	private boolean lastAimState = false;

	public EntityMachinegun(World world)
	{
		super(world);
		this.baseAabb = new AxisAlignedBB(-0.35, 0, -0.35, 0.35, 0.65, 0.35);
		this.controls = new VehicleControls().withStates("fire", "reload", "aim");
		if(world.isRemote)
			this.controls
					.withKeyBinding(ClientProxy.keybindManualReload, "reload")
					.withKeyBinding(ClientProxy.keybindZoom, "aim");

		setOriginStack(new ItemStack(IIContent.itemMachinegun));
	}

	public EntityMachinegun(World world, BlockPos pos, float yaw, ItemStack stack)
	{
		this(world);
		this.setPosition(pos.getX(), pos.getY(), pos.getZ());
		this.setYaw = yaw;
		setOriginStack(stack);
	}

	@Override
	protected void setOriginStack(ItemStack stack)
	{
		super.setOriginStack(stack);

		//Set defaults
		this.upgrades = EnumSet.noneOf(WeaponUpgrade.class);
		this.maxSetupTime = Machinegun.setupTime;
		this.aim.withAimCorrectionFunction(this.ammoFactory::getAnglePrediction)
				.withPitchLimit(-25.0f, 25f)
				.withYawLimit(-55.0f, 55f)
				.withAimSpeed(2f, 1f);
		this.recoil.withRecoilLimits(22.5f, 22.5f)
				.withRecoilStrength(Machinegun.recoilVertical, Machinegun.recoilHorizontal, 0.5f, 0.05f)
				.withOverheating(Machinegun.maxOverheat, 0.75f, () -> tank);

		//Set upgrade parameters
		assert stack.getItem()==IIContent.itemMachinegun;
		this.upgrades = IIContent.itemMachinegun.listUpgrades(stack, WeaponUpgrade.class);
		for(WeaponUpgrade upgrade : upgrades)
			switch(upgrade)
			{
				case HEAVY_BARREL:
					this.shooting.withMaxShotDelay(Machinegun.heavyBarrelFireDelay);
					break;
				case WATER_COOLING:
					//TODO: 17.05.2026
					this.shooting.withMaxShotDelay(Machinegun.heavyBarrelFireDelay);
					break;
				case SECOND_MAGAZINE:
					break;
				case HASTY_BIPOD:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.hastyBipodSetupTimeMultiplier);
					break;
				case PRECISE_BIPOD:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.preciseBipodSetupTimeMultiplier);
					break;
				case SCOPE:
					break;
				case INFRARED_SCOPE:
					break;
				case SHIELD:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.shieldSetupTimeMultiplier);
					break;
				case BELT_FED_LOADER:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.beltFedLoaderSetupTimeMultiplier);
					break;
				case TRIPOD:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.tripodSetupTimeMultiplier);
					this.aim.withYawLimit(-82.5f, 82.5f);
					break;
				default:
					break;
			}
	}

	@Override
	@Nonnull
	protected Vec3d getPassengerPosition(Entity passenger)
	{
		return IIMath.offsetPosDirectionXZ(-1.65f, -0.25f, rotationYaw, rotationPitch)
				.addVector(0.5, -1.15, 0.5);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		//Update components
		recoil.update();
		shooting.update();

		//Apply controls
		if(!world.isRemote&&isBeingRidden())
		{
			Entity operator = getPassengers().get(0);
			boolean currentAim = isAiming();
			if(currentAim!=lastAimState)
			{
				IIPacketHandler.INSTANCE.sendToDimension(new MessagePlayerAimAnimationSync(operator, currentAim),
						this.world.provider.getDimension());
				lastAimState = currentAim;
			}
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		//Try filling coolant tank
		if(FluidUtil.interactWithFluidHandler(player, hand, tank))
		{
			if(!world.isRemote)
				updateEntityForEvent(SyncEvents.ENTITY_INTERACT);
			return true;
		}
		return super.processInitialInteract(player, hand);
	}

	@Override
	protected void removePassenger(Entity passenger)
	{
		if(controls!=null)
		{
			controls.setKey("fire", false);
			controls.setKey("aim", false);
		}
		lastAimState = false;
		if(!world.isRemote)
			IIPacketHandler.INSTANCE.sendToDimension(new MessagePlayerAimAnimationSync(passenger, false), this.world.provider.getDimension());
		super.removePassenger(passenger);
	}


	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		entityToUpdate.prevRotationYaw = aim.clampYawToRange(entityToUpdate.prevRotationYaw);
		entityToUpdate.rotationYaw = aim.clampYawToRange(entityToUpdate.rotationYaw);
		entityToUpdate.prevRotationPitch = aim.clampPitchToRange(entityToUpdate.prevRotationYaw);
		entityToUpdate.rotationPitch = aim.clampPitchToRange(entityToUpdate.rotationPitch);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(!isDead&&source.isProjectile())
		{
			Vec3d attackVector = source.getDamageLocation();
			if(attackVector!=null)
			{
				attackVector = attackVector.subtract(posX, posY, posZ);
				if(getHorizontalFacing()==EnumFacing.getFacingFromVector((float)attackVector.x, (float)attackVector.y, (float)attackVector.z))
				{
					shield.attackFrom(source, amount);
					return shield.isDead();
				}
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	//--- Getters ---//

	public float getGunYaw(float partialTicks)
	{
		return aim.getYaw(partialTicks);
	}

	public float getGunPitch(float partialTicks)
	{
		return aim.getPitch(partialTicks);
	}

	private boolean isFiring()
	{
		return controls.getKey("fire");
	}

	private boolean isAiming()
	{
		return controls.getKey("aim");
	}

	//--- IEntitySpecialRepairable ---//

	@Override
	public boolean canRepair()
	{
		return shield.canRepair();
	}

	@Override
	public boolean repair(int repairPoints)
	{
		return shield.repair(repairPoints);
	}

	@Override
	public int getRepairCost()
	{
		return 1;
	}

	//--- IEntityZoomProvider ---//

	@Override
	public IAdvancedZoomTool getZoom()
	{
		//TODO: 15.05.2026 check for scope
		return null;
	}

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		return new String[0];
	}
}
