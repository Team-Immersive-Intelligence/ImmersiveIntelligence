package pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.MachinegunCoolantHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ICameraEntity;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ZoomSettings;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoom;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls.MouseBinding;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunRecoil;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunShootingHandler;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderAmmoCrate;
import pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider.GunAmmoProviderMagazine;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.AxisAlignedFacingBB;

import javax.annotation.Nonnull;
import java.util.EnumSet;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 15.05.2026
 * @ii-approved 0.3.1
 * @since 01.11.2019
 */
public class EntityMachinegun extends EntityMountedWeapon implements IAdvancedTextOverlay, IEntitySpecialRepairable, ICameraEntity
{
	private final static ZoomSettings SCOPE = new ZoomSettings(Machinegun.machinegunScopeZoom,
			IIReference.RES_TEXTURES_GUI.with("item/machinegun/scope.png"));
	private final static ZoomSettings SCOPE_IR = new ZoomSettings(Machinegun.machinegunScopeZoom,
			IIReference.RES_TEXTURES_GUI.with("item/machinegun/scope_infrared.png"));
	private final static AxisAlignedFacingBB SANDBAG_AABB = new AxisAlignedFacingBB(new AxisAlignedBB(0, 0, 0, 1.0, 1.0, 0.5));

	private final AmmoFactory<EntityAmmoProjectile> ammoFactory = new AmmoFactory<>(this);
	public EnumSet<WeaponUpgrade> upgrades = EnumSet.noneOf(WeaponUpgrade.class);

	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public GunShootingHandler gunHandler = new GunShootingHandler();
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public GunRecoil recoil = new GunRecoil();

	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public GunAmmoProviderMagazine loadingMagazine1 = new GunAmmoProviderMagazine(this, this::getUser,
			Magazines.MACHINEGUN, Machinegun.clipReloadTime);

	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public GunAmmoProviderMagazine loadingMagazine2 = new GunAmmoProviderMagazine(this, this::getUser,
			Magazines.MACHINEGUN, Machinegun.clipReloadTime);

	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public GunAmmoProviderAmmoCrate loadingCrate = new GunAmmoProviderAmmoCrate(this, this::getUser);

	@SyncNBT(events = SyncEvents.ENTITY_DAMAGED)
	public SyncedDurability shield = new SyncedDurability(Machinegun.shieldStrengthInitial, 2);

	@SyncNBT(events = SyncEvents.ENTITY_INTERACT)
	public FilteredFluidTank tank = new FilteredFluidTank(0)
			.withInputFilter(MachinegunCoolantHandler::isValidCoolant);
	public FluxStorage infraredFluxStorage = new FluxStorage(0, 16000);

	public EntityMachinegun(World world)
	{
		super(world);
		this.controls = new VehicleControls().withStates("fire", "reload", "aim");
		if(world.isRemote)
			this.controls
					.withMouseBinding(MouseBinding.MOUSE_RIGHT, "fire")
					.withKeyBinding(ClientProxy.keybindManualReload, "reload")
					.withKeyBinding(ClientProxy.keybindZoom, "aim");

		setOriginStack(new ItemStack(IIContent.itemMachinegun));
	}

	public EntityMachinegun(World world, BlockPos pos, float yaw, ItemStack stack)
	{
		this(world);
		this.setPosition(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
		this.aim.withCenterYaw(yaw).withCurrentAngles(yaw, 0);
		setOriginStack(stack);
		if(upgrades.contains(WeaponUpgrade.TRIPOD))
			this.posY += 0.385f;
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
				.withAimSpeed(3.5f, 3f);
		this.recoil.withRecoilLimits(22.5f, 22.5f)
				.withRecoilStrength(Machinegun.recoilVertical, Machinegun.recoilHorizontal, 0.5f, 0.05f)
				.withOverheating(Machinegun.maxOverheat, 0.75f)
				.withCoolantTank(() -> tank, Integer.MAX_VALUE);
		this.gunHandler.withMaxShotDelay(Machinegun.fireDelay)
				.withRecoilHandler(recoil)
				.withAmmoFactory(ammoFactory)
				.withAmmoProvider(this.loadingMagazine1)
				.withShootSound(IISounds.machinegunShot, 70)
				.withDryFireSound(IISounds.machinegunShotDry);
		this.loadingMagazine1.withSounds(IISounds.machinegunReload, IISounds.machinegunUnload);
		this.loadingMagazine2.withSounds(IISounds.machinegunReload, IISounds.machinegunUnload);
		this.loadingCrate.withSounds(IISounds.machinegunReload, IISounds.machinegunUnload);
		this.infraredFluxStorage.setCapacity(0);
		this.setSize(0.77f, 0.65f);

		this.loadingMagazine1.setLoadedStack(ItemNBTHelper.getItemStack(stack, "magazine1"));

		//Set upgrade parameters
		assert stack.getItem()==IIContent.itemMachinegun;
		this.upgrades = IIContent.itemMachinegun.listUpgrades(stack, WeaponUpgrade.class);
		for(WeaponUpgrade upgrade : upgrades)
			switch(upgrade)
			{
				case HEAVY_BARREL:
					this.gunHandler.withMaxShotDelay(Machinegun.heavyBarrelFireDelay)
							.withShootSound(IISounds.machinegunShotHeavyBarrel, 70);
					this.recoil.withRecoilStrength(Machinegun.recoilHBVertical, Machinegun.recoilHBHorizontal, 0.5f, 0.05f);
					break;
				case WATER_COOLING:
					this.gunHandler.withShootSound(IISounds.machinegunShotWaterCooled, 70);
					this.recoil.withCoolantTank(() -> tank, Machinegun.waterCoolingFluidUsage);
					break;
				case SECOND_MAGAZINE:
					this.gunHandler.withAmmoProvider(loadingMagazine2);
					this.loadingMagazine2.setLoadedStack(ItemNBTHelper.getItemStack(stack, "magazine2"));
					break;
				case HASTY_BIPOD:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.hastyBipodSetupTimeMultiplier);
					break;
				case PRECISE_BIPOD:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.preciseBipodSetupTimeMultiplier);
					break;
				case SHIELD:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.shieldSetupTimeMultiplier);
					break;
				case BELT_FED_LOADER:
					this.gunHandler.withAmmoProvider(loadingCrate);
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.beltFedLoaderSetupTimeMultiplier);
					break;
				case TRIPOD:
					this.maxSetupTime = (int)(this.maxSetupTime*Machinegun.tripodSetupTimeMultiplier);
					this.aim.withYawLimit(-180, 180f);
					this.setSize(0.77f, 1.65f);
					break;
				case SCOPE:
					break;
				case INFRARED_SCOPE:
					this.infraredFluxStorage.setCapacity(16000);
					break;
				default:
					break;
			}
	}

	@Override
	public ItemStack getOriginStack()
	{
		ItemStack stack = super.getOriginStack();
		EasyNBT.wrapNBT(stack)
				.withItemStack("magazine1", loadingMagazine1.getLoadedStack())
				.withItemStack("magazine2", loadingMagazine2.getLoadedStack());
		return stack;
	}

	@Override
	@Nonnull
	protected Vec3d getPassengerPosition(Entity passenger)
	{
		return IIMath.offsetPosDirectionXZ(-0.65f-1f, 0f, aim.getYaw(0), 0)
				.addVector(0, -1.15+(upgrades.contains(WeaponUpgrade.TRIPOD)?1f: 0f), 0);
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		//Update components
		this.gunHandler.update();

		//Apply controls
		if(isBeingRidden())
		{
			Entity user = getPassengers().get(0);
			//Aim to where the user is pointing
			this.aim.setTargetClamped(user.getRotationYawHead(), user.rotationPitch);
			//Set shooter and gun info
			this.ammoFactory.setShooterAndGun(user, this)
					.setPositionAndVelocity(this.getPositionVector().addVector(0, upgrades.contains(WeaponUpgrade.TRIPOD)?1.25f: 0.5f, 0), this.aim, 0.25f, 1f);

			//Drain energy from user
			for(ItemStack equipmentStack : user.getEquipmentAndArmor())
				if(equipmentStack.hasCapability(CapabilityEnergy.ENERGY, null))
				{
					IEnergyStorage userStorage = equipmentStack.getCapability(CapabilityEnergy.ENERGY, null);
					assert userStorage!=null;
					int received = infraredFluxStorage.receiveEnergy(userStorage.extractEnergy(Machinegun.infraredScopeEnergyUsage*20, true), false);
					userStorage.extractEnergy(received, false);
				}

			//Apply infrared effect
			if(controls.getKey("aim")&&infraredFluxStorage.extractEnergy(Machinegun.infraredScopeEnergyUsage, false)==Machinegun.infraredScopeEnergyUsage)
				IIUtils.applyInfraredVision(user, 10);

			//Check for buttons pressed
			if(controls.getKey("reload"))
				this.gunHandler.startReloading();
			else if(controls.getKey("fire"))
				this.gunHandler.fire();
		}
		else
		{
			this.ammoFactory.setOwner(null);
			this.aim.setTarget(aim.getTargetYaw(), aim.clampPitchToRange(-10f));
			this.aim.update();
		}
	}

	@Override
	protected boolean hasSupport()
	{
		BlockPos checkPos = getPosition().down();
		IBlockState state = world.getBlockState(checkPos);
		EnumFacing facing = EnumFacing.fromAngle(aim.getCenterYaw());

		if(state.getBlock().isAir(state, world, checkPos))
			return false;

		AxisAlignedBB blockBB = state.getCollisionBoundingBox(world, checkPos);
		return IIMath.isAABBContained(SANDBAG_AABB.getFacing(facing, false),
				blockBB==null?new AxisAlignedBB(0, 0, 0, 1, 1, 1): blockBB);
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
		super.removePassenger(passenger);
	}


	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		entityToUpdate.rotationYaw = aim.clampYawToRange(entityToUpdate.rotationYaw);
		entityToUpdate.rotationPitch = aim.clampPitchToRange(entityToUpdate.rotationPitch);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(source.isProjectile()&&upgrades.contains(WeaponUpgrade.SHIELD))
		{
			shield.attackFrom(source, amount);
			return shield.isDead();
		}
		return super.attackEntityFrom(source, amount);
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

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		return new String[0];
	}

	//--- ICameraEntity ---//

	@Override
	public boolean isCameraEnabled(EntityPlayer player)
	{
		boolean aiming = isSetupComplete()&&controls.getKey("aim");
		SCOPE.withZoomEnabled(aiming);
		SCOPE_IR.withZoomEnabled(aiming);
		return aiming;
	}

	@Override
	public float getCameraPitch(EntityPlayer cameraPlayer, float partialTicks)
	{
		return aim.getPitch(partialTicks)+recoil.getRecoilPitch(partialTicks);
	}

	@Override
	public float getCameraYaw(EntityPlayer cameraPlayer, float partialTicks)
	{
		return aim.getYaw(partialTicks)+recoil.getRecoilYaw(partialTicks);
	}

	@Override
	public Vec3d getCameraPos(EntityPlayer cameraPlayer, float partialTicks)
	{
		float cameraYaw = getCameraYaw(cameraPlayer, partialTicks);
		float cameraPitch = getCameraPitch(cameraPlayer, partialTicks);
		return getPositionVector()
				.addVector(0, (0.34375+0.0625)*0.85-1.75f+0.5+(upgrades.contains(WeaponUpgrade.TRIPOD)?1f: 0f), 0)
				.add(IIMath.offsetPosDirectionXYZ(new Vec3d(-1.5+(upgrades.contains(WeaponUpgrade.SCOPE)||upgrades.contains(WeaponUpgrade.INFRARED_SCOPE)?1f: 0f),
						0.0625, 0), cameraYaw, -cameraPitch, 0));
	}

	//--- IEntityZoomProvider ---//

	@Override
	public IAdvancedZoom getZoom()
	{
		if(upgrades.contains(WeaponUpgrade.SCOPE))
			return SCOPE;
		return upgrades.contains(WeaponUpgrade.INFRARED_SCOPE)?SCOPE_IR: null;
	}
}
