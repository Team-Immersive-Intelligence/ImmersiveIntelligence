package pl.pabilo8.immersiveintelligence.client.model.builtin;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMissile;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 15.03.2024
 */
public class ModelAmmoMissile<T extends IAmmoType<T, E>, E extends EntityAmmoMissile> extends ModelAmmo<T, E>
{
	protected AMT modelJet;

	protected ModelAmmoMissile(T ammo, ResLoc modelLocation)
	{
		super(ammo, modelLocation);
	}

	/**
	 * @param ammo Ammo Type
	 * @param <T>  Ammo Type
	 * @param <E>  Ammo Entity
	 * @return Reloadable AMT model container for a missile
	 */
	public static <T extends IAmmoType<T, E>, E extends EntityAmmoMissile> ModelAmmoMissile<T, E> createMissileModel(T ammo)
	{
		//Create model
		String name = ammo.getName().toLowerCase();
		ModelAmmoMissile<T, E> model = new ModelAmmoMissile<>(ammo, ResLoc.of(RES_ITEM_MODEL, name).withExtension(ResLoc.EXT_OBJ));
		model.reloadModels();
		model.subscribeToList("ammo/missile/"+name);
		return model;
	}

	@Override
	public void renderAmmoComplete(boolean used, IIColor paintColor, AmmoCore coreMaterial, CoreType coreType)
	{
		if(!loaded)
			return;
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		modelCasingSimple.render(tes, buf);
		modelCoreSimple.get(coreType).get(coreMaterial).render(tes, buf);

		if(used)
		{
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, DestFactor.ONE);
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
			modelJet.render(tes, buf);
			modelJet.render(tes, buf);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		}
	}

	@Override
	public void renderAmmoComplete(E entity, float partialTicks)
	{
		//--- air drag animation ---//

		//max drag depends on missile caliber
		double maxDrag = ammo.getCaliber()*0.015625;

		//max horizontal and vertical progress times differ to look more natural

		double progressHorizontal = Math.sin((IIAnimationUtils.getAnimationProgress(entity.ticksExisted%7, 7, false, partialTicks)-0.5)*2*Math.PI);
		double progressVertical = Math.cos((IIAnimationUtils.getAnimationProgress(entity.ticksExisted%7, 7, false, partialTicks)-0.5)*2*Math.PI);
		GlStateManager.translate(progressHorizontal*maxDrag, 0f, progressVertical*maxDrag);

		super.renderAmmoComplete(entity, partialTicks);
	}

	@Override
	protected void loadModels(AMT[] amt)
	{
		super.loadModels(amt);
		modelJet = IIAnimationUtils.getPart(amt, "jet_flame");
	}
}
