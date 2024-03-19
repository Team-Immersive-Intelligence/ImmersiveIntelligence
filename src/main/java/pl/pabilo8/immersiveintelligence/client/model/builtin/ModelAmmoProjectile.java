package pl.pabilo8.immersiveintelligence.client.model.builtin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 15.03.2024
 */
public class ModelAmmoProjectile<T extends IAmmoType<T, E>, E extends EntityAmmoProjectile> extends ModelAmmo<T, E>
{
	protected AMT modelCasingFilling;
	/**
	 * Propellant filling animation
	 */
	private IIAnimationCompiledMap casingFilling;
	/**
	 * Sabot discarding, sub-munitions discarding, fin stabilization opening animations
	 */
	private HashMap<AmmoCore, IIAnimationCompiledMap> sabotDiscarding, clusterDiscarding, shapedFins;

	protected ModelAmmoProjectile(T ammo, ResLoc modelLocation)
	{
		super(ammo, modelLocation);
	}

	public static <T extends IAmmoType<T, E>, E extends EntityAmmoProjectile> ModelAmmoProjectile<T, E> createProjectileModel(T ammo)
	{
		//Create model
		String name = ammo.getName().toLowerCase();
		ModelAmmoProjectile<T, E> model = new ModelAmmoProjectile<>(ammo, ResLoc.of(RES_ITEM_MODEL, name).withExtension(ResLoc.EXT_OBJ));
		model.reloadModels();
		model.subscribeToList("ammo_"+name);
		return model;
	}

	@Override
	public void reloadModels()
	{
		IIAnimationUtils.disposeOf(new AMT[]{modelCasingFilling});
		super.reloadModels();
	}

	@Override
	protected void loadModels(AMT[] amt)
	{
		super.loadModels(amt);
		modelCasingFilling = IIAnimationUtils.getPart(amt, "casing_filling");
		IIAnimationCompiledMap.create(amt, ResLoc.of(IIReference.RES_II, "ammo/"+this.ammo.getName()+"/filling"));
		this.sabotDiscarding = new HashMap<>();
		this.clusterDiscarding = new HashMap<>();
		this.shapedFins = new HashMap<>();

		//check if the ammo type allows a sabot, cluster or shaped_sabot core is allowed
		for(EnumCoreTypes coreType : EnumCoreTypes.values())
		{
			//if so, load the animation for all core materials
			switch(coreType)
			{
				case PIERCING_SABOT:
					loadSpecialCoreAnimation(EnumCoreTypes.PIERCING_SABOT, this.sabotDiscarding, amt, "piercing_sabot");
					break;
				case SHAPED_SABOT:
					loadSpecialCoreAnimation(EnumCoreTypes.SHAPED_SABOT, this.shapedFins, amt, "shaped_sabot");
					break;
				case CLUSTER:
					loadSpecialCoreAnimation(EnumCoreTypes.CLUSTER, this.clusterDiscarding, amt, "cluster");
					break;
				default:
					break;
			}
		}

		//casing filling animation
		casingFilling = IIAnimationCompiledMap.create(amt, ResLoc.of(IIReference.RES_II, "ammo/"+this.ammo.getName()+"/filling"));
	}

	private void loadSpecialCoreAnimation(EnumCoreTypes piercingSabot, HashMap<AmmoCore, IIAnimationCompiledMap> sabotDiscarding, AMT[] amt, String x)
	{
		//check if the model has the core type
		HashMap<AmmoCore, AMT> coreMap = modelCore.get(piercingSabot);
		if(coreMap==null||coreMap.isEmpty())
			return;

		//load the unbaked animation
		IIAnimation unbaked = IIAnimationLoader.loadAnimation(ResLoc.of(IIReference.RES_II, "ammo/"+this.ammo.getName()+"/"+x));

		//apply the animation to the core types
		for(AmmoCore coreTypes : coreMap.keySet())
			sabotDiscarding.put(coreTypes, IIAnimationCompiledMap.create(amt, unbaked));
	}

	@Override
	public void renderAmmoComplete(E entity, float partialTicks)
	{
		float rotationProgress = IIAnimationUtils.getDebugProgress(entity.world, (float)(20*MathHelper.fastInvSqrt(EntityAmmoProjectile.SLOWMO)), partialTicks);
		GlStateManager.rotate(rotationProgress*360, 0, 1, 0);
		super.renderAmmoComplete(entity, partialTicks);
	}
}
