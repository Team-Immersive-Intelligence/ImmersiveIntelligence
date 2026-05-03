package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;
import blusunrize.immersiveengineering.api.energy.DieselHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.client.render.ItemRendererIEOBJ;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemToolUpgrade.ToolUpgrades;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Railgun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIEFluidConcreteOverride;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIISkyhookOverride;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIFluid;

import java.util.Objects;

import static blusunrize.immersiveengineering.api.tool.BelljarHandler.cropHandler;
import static blusunrize.immersiveengineering.api.tool.BelljarHandler.registerHandler;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 11.01.2026
 * @ii-approved 0.3.1
 * @since 17.08.2023
 */
public class ImmersiveEngineeringHelper extends IICompatModule
{
	@Override
	public void preInit()
	{
		if(Railgun.enableRailgunOverride)
		{
			IEContent.itemRailgun = new ItemIIRailgunOverride();
			IILogger.info("Immersive Engineering Railgun was overridden by Immersive Intelligence");
		}
		if(IIConfig.concreteOverride)
		{
			IEContent.blockFluidConcrete = new BlockIEFluidConcreteOverride();
			ReflectionHelper.setPrivateValue(Fluid.class, IEContent.fluidConcrete, IEContent.blockFluidConcrete, "block");
			IILogger.info("Immersive Engineering Fluid Concrete was overridden by Immersive Intelligence");
		}
		if(IIConfig.skyhookOverride)
		{
			IEContent.itemSkyhook = new ItemIISkyhookOverride();
			IILogger.info("Immersive Engineering Engineers Skyhook was overridden by Immersive Intelligence");
		}

		ReflectionHelper.setPrivateValue(ToolUpgrades.class, ToolUpgrades.REVOLVER_BAYONET, ImmutableSet.of("REVOLVER", "SUBMACHINEGUN", "RIFLE"), "toolset");

		//Utilize the total burn time gained from 1000 mB.
		//1 bucket of Biodiesel burns for 12.5 seconds = 250 ticks
		//DieselHandler.registerFuel(fluid, time)
		//High cetane is better by 20% efficiency.
		//Possible stats for engines later: smoother engine operations, reduced ignition delay, less noise, less engine damage.
		DieselHandler.registerFuel(IIContent.fluidBiodieselHighcetane,300);
		DieselHandler.isValidFuel(IIContent.fluidBiodieselHighcetane);
		DieselHandler.registerDrillFuel(IIContent.fluidBiodieselHighcetane);
		DieselHandler.isValidDrillFuel(IIContent.fluidBiodieselHighcetane);
	}

	@Override
	public String getName()
	{
		return "ImmersiveEngineering";
	}

	public static final ResLoc RES_MC = ResLoc.of(ResLoc.root("minecraft"));

	@Override
	public void registerRecipes()
	{
		//29.04.2026 Carver: added crusher recipes for turning flowers into dye.

		OreDictionary.registerOre("flowerYellow1", new ItemStack(Blocks.YELLOW_FLOWER,1,0));
		OreDictionary.registerOre("flowerYellow2", new ItemStack(Blocks.DOUBLE_PLANT,1,0));
		OreDictionary.registerOre("flowerRed1", new ItemStack(Blocks.RED_FLOWER, 1,14));
		OreDictionary.registerOre("flowerRed2", new ItemStack(Blocks.DOUBLE_PLANT, 1,4));
		OreDictionary.registerOre("flowerRed3", new ItemStack(Blocks.RED_FLOWER, 1,4));
		OreDictionary.registerOre("flowerLightGray1", new ItemStack(Blocks.RED_FLOWER, 1,3));
		OreDictionary.registerOre("flowerLightGray2", new ItemStack(Blocks.RED_FLOWER, 1,8));
		OreDictionary.registerOre("flowerLightGray3", new ItemStack(Blocks.RED_FLOWER, 1,6));
		OreDictionary.registerOre("flowerMagenta1", new ItemStack(Blocks.RED_FLOWER, 1,2));
		OreDictionary.registerOre("flowerMagenta2", new ItemStack(Blocks.DOUBLE_PLANT, 1,1));
		OreDictionary.registerOre("flowerPink1", new ItemStack(Blocks.DOUBLE_PLANT, 1,5));
		OreDictionary.registerOre("flowerPink2", new ItemStack(Blocks.RED_FLOWER, 1,7));
		OreDictionary.registerOre("flowerLightBlue1", new ItemStack(Blocks.RED_FLOWER, 1,1));
		OreDictionary.registerOre("flowerOrange1", new ItemStack(Blocks.RED_FLOWER, 1,5));
		OreDictionary.registerOre("flowerGreen", new ItemStack(Blocks.CACTUS, 1,0));

		//yellow
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,4),
				new IngredientStack("flowerYellow1"), 500);
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 4,4),
				new IngredientStack("flowerYellow2"), 500);
		//red
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,14),
				new IngredientStack("flowerRed1"), 500);
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 4,14),
				new IngredientStack("flowerRed2"), 500);
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,14),
				new IngredientStack("flowerRed3"), 500);
		//Light Gray
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,8),
				new IngredientStack("flowerLightGray1"), 500);
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,8),
				new IngredientStack("flowerLightGray2"), 500);
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,8),
				new IngredientStack("flowerLightGray3"), 500);
		//Magenta
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,2),
				new IngredientStack("flowerMagenta1"), 500);
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 4,2),
				new IngredientStack("flowerMagenta2"), 500);
		//Pink
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 4,6),
				new IngredientStack("flowerPink1"), 500);
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,6),
				new IngredientStack("flowerPink2"), 500);
		//Light Blue
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,3),
				new IngredientStack("flowerLightBlue1"), 500);
		//Orange
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,1),
				new IngredientStack("flowerOrange1"), 500);
		//Green
		CrusherRecipe.addRecipe(new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(RES_MC.with("dye"))), 2,13),
				new IngredientStack("flowerGreen"), 500);

		//Given the nitrogen is made from mostly fatty meat, and high-cetane biodiesel is partially made of fats... some concessions have to be made.
		RefineryRecipe.addRecipe(new FluidStack(IIContent.fluidBiodieselHighcetane, 200), new FluidStack(IIContent.gasNitrogen, 80),
				new FluidStack(IEContent.fluidBiodiesel, 120), 50);
	}

	@Override
	public void init()
	{
		IILogger.info("Adding Railgun Projectiles");
		RailgunHandler.registerProjectileProperties(new IngredientStack("stickTungsten"), 32, 1.3).setColourMap(new int[][]{{0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0x9EA2A7, 0x9EA2A7}});


		//29.04.2026 Carver: added cloche recipes for flowers.

		//Soil: dirt yellow_flower (dandellion)
		registerHandler(cropHandler);

		IngredientStack flowerSoil = new IngredientStack(ImmutableList.of(new ItemStack(Blocks.DIRT,1,0)));

		cropHandler.register(new ItemStack(Blocks.YELLOW_FLOWER), new ItemStack[]{new ItemStack(Blocks.YELLOW_FLOWER,2)}, flowerSoil, Blocks.YELLOW_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,0), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,0)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,1), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,1)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,2), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,2)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,3), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,3)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,4), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,4)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,5), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,5)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,6), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,6)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,7), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,7)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.RED_FLOWER,1,8), new ItemStack[]{new ItemStack(Blocks.RED_FLOWER,2,8)}, flowerSoil, Blocks.RED_FLOWER.getDefaultState());

		cropHandler.register(new ItemStack(Blocks.DOUBLE_PLANT,1,0), new ItemStack[]{new ItemStack(Blocks.DOUBLE_PLANT,2,0)}, flowerSoil, Blocks.DOUBLE_PLANT.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.DOUBLE_PLANT,1,1), new ItemStack[]{new ItemStack(Blocks.DOUBLE_PLANT,2,1)}, flowerSoil, Blocks.DOUBLE_PLANT.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.DOUBLE_PLANT,1,4), new ItemStack[]{new ItemStack(Blocks.DOUBLE_PLANT,2,4)}, flowerSoil, Blocks.DOUBLE_PLANT.getDefaultState());
		cropHandler.register(new ItemStack(Blocks.DOUBLE_PLANT,1,5), new ItemStack[]{new ItemStack(Blocks.DOUBLE_PLANT,2,5)}, flowerSoil, Blocks.DOUBLE_PLANT.getDefaultState());
	}

	@Override
	public void postInit()
	{

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientPreInit()
	{
		//<s>Railgun overwrite</s> <b>Sunlight Railgun Overdrive!</b>
		ImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(IEContent.itemRailgun, 1, 0), new ImmersiveModelRegistry.ItemModelReplacement_OBJ("immersiveengineering:models/item/railgun.obj", true)
				.setTransformations(TransformType.FIRST_PERSON_RIGHT_HAND, new Matrix4().scale(.125, .125, .125).translate(-.1875f, 2.5f, .25f).rotate(Math.PI*.46875, 0, 1, 0).translate(0.5, 0.25, -0.75f)
						.rotate(Math.PI*.0225, 0, 0, 1).scale(1.125, 1.125, 1.125))
				.setTransformations(TransformType.FIRST_PERSON_LEFT_HAND, new Matrix4().scale(.125, .125, .125).translate(-1.75, 1.625, .875).rotate(-Math.PI*.46875, 0, 1, 0))
				.setTransformations(TransformType.THIRD_PERSON_RIGHT_HAND, new Matrix4().scale(.1875, .1875, .1875).translate(0.5, 0.5f, -3.5).rotate(Math.PI*.40125, 0, 1, 0))
				.setTransformations(TransformType.THIRD_PERSON_LEFT_HAND, new Matrix4().translate(-.1875, .5, -.3125).scale(.1875, .1875, .1875).rotate(-Math.PI*.46875, 0, 1, 0).rotate(-Math.PI*.25, 0, 0, 1))
				.setTransformations(TransformType.FIXED, new Matrix4().translate(.1875, .0625, .0625).scale(.125, .125, .125).rotate(-Math.PI*.25, 0, 0, 1))
				.setTransformations(TransformType.GUI, new Matrix4().translate(-.1875, 0, 0).scale(.1875, .1875, .1875).rotate(-Math.PI*.6875, 0, 1, 0).rotate(-Math.PI*.1875, 0, 0, 1))
				.setTransformations(TransformType.GROUND, new Matrix4().translate(.125, .125, .0625).scale(.125, .125, .125)));
		IEContent.itemRailgun.setTileEntityItemStackRenderer(ItemRendererIEOBJ.INSTANCE);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientInit()
	{

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientPostInit()
	{

	}

}
