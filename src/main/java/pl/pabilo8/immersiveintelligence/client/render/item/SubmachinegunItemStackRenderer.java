package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Submachinegun;
import pl.pabilo8.immersiveintelligence.client.model.weapon.ModelSubmachinegun;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtNamedBoxGroup;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.IISpecialSkin;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

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
public class SubmachinegunItemStackRenderer extends TileEntityItemStackRenderer implements IReloadableModelContainer<SubmachinegunItemStackRenderer>, ISpecificHandRenderer
{
	public static SubmachinegunItemStackRenderer instance = new SubmachinegunItemStackRenderer().subscribeToList("submachinegun");
	public static final String texture = "submachinegun.png";
	public static ModelSubmachinegun model = new ModelSubmachinegun();

	public static HashMap<Predicate<ItemStack>, BiConsumer<ItemStack, List<TmtNamedBoxGroup>>> upgrades = new HashMap<>();
	public static List<TmtNamedBoxGroup> defaultGunParts = new ArrayList<>();
	public static List<TmtNamedBoxGroup> skinParts = new ArrayList<>();

	static
	{
		addDefaultModelParts();

		//skinParts.add(model.baubleBox);
	}

	private static void addDefaultModelParts()
	{
		defaultGunParts.clear();
		defaultGunParts.add(model.baseBox);
		defaultGunParts.add(model.loaderBox);
		defaultGunParts.add(model.barrelBox);
		defaultGunParts.add(model.sightsBox);
		defaultGunParts.add(model.triggerBox);
		defaultGunParts.add(model.ammoBox);
		defaultGunParts.add(model.slideBox);
		defaultGunParts.add(model.gripBox);
		defaultGunParts.add(model.stockBox);
		defaultGunParts.add(model.casingEjectionBox);
	}


	@Override
	public void renderByItem(ItemStack stack, float partialTicks)
	{
		GlStateManager.pushMatrix();

		List<TmtNamedBoxGroup> renderParts = new ArrayList<>(defaultGunParts);
		String skin = IIContent.itemSubmachinegun.getSkinnableCurrentSkin(stack);
		boolean drawText = true, canApply = false;

		if(!skin.isEmpty())
		{
			IISpecialSkin s = IISkinHandler.specialSkins.get(skin);
			if(s!=null)
			{
				ItemIIGunBase gun = (ItemIIGunBase)stack.getItem();
				canApply = s.doesApply(gun.getSkinnableName());
				if(s.mods.contains("skin_mg_text"))
				{
				}
				skinParts.forEach(tmtNamedBoxGroup -> {
					if(s.mods.contains(tmtNamedBoxGroup.getName()))
						renderParts.add(tmtNamedBoxGroup);
				});
			}
		}
		//specialText = I18n.format("skin.immersiveintelligence."+skin+".name");
		skin = ((skin.isEmpty()&&!canApply)?IIContent.itemSubmachinegun.getSkinnableDefaultTextureLocation(): IIReference.SKIN_LOCATION+skin+"/");

		for(Entry<Predicate<ItemStack>, BiConsumer<ItemStack, List<TmtNamedBoxGroup>>> s : upgrades.entrySet())
		{
			if(s.getKey()!=null&&s.getValue()!=null&&s.getKey().test(stack))
				s.getValue().accept(stack, renderParts);
		}

		for(TmtNamedBoxGroup nmod : renderParts)
		{
			switch(nmod.getName())
			{
				case "ammo":
				case "bottomLoader":
				{
					GlStateManager.pushMatrix();
					int reloading = ItemNBTHelper.getInt(stack, "reloading");
					ItemStack magazine = ItemNBTHelper.getItemStack(stack, "magazine");

					int maxReload = (ItemNBTHelper.getBoolean(stack, "isDrum")?Submachinegun.drumReloadTime: Submachinegun.clipReloadTime);
					float reload = MathHelper.clamp(
							reloading+(reloading > 0?partialTicks: 0),
							0,
							maxReload
					);
					reload /= maxReload;
					float clipReload;

					if(reloading==0)
					{
						if(IIContent.itemSubmachinegun.getUpgrades(stack).hasKey("bottom_loading"))
						{
							ClientUtils.bindTexture(skin+nmod.getTexturePath());
							nmod.render(0.0625f);
							if(!magazine.isEmpty())
								(magazine.getMetadata()==1?model.magBottomBox: model.magDrumBox).render(0.0625f);
						}
						else if(!magazine.isEmpty())
						{
							ClientUtils.bindTexture(skin+nmod.getTexturePath());
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
						ClientUtils.bindTexture(skin+nmod.getTexturePath());
						if(IIContent.itemSubmachinegun.getUpgrades(stack).hasKey("bottom_loading"))
						{
							nmod.render(0.0625f);
							GlStateManager.translate(0, -clipReload, 0);
							GlStateManager.color(1f, 1f, 1f, 1f-Math.min(clipReload/0.5f, 1f));
							(ItemNBTHelper.getBoolean(stack, "isDrum")?model.magDrumBox: model.magBottomBox).render(0.0625f);
						}
						else
						{
							GlStateManager.translate(0.65f*clipReload, 0, 0);
							GlStateManager.color(1f, 1f, 1f, 1f-Math.min(clipReload/0.5f, 1f));
							nmod.render(0.0625f);
						}

						GlStateManager.disableBlend();
						GlStateManager.color(1f, 1f, 1f, 1f);

					}
					GlStateManager.popMatrix();
				}
				break;
				case "foldingStock":
				{
					int aiming = ItemNBTHelper.getInt(stack, "aiming");
					double preciseAim = MathHelper.clamp(
							aiming+(aiming > 0?(Minecraft.getMinecraft().player.isSneaking()?partialTicks: -3*partialTicks): 0),
							0,
							Submachinegun.aimTimeFoldedStock
					);

					GlStateManager.pushMatrix();

					GlStateManager.translate(2.5/16f, 0, 0.0625f);
					GlStateManager.rotate(175*(float)(1d-(preciseAim/Submachinegun.aimTimeFoldedStock)), 0, 1, 0);
					GlStateManager.translate(-2.5/16f, 0, -0.0625f);

					ClientUtils.bindTexture(skin+nmod.getTexturePath());
					nmod.render(0.0625f);
					GlStateManager.popMatrix();
				}
				break;
				case "slide":
				{
					float delay = Math.max(ItemNBTHelper.getInt(stack, "fireDelay")-partialTicks, 0);
					float movement = 1f-(Math.abs(delay/(float)Submachinegun.bulletFireTime-0.5f)/0.5f);
					GlStateManager.pushMatrix();
					GlStateManager.translate(0, 0, 0.5f*movement);
					ClientUtils.bindTexture(skin+nmod.getTexturePath());
					nmod.render(0.0625f);
					GlStateManager.popMatrix();
				}
				break;
				default:
				{
					ClientUtils.bindTexture(skin+nmod.getTexturePath());
					nmod.render(0.0625f);

				}
				break;
			}
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelSubmachinegun();
		addDefaultModelParts();
	}

	@Override
	public boolean doHandRender(ItemStack stack, EnumHand hand, ItemStack otherHand, float swingProgress, float partialTicks)
	{
		int aiming = ItemNBTHelper.getInt(stack, "aiming");
		int reloading = ItemNBTHelper.getInt(stack, "reloading");
		int maxReload = ItemNBTHelper.getBoolean(stack, "isDrum")?Submachinegun.drumReloadTime: Submachinegun.clipReloadTime;
		float reload = MathHelper.clamp(
				reloading+(reloading > 0?partialTicks: 0),
				0,
				maxReload
		);
		reload /= maxReload;

		boolean foldingStock = IIContent.itemSubmachinegun.getUpgrades(stack).hasKey("folding_stock");
		float preciseAim = MathHelper.clamp(
				aiming+(aiming > 0?Minecraft.getMinecraft().player.isSneaking()?partialTicks: -3*partialTicks: 0),
				0,
				foldingStock?Submachinegun.aimTimeFoldedStock: Submachinegun.aimTime
		);
		preciseAim /= foldingStock?Submachinegun.aimTimeFoldedStock: Submachinegun.aimTime;

		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.translate(11.5/16f, -11/16f, -1.25+2/16f);
		GlStateManager.rotate(2f, 1, 0, 0);
		GlStateManager.rotate(8.5f, 0, 1, 0);

		if(swingProgress > 0)
			GlStateManager.translate(0, 0, -0.5f*(1f-Math.abs((swingProgress-0.5f)/0.5f)));

		if(reloading > 0)
		{
			float rpart = reload <= 0.33?reload/0.33f: reload <= 0.66?1f: 1f-(reload-0.66f)/0.33f;
			GlStateManager.rotate(rpart*90f, 0, 1, 0);
			GlStateManager.rotate(rpart*85f, 0, 0, 1);
		}
		if(preciseAim > 0)
		{
			GlStateManager.translate(-preciseAim*0.725, 0.2*preciseAim, 0);
			GlStateManager.rotate(preciseAim*-8.5f, 0, 1, 0);
			GlStateManager.rotate(preciseAim*-2.25f, 1, 0, 0);

		}
		SubmachinegunItemStackRenderer.instance.renderByItem(stack, partialTicks);
		GlStateManager.popMatrix();

		return true;
	}

	@Override
	public boolean renderCrosshair(ItemStack stack, EnumHand hand)
	{
		return ItemNBTHelper.getInt(stack, "aiming") > Submachinegun.aimTime*0.75;
	}
}
