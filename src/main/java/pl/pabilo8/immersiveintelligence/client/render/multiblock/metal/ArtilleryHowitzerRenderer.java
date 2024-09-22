package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArtilleryHowitzer.ArtilleryHowitzerAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

/**
 * @author Pabilo8
 * @since 29.07.2022
 */
@RegisteredTileRenderer(name = "multiblock/artillery_howitzer", clazz = TileEntityArtilleryHowitzer.class)
public class ArtilleryHowitzerRenderer extends IIMultiblockRenderer<TileEntityArtilleryHowitzer>
{
	private AMT[] model = null, allParts = null;
	//animations
	private IIAnimationCompiledMap animationDefault, animationOpen, animationPlatform;
	private IIAnimationCompiledMap[] animationFire, animationLoading, animationUnloading;

	//shell queue
	private IIAnimationGroup animationQueueIn, animationQueueOut;
	private AMTBullet[] shells, shellsStorage;

	//shells used by other animations
	private AMTBullet shellHeld, shellEjected, shellLoaded;

	private AMT gunYaw, gunPitch;

	@Override
	public void drawAnimated(TileEntityArtilleryHowitzer te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//defaultize
		for(AMT mod : allParts)
			mod.defaultize();
		//apply default animation (for inserter angle)
		animationDefault.apply(0);

		//has enough power for active and passive action
		boolean canOperatePassive = te.energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive;
		boolean canOperateActive = canOperatePassive&&te.energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive+ArtilleryHowitzer.energyUsageActive;

		//platform and door animation
		float doorAnim = IIAnimationUtils.getAnimationProgress(te.doorTime, ArtilleryHowitzer.doorTime,
				canOperatePassive, !te.isDoorOpened, 1f, 2f, partialTicks);
		float platformAnim = IIAnimationUtils.getAnimationProgress(te.platformTime, ArtilleryHowitzer.platformTime,
				canOperatePassive, !te.platformPosition, 1f, 1f, partialTicks);
		animationOpen.apply(doorAnim);
		animationPlatform.apply(platformAnim);

		//gun pitch and yaw
		//calculated before animations, so animations can modify it
		float pDiff = te.plannedPitch-te.turretPitch, yDiff = MathHelper.wrapDegrees(360+te.plannedYaw-te.turretYaw);
		float turretYaw = te.turretYaw+Math.signum(yDiff)*MathHelper.clamp(Math.abs(yDiff)*partialTicks, 0, ArtilleryHowitzer.rotateSpeed);
		float turretPitch = te.turretPitch+Math.signum(pDiff)*MathHelper.clamp(Math.abs(yDiff)*partialTicks, 0, ArtilleryHowitzer.rotateSpeed);

		//conveyor animation
		float conveyorAnim = IIAnimationUtils.getAnimationProgress(te.shellConveyorTime, ArtilleryHowitzer.conveyorTime,
				canOperatePassive, false, 1f, 0f, partialTicks);

		//apply conveyor animation directly to shells
		boolean next = false;
		for(int i = 5; i >= 0; i--) //checked from bottom to top
			next = animateShellConveyor(te, 0, i, conveyorAnim, BulletState.BULLET_UNUSED, animationQueueIn, next);
		next = false;
		for(int i = 11; i >= 6; i--) //checked from top to bottom
			next = animateShellConveyor(te, 6, i, conveyorAnim, BulletState.CASING, animationQueueOut, next);

		shellEjected.withStack(ItemStack.EMPTY, BulletState.CASING);

		//shell rack display
		for(int i = 0; i < 4; i++)
		{
			IIAnimationUtils.setModelVisibility(shellsStorage[i], true);
			shellsStorage[i].withStack(te.loadedShells.get(i),
					te.loadedShells.get(i).getItem()==IIContent.itemAmmoHeavyArtillery?BulletState.BULLET_UNUSED: BulletState.CASING);
		}
		IIAnimationUtils.setModelVisibility(shellHeld, false);
		IIAnimationUtils.setModelVisibility(shellEjected, false);
		IIAnimationUtils.setModelVisibility(shellLoaded, false);

		float animationProgress = IIAnimationUtils.getAnimationProgress(te.animationTime, te.animationTimeMax,
				canOperateActive&&te.animation!=ArtilleryHowitzerAnimation.STOP, false, 1f, 0f, partialTicks);
		switch(te.animation)
		{
			case LOAD1:
			case LOAD2:
			case LOAD3:
			case LOAD4:
			{
				//loading animation
				int slot = te.animation.ordinal()-ArtilleryHowitzerAnimation.LOAD1.ordinal();
				setupShellDisplay(te, BulletState.BULLET_UNUSED, slot);
				animationLoading[slot].apply(animationProgress);
			}
			break;
			case UNLOAD1:
			case UNLOAD2:
			case UNLOAD3:
			case UNLOAD4:
			{
				//unloading animation
				int slot = te.animation.ordinal()-ArtilleryHowitzerAnimation.UNLOAD1.ordinal();
				setupShellDisplay(te, shellsStorage[slot].getState(), slot);
				animationUnloading[slot].apply(animationProgress);
			}
			break;
			case FIRE1:
			case FIRE2:
			case FIRE3:
			case FIRE4:
			{
				//loading from rack / firing animation
				int slot = te.animation.ordinal()-ArtilleryHowitzerAnimation.FIRE1.ordinal();
				BulletState firingState = animationProgress > ArtilleryHowitzer.gunFireMoment?BulletState.CASING: BulletState.BULLET_UNUSED;
				setupShellDisplay(te, firingState, slot);
				animationFire[slot].apply(animationProgress);

				double firstMarker = ArtilleryHowitzer.gunFireMoment-0.03f;
				double secondMarker = ArtilleryHowitzer.gunFireMoment-0.07f;
				double dist = Math.abs(firstMarker-secondMarker);
				double firstMarker2 = ArtilleryHowitzer.gunFireMoment+0.01f;
				double secondMarker2 = ArtilleryHowitzer.gunFireMoment+0.04f;
				double dist2 = Math.abs(firstMarker2-secondMarker2);

				// TODO: 11.08.2022 add parameter handling to animation system and remove this mess
				if(animationProgress < 0.1f)
					turretPitch = lerp(turretPitch, 90, Math.min(animationProgress/0.1f, 1f));
				else if(animationProgress > 0.9f)
					turretPitch = lerp(90, turretPitch, (animationProgress-0.9f)/0.1f);
				else if(animationProgress > secondMarker&&animationProgress < firstMarker)
					turretPitch = lerp(90, turretPitch, (float)((animationProgress-secondMarker)/dist));
				else if(animationProgress > firstMarker2&&animationProgress < secondMarker2)
					turretPitch = lerp(turretPitch, 90, (float)((animationProgress-firstMarker2)/dist2));
				else if(animationProgress < secondMarker||animationProgress > secondMarker2)
					turretPitch = 90;
			}
			break;
			case STOP:
			case HIDE:
			case AIM:
			default:
				break;
		}

		//set gun pitch and yaw

		IIAnimationUtils.setModelRotation(gunYaw, 0, (te.mirrored?-1: 1)*(te.facing.getHorizontalAngle()-turretYaw), 0);
		IIAnimationUtils.setModelRotation(gunPitch, -turretPitch, 0, 0);

		//flipping
		applyStandardRotation(te.facing);
		GlStateManager.translate(0, 0, -0.5);
		if(te.mirrored)
			mirrorRender();

		//render
		for(AMT mod : model)
			mod.render(tes, buf);

		if(te.mirrored)
			unMirrorRender();
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		GlStateManager.translate(0, 0, -0.5);

		//defaultize
		for(AMT mod : allParts)
			mod.defaultize();

		//apply default animation (for inserter angle)
		animationDefault.apply(0);

		//render
		for(AMT mod : model)
			mod.render(tes, buf);
	}

	private void setupShellDisplay(TileEntityArtilleryHowitzer te, BulletState state, int slot)
	{
		shellLoaded.withStack(te.inventory.get(5), state);
		shellHeld.withStack(te.inventory.get(5), state);
		shellEjected.withStack(te.inventory.get(5), state);
		shellsStorage[slot].withStack(te.inventory.get(5), state);
	}

	/**
	 * Check if shell is present, causes next shells to animate.
	 */
	private boolean animateShellConveyor(TileEntityArtilleryHowitzer te, int startFrom, int i, float conveyorAnim, BulletState bulletUnused, IIAnimationGroup animationQueueIn, boolean next)
	{
		boolean here = !te.inventory.get(i).isEmpty();
		float shellTime = 0.16666667f*((i-startFrom)+(next?conveyorAnim: 0f));

		IIAnimationUtils.setModelVisibility(shells[i], here);
		if(here)
		{
			shells[i].withStack(te.inventory.get(i), bulletUnused);
			IIAnimationUtils.setModelAnimations(shells[i], animationQueueIn, shellTime);
		}
		else
			next = true;
		return next;
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		shells = new AMTBullet[12];
		shellsStorage = new AMTBullet[4];

		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()),
				header -> new AMT[]{
						//add shell models to placeholders
						createShellQueueAMT(true, 0, header),
						createShellQueueAMT(true, 1, header),
						createShellQueueAMT(true, 2, header),
						createShellQueueAMT(true, 3, header),
						createShellQueueAMT(true, 4, header),
						createShellQueueAMT(true, 5, header),
						createShellQueueAMT(false, 0, header),
						createShellQueueAMT(false, 1, header),
						createShellQueueAMT(false, 2, header),
						createShellQueueAMT(false, 3, header),
						createShellQueueAMT(false, 4, header),
						createShellQueueAMT(false, 5, header),

						shellsStorage[0] = createDefaultShellAMT(header, "shell_storage1"),
						shellsStorage[1] = createDefaultShellAMT(header, "shell_storage2"),
						shellsStorage[2] = createDefaultShellAMT(header, "shell_storage3"),
						shellsStorage[3] = createDefaultShellAMT(header, "shell_storage4"),

						shellLoaded = createDefaultShellAMT(header, "shell_loaded"),
						shellEjected = createDefaultShellAMT(header, "shell_hatch"),
						shellHeld = createDefaultShellAMT(header, "shell_held"),

						new AMTParticle("muzzle_flash", header).setParticle(IIParticles.PARTICLE_GUNFIRE)
				}
		);
		allParts = IIAnimationUtils.getChildrenRecursive(model);
		animationDefault = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "artillery_howitzer/artillery_howitzer_default"));
		animationOpen = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "artillery_howitzer/artillery_howitzer_door"));
		animationPlatform = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "artillery_howitzer/artillery_howitzer_platform"));

		//firing and loading animations, for each ammo rack shell
		animationLoading = new IIAnimationCompiledMap[4];
		animationUnloading = new IIAnimationCompiledMap[4];
		animationFire = new IIAnimationCompiledMap[4];
		for(int i = 0; i < 4; i++)
		{
			animationLoading[i] = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "artillery_howitzer/artillery_howitzer_loading"+(i+1)));
			animationUnloading[i] = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "artillery_howitzer/artillery_howitzer_unloading"+(i+1)));
			animationFire[i] = IIAnimationCompiledMap.create(model, new ResourceLocation(ImmersiveIntelligence.MODID, "artillery_howitzer/artillery_howitzer_fire"+(i+1)));
		}

		animationQueueIn = IIAnimationLoader.loadAnimation(new ResourceLocation(ImmersiveIntelligence.MODID, "artillery_howitzer/artillery_howitzer_queue_in")).getLeadingGroup();
		animationQueueOut = IIAnimationLoader.loadAnimation(new ResourceLocation(ImmersiveIntelligence.MODID, "artillery_howitzer/artillery_howitzer_queue_out")).getLeadingGroup();

		gunYaw = IIAnimationUtils.getPart(model, "turret");
		gunPitch = IIAnimationUtils.getPart(model, "gun");
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		model = IIAnimationUtils.disposeOf(model);
		animationOpen = animationPlatform = null;
		animationFire = animationLoading = null;
		gunYaw = gunPitch = null;
	}

	//--- Internal Methods ---//

	private AMTBullet createDefaultShellAMT(IIModelHeader header, String name)
	{
		return createDefaultShellAMT(header, name, name);
	}

	private AMTBullet createDefaultShellAMT(IIModelHeader header, String name, String originName)
	{
		return new AMTBullet(name, header.getOffset(originName), AmmoRegistry.getModel(IIContent.itemAmmoHeavyArtillery));
	}

	private AMTBullet createShellQueueAMT(boolean in, int id, IIModelHeader header)
	{
		String name = in?"shell_in": "shell_out";
		AMTBullet mod = createDefaultShellAMT(header, name+id, name)
				.withState(BulletState.BULLET_UNUSED);
		shells[(in?0: 6)+id] = mod;
		return mod;
	}

	float lerp(float a, float b, float f)
	{
		return a*(1.0f-f)+b*f;
	}
}
