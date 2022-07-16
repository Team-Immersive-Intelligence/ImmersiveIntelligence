package pl.pabilo8.immersiveintelligence.common.compat.it;

import mctmods.immersivetechnology.common.ITContent;
import mctmods.immersivetechnology.common.blocks.metal.types.BlockType_MetalBarrel;
import mctmods.immersivetechnology.common.blocks.wooden.types.BlockType_WoodenCrate;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerMetals.PenetrationHandlerSteel;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIMinecart.Minecarts;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class ImmersiveTechnologyHelper extends IICompatModule
{
	@Override
	public void preInit()
	{
		addMinecarts();

		BulletHelper.batchRegisterHandler(new PenetrationHandlerSteel(), ITContent.blockMetalMultiblock,
				ITContent.blockMetalMultiblock1, ITContent.blockMetalBarrel, ITContent.blockMetalDevice,
				ITContent.blockMetalTrash, ITContent.blockMetalDevice0Dummy, ITContent.blockMetalDevice1Dummy,
				ITContent.blockValve);

		/*
		addMinecartToItem("trashcan_item", EntityMinecartTrashcanItem::new,
				() -> new ItemStack(ITContent.blockMetalTrash, 1, BlockType_MetalTrash.TRASH_ITEM.getMeta()));
		addMinecartToItem("trashcan_fluid", EntityMinecartTrashcanFluid::new,
				() -> new ItemStack(ITContent.blockMetalTrash, 1, BlockType_MetalTrash.TRASH_FLUID.getMeta()));
		addMinecartToItem("trashcan_energy", EntityMinecartTrashcanEnergy::new,
				() -> new ItemStack(ITContent.blockMetalTrash, 1, BlockType_MetalTrash.TRASH_ENERGY.getMeta()));
		 */
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
		addEntities();
	}

	@Override
	public void postInit()
	{

	}

	@Optional.Method(modid = "immersivetech")
	private void addMinecarts()
	{
		addMinecartToItem("crate_creative", EntityMinecartCrateCreative::new,
				() -> new ItemStack(ITContent.blockWoodenCrate, 1, BlockType_WoodenCrate.CRATE.getMeta()));
		addMinecartToItem("barrel_creative", EntityMinecartBarrelCreative::new,
				() -> new ItemStack(ITContent.blockMetalBarrel, 1, BlockType_MetalBarrel.BARREL.getMeta()));
		addMinecartToItem("steel_barrel", EntityMinecartBarrelSteelIT::new,
				() -> new ItemStack(ITContent.blockMetalBarrel, 1, BlockType_MetalBarrel.BARREL_STEEL.getMeta()));
		addMinecartToItem("open_barrel", EntityMinecartBarrelOpen::new,
				() -> new ItemStack(ITContent.blockMetalBarrel, 1, BlockType_MetalBarrel.BARREL_OPEN.getMeta()));
	}

	@Optional.Method(modid = "immersivetech")
	private void addEntities()
	{
		//starts from 500
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_crate_creative"),
				EntityMinecartCrateCreative.class, "minecart_crate_creative", 500, ImmersiveIntelligence.INSTANCE, 64, 1,
				true);
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_barrel_creative"),
				EntityMinecartBarrelCreative.class, "minecart_barrel_creative", 501, ImmersiveIntelligence.INSTANCE, 64, 1,
				true);
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_steel_barrel"),
				EntityMinecartBarrelSteelIT.class, "minecart_steel_barrel", 502, ImmersiveIntelligence.INSTANCE, 64, 1,
				true);
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_open_barrel"),
				EntityMinecartBarrelOpen.class, "minecart_open_barrel", 503, ImmersiveIntelligence.INSTANCE, 64, 1,
				true);
	}

	public static Minecarts addMinecartToItem(String name, BiFunction<World, Vec3d, EntityMinecart> minecart, Supplier<ItemStack> stack)
	{
		return EnumHelper.addEnum(Minecarts.class, name.toUpperCase(),
				new Class[]{BiFunction.class, Supplier.class},
				minecart, stack
		);
	}
}
