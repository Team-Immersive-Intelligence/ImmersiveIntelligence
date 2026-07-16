package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement_OBJ;
import blusunrize.immersiveengineering.client.gui.GuiCrate;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.render.ItemRendererIEOBJ;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenCrate;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.items.ItemToolUpgrade.ToolUpgrades;
import blusunrize.immersiveengineering.common.util.IEVillagerHandler;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.gui.block.overrides.GuiIECrateOverride;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Overrides;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Overrides.Railgun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIEFluidConcreteOverride;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.compat.ie.recipe.*;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerIICrate;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casing;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.util.IIReflectionUtils;

import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 11.01.2026
 * @ii-approved 0.3.1
 * @since 17.08.2023
 */
public class ImmersiveEngineeringHelper extends IICompatModule
{
	public static IIGUI GUI_IE_CRATE_OVERRIDE = null;

	@Override
	public void preInit()
	{
		if(Railgun.enableRailgunOverride)
		{
			IEContent.itemRailgun = new ItemIIRailgunOverride();
			IILogger.info("Immersive Engineering Railgun was overridden by Immersive Intelligence");
		}
		if(Overrides.concreteOverride)
		{
			IEContent.blockFluidConcrete = new BlockIEFluidConcreteOverride();
			ReflectionHelper.setPrivateValue(Fluid.class, IEContent.fluidConcrete, IEContent.blockFluidConcrete, "block");
			IILogger.info("Immersive Engineering Fluid Concrete was overridden by Immersive Intelligence");
		}

		if(Overrides.enableDecoOverride)
		{
			GUI_IE_CRATE_OVERRIDE = EnumHelper.addEnum(IIGUI.class, "IE_CRATE_OVERRIDE", new Class[]{Class.class, BiFunction.class},
					TileEntityWoodenCrate.class, (BiFunction<EntityPlayer, TileEntity, Container>)
							(player, te) -> new ContainerIICrate<>(player, (TileEntityWoodenCrate)te)
			);
		}

		ReflectionHelper.setPrivateValue(ToolUpgrades.class, ToolUpgrades.REVOLVER_BAYONET, ImmutableSet.of("REVOLVER", "SUBMACHINEGUN", "RIFLE"), "toolset");
	}

	@Override
	public String getName()
	{
		return "ImmersiveEngineering";
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
		IILogger.info("Adding Railgun Projectiles");
		RailgunHandler.registerProjectileProperties(new IngredientStack("stickTungsten"), 32, 1.3).setColourMap(new int[][]{{0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0x9EA2A7, 0x9EA2A7}});
	}

	@Override
	public void postInit()
	{
		if(Overrides.enableTradeOverride)
		{
			VillagerCareer gunsmithCareer;
			if(IEVillagerHandler.PROF_ENGINEER==null||(gunsmithCareer = findVillagerCareer(IEVillagerHandler.PROF_ENGINEER, "immersiveengineering.gunsmith"))==null)
			{
				IILogger.error("Could not modify Gunsmith villager to sell II iron gun barrels.");
				return;
			}

			List<ITradeList> trades = gunsmithCareer.getTrades(1);
			if(trades!=null)
			{
				gunsmithCareer.addTrade(1,
						new ItemstackForEmerald(IIContent.itemMaterial.getStack(Materials.IRON_GUN_BARREL), new PriceInfo(2, 4))
				);
				gunsmithCareer.addTrade(2,
						new ItemstackForEmerald(IIContent.itemAmmoCasing.getStack(Casing.SMG_1BCAL, 12), new PriceInfo(1, 5)),
						new ItemstackForEmerald(IIContent.itemAmmoCasing.getStack(Casing.MG_2BCAL, 12), new PriceInfo(2, 6))
				);
				gunsmithCareer.addTrade(3,
						new ItemstackForEmerald(IIContent.itemAmmoCasing.getStack(Casing.STG_1BCAL, 8), new PriceInfo(6, 12))
				);
				gunsmithCareer.addTrade(4,
						new ItemstackForEmerald(IIContent.itemMaterial.getStack(Materials.TUNGSTEN_GUN_BARREL), new PriceInfo(6, 12))
				);
			}
		}

		//Import IE's recipe lists into II's registry, so that they can be used inside manual pages
		importIERecipes();
	}

	@Override
	public void loadComplete()
	{
		super.loadComplete();
		//Override IE's Event Handler
		IIReflectionUtils.overrideEventHandler(blusunrize.immersiveengineering.common.EventHandler.class, new IEOverrideEventHandler());
	}

	private void importIERecipes()
	{
		int imported = 0;
		imported += adaptRecipes(ArcFurnaceRecipe.recipeList,
				ArcFurnaceRecipeAdapter::new);
		imported += adaptRecipes(MetalPressRecipe.recipeList.values(), MetalPressRecipe::listInJEI,
				MetalPressRecipeAdapter::new);
		imported += adaptRecipes(SqueezerRecipe.recipeList,
				SqueezerRecipeAdapter::new);
		imported += adaptRecipes(FermenterRecipe.recipeList,
				FermenterRecipeAdapter::new);
		imported += adaptRecipes(RefineryRecipe.recipeList,
				RefineryRecipeAdapter::new);
		imported += adaptRecipes(MixerRecipe.recipeList,
				MixerRecipeAdapter::new);
		imported += adaptRecipes(CrusherRecipe.recipeList,
				CrusherRecipeAdapter::new);
		imported += adaptRecipes(CokeOvenRecipe.recipeList,
				CokeOvenRecipeAdapter::new);
		imported += adaptRecipes(BlastFurnaceRecipe.recipeList,
				BlastFurnaceRecipeAdapter::new);
		imported += adaptRecipes(AlloyRecipe.recipeList,
				AlloyingFurnaceRecipeAdapter::new);

		IILogger.info("Imported "+imported+" Immersive Engineering recipes for II manual layouts");
	}

	private <T> int adaptRecipes(Iterable<T> recipes, Consumer<T> adapterFactory)
	{
		return adaptRecipes(recipes, recipe -> true, adapterFactory);
	}

	private <T> int adaptRecipes(Iterable<T> recipes, Predicate<T> filter, Consumer<T> adapterFactory)
	{
		int count = 0;
		for(T recipe : recipes)
			if(recipe!=null&&filter.test(recipe))
			{
				adapterFactory.accept(recipe);
				count++;
			}
		return count;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiOpen(GuiOpenEvent event)
	{
		if(!Overrides.enableDecoOverride)
			return;
		GuiScreen gui = event.getGui();
		if(gui instanceof GuiCrate)
			event.setGui(new GuiIECrateOverride(ClientUtils.mc().player, getTileFromIEGUI(gui)));
	}

	private <T extends TileEntity> T getTileFromIEGUI(Gui gui)
	{
		//noinspection unchecked
		return ((ContainerIEBase<T>)((GuiIEContainerBase)gui).inventorySlots).tile;
	}

	private VillagerCareer findVillagerCareer(VillagerProfession profession, String careerName)
	{
		VillagerCareer career = profession.getCareer(0), careerZero = career;
		int iterator = 1;
		do
		{
			if(career.getName().equals(careerName))
				return career;
			career = profession.getCareer(iterator++);
		}
		while(career!=careerZero);
		return career;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientPreInit()
	{
		if(Railgun.enableRailgunOverride)
		{
			//<s>Railgun overwrite</s> <b>Sunlight Railgun Overdrive!</b>
			ImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(IEContent.itemRailgun, 1, 0), new ItemModelReplacement_OBJ("immersiveengineering:models/item/railgun.obj", true)
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

		//Enable overriding IE GUIs with II's Deco based ones
		if(Overrides.enableDecoOverride)
			MinecraftForge.EVENT_BUS.register(this);

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

	private static class ItemstackForEmerald implements ITradeList
	{
		public ItemStack sellingItem;
		public PriceInfo priceInfo;

		public ItemstackForEmerald(ItemStack stack, PriceInfo priceInfo)
		{
			this.sellingItem = stack;
			this.priceInfo = priceInfo;
		}

		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
		{
			int i = 1;
			if(this.priceInfo!=null)
				i = this.priceInfo.getPrice(random);
			ItemStack itemstack;
			ItemStack itemstack1;
			if(i < 0)
			{
				itemstack = new ItemStack(Items.EMERALD);
				itemstack1 = Utils.copyStackWithAmount(sellingItem, -i);
			}
			else
			{
				itemstack = new ItemStack(Items.EMERALD, i, 0);
				itemstack1 = Utils.copyStackWithAmount(sellingItem, 1);
			}
			recipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}
}
