package pl.pabilo8.immersiveintelligence.client.model.builtin;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
	public void renderCasing(float progress, int paintColour)
	{
		super.renderCasing(progress, paintColour);
		if(progress==0||casingFilling==null)
			return;

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		casingFilling.apply(progress);
		modelCasingFilling.render(tes, buf);
	}

	@Override
	public void reloadModels()
	{
		IIAnimationUtils.disposeOf(new AMT[]{modelCasingFilling});
		super.reloadModels();
	}

	@Override
	protected AMT[] getExtraModelParts(IIModelHeader header)
	{
		ArrayList<AMT> extraParts = new ArrayList<>();
		for(CoreTypes coreType : ammo.getAllowedCoreTypes())
			switch(coreType)
			{
				case PIERCING_SABOT:
					extraParts.add(new AMTLocator("core_piercing_sabot", header));
					break;
				case SHAPED_SABOT:
					extraParts.add(new AMTLocator("core_shaped_sabot", header));
					break;
				case CLUSTER:
					extraParts.add(new AMTLocator("core_cluster", header));
					break;
				default:
					break;
			}

		return extraParts.toArray(new AMT[0]);
	}

	@Override
	protected void loadModels(AMT[] amt)
	{
		super.loadModels(amt);

		//load propellant filling animation
		modelCasingFilling = IIAnimationUtils.getPart(amt, "casing_filling");
		IIAnimationCompiledMap.create(amt, ResLoc.of(IIReference.RES_II, "ammo/"+this.ammo.getName()+"/filling"));

		//load special core animations
		this.sabotDiscarding = new HashMap<>();
		this.clusterDiscarding = new HashMap<>();
		this.shapedFins = new HashMap<>();

		//check if the ammo type allows a sabot, cluster or shaped_sabot core is allowed
		for(CoreTypes coreType : ammo.getAllowedCoreTypes())
		{
			//if so, load the animation for all core materials
			switch(coreType)
			{
				case PIERCING_SABOT:
					loadSpecialCoreAnimation(CoreTypes.PIERCING_SABOT, this.sabotDiscarding, "sabot");
					break;
				case SHAPED_SABOT:
					loadSpecialCoreAnimation(CoreTypes.SHAPED_SABOT, this.shapedFins, "shaped_sabot");
					break;
				case CLUSTER:
					loadSpecialCoreAnimation(CoreTypes.CLUSTER, this.clusterDiscarding, "cluster");
					break;
				default:
					break;
			}
		}
	}

	private void loadSpecialCoreAnimation(CoreTypes coreType, HashMap<AmmoCore, IIAnimationCompiledMap> animationMap, String animationName)
	{
		//check if the model has the core type
		HashMap<AmmoCore, AMT> coreMap = modelCore.get(coreType);
		if(coreMap==null||coreMap.isEmpty())
			return;

		//load the unbaked animation
		IIAnimation unbaked = IIAnimationLoader.loadAnimation(ResLoc.of(IIReference.RES_II, "ammo/"+this.ammo.getName()+"/"+animationName));

		//apply the animation to the core types
		for(Entry<AmmoCore, AMT> entry : coreMap.entrySet())
			animationMap.put(entry.getKey(), IIAnimationCompiledMap.create(new AMT[]{entry.getValue()}, unbaked));
	}

	@Override
	public void renderAmmoComplete(E entity, float partialTicks)
	{
		if(!loaded)
			return;

		//rotation animation for rifled guns
		float rotationProgress = IIAnimationUtils.getDebugProgress(entity.world, (float)(20*MathHelper.fastInvSqrt(EntityAmmoProjectile.SLOWMO)), partialTicks);
		GlStateManager.rotate(rotationProgress*360, 0, 1, 0);

		//special core animations
		switch(entity.getCoreType())
		{
			case PIERCING_SABOT:
			case SHAPED_SABOT:
			case CLUSTER:
			{
				float progress = IIAnimationUtils.getAnimationOffsetProgress(entity.ticksExisted, 10, 30, partialTicks);
				switch(entity.getCoreType())
				{
					case PIERCING_SABOT:
						sabotDiscarding.get(entity.getCore()).apply(progress);
						break;
					case SHAPED_SABOT:
						shapedFins.get(entity.getCore()).apply(progress);
						break;
					case CLUSTER:
						clusterDiscarding.get(entity.getCore()).apply(progress);
						break;
				}
				Tessellator tes = Tessellator.getInstance();
				BufferBuilder buf = tes.getBuffer();
				AMT amt = modelCore.get(entity.getCoreType()).get(entity.getCore());
				amt.render(tes, buf);
				amt.defaultize();
				return;
			}
			default:
				break;
		}

		//default rendering
		super.renderAmmoComplete(entity, partialTicks);
	}
}
