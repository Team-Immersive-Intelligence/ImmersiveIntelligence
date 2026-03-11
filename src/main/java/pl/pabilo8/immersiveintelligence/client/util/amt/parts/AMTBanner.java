package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
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
	private final AMTModel bannerModel;
	private IIAnimationCompiledMap animation;

	private boolean isFlag = true;

	public AMTBanner(String name, AMTModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	public AMTBanner(String name, Vec3d originPos)
	{
		super(name, originPos);

		//Create banner model
		AMTQuads[] parts = new AMTQuads[4];
		for(int i = 0; i < 4; i++)
		{
			Vec3d basePos = new Vec3d(0, -10*i, 0);
			//Create banner/flag segment
			parts[i] = new AMTQuadsBuilder(null).withBox(
					basePos.addVector(-10, -10, -0.5), basePos.addVector(10, 0, 0.5),
					new Vec2f(0, i*10), new Vec2f(20, (i+1)*10), 64, 64
			).build("banner"+i, new Vec3d(0, -i*10*0.0625f, 0));
			//Add it as a child of the previous segment, so they rotate together
			if(i > 0)
				parts[i-1].withChildren(parts[i]);
		}
		this.bannerModel = new AMTModel(parts[0]);

		//Load banner waving animation
		this.animation = IIAnimationCompiledMap.create(this.bannerModel, IIReference.RES_II.with("banner_wave"));
	}

	public AMTBanner setBanner(@Nonnull ItemStack banner)
	{
		if(banner.isItemEqual(this.banner))
			return this;
		this.banner = banner;
		if(banner.isEmpty())
		{
			this.bannerRes = null;
			return this;
		}
		this.virtualTile.setItemValues(this.banner, false);
		this.bannerRes = BannerTextures.BANNER_DESIGNS.getResourceLocation(
				virtualTile.getPatternResourceLocation(), virtualTile.getPatternList(), virtualTile.getColorList());
		return this;
	}

	public AMTBanner setIsFlag(boolean isFlag)
	{
		this.isFlag = isFlag;
		return this;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(bannerRes==null||bannerModel==null||animation==null)
			return;

		ClientUtils.mc().getTextureManager().bindTexture(bannerRes);
		GlStateManager.translate(originPos.x, originPos.y, originPos.z);
		//Flags are banners, just sideways
		if(isFlag)
			GlStateManager.rotate(90, 0, 0, 1);

		//Render banner model
		animation.apply(property);
		bannerModel.render(tes, buf);
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
