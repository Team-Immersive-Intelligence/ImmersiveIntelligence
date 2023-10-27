package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.client.render.ItemRendererIEOBJ;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemToolUpgrade.ToolUpgrades;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Railgun;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;

/**
 * @author Pabilo8
 * @since 17.08.2023
 */
public class ImmersiveEngineeringHelper extends IICompatModule
{
	@Override
	public void preInit()
	{
		if(Railgun.enableRailgunOverride)
			IEContent.itemRailgun = new ItemIIRailgunOverride();

		ReflectionHelper.setPrivateValue(ToolUpgrades.class, ToolUpgrades.REVOLVER_BAYONET, ImmutableSet.of("REVOLVER", "SUBMACHINEGUN", "RIFLE"), "toolset");

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
