package pl.pabilo8.immersiveintelligence.common.compat.it;

import mctmods.immersivetechnology.common.ITContent;
import mctmods.immersivetechnology.common.blocks.metal.types.BlockType_MetalBarrel;
import mctmods.immersivetechnology.common.blocks.wooden.types.BlockType_WoodenCrate;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandlerMetal;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIMinecart.Minecarts;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class ImmersiveTechnologyHelper extends IICompatModule
{

	@Override
	public String getName()
	{
		return "ImmersiveTechnology";
	}

	@Override
	public void preInit()
	{
		addMinecarts();

		/*
		addMinecartToItem("trashcan_item", EntityMinecartTrashcanItem::new,
				() -> new ItemStack(ITContent.blockMetalTrash, 1, BlockType_MetalTrash.TRASH_ITEM.getMeta()));
		addMinecartToItem("trashcan_fluid", EntityMinecartTrashcanFluid::new,
				() -> new ItemStack(ITContent.blockMetalTrash, 1, BlockType_MetalTrash.TRASH_FLUID.getMeta()));
		addMinecartToItem("trashcan_energy", EntityMinecartTrashcanEnergy::new,
				() -> new ItemStack(ITContent.blockMetalTrash, 1, BlockType_MetalTrash.TRASH_ENERGY.getMeta()));
		 */
	}

	private void addBlock(ArrayList<Block> blocks, ResourceLocation res)
	{
		Block block = Block.REGISTRY.getObject(res);
		if(block!=Blocks.AIR)
			blocks.add(block);
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
		ArrayList<Block> blocks = new ArrayList<>();
		addBlock(blocks, new ResourceLocation("immersivetech", "metal_multiblock"));
		addBlock(blocks, new ResourceLocation("immersivetech", "metal_multiblock1"));
		addBlock(blocks, new ResourceLocation("immersivetech", "metal_barrel"));
		addBlock(blocks, new ResourceLocation("immersivetech", "metal_device"));
		addBlock(blocks, new ResourceLocation("immersivetech", "metal_trash"));
		PenetrationRegistry.batchRegisterHandler(PenetrationHandlerMetal.get("steel"), blocks.toArray(new Block[0]));

		blocks.clear();
		addBlock(blocks, new ResourceLocation("immersivetech", "stone_multiblock"));
		addBlock(blocks, new ResourceLocation("immersivetech", "stone_decoration"));
		addBlock(blocks, new ResourceLocation("immersivetech", "stone_decoration_slab"));
		PenetrationRegistry.batchRegisterHandler(new PenetrationHandler(PenetrationHardness.CONCRETE, 1f, 150, IIParticles.PARTICLE_DEBRIS_BRICK, IISounds.hitStone),
				blocks.toArray(new Block[0]));
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
		IIContent.itemMinecart.updateValues(Minecarts.values());
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

	public static void addMinecartToItem(String name, BiFunction<World, Vec3d, EntityMinecart> minecart, Supplier<ItemStack> stack)
	{
		EnumHelper.addEnum(Minecarts.class, name.toUpperCase(),
				new Class[]{BiFunction.class, Supplier.class},
				minecart, stack
		);
	}
}
