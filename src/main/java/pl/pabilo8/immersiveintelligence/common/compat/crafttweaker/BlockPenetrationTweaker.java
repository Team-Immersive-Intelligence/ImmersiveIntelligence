package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.block.IMaterial;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandlerMetal;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @updated 27.03.2024
 * @ii-approved 0.3.1
 * @since 06.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".BlockPenetration")
@ZenRegister
public class BlockPenetrationTweaker
{
	@ZenMethod
	public static void addMaterial(IMaterial material, String hardnessTier, float thickness, float integrity, @Optional String sound)
	{
		PenetrationRegistry.registerMaterial(CraftTweakerMC.getMaterial(material),
				new CTPenetrationHandler(hardnessTier, thickness, integrity, sound));
	}

	@ZenMethod
	public static void addBlock(IBlockState state, String hardnessTier, float thickness, float integrity, @Optional String sound)
	{
		PenetrationRegistry.registerState(b -> state.compare(CraftTweakerMC.getBlockState(b))==0,
				new CTPenetrationHandler(hardnessTier, thickness, integrity, sound));
	}

	@ZenMethod
	public static void addMetal(String name, String hardnessTier, float thickness, float integrity)
	{
		PenetrationRegistry.registerMetalMaterial(PenetrationHandlerMetal.create(
				name,
				IIUtils.enumValue(PenetrationHardness.class, hardnessTier),
				thickness, integrity
		));
	}

	private static class CTPenetrationHandler extends PenetrationHandler
	{
		public CTPenetrationHandler(String hardness, float integrity, float density, @Nullable String sound)
		{
			//TODO: 05.05.2024 add particles
			super(PenetrationHardness.valueOf(hardness.toUpperCase()), integrity, density, null,
					sound==null?null: SoundEvent.REGISTRY.getObject(new ResourceLocation(sound)),
					sound==null?null: SoundEvent.REGISTRY.getObject(new ResourceLocation(sound)));
		}
	}
}
