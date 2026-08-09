package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler.PlayerInfo;

import javax.annotation.Nullable;
import java.util.Collections;

/**
 * Displays the face of a player or a vanilla mob that has a skull item.
 * Unsupported entities use a black fallback image.
 *
 * @Author Pabilo8(pabilo.iiteam.net)
 * @Since 06.08.2026
 */
public class DecoPlayerDisplay extends DecoComponent<DecoPlayerDisplay>
{
	private static final ResourceLocation CREEPER_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper.png");
	private static final ResourceLocation SKELETON_TEXTURE = new ResourceLocation("textures/entity/skeleton/skeleton.png");
	private static final ResourceLocation WITHER_SKELETON_TEXTURE = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
	private static final ResourceLocation ZOMBIE_TEXTURE = new ResourceLocation("textures/entity/zombie/zombie.png");

	private ResourceLocation imageLocation = DecoTextures.TEXTURE_WHITE;
	private boolean usesBlockAtlas = true;
	private boolean drawPlayerOverlay;
	private int textureWidth = 16;
	private int textureHeight = 16;
	private int u;
	private int v;
	private int faceWidth = 16;
	private int faceHeight = 16;
	private IIColor color = IIColor.BLACK;
	private String displayedName = "";

	/**
	 * Creates an empty player display.
	 *
	 * @param x x position
	 * @param y y position
	 */
	public DecoPlayerDisplay(int x, int y)
	{
		super(x, y);
		withSize(16, 16);
		withOnTooltip(component -> displayedName.isEmpty()?Collections.emptyList(): Collections.singletonList(displayedName));
	}

	/**
	 * Uses a player skin from diplomacy player information.
	 *
	 * @param playerInfo player information
	 * @return this display
	 */
	public DecoPlayerDisplay withPlayerInfo(@Nullable PlayerInfo playerInfo)
	{
		if(playerInfo==null)
			return useFallback("");

		displayedName = playerInfo.getName()==null?"": playerInfo.getName();
		try
		{
			return usePlayerSkin(playerInfo.getSkin());
		} catch(RuntimeException ignored)
		{
			return useFallback(displayedName);
		}
	}

	/**
	 * Uses the skin or skull texture that matches an entity.
	 *
	 * @param entity entity to display
	 * @return this display
	 */
	public DecoPlayerDisplay withEntity(@Nullable Entity entity)
	{
		if(entity==null)
			return useFallback("");

		displayedName = entity.getName()==null?"": entity.getName();
		if(entity instanceof AbstractClientPlayer)
			return usePlayerSkin(((AbstractClientPlayer)entity).getLocationSkin());
		if(entity instanceof EntityPlayer)
		{
			PlayerInfo playerInfo = DiplomacyHandler.getInstance(true).getPlayerInfo(entity.getUniqueID());
			try
			{
				return usePlayerSkin(playerInfo.getSkin());
			} catch(RuntimeException ignored)
			{
				return useFallback(displayedName);
			}
		}
		if(entity instanceof EntityCreeper)
			return useMobTexture(CREEPER_TEXTURE, 64, 32);
		if(entity instanceof EntityWitherSkeleton)
			return useMobTexture(WITHER_SKELETON_TEXTURE, 64, 32);
		if(entity instanceof AbstractSkeleton)
			return useMobTexture(SKELETON_TEXTURE, 64, 32);
		if(entity instanceof EntityZombie)
			return useMobTexture(ZOMBIE_TEXTURE, 64, 64);
		return useFallback(displayedName);
	}

	private DecoPlayerDisplay usePlayerSkin(@Nullable ResourceLocation skin)
	{
		if(skin==null)
			return useFallback(displayedName);
		this.imageLocation = skin;
		this.usesBlockAtlas = false;
		this.drawPlayerOverlay = true;
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.u = 8;
		this.v = 8;
		this.faceWidth = 8;
		this.faceHeight = 8;
		this.color = IIColor.WHITE;
		return this;
	}

	private DecoPlayerDisplay useMobTexture(ResourceLocation texture, int textureWidth, int textureHeight)
	{
		this.imageLocation = texture;
		this.usesBlockAtlas = false;
		this.drawPlayerOverlay = false;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.u = 8;
		this.v = 8;
		this.faceWidth = 8;
		this.faceHeight = 8;
		this.color = IIColor.WHITE;
		return this;
	}

	private DecoPlayerDisplay useFallback(String name)
	{
		this.displayedName = name==null?"": name;
		this.imageLocation = DecoTextures.TEXTURE_WHITE;
		this.usesBlockAtlas = true;
		this.drawPlayerOverlay = false;
		this.textureWidth = 16;
		this.textureHeight = 16;
		this.u = 0;
		this.v = 0;
		this.faceWidth = 16;
		this.faceHeight = 16;
		this.color = IIColor.BLACK;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		return imageLocation!=null;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.enableBlend();
		drawFace(u, v, faceWidth, faceHeight, color);
		if(drawPlayerOverlay)
			drawFace(40, 8, 8, 8, IIColor.WHITE);
	}

	private void drawFace(int sourceX, int sourceY, int sourceWidth, int sourceHeight, IIColor tint)
	{
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		if(usesBlockAtlas)
		{
			ClientUtils.bindAtlas();
			TextureAtlasSprite sprite = ClientUtils.getSprite(imageLocation);
			draw.drawTexColorRect(x, y, width, height, tint,
					sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
		}
		else
		{
			IIClientUtils.bindTexture(imageLocation);
			draw.drawTexColorRect(x, y, width, height, tint,
					sourceX/(float)textureWidth, (sourceX+sourceWidth)/(float)textureWidth,
					sourceY/(float)textureHeight, (sourceY+sourceHeight)/(float)textureHeight);
		}
		draw.finish();
	}

	@Override
	public void cleanup()
	{

	}
}
