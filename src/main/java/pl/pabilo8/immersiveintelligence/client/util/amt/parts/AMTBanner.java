package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.10.2023
 */
public class AMTBanner extends AMT
{
	@Nonnull
	private ItemStack banner = ItemStack.EMPTY;
	private final TileEntityBanner virtualTile = new TileEntityBanner();
	private ResourceLocation bannerRes = null;
	private boolean isFlag = true;

	public AMTBanner(String name, AMTModelHeader header)
	{
		super(name, header);
	}

	public AMTBanner(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public void setBanner(@Nonnull ItemStack banner)
	{
		if(banner.isItemEqual(this.banner))
			return;
		this.banner = banner;
		if(banner.isEmpty())
		{
			this.bannerRes = null;
			return;
		}
		this.virtualTile.setItemValues(this.banner, false);
		this.bannerRes = BannerTextures.BANNER_DESIGNS.getResourceLocation(
				virtualTile.getPatternResourceLocation(), virtualTile.getPatternList(), virtualTile.getColorList());
	}

	public AMTBanner setIsFlag(boolean isFlag)
	{
		this.isFlag = isFlag;
		return this;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(bannerRes==null)
			return;
		ClientUtils.mc().getTextureManager().bindTexture(bannerRes);
		GlStateManager.translate(originPos.x, originPos.y, originPos.z);
		GlStateManager.rotate(90, 0, 0, 1);
		drawFlag(3, Math.abs(1f-(property*2)));
	}

	private void drawFlag(int n, double rot)
	{
		float length = 40/(float)n;
		for(int i = 0; i < n; i++)
		{
			GlStateManager.rotate((float)(rot*6.5f*n*(n%2==0?1: -1)), 1, 0, 0);
			ClientUtils.drawTexturedRect(0f, 0f, 1f, length/21f, 0.015625f, 21/64f, 0.015625f+(i*length/64f), (length*(i+1))/64f);
			GlStateManager.translate(0f, 0f, 0.0625f);
			ClientUtils.drawTexturedRect(0f, length/21f, 1f, -length/21f, 0.015625f, 21/64f, 0.015625f+((i+1)*length/64f), (length*i)/64f);
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90, 0, 1, 0);
			ClientUtils.drawTexturedRect(0f, 0f, 0.0625f, length/21f, 0.015625f, 0.015625f, 0.015625f+(i*length/64f), (length*(i+1))/64f);
			GlStateManager.translate(0f, 0f, 1f);
			ClientUtils.drawTexturedRect(0f, length/21f, 0.0625f, -length/21f, 0.328125f, 0.328125f, 0.015625f+((i+1)*length/64f), (length*i)/64f);

			GlStateManager.popMatrix();
			GlStateManager.translate(0f, length/24f, -0.0625f);
		}
		//GlStateManager.rotate(90,0,0,1);
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.translate(0f, 0f, -0.0625f);
		ClientUtils.drawTexturedRect(0f, 0f, 1f, 1.5f/21f, 0.015625f, 0.015625f, 0.625f, 0.625f);

	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetItemStack("banner", this::setBanner);
	}

	@Override
	public void disposeOf()
	{

	}
}
