package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoSprite;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Displays a minecraft banner as a flat image inside a Deco GUI.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.04.2026
 */
@ParametersAreNonnullByDefault
public class DecoBannerDisplay extends DecoComponent<DecoBannerDisplay>
{
	private ItemStack bannerStack = ItemStack.EMPTY;
	private String patternID;
	private List<BannerPattern> patternList = new ArrayList<>();
	private List<EnumDyeColor> colorList = new ArrayList<>();
	private ResourceLocation bannerTexture = null;

	private boolean isFlag = true;
	protected int[] padding = new int[]{2, 2, 2, 2};
	private DecoSprite backgroundSprite;
	private int drawX, drawY, drawW, drawH;

	public DecoBannerDisplay(int x, int y)
	{
		super(x, y);
		withSize(40, 20);
	}

	public DecoBannerDisplay withBanner(ItemStack banner)
	{
		this.bannerStack = banner.copy();

		TileEntityBanner virtualBanner = new TileEntityBanner();
		virtualBanner.setItemValues(bannerStack, false);

		this.patternID = virtualBanner.getPatternResourceLocation();
		this.patternList = virtualBanner.getPatternList();
		this.colorList = virtualBanner.getColorList();

		this.initialized = false;
		return this;
	}

	public DecoBannerDisplay withBannerPatterns(IIColor baseColor, List<BannerPattern> patterns, List<IIColor> colors)
	{
		StringBuilder sb = new StringBuilder("b"+baseColor.getDyeColor().getDyeDamage());
		this.patternList = patterns;
		this.colorList = colors.stream().map(IIColor::getDyeColor).collect(Collectors.toList());

		int limit = Math.min(patterns.size(), colors.size());
		for(int i = 0; i < limit; i++)
		{
			BannerPattern pattern = patterns.get(i);
			if(pattern==null)
				continue;
			sb.append(pattern.getHashname()).append(colors.get(i).getDyeColor().getDyeDamage());
		}
		this.patternID = sb.toString();
		this.bannerStack = new ItemStack(Items.BANNER); //ItemBanner.makeBanner(baseColor.getDyeColor(), nbtList);

		this.initialized = false;
		return this;
	}

	/**
	 * Sets the padding between the banner and the component's edges.
	 *
	 * @param padding array of 4 integers representing left, top, right, and bottom padding
	 * @return this
	 */
	public DecoBannerDisplay withPadding(int[] padding)
	{
		this.padding = padding;
		this.initialized = false;
		return this;
	}

	/**
	 * Sets a background texture to be drawn behind the banner
	 *
	 * @param backgroundTexture background texture
	 * @return this
	 * @implNote The texture will be stretched to fit the component's size.
	 */
	public DecoBannerDisplay withBackgroundTexture(DecoSprite backgroundTexture)
	{
		this.backgroundSprite = backgroundTexture;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		int xPadding = padding[0]+padding[2];
		int yPadding = padding[1]+padding[3];
		int availableW = Math.max(1, width-xPadding);
		int availableH = Math.max(1, height-yPadding);

		this.isFlag = availableW > availableH;
		this.bannerTexture = BannerTextures.BANNER_DESIGNS.getResourceLocation(patternID, patternList, colorList);
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		if(backgroundSprite!=null)
		{
			bindAtlas();
			IIDrawUtils.startTexturedColored()
					.drawTexColorRect(x-padding[0], y-padding[1], width, height, IIColor.WHITE, backgroundSprite)
					.finish();
		}

		if(bannerTexture==null)
			return;
		ClientUtils.mc().getTextureManager().bindTexture(bannerTexture);
		GlStateManager.color(1f, 1f, 1f, 1f);
		GL11.glPushMatrix();
		IIDrawUtils.startTextured()
				.setOffset(x, isFlag?(y+height-padding[1]-padding[3]): y)
				.addRotation(isFlag?-90: 0)
				.drawTexRect(0, 0, Math.min(width, height)-(padding[0]+padding[2]), Math.max(width, height)-(padding[1]+padding[3]), 1/64f, 21/64f, 1/64f, 41/64f)
				.finish();
		GlStateManager.popMatrix();
	}

	@Override
	public void cleanup()
	{
		this.bannerTexture = null;
		this.drawX = this.drawY = this.drawW = this.drawH = 0;
	}

	@Nullable
	@Override
	public Object getProvidedIngredient()
	{
		return bannerStack;
	}
}
