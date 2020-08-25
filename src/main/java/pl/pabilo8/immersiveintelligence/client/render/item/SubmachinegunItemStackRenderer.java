package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler.SpecialSkin;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.weapon.ModelSubmachinegun;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtNamedBoxGroup;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 13-10-2019
 */
public class SubmachinegunItemStackRenderer extends TileEntityItemStackRenderer
{
	public static SubmachinegunItemStackRenderer instance = new SubmachinegunItemStackRenderer();
	public static final String texture = ImmersiveIntelligence.MODID+":textures/items/weapons/submachinegun.png";
	public static ModelSubmachinegun model = new ModelSubmachinegun();

	public static HashMap<Predicate<ItemStack>, BiConsumer<ItemStack, List<TmtNamedBoxGroup>>> upgrades = new HashMap<>();
	public static List<TmtNamedBoxGroup> defaultGunParts = new ArrayList<>();
	public static List<TmtNamedBoxGroup> skinParts = new ArrayList<>();

	static
	{
		defaultGunParts.add(model.baseBox);
		defaultGunParts.add(model.barrelBox);
		defaultGunParts.add(model.sightsBox);
		defaultGunParts.add(model.triggerBox);
		defaultGunParts.add(model.ammoBox);
		defaultGunParts.add(model.slideBox);
		defaultGunParts.add(model.gripBox);
		defaultGunParts.add(model.stockBox);
		defaultGunParts.add(model.casingEjectionBox);

		//skinParts.add(model.baubleBox);
	}


	@Override
	public void renderByItem(ItemStack stack, float partialTicks)
	{
		GlStateManager.pushMatrix();

		RenderHelper.enableStandardItemLighting();

		List<TmtNamedBoxGroup> renderParts = new ArrayList<>(defaultGunParts);
		String skin = CommonProxy.item_submachinegun.getSkinnableCurrentSkin(stack);
		boolean drawText = true;

		if(!skin.isEmpty())
		{
			SpecialSkin s = CustomSkinHandler.specialSkins.get(skin);
			if(s!=null)
			{
				if(s.mods.contains("skin_mg_text"))
					drawText = true;
				skinParts.forEach(tmtNamedBoxGroup -> {
					if(s.mods.contains(tmtNamedBoxGroup.getName()))
						renderParts.add(tmtNamedBoxGroup);
				});
			}

		}

		for(Entry<Predicate<ItemStack>, BiConsumer<ItemStack, List<TmtNamedBoxGroup>>> s : upgrades.entrySet())
		{
			if(s.getKey()!=null&&s.getValue()!=null&&s.getKey().test(stack))
				s.getValue().accept(stack, renderParts);
		}

		for(TmtNamedBoxGroup nmod : renderParts)
		{
			ClientUtils.bindTexture(nmod.getTexturePath());
			nmod.render(0.0625f);
		}

		GlStateManager.popMatrix();
	}
}
