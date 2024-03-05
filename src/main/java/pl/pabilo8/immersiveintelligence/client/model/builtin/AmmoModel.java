package pl.pabilo8.immersiveintelligence.client.model.builtin;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoGrenade;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMine;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.HashMap;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 19.02.2024
 */
@SideOnly(Side.CLIENT)
public class AmmoModel<T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>> implements IReloadableModelContainer<AmmoModel<T, E>>, IAmmoModel<T, E>
{
	//--- Constants ---//
	public static final ResLoc RES_ITEM_MODEL = ResLoc.of(IIReference.RES_ITEM_MODEL, "ammo/");

	//--- Local ---//
	private final ResLoc modelLocation;
	/**
	 * Casing models, generally casings shouldn't change
	 */
	private AMT modelCasing, modelCasingFilling, modelCasingSimple;
	/**
	 * Core models, baked and assigned by material
	 */
	private final HashMap<EnumCoreTypes, HashMap<IAmmoCore, AMTQuads>> modelCore = new HashMap<>();
	/**
	 * Core models, baked and assigned by material
	 */
	private final HashMap<Integer, AMTQuads> modelPaint = new HashMap<>();
	/**
	 * Size of the square (for example on conveyor) this ammo piece should take
	 */
	private int spaceRequired;

	IIAnimationCachedMap casingFilling;

	private AmmoModel(ResLoc modelLocation)
	{
		this.modelLocation = modelLocation;
	}

	//--- Model Creation Methods ---//

	public static <T extends IAmmoType<T, E>, E extends EntityAmmoProjectile> AmmoModel<T, E> createProjectileModel(T ammo)
	{
		//Create model
		String name = ammo.getName().toLowerCase();
		AmmoModel<T, E> model = new AmmoModel<>(ResLoc.of(RES_ITEM_MODEL, name).withExtension(ResLoc.EXT_OBJ));
		model.reloadModels();
		return model.subscribeToList("ammo_"+name);
	}

	public static <T extends IAmmoType<T, E>, E extends EntityAmmoGrenade> AmmoModel<T, E> createGrenadeModel(T ammo)
	{
		//Create model
		String name = ammo.getName().toLowerCase();
		AmmoModel<T, E> model = new AmmoModel<>(ResLoc.of(RES_ITEM_MODEL, name).withExtension(ResLoc.EXT_OBJ));
		model.reloadModels();
		return model.subscribeToList("ammo_"+name);
	}

	public static <T extends IAmmoType<T, E>, E extends EntityAmmoMine> AmmoModel<T, E> createExplosivesModel(T ammo)
	{
		//Create model
		String name = ammo.getName().toLowerCase();
		AmmoModel<T, E> model = new AmmoModel<>(ResLoc.of(RES_ITEM_MODEL, name).withExtension(ResLoc.EXT_OBJ));
		model.reloadModels();
		return model.subscribeToList("ammo_"+name);
	}

	//--- Rendering Methods ---//

	/**
	 * @param progress    how much is the casing filled with gunpowder
	 * @param paintColour in rgbInt format, -1 if unpainted
	 */
	@Override
	public void renderCasing(float progress, int paintColour)
	{

	}

	/**
	 * @param coreMaterial of the ammo, see {@link IAmmoCore}
	 * @param coreType     of the ammo, see {@link IAmmoType#getAllowedCoreTypes()}
	 */
	public void renderCore(IAmmoCore coreMaterial, EnumCoreTypes coreType)
	{

	}

	/**
	 * @param used
	 * @param paintColour  in rgbInt format
	 * @param coreMaterial of the ammo, see {@link IAmmoCore}
	 * @param coreType     of the ammo, see {@link IAmmoType#getAllowedCoreTypes()}
	 */
	public void renderAmmoComplete(boolean used, int paintColour, IAmmoCore coreMaterial, EnumCoreTypes coreType)
	{

	}

	//--- Model Loading ---//
	@Override
	public void reloadModels()
	{
		//Cleanup old models
		modelCore.clear();
		modelPaint.clear();
		IIAnimationUtils.disposeOf(new AMT[]{modelCasing, modelCasingFilling, modelCasingSimple});

		//Load new models
		AMT[] amt = IIAnimationUtils.getAMTFromRes(modelLocation, modelLocation.withExtension(ResLoc.EXT_OBJAMT));


		modelCasing = IIAnimationUtils.getPart(amt, "casing");
		modelCasingFilling = IIAnimationUtils.getPart(amt, "casing_filling");
		modelCasingSimple = IIAnimationUtils.getPart(amt, "casing_simple");

	}

	@Override
	public void registerSprites(TextureMap map)
	{
		IIAnimationLoader.preloadTexturesFromMTL(RES_ITEM_MODEL.withExtension(ResLoc.EXT_MTL), map);
	}
}
