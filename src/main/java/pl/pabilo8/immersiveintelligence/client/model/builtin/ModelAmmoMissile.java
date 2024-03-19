package pl.pabilo8.immersiveintelligence.client.model.builtin;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMissile;

/**
 * @author Pabilo8
 * @since 15.03.2024
 */
public class ModelAmmoMissile<T extends IAmmoType<T, E>, E extends EntityAmmoMissile> extends ModelAmmo<T, E>
{
	protected AMT modelJet;

	protected ModelAmmoMissile(T ammo, ResLoc modelLocation)
	{
		super(ammo, modelLocation);
	}

	public static <T extends IAmmoType<T, E>, E extends EntityAmmoMissile> ModelAmmoMissile<T, E> createMissileModel(T ammo)
	{
		//Create model
		String name = ammo.getName().toLowerCase();
		ModelAmmoMissile<T, E> model = new ModelAmmoMissile<>(ammo, ResLoc.of(RES_ITEM_MODEL, name).withExtension(ResLoc.EXT_OBJ));
		model.reloadModels();
		model.subscribeToList("ammo_"+name);
		return model;
	}

	@Override
	public void renderAmmoComplete(boolean used, int paintColour, AmmoCore coreMaterial, EnumCoreTypes coreType)
	{
		if(!loaded)
			return;
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		modelCasingSimple.render(tes, buf);
		modelCoreSimple.get(coreType).get(coreMaterial).render(tes, buf);

		//TODO: 19.03.2024 emmisive rendering
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		if(used)
			modelJet.render(tes, buf);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	}

	@Override
	protected void loadModels(AMT[] amt)
	{
		super.loadModels(amt);
		modelJet = IIAnimationUtils.getPart(amt, "jet_flame");
	}
}
