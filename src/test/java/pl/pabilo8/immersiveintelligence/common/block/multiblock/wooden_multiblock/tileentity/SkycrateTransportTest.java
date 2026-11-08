package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.*;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorGear;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy.RotationSide;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartCrateBase;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkycrateInternal;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Verifies cargo conservation, station recovery and authoritative synchronisation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.10.2026
 */
public class SkycrateTransportTest extends GameTestBasic
{
	private static final int DIMENSION = 237;
	private static MultiblockSkyCrateStation originalCrate;
	private static MultiblockSkyCartStation originalCart;
	private static MultiblockSkyCratePost originalPost;
	private static TestMount mountItem;
	private World world;
	private WireType wire;
	private final AtomicLong worldTime = new AtomicLong();

	@BeforeAll
	static void initialise()
	{
		IILogger.logger = LogManager.getLogger("skycrate-test");
		NBTSerialisation.preInit();
		originalCrate = MultiblockSkyCrateStation.INSTANCE;
		originalCart = MultiblockSkyCartStation.INSTANCE;
		originalPost = MultiblockSkyCratePost.INSTANCE;
		MultiblockSkyCrateStation.INSTANCE = mock(MultiblockSkyCrateStation.class);
		MultiblockSkyCartStation.INSTANCE = mock(MultiblockSkyCartStation.class);
		MultiblockSkyCratePost.INSTANCE = mock(MultiblockSkyCratePost.class);
		configure(MultiblockSkyCrateStation.INSTANCE);
		configure(MultiblockSkyCartStation.INSTANCE);
		configure(MultiblockSkyCratePost.INSTANCE);
		mountItem = new TestMount();
		net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS.register(mountItem.setRegistryName(new ResourceLocation("minecraft", "test_skycrate_mount")));
	}

	private static void configure(pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase<?> structure)
	{
		when(structure.getSize()).thenReturn(new int[]{3, 3, 3});
		when(structure.getRotation(anyString())).thenAnswer(call -> {
			String name = call.getArgument(0);
			return name.equals("rotary")||name.equals("cargo_output")?Rotation.COUNTERCLOCKWISE_90:
					name.equals("mount")?Rotation.NONE: Rotation.CLOCKWISE_90;
		});
		when(structure.getPointOfInterest(anyString())).thenAnswer(call -> {
			return switch((String)call.getArgument(0))
			{
				case "mount" -> 7;
				case "wire" -> 19;
				case "cart_io", "cargo_input" -> 2;
				case "cart_cradle" -> 1;
				default -> 9;
			};
		});
		when(structure.getPointsOfInterest(any(MultiblockPOI.class))).thenAnswer(call -> {
			return switch((MultiblockPOI)call.getArgument(0))
			{
				case ROTARY_INPUT -> new int[]{6};
				case SKYCRATE_WIRE_MOUNT, WIRE_MOUNT -> new int[]{19};
				case REDSTONE_INPUT -> new int[]{8};
				case ITEM_INPUT -> new int[]{2};
				case ITEM_OUTPUT -> new int[]{9};
				default -> new int[]{20};
			};
		});
	}

	@AfterAll
	static void restoreStructures()
	{
		MultiblockSkyCrateStation.INSTANCE = originalCrate;
		MultiblockSkyCartStation.INSTANCE = originalCart;
		MultiblockSkyCratePost.INSTANCE = originalPost;
	}

	@BeforeEach
	void setUp() throws ReflectiveOperationException
	{
		world = mock(World.class);
		java.lang.reflect.Field provider = World.class.getField("provider");
		provider.setAccessible(true);
		provider.set(world, mock(WorldProvider.class));
		java.lang.reflect.Field random = World.class.getField("rand");
		random.setAccessible(true);
		random.set(world, new Random());
		when(world.provider.getDimension()).thenReturn(DIMENSION);
		when(world.isBlockLoaded(any(BlockPos.class))).thenReturn(true);
		when(world.getBlockState(any(BlockPos.class))).thenReturn(net.minecraft.init.Blocks.AIR.getDefaultState());
		when(world.getTotalWorldTime()).thenAnswer(call -> worldTime.get());
		when(world.getEntitiesWithinAABB(eq(EntitySkycrateInternal.class), any(), any())).thenReturn(Collections.emptyList());
		wire = mock(WireType.class);
		when(wire.getCategory()).thenReturn(WireType.STRUCTURE_CATEGORY);
		ImmersiveNetHandler.INSTANCE.directConnections.put(DIMENSION, new ConcurrentHashMap<>());
	}

	@AfterEach
	void clearConnections()
	{
		ImmersiveNetHandler.INSTANCE.directConnections.remove(DIMENSION);
	}

	private CrateStation crateStation()
	{
		CrateStation station = new CrateStation();
		station.setWorld(world);
		station.setPos(BlockPos.ORIGIN);
		station.formed = true;
		return station;
	}

	private CartStation cartStation()
	{
		CartStation station = new CartStation();
		station.setWorld(world);
		station.setPos(BlockPos.ORIGIN);
		station.formed = true;
		station.internalEntity = mock(EntitySkycrateInternal.class);
		when(station.internalEntity.isEntityAlive()).thenReturn(true);
		return station;
	}

	private Connection connect(BlockPos start, BlockPos end)
	{
		Connection connection = new Connection(start, end, wire, 10);
		connection.getSubVertices(new net.minecraft.util.math.Vec3d(.5, .625, .5),
				new net.minecraft.util.math.Vec3d(10.5, .625, .5));
		ImmersiveNetHandler.INSTANCE.directConnections.get(DIMENSION).put(start, new HashSet<>(Collections.singleton(connection)));
		return connection;
	}

	@Test
	void failedSkycrateSpawnRetainsCargoAndMount()
	{
		CrateStation station = crateStation();
		station.inventory.set(3, new ItemStack(Items.APPLE));
		station.inventory.set(4, new ItemStack(mountItem));
		station.animation = 5;
		station.progress = station.getAnimationLength();
		BlockPos wirePos = station.getPOIPos(MultiblockPOI.SKYCRATE_WIRE_MOUNT);
		connect(wirePos, wirePos.east(10));

		station.onUpdate();
		assertEquals(5, station.animation);
		assertEquals(Items.APPLE, station.inventory.get(3).getItem());
		assertEquals(mountItem, station.inventory.get(4).getItem());
		verify(world).spawnEntity(isA(EntitySkyCrate.class));

		when(world.spawnEntity(isA(EntitySkyCrate.class))).thenReturn(true);
		station.onUpdate();
		assertEquals(0, station.animation);
		assertTrue(station.inventory.get(3).isEmpty());
		assertTrue(station.inventory.get(4).isEmpty());
		assertEquals(0, station.progress);
	}

	@Test
	void disconnectedLaunchWaitsWithoutResettingCargo()
	{
		CartStation station = cartStation();
		station.crate = new ItemStack(Items.APPLE);
		station.mount = new ItemStack(mountItem);
		station.animation = 5;
		station.progress = station.getAnimationLength();
		station.onUpdate();
		assertEquals(5, station.animation);
		assertFalse(station.crate.isEmpty());
		assertFalse(station.mount.isEmpty());
		verify(world, never()).spawnEntity(any());
	}

	@Test
	void busyArrivalRetainsEntityPayloadThenConsumesItOnce()
	{
		CrateStation station = crateStation();
		EntitySkyCrate arriving = mock(EntitySkyCrate.class);
		arriving.crate = new ItemStack(Items.APPLE);
		arriving.mount = new ItemStack(mountItem);
		station.animation = 1;
		assertTrue(station.onSkycrateMeeting(arriving));
		assertFalse(arriving.crate.isEmpty());
		verify(arriving, never()).setDead();
		station.animation = 0;
		assertFalse(station.onSkycrateMeeting(arriving));
		assertEquals(4, station.animation);
		assertEquals(Items.APPLE, station.inventory.get(3).getItem());
		assertTrue(arriving.crate.isEmpty());
		assertTrue(arriving.mount.isEmpty());
		verify(arriving).setDead();
	}

	@Test
	void rejectedMountReturnDropsOnceAndClearsStoredMount()
	{
		CrateStation station = crateStation();
		station.inventory.set(3, new ItemStack(Items.APPLE));
		station.inventory.set(4, new ItemStack(mountItem));
		station.animation = 4;
		station.progress = station.getAnimationLength();
		when(world.spawnEntity(isA(EntityItem.class))).thenReturn(true);
		station.onUpdate();
		assertTrue(station.inventory.get(4).isEmpty());
		assertEquals(3, station.animation);
		station.onUpdate();
		verify(world, times(1)).spawnEntity(isA(EntityItem.class));
	}

	@Test
	void dummyCapabilitiesUseMasterStorageAndMirrorTheirInputSide()
	{
		CrateStation master = crateStation();
		TileEntitySkyCrateStation dummy = spy(new TileEntitySkyCrateStation());
		dummy.setWorld(world);
		dummy.formed = true;
		dummy.pos = 6;
		dummy.mirrored = master.mirrored = true;
		doReturn(master).when(dummy).master();
		dummy.dummyCleanup();
		EnumFacing side = dummy.getDirection("rotary");
		assertSame(master.rotation, dummy.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, side));
		assertEquals(RotationSide.INPUT, master.rotation.getSide(side));
		assertNull(dummy.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, side.getOpposite()));
		dummy.pos = master.pos = 2;
		assertSame(master.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, master.getDirection("cargo_input")),
				dummy.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dummy.getDirection("cargo_input")));
	}

	@Test
	void saveRoundTripKeepsDenseInventoryAndRotaryReferences()
	{
		CrateStation station = crateStation();
		station.inventory.set(3, new ItemStack(Items.APPLE));
		station.inventory.set(5, new ItemStack(Items.BANNER));
		station.animation = 4;
		station.progress = 13;
		station.rotation.setRotationSpeed(5);
		station.rotation.setTorque(7);
		NBTTagCompound saved = new NBTTagCompound();
		station.writeCustomNBT(saved, false);
		assertEquals(6, saved.getTagList("inventory", 10).tagCount());
		CrateStation restored = crateStation();
		Object originalRotation = restored.rotation;
		restored.readCustomNBT(saved, false);
		assertEquals(4, restored.animation);
		assertEquals(13, restored.progress);
		assertSame(originalRotation, restored.rotation);
		assertEquals(5, restored.rotation.getRotationSpeed());
		assertEquals(7, restored.rotation.getTorque());
		assertEquals(Items.APPLE, restored.inventory.get(3).getItem());
	}

	@Test
	void clientNbtCannotOverwriteAuthoritativeCargo()
	{
		CartStation station = cartStation();
		station.crate = new ItemStack(Items.APPLE);
		station.animation = 5;
		NBTTagCompound update = new NBTTagCompound();
		update.setInteger("animation", 0);
		update.setTag("crate", ItemStack.EMPTY.serializeNBT());
		station.receiveMessageFromClient(update);
		assertEquals(5, station.animation);
		assertEquals(Items.APPLE, station.crate.getItem());
	}

	@Test
	void stateSyncContinuesPastOneThousandWorldTicks()
	{
		CrateStation station = crateStation();
		station.inventory.set(3, new ItemStack(Items.APPLE));
		station.animation = 5;
		station.progress = station.getAnimationLength();
		for(int tick = 990; tick <= 1020; tick++)
		{
			worldTime.set(tick);
			station.onUpdate();
		}
		assertEquals(7, station.messages.size());
		for(NBTTagCompound message : station.messages)
		{
			assertEquals(5, message.getInteger("animation"));
			assertTrue(message.hasKey("progress"));
			assertTrue(message.hasKey("rotation"));
			assertFalse(message.hasKey("inventory"));
		}
	}

	@Test
	void savingCartStationDoesNotCreateEntitiesAndDropsIncludeSeparatePayload()
	{
		CartStation station = cartStation();
		station.internalEntity = null;
		station.crate = new ItemStack(Items.APPLE);
		station.mount = new ItemStack(mountItem);
		station.banner = new ItemStack(Items.BANNER);
		station.writeCustomNBT(new NBTTagCompound(), false);
		verify(world, never()).spawnEntity(any());
		assertEquals(6, station.getDroppedItems().size());
		assertTrue(station.getDroppedItems().contains(station.crate));
		assertTrue(station.getDroppedItems().contains(station.mount));
		assertTrue(station.getDroppedItems().contains(station.banner));
	}

	@Test
	void recoveryFindsOwnedHelperAndRetainsSavedAnimation()
	{
		CartStation station = cartStation();
		station.internalEntity = null;
		station.animation = 4;
		station.progress = 3;
		EntitySkycrateInternal helper = mock(EntitySkycrateInternal.class);
		helper.origin_pos = station.getPos();
		EntityMinecart cart = mock(EntityMinecart.class);
		when(helper.isEntityAlive()).thenReturn(true);
		when(helper.getPassengers()).thenReturn(Collections.singletonList(cart));
		when(cart.isEntityAlive()).thenReturn(true);
		when(cart.getRidingEntity()).thenReturn(helper);
		when(world.getEntitiesWithinAABB(eq(EntitySkycrateInternal.class), any(), any())).thenReturn(Collections.singletonList(helper));
		station.onUpdate();
		assertSame(helper, station.internalEntity);
		assertSame(cart, station.cart);
		assertTrue(station.occupied);
		assertEquals(4, station.animation);
		verify(world, never()).spawnEntity(any());
	}

	@Test
	void postSelectsOtherConnectionWithoutDependingOnReverseObject()
	{
		TileEntitySkyCratePost post = spy(new TileEntitySkyCratePost());
		post.setWorld(world);
		post.setPos(BlockPos.ORIGIN);
		post.formed = true;
		doReturn(post).when(post).master();
		BlockPos wirePos = post.getPOIPos(MultiblockPOI.SKYCRATE_WIRE_MOUNT);
		Connection incoming = new Connection(wirePos.west(10), wirePos, wire, 10);
		Connection reversed = new Connection(wirePos, incoming.start, wire, 10);
		Connection outgoing = new Connection(wirePos, wirePos.east(10), wire, 10);
		ImmersiveNetHandler.INSTANCE.directConnections.get(DIMENSION).put(wirePos, new HashSet<>(Arrays.asList(reversed, outgoing)));
		EntitySkyCrate skycrate = mock(EntitySkyCrate.class);
		skycrate.connection = incoming;
		assertTrue(post.onSkycrateMeeting(skycrate));
		verify(skycrate).setConnection(outgoing, wirePos, 0);
	}

	@Test
	void entityReloadPreservesSavedEnergy()
	{
		SavedSkycrate entity = new SavedSkycrate(world);
		entity.setSkycrate(new ItemStack(mountItem), new ItemStack(Items.APPLE));
		entity.energy = 42;
		NBTTagCompound saved = new NBTTagCompound();
		entity.save(saved);
		SavedSkycrate restored = new SavedSkycrate(world);
		restored.load(saved);
		assertFalse(restored.isDead);
		assertEquals(42, restored.energy);
		assertEquals(100, restored.horizontalSpeedPowered);
	}

	@Test
	void successfulCartLoadingReturnsMountOnce()
	{
		CartStation station = cartStation();
		station.cart = mock(EntityMinecartEmpty.class);
		when(station.cart.isEntityAlive()).thenReturn(true);
		when(station.cart.getRidingEntity()).thenReturn(station.internalEntity);
		EntityMinecart loaded = mock(EntityMinecart.class, withSettings().extraInterfaces(IMinecartBlockPickable.class));
		station.crate = new ItemStack(Items.APPLE);
		station.mount = new ItemStack(mountItem);
		station.animation = 4;
		station.progress = station.getAnimationLength();
		java.util.function.Predicate<ItemStack> predicate = stack -> stack.getItem()==Items.APPLE;
		MinecartBlockHelper.blocks.put(predicate, ignored -> loaded);
		try
		{
			when(world.spawnEntity(any())).thenReturn(true);
			station.onUpdate();
			assertTrue(station.crate.isEmpty());
			assertTrue(station.mount.isEmpty());
			assertSame(loaded, station.cart);
			assertEquals(3, station.animation);
			verify((IMinecartBlockPickable)loaded).setMinecartBlock(argThat(stack -> stack.getItem()==Items.APPLE));
			station.progress = station.getAnimationLength();
			station.onUpdate();
			verify(world, times(1)).spawnEntity(isA(EntityItem.class));
		} finally
		{
			MinecartBlockHelper.blocks.remove(predicate);
		}
	}

	@Test
	void failedMinecartConversionLeavesOriginalContainerAlive()
	{
		TestCrateCart cart = new TestCrateCart(world);
		cart.setPosition(12, 34, 56);
		cart.setInventorySlotContents(0, new ItemStack(Items.DIAMOND, 3));
		Tuple<ItemStack, EntityMinecart> failed = cart.getBlockForPickup();
		assertTrue(failed.getFirst().isEmpty());
		assertSame(cart, failed.getSecond());
		assertFalse(cart.isDead);
		assertEquals(3, cart.getStackInSlot(0).getCount());
		when(world.spawnEntity(isA(EntityMinecartEmpty.class))).thenReturn(true);
		Tuple<ItemStack, EntityMinecart> success = cart.getBlockForPickup();
		assertFalse(success.getFirst().isEmpty());
		assertTrue(cart.isDead);
		assertEquals(12, success.getSecond().posX);
		assertEquals(34, success.getSecond().posY);
		assertEquals(56, success.getSecond().posZ);
	}

	@Test
	void gearRangeExcludesCargoAndRetainsExistingEfficiency()
	{
		NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
		Gear gear = new Gear();
		inventory.set(0, new ItemStack(gear));
		assertEquals(2f/3, IIRotaryUtils.getGearEfficiency(inventory, 0, 3), .0001f);
		assertEquals(2f/3, IIRotaryUtils.getGearTorqueRatio(inventory, 0, 3), .0001f);
		inventory.set(3, new ItemStack(Items.APPLE));
		inventory.set(4, new ItemStack(mountItem));
		inventory.set(5, new ItemStack(Items.BANNER));
		assertEquals(2f/3, IIRotaryUtils.getGearEfficiency(inventory, 0, 3), .0001f);
		assertEquals(2f/3, IIRotaryUtils.getGearTorqueRatio(inventory, 0, 3), .0001f);
		assertEquals(0, IIRotaryUtils.getGearTorqueRatio(NonNullList.create()));
		assertThrows(IndexOutOfBoundsException.class, () -> IIRotaryUtils.getGearEfficiency(inventory, 3, 7));
	}

	@Test
	void wireYawUsesDirectionRatherThanWorldPosition()
	{
		BlockPos start = new BlockPos(50, 70, -80);
		Connection connection = connect(start, start.east(10));
		EntitySkyCrate skycrate = new EntitySkyCrate(world, connection, new ItemStack(mountItem), new ItemStack(Items.APPLE), start);
		skycrate.energy = 0;
		skycrate.nextPos();
		assertEquals(0, skycrate.rotationYaw, .001f);
		assertTrue(skycrate.posX > start.getX());
		assertEquals(start.getZ()+.5, skycrate.posZ, .001);
	}

	@Test
	void clientsClampVisualProgressWithoutCompletingTransport() throws ReflectiveOperationException
	{
		CrateStation station = crateStation();
		java.lang.reflect.Field remote = World.class.getField("isRemote");
		remote.setAccessible(true);
		remote.setBoolean(world, true);
		station.inventory.set(3, new ItemStack(Items.APPLE));
		station.inventory.set(4, new ItemStack(mountItem));
		station.animation = 4;
		station.progress = station.getAnimationLength();
		station.onUpdate();
		assertEquals(4, station.animation);
		assertFalse(station.inventory.get(3).isEmpty());
		assertFalse(station.inventory.get(4).isEmpty());
		assertEquals(1, station.getAnimationProgress(1));
		verify(world, never()).spawnEntity(any());
	}

	private static class TestCrateCart extends EntityMinecartCrateBase
	{
		TestCrateCart(World world)
		{
			super(world);
		}

		@Override
		protected net.minecraft.block.Block getCarriedBlock()
		{
			return Blocks.CHEST;
		}

		@Override
		protected int getBlockMetaID()
		{
			return 0;
		}

		@Override
		public boolean isIECrate()
		{
			return false;
		}

		@Override
		public int getSizeInventory()
		{
			return 27;
		}

		@Override
		public String getGuiID()
		{
			return "minecraft:chest";
		}
	}

	private static class Gear extends Item implements IMotorGear
	{
		@Override
		public float getGearTorqueModifier(ItemStack stack)
		{
			return 2;
		}
	}

	public static class CrateStation extends TileEntitySkyCrateStation
	{
		final List<NBTTagCompound> messages = new ArrayList<>();

		@Override
		public TileEntitySkyCrateStation master()
		{
			return this;
		}

		@Override
		public void sendNBTMessageClient(NBTTagCompound message)
		{
			messages.add(message.copy());
		}
	}

	public static class CartStation extends TileEntitySkyCartStation
	{
		@Override
		public TileEntitySkyCartStation master()
		{
			return this;
		}

		@Override
		public void sendNBTMessageClient(NBTTagCompound message)
		{
		}
	}

	private static class SavedSkycrate extends EntitySkyCrate
	{
		SavedSkycrate(World world)
		{
			super(world);
		}

		void save(NBTTagCompound nbt)
		{
			writeEntityToNBT(nbt);
		}

		void load(NBTTagCompound nbt)
		{
			readEntityFromNBT(nbt);
		}
	}

	private static class TestMount extends Item implements ISkycrateMount
	{
		@Override
		public double getMountEnergy(ItemStack stack)
		{
			return 100;
		}

		@Override
		public double getMountMaxEnergy(ItemStack stack)
		{
			return 200;
		}

		@Override
		public double getPoweredSpeed(ItemStack stack)
		{
			return 100;
		}

		@Override
		public void render(ItemStack stack, World world, float partialTicks, double energy)
		{
		}

		@Override
		public boolean isTesla(ItemStack stack)
		{
			return true;
		}
	}
}
