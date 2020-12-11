package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Submachinegun;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler.SpecialSkin;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.weapon.ModelSubmachinegun;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtNamedBoxGroup;
import pl.pabilo8.immersiveintelligence.common.IIContent;

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

		List<TmtNamedBoxGroup> renderParts = new ArrayList<>(defaultGunParts);
		String skin = IIContent.item_submachinegun.getSkinnableCurrentSkin(stack);
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
			if(nmod.getName().equals("ammo"))
			{
				GlStateManager.pushMatrix();
				int reloading = ItemNBTHelper.getInt(stack, "reloading");
				ItemStack magazine = ItemNBTHelper.getItemStack(stack, "magazine");

				float reload = MathHelper.clamp(
						reloading+(reloading > 0?partialTicks: 0),
						0,
						Submachinegun.clipReloadTime
				);
				reload /= Submachinegun.clipReloadTime;
				float clipReload = magazine.isEmpty()?1f: 0f;

				if(reloading==0)
				{
					if(!magazine.isEmpty())
					{
						ClientUtils.bindTexture(nmod.getTexturePath());
						nmod.render(0.0625f);
					}
				}
				else
				{
					GlStateManager.enableBlend();
					if(magazine.isEmpty())
					{
						if(reload <= 0.33)
							clipReload = 1f;
						else if(reload <= 0.66)
							clipReload = 1f-((reload-0.33f)/0.33f);
						else
							clipReload = 0f;
					}
					else
					{
						if(reload <= 0.33)
							clipReload = 0f;
						else if(reload <= 0.66)
							clipReload = (reload-0.33f)/0.33f;
						else
							clipReload = 1f;

					}
					GlStateManager.color(1f, 1f, 1f, 1f-Math.min(clipReload/0.5f, 1f));
					ClientUtils.bindTexture(nmod.getTexturePath());
					GlStateManager.translate(0.65f*clipReload, 0, 0);
					nmod.render(0.0625f);
					GlStateManager.disableBlend();
					GlStateManager.color(1f, 1f, 1f, 1f);

				}

				GlStateManager.popMatrix();
			}
			else if(nmod.getName().equals("slide"))
			{
				float delay = Math.max(ItemNBTHelper.getInt(stack, "fireDelay")-partialTicks, 0);
				float movement = 1f-(Math.abs(delay/(float)Submachinegun.bulletFireTime-0.5f)/0.5f);
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, 0, 0.5f*movement);
				ClientUtils.bindTexture(nmod.getTexturePath());
				nmod.render(0.0625f);
				GlStateManager.popMatrix();
			}
			else
			{
				ClientUtils.bindTexture(nmod.getTexturePath());
				nmod.render(0.0625f);
			}
		}

		GlStateManager.popMatrix();
	}
}
