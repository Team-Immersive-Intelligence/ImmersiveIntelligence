package pl.pabilo8.immersiveintelligence.client.model.builtin;

import blusunrize.immersiveengineering.api.ApiUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.MaterialLibrary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLocator;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTQuads;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMine;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 19.02.2024
 */
@SideOnly(Side.CLIENT)
public class ModelAmmo<T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>> implements IReloadableModelContainer<ModelAmmo<T, E>>, IAmmoModel<T, E>
{
	//--- Constants ---//
	public static final ResLoc RES_ITEM_MODEL = ResLoc.of(IIReference.RES_ITEM_MODEL, "ammo/");

	//--- Local ---//
	protected boolean loaded = false;
	protected final T ammo;
	private final ResLoc modelLocation;
	/**
	 * Casing models, generally casings shouldn't change
	 */
	protected AMT modelCasing, modelCasingSimple, modelPaintBase;
	/**
	 * Core models, baked and assigned by material
	 */
	protected final EnumMap<CoreType, HashMap<AmmoCore, AMT>> modelCore = new EnumMap<>(CoreType.class);
	protected final EnumMap<CoreType, HashMap<AmmoCore, AMT>> modelCoreSimple = new EnumMap<>(CoreType.class);
	/**
	 * Core models, baked and assigned by material
	 */
	protected final HashMap<IIColor, AMT> modelPaint = new HashMap<>();


	protected ModelAmmo(T ammo, ResLoc modelLocation)
	{
		this.ammo = ammo;
		this.modelLocation = modelLocation;
	}

	//--- Model Creation Methods ---//

	/**
	 * @param ammo Ammo Type
	 * @param <T>  Ammo Type
	 * @param <E>  Ammo Entity
	 * @return Reloadable AMT model container for mine or explosive
	 */
	public static <T extends IAmmoType<T, E>, E extends EntityAmmoMine> ModelAmmo<T, E> createExplosivesModel(T ammo)
	{
		//Create model
		String name = ammo.getName().toLowerCase();
		ModelAmmo<T, E> model = new ModelAmmo<>(ammo, ResLoc.of(RES_ITEM_MODEL, name).withExtension(ResLoc.EXT_OBJ));
		model.reloadModels();
		return model.subscribeToList("ammo/explosives/"+name);
	}

	//--- Rendering Methods ---//

	/**
	 * @param progress   how much is the casing filled with gunpowder
	 * @param paintColor in rgbInt format, -1 if unpainted
	 */
	@Override
	public void renderCasing(float progress, @Nullable IIColor paintColor)
	{
		if(!loaded)
			return;
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		modelCasing.render(tes, buf);
	}

	/**
	 * @param coreMaterial of the ammo, see {@link AmmoCore}
	 * @param coreType     of the ammo, see {@link IAmmoType#getAllowedCoreTypes()}
	 */
	public void renderCore(AmmoCore coreMaterial, CoreType coreType)
	{
		if(!loaded)
			return;
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		modelCore.get(coreType).get(coreMaterial).render(tes, buf);
	}

	/**
	 * @param used         if the ammo was fired already
	 * @param paintColor   in rgbInt format
	 * @param coreMaterial of the ammo, see {@link AmmoCore}
	 * @param coreType     of the ammo, see {@link IAmmoType#getAllowedCoreTypes()}
	 */
	public void renderAmmoComplete(boolean used, IIColor paintColor, AmmoCore coreMaterial, CoreType coreType)
	{
		if(!loaded)
			return;
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		if(!used)
		{
			modelCasingSimple.render(tes, buf);
			if(paintColor!=null)
				modelPaint.computeIfAbsent(paintColor, color -> ((AMTQuads)modelPaintBase).recolor(color)).render(tes, buf);
		}
		modelCoreSimple.get(coreType).get(coreMaterial).render(tes, buf);
	}

	//--- Model Loading ---//
	@Override
	public void reloadModels()
	{
		//Cleanup old models
		modelCore.clear();
		modelCoreSimple.clear();
		modelPaint.clear();
		IIAnimationUtils.disposeOf(new AMT[]{modelCasing, modelCasingSimple, modelPaintBase});

		//Load new models
		AMT[] amt = IIAnimationUtils.getAMTFromRes(modelLocation, modelLocation.withExtension(ResLoc.EXT_OBJAMT), this::getExtraModelParts);
		//Either a proper model or no model at all
		if(!(loaded = amt.length > 0))
			return;

		loadModels(amt);
	}

	protected AMT[] getExtraModelParts(IIModelHeader header)
	{
		return new AMT[0];
	}

	protected void loadModels(AMT[] amt)
	{
		modelCasing = IIAnimationUtils.getPart(amt, "casing");
		modelCasingSimple = IIAnimationUtils.getPart(amt, "casing_simple");
		if(modelCasingSimple==null)
			modelCasingSimple = modelCasing;

		//Preload all core models
		for(CoreType coreType : ammo.getAllowedCoreTypes())
		{
			HashMap<AmmoCore, AMT> modelMap = new HashMap<>();
			HashMap<AmmoCore, AMT> modelSimpleMap = new HashMap<>();

			AMT coreModel = IIAnimationUtils.getPart(amt, "core_"+coreType.getName());
			if(coreModel==null)
				continue;
			//Simple model variant is optional
			AMTQuads coreSimpleModel = (AMTQuads)IIAnimationUtils.getPart(amt, "core_"+coreType.getName()+"_simple");

			for(AmmoCore core : AmmoRegistry.getAllCores())
			{
				AMT quads;
				if(coreModel instanceof AMTQuads)
					quads = ((AMTQuads)coreModel).recolor(core.getColor());
				else
				{
					quads = new AMTLocator(coreModel.name, Vec3d.ZERO);
					quads.setChildren(
							coreModel.getChildrenRecursive().stream()
									.filter(amt1 -> amt1 instanceof AMTQuads)
									.map(amt1 -> ((AMTQuads)amt1))
									.map(amtQuads -> amtQuads.recolor(core.getColor()))
									.toArray(AMTQuads[]::new)
					);
				}

				modelMap.put(core, quads);
				//If simple variant is not present, use the main model
				modelSimpleMap.put(core, coreSimpleModel==null?quads: coreSimpleModel.recolor(core.getColor()));
			}
			modelCore.put(coreType, modelMap);
			modelCoreSimple.put(coreType, modelSimpleMap);
		}

		//Load a paint model, variants will be assigned dynamically
		modelPaintBase = IIAnimationUtils.getPart(amt, "paint");
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		try
		{
			OBJModel objModel = (OBJModel)OBJLoader.INSTANCE.loadModel(modelLocation);
			MaterialLibrary matLib = objModel.getMatLib();
			matLib.getMaterialNames().forEach(s -> ApiUtils.getRegisterSprite(map, matLib.getMaterial(s).getTexture().getTextureLocation()));
		} catch(Exception ignored) {}
	}
}
