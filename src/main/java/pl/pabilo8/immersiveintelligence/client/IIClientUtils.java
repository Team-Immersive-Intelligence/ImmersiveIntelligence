package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.*;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.client.model.ModelIIBase;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRendererCustomGlyphs;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIMetalBase.Metals;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 29.08.2022
 */
@SideOnly(Side.CLIENT)
public class IIClientUtils
{
	@SideOnly(Side.CLIENT)
	public static IIFontRenderer fontRegular;
	@SideOnly(Side.CLIENT)
	public static IIFontRendererCustomGlyphs fontEngineerTimes, fontNormung, fontKaiser, fontTinkerer;

	@SideOnly(Side.CLIENT)
	public static void drawStringCentered(FontRenderer fontRenderer, String string, int x, int y, int w, int h, int colour)
	{
		fontRenderer.drawString(string, x+(w/2)-(fontRenderer.getStringWidth(string)/2), y+h, colour);
	}

	@SideOnly(Side.CLIENT)
	public static void drawStringCenteredScaled(FontRenderer fontRenderer, String string, int x, int y, int w, int h, float scale, int colour)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+((w/2)-(fontRenderer.getStringWidth(string)*scale/2)), y+h, 0);
		GlStateManager.scale(scale, scale, 1);
		fontRenderer.drawString(string, 0, 0, colour);
		GlStateManager.popMatrix();
	}

	//Cheers, Blu ^^
	@SideOnly(Side.CLIENT)
	public static void drawBlockBreak(WorldClient world, float partialTicks, DamageBlockPos... positions)
	{
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		TextureManager tex = Minecraft.getMinecraft().renderEngine;
		EntityPlayer player = ClientUtils.mc().player;

		//get rendering centre position
		double posX = player.lastTickPosX+(player.posX-player.lastTickPosX)*(double)partialTicks;
		double posY = player.lastTickPosY+(player.posY-player.lastTickPosY)*(double)partialTicks;
		double posZ = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*(double)partialTicks;

		tex.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(774, 768, 1, 1);
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
		GlStateManager.doPolygonOffset(-3.0F, -3.0F);
		GlStateManager.enablePolygonOffset();


		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		buf.setTranslation(-posX, -posY, -posZ);
		buf.noColor();

		for(DamageBlockPos pos : positions)
		{
			IBlockState state = world.getBlockState(pos);
			int progress = 9-(int)MathHelper.clamp(pos.damage*10f, 0f, 10f); // 0-10
			if(progress < 0||state.getMaterial()==Material.AIR)
				continue;

			Block block = state.getBlock();
			TileEntity te = world.getTileEntity(pos);
			boolean shouldOmit = block instanceof BlockIIMultiblock<?>;
			if(!shouldOmit)
				shouldOmit = te!=null&&te.canRenderBreaking();

			if(shouldOmit)
				continue;

			brd.renderBlockDamage(state, pos, ClientUtils.destroyBlockIcons[progress], world);
		}


		tes.draw();
		buf.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.disableAlpha();
		GlStateManager.doPolygonOffset(0.0F, 0.0F);
		GlStateManager.disablePolygonOffset();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_COLOR, GL11.GL_DST_COLOR, 1, 1);
		GlStateManager.enableAlpha();
		GlStateManager.color(1f, 1f, 1f, 1f);

		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

	//Thanks Blu, these stencil buffers look really capable
	public static void drawArmorBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, IIReference.COLOR_ARMORBAR_1, IIReference.COLOR_ARMORBAR_2, progress);
	}

	public static void drawPowerBar(int x, int y, int w, int h, float progress)
	{
		drawGradientBar(x, y, w, h, IIReference.COLOR_POWERBAR_1, IIReference.COLOR_POWERBAR_2, progress);
	}

	public static void drawGradientBar(int x, int y, int w, int h, int color1, int color2, float progress)
	{
		int stored = (int)(h*progress);
		ClientUtils.drawGradientRect(x, y+(h-stored), x+w, y+h, color1, color2);
	}

	@SideOnly(Side.CLIENT)
	public static void bindTexture(ResourceLocation path)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(path);
	}

	@SideOnly(Side.CLIENT)
	public static void drawRope(BufferBuilder buff, double x, double y, double z, double xx, double yy, double zz, double xdiff, double zdiff)
	{
		buff.pos(x+xdiff, y, z-zdiff).tex(0f, 0f).endVertex();
		buff.pos(xx+xdiff, yy, zz-zdiff).tex(0f, 1f).endVertex();
		buff.pos(xx-xdiff, yy, zz+zdiff).tex(0.125f, 1f).endVertex();
		buff.pos(x-xdiff, y, z+zdiff).tex(0.125f, 0f).endVertex();
	}

	@SideOnly(Side.CLIENT)
	public static void drawFace(BufferBuilder buff, double x, double y, double z, double xx, double yy, double zz, double u, double uu, double v, double vv)
	{
		buff.pos(x, y, z).tex(u, v).endVertex();
		buff.pos(x, yy, z).tex(u, vv).endVertex();
		buff.pos(xx, yy, zz).tex(uu, vv).endVertex();
		buff.pos(xx, y, zz).tex(uu, v).endVertex();
	}

	@SideOnly(Side.CLIENT)
	public static void drawFluidBlock(@Nonnull FluidStack fs, boolean flowing, double x, double y, double z, double xx, double yy, double zz, boolean drawEnd)
	{
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		String tex = (flowing?fs.getFluid().getFlowing(fs): fs.getFluid().getStill(fs)).toString();
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tex);

		if(sprite!=null)
		{
			int col = fs.getFluid().getColor(fs);
			double u = sprite.getMinU(), uu = sprite.getMaxU(), v = sprite.getMinV(), vv = v+((sprite.getMaxV()-v)*Math.min(1, (yy-y)/sprite.getIconHeight()));
			double ux = u+((uu-u)*Math.min(1, (xx-x)/sprite.getIconWidth())), uz = u+(uu*Math.min(1, (zz-z)/sprite.getIconWidth()));

			GlStateManager.color((col>>16&255)/255.0f, (col>>8&255)/255.0f, (col&255)/255.0f, 1);

			BufferBuilder buff = Tessellator.getInstance().getBuffer();

			buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			drawFace(buff, x, yy, z, xx, y, z, u, ux, v, vv);
			drawFace(buff, x, yy, zz, xx, y, zz, u, ux, v, vv);
			drawFace(buff, x, yy, z, x, y, zz, u, ux, v, vv);
			drawFace(buff, xx, yy, z, xx, y, zz, u, ux, v, vv);

			Tessellator.getInstance().draw();

			GlStateManager.color(1f, 1f, 1f);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void drawItemProgress(ItemStack stack1, ItemStack stack2, float progress, TransformType type, Tessellator tessellator, float scale)
	{
		GlStateManager.scale(scale, scale, scale);

		if(progress==0)
			ClientUtils.mc().getRenderItem().renderItem(stack1, type);
		else if(progress==1)
			ClientUtils.mc().getRenderItem().renderItem(stack2, type);
		else
		{
			float h0 = -.5f;
			float h1 = h0+progress;

			BufferBuilder worldrenderer = tessellator.getBuffer();

			GL11.glEnable(GL11.GL_STENCIL_TEST);

			GlStateManager.colorMask(false, false, false, false);
			GlStateManager.depthMask(false);

			GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
			GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);

			GL11.glStencilMask(0xFF);
			GlStateManager.clear(GL11.GL_STENCIL_BUFFER_BIT);

			GlStateManager.rotate(90.0F-ClientUtils.mc().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

			GlStateManager.disableTexture2D();
			worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			ClientUtils.renderBox(worldrenderer, -.5, h0, -.5, .5, h1, .5);
			tessellator.draw();
			GlStateManager.enableTexture2D();

			GlStateManager.rotate(-(90.0F-ClientUtils.mc().getRenderManager().playerViewY), 0.0F, 1.0F, 0.0F);

			GlStateManager.colorMask(true, true, true, true);
			GlStateManager.depthMask(true);

			GL11.glStencilMask(0x00);

			GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF);
			ClientUtils.mc().getRenderItem().renderItem(stack1, type);

			GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
			ClientUtils.mc().getRenderItem().renderItem(stack2, type);

			GL11.glDisable(GL11.GL_STENCIL_TEST);
		}

		GlStateManager.scale(1/scale, 1/scale, 1/scale);
	}

	@SideOnly(Side.CLIENT)
	public static ModelRendererTurbo[] createConstructionModel(@Nullable MachineUpgrade upgrade, ModelIIBase model)
	{
		int partCount = model.parts.values().stream().mapToInt(modelRendererTurbos -> modelRendererTurbos.length).sum();
		if(upgrade!=null)
			upgrade.setRequiredSteps(partCount);
		ModelRendererTurbo[] output = new ModelRendererTurbo[partCount];
		int i = 0;
		for(ModelRendererTurbo[] value : model.parts.values())
		{
			Arrays.sort(value, (o1, o2) -> (int)(o1.rotationPointY-o2.rotationPointY));
			for(ModelRendererTurbo mod : value)
			{
				output[i] = mod;
				i++;
			}
		}

		return output;
	}

	@SideOnly(Side.CLIENT)
	public static void createAmmoTooltip(IAmmo ammo, ItemStack stack, @Nullable World worldIn, List<String> tooltip)
	{
		tooltip.add(getFormattedBulletTypeName(ammo, stack));
		if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT, "%s - Composition", tooltip))
		{
			//get parameters
			EnumFuseTypes fuse = ammo.getFuseType(stack);
			IAmmoCore core = ammo.getCore(stack);
			EnumCoreTypes coreType = ammo.getCoreType(stack);
			IAmmoComponent[] components = ammo.getComponents(stack);

			//list general information
			tooltip.add(IIUtils.getHexCol(IIReference.COLORS_HIGHLIGHT_S[1], "Details:"));

			//core + type
			if(ammo.isProjectile())
			{
				tooltip.add("⦳ "+I18n.format(IIReference.DESCRIPTION_KEY+"bullets.core",
						I18n.format(IIReference.DESCRIPTION_KEY+"bullet_core_type."+coreType.getName()),
						IIUtils.getHexCol(core.getColour(), I18n.format("item."+ImmersiveIntelligence.MODID+".bullet.component."+core.getName()+".name"))
				));

				//fuse
				tooltip.add(fuse.symbol+" "+I18n.format(IIReference.DESCRIPTION_KEY+"bullets.fuse",
						I18n.format(IIReference.DESCRIPTION_KEY+"bullet_fuse."+fuse.getName())
				));
			}
			else
			{
				tooltip.add("⦳ "+I18n.format(IIReference.DESCRIPTION_KEY+"bullets.core", "",
						IIUtils.getHexCol(core.getColour(), I18n.format("item."+ImmersiveIntelligence.MODID+".bullet.component."+core.getName()+".name"))
				));
			}

			//mass
			tooltip.add("\u2696 "+I18n.format(IIReference.DESCRIPTION_KEY+"bullets.mass", Utils.formatDouble(ammo.getMass(stack), "0.##")));

			//list components
			if(components.length > 0)
			{
				tooltip.add(IIUtils.getHexCol(IIReference.COLORS_HIGHLIGHT_S[1], "Components:"));
				for(IAmmoComponent comp : components)
					tooltip.add("   "+comp.getTranslatedName());
			}
		}

		if(ammo.isProjectile()&&!ammo.isBulletCore(stack)&&ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, "%s - Ballistics", tooltip))
		{
			tooltip.add(IIUtils.getHexCol(IIReference.COLORS_HIGHLIGHT_S[0], "Performance:"));
			tooltip.add(String.format("\u2295 "+"Damage Dealt: %s", ammo.getDamage()));
			tooltip.add(String.format("\u29c1 "+"Standard Velocity: %s B/s", ammo.getDefaultVelocity()));

			tooltip.add(IIUtils.getHexCol(IIReference.COLORS_HIGHLIGHT_S[0], "Armor Penetration:"));

			float hardness = ammo.getCore(stack).getPenetrationHardness();
			EnumCoreTypes coreType = ammo.getCoreType(stack);

			listPenetratedAmount(tooltip, ammo, hardness, coreType, Blocks.GLASS, 0);
			listPenetratedAmount(tooltip, ammo, hardness, coreType, Blocks.LOG, 0);
			listPenetratedAmount(tooltip, ammo, hardness, coreType, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE_TILE.getMeta());
			listPenetratedAmount(tooltip, ammo, hardness, coreType, IEContent.blockStorage, BlockTypes_MetalsIE.STEEL.getMeta());
			listPenetratedAmount(tooltip, ammo, hardness, coreType, IIContent.blockMetalStorage, Metals.TUNGSTEN.getMeta());
		}
	}

	private static void listPenetratedAmount(List<String> tooltip, IAmmo ammo, float penetrationHardness, EnumCoreTypes coreType, Block block, int meta)
	{
		int penetratedAmount = getPenetratedAmount(ammo, penetrationHardness, coreType, block, meta);
		String displayName = new ItemStack(block, 1, meta).getDisplayName();

		if(penetratedAmount < 1)
			tooltip.add(TextFormatting.RED+"✕ "+displayName);
		else
			tooltip.add(TextFormatting.DARK_GREEN+String.format("⦴ %s: %d B", displayName, penetratedAmount));

	}

	private static int getPenetratedAmount(IAmmo ammo, float penetrationHardness, EnumCoreTypes coreType, Block block, int meta)
	{
		IPenetrationHandler penHandler = PenetrationRegistry.getPenetrationHandler(block.getStateFromMeta(meta));
		double realDrag = 1d-(EntityBullet.DRAG*EntityBullet.DEV_SLOMO);
		float density = penHandler.getDensity(), hardness = block.blockHardness, force = 1;
		int count = 0, speed = (int)(ammo.getDefaultVelocity());

		while(force > 0.1)
		{
			float pen = penetrationHardness*coreType.getPenMod(penHandler.getPenetrationType());
			if(pen > hardness/density)
				count++;
			else
				return count;

			penetrationHardness -= ((hardness*16f)/pen);
			if(count%speed==0)
			{
				force *= realDrag;
				force *= 0.85;
			}

			/*
			if(((hardness*1.5f)/pen)==1)
				return 64;
			 */
		}

		return count;
	}

	private static String getFormattedBulletTypeName(IAmmo ammo, ItemStack stack)
	{
		Set<EnumComponentRole> collect = new HashSet<>();
		if(ammo.getCoreType(stack).getRole()!=null)
			collect.add(ammo.getCoreType(stack).getRole());
		collect.addAll(Arrays.stream(ammo.getComponents(stack)).map(IAmmoComponent::getRole).collect(Collectors.toSet()));
		StringBuilder builder = new StringBuilder();
		for(EnumComponentRole enumComponentRole : collect)
		{
			if(enumComponentRole==EnumComponentRole.GENERAL_PURPOSE)
				continue;
			builder.append(IIUtils.getHexCol(enumComponentRole.getColor(), I18n.format(IIReference.DESCRIPTION_KEY+"bullet_type."+enumComponentRole.getName())));
			builder.append(" - ");
		}
		if(builder.toString().isEmpty())
		{
			builder.append(I18n.format(IIReference.DESCRIPTION_KEY+"bullet_type."+EnumComponentRole.GENERAL_PURPOSE.getName()));
			builder.append(" - ");
		}
		//trim last " - "
		builder.delete(builder.length()-3, builder.length());
		if(stack.hasDisplayName())
			builder.append(" ").append(TextFormatting.GRAY).append(stack.getItem().getItemStackDisplayName(stack));
		return builder.toString();
	}

}
