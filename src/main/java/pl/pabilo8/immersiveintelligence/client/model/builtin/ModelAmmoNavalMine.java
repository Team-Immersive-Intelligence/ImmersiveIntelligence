package pl.pabilo8.immersiveintelligence.client.model.builtin;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine.EntityNavalMine;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;

public class ModelAmmoNavalMine<T extends IAmmoType<T, E>, E extends EntityNavalMine> extends ModelAmmo<T, E>
{
	private AMT modelLid;

	protected ModelAmmoNavalMine(T ammo, ResLoc modelLocation)
	{
		super(ammo, modelLocation);
	}

	/**
	 * @param ammo Ammo Type
	 * @param <T>  Ammo Type
	 * @param <E>  Ammo Entity
	 * @return Reloadable AMT model container for a naval mine
	 */
	public static <T extends IAmmoType<T, E>, E extends EntityNavalMine> ModelAmmoNavalMine<T, E> createNavalMineModel(T ammo)
	{
		//Create model
		String name = ammo.getName().toLowerCase();
		ModelAmmoNavalMine<T, E> model = new ModelAmmoNavalMine<>(ammo, ResLoc.of(RES_ITEM_MODEL, name).withExtension(ResLoc.EXT_OBJ));
		model.reloadModels();
		model.subscribeToList("ammo/explosives/"+name);
		return model;
	}

	@Override
	public void reloadModels()
	{
		IIAnimationUtils.disposeOf(modelLid);
		super.reloadModels();
	}

	@Override
	protected void loadModels(AMT[] amt)
	{
		super.loadModels(amt);
		modelLid = IIAnimationUtils.getPart(amt, "casing_lid");
	}

	@Override
	public void renderAmmoComplete(boolean used, IIColor paintColor, @Nullable AmmoCore coreMaterial, @Nullable CoreType coreType)
	{
		if(!loaded)
			return;
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		modelCasingSimple.render(tes, buf);
	}

	@Override
	public void renderAmmoComplete(boolean used, ItemStack stack)
	{
		if(stack.isEmpty())
			return;
		renderAmmoComplete(false, ammo.getPaintColor(stack), null, null);
	}
}
