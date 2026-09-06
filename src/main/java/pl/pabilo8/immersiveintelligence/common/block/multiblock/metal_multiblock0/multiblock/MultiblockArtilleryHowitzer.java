package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockArtilleryHowitzer extends MultiblockStuctureBase<TileEntityArtilleryHowitzer>
{
	public static MultiblockArtilleryHowitzer INSTANCE;

	//Sound Animations
	public final IISoundAnimation loadingSoundAnimation;
	public final IISoundAnimation unloadingSoundAnimation;
	public final IISoundAnimation firingSoundAnimation;
	//Tactile Animations
	public final ResLoc animationPlatform;
	public final ResLoc animationOpen;
	public final ResLoc animationFire;
	public final ResLoc animationLoading;
	public final ResLoc animationUnloading;
	public final ResLoc animationPitch;
	public final ResLoc animationYaw;

	public MultiblockArtilleryHowitzer()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/artillery_howitzer"));
		offset = new Vec3i(4, 5, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy");
		addPOI(MultiblockPOI.ITEM_INPUT, "item_input");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "item_output");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
		addPOI(MultiblockPOI.DATA_INPUT, "data");
		addPOI(MultiblockPOI.MISC_DOOR, "bunker_door");
		addPOI(MultiblockPOI.MISC_WEAPON, "gun");

		//Load tactile animations
		animationOpen = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_door");
		animationPlatform = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_platform");
		animationLoading = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_loading1");
		animationUnloading = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_unloading1");
		animationFire = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_fire1");
		animationPitch = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_tactile_pitch");
		animationYaw = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_tactile_yaw");

		//load sound animations
		loadingSoundAnimation = new IISoundAnimation(18);
		unloadingSoundAnimation = new IISoundAnimation(18);
		firingSoundAnimation = new IISoundAnimation(15.96);

		loadingSoundAnimation
				.withRepeatedSound(0.64, 3.6, IISounds.slidingDoorOpenLoop)

				.withSound(4.36, IISounds.metalLockerOpen)
				.withRepeatedSound(5.0, 11.28, IISounds.chainLoop)

				.withSound(8.4, IISounds.artilleryShellPlace)
				.withSound(9, SoundEvents.ENTITY_MINECART_RIDING)
				.withSound(8.92, IISounds.howitzerPlatformStart)

				.withSound(11.52, IISounds.metalLockerClose)

				.withRepeatedSound(11.96, 13.24, IISounds.inserterYawM)
				.withSound(13.96, IISounds.artilleryShellPick)
				.withSound(14, SoundEvents.ENTITY_MINECART_RIDING)
				.withRepeatedSound(14.24, 14.96, IISounds.inserterPitchM)
				.withRepeatedSound(15.12, 15.90, IISounds.inserterYawM)
				.withSound(16.36, IISounds.artilleryShellPlace)
				.withRepeatedSound(15.12, 16.68, IISounds.inserterPitchM)

				.withRepeatedSound(17.0, 18.0, IISounds.slidingDoorCloseLoop)
				.compile(ArtilleryHowitzer.loadRackTime);

		firingSoundAnimation
				.withSound(0.0, IISounds.metalBreadboxOpen)
				.withSound(0.52, IISounds.electricMotorForward)
				.withSound(1.0, IISounds.electricMotorForward)
				.withSound(1.32, IISounds.electricMotorForward)
				.withSound(1.76, IISounds.electricMotorForward)
				.withSound(2.24, IISounds.electricMotorForward)
				.withSound(2.28, IISounds.electricMotorForward)
				.withSound(2.8, IISounds.electricMotorForward)
				.withSound(3.24, IISounds.electricMotorForward)
				.withSound(3.72, IISounds.electricMotorForward)
				.withSound(4.16, IISounds.electricMotorForward)
				.withSound(4.4, IISounds.metalBreadboxClose)
				.withSound(4.56, IISounds.electricMotorForward)
				.withSound(4.96, IISounds.artilleryShellPick)
				.withSound(5.04, IISounds.electricMotorForward)
				.withSound(5.48, IISounds.electricMotorForward)
				.withSound(5.92, IISounds.electricMotorForward)
				.withSound(6.36, IISounds.electricMotorForward)
				.withSound(6.8, IISounds.electricMotorForward)
				.withSound(7.08, IISounds.metalBreadboxClose)
				.withSound(7.72, IISounds.artilleryShellPick)
				.withSound(7.8, IISounds.electricMotorForward)
				.withSound(8.24, IISounds.electricMotorForward)
				.withSound(8.44, IISounds.electricMotorForward)
				.withSound(8.48, IISounds.electricMotorForward)
				.withSound(9.0, IISounds.electricMotorForward)
				//fire!
				.withSound(9.52, IISounds.electricMotorForward)
				.withSound(10.0, IISounds.electricMotorForward)
				.withSound(10.36, IISounds.electricMotorForward)
				.withSound(10.64, IISounds.electricMotorForward)
				.withSound(10.68, IISounds.electricMotorForward)
				.withSound(11.2, IISounds.electricMotorForward)
				.withSound(11.68, IISounds.electricMotorForward)
				.withSound(12.16, IISounds.electricMotorForward)
				.withSound(12.24, IISounds.artilleryShellPick)
				.withSound(12.64, IISounds.electricMotorBackward)
				.withSound(13.08, IISounds.electricMotorBackward)
				.withSound(13.52, IISounds.electricMotorBackward)
				.withSound(13.96, IISounds.electricMotorForward)
				.withSound(14.12, IISounds.metalBreadboxClose)
				.withSound(14.48, IISounds.artilleryShellPlace)
				.withSound(14.76, IISounds.electricMotorBackward)
				.withSound(15.24, IISounds.electricMotorBackward)
				.withSound(15.6, IISounds.electricMotorBackward)
				.withSound(15.72, IISounds.metalBreadboxOpen)
				.compile(ArtilleryHowitzer.gunFireTime);

		unloadingSoundAnimation
				.withRepeatedSound(0.04, 1.24, IISounds.inserterYawM)

				.withRepeatedSound(0.36, 1, IISounds.slidingDoorCloseLoop)

				.withSound(1.36, IISounds.artilleryShellPick)
				.withSound(2, SoundEvents.ENTITY_MINECART_RIDING)

				.withRepeatedSound(1.44, 3.8, IISounds.inserterYawM)
				.withSound(4.0, IISounds.artilleryShellPlace)
				.withSound(4.5, SoundEvents.ENTITY_MINECART_RIDING)


				.withRepeatedSound(4.28, 5.8, IISounds.inserterYawM)

				.withSound(5.92, IISounds.metalLockerOpen)
				.withRepeatedSound(6.6, 9.28, IISounds.chainLoop)
				.withSound(8, IISounds.artilleryShellPick)
				.withSound(12.44, IISounds.metalLockerClose)

				.withRepeatedSound(8.08, 10.5, IISounds.slidingDoorCloseLoop)
				.compile(ArtilleryHowitzer.loadRackTime);
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockMetalMultiblock0;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks0.ARTILLERY_HOWITZER.getMeta();
	}

	@Override
	protected TileEntityArtilleryHowitzer getMBInstance()
	{
		return new TileEntityArtilleryHowitzer();
	}
}
