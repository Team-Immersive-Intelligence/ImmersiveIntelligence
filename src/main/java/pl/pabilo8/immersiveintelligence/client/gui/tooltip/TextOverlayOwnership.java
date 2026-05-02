package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.10.2022
 */
public class TextOverlayOwnership extends TextOverlayBase
{
	IIColor lastColor = IIColor.WHITE;

	@ParametersAreNonnullByDefault
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		return mouseOver.typeOfHit==Type.BLOCK&&te instanceof IOwnableProperty;
	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		IOwnableProperty property = IIUtils.requireMaster(((IOwnableProperty)te), IOwnableProperty::master);
		if(property==null)
			return null;

		OwnerIdentity owner = property.getOwnerIdentity();
		owner.getRelationTowards(player);
		this.lastColor = owner.getColor();

		Block block = te.getBlockType();
		ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(player.getEntityWorld().getBlockState(te.getPos())));

		return new String[]{
				stack.getDisplayName(),
				//Abandoned Property
				owner==DiplomacyHandler.NEUTRAL?I18n.format("desc.immersiveintelligence.diplomacy.noowner"):
						//Owned by Player
						I18n.format("desc.immersiveintelligence.diplomacy.ownedby", owner.getDisplayName())
		};
	}

	@Override
	public IIColor getDefaultFontColor()
	{
		return this.lastColor;
	}

	@Nonnull
	@Override
	public FontRenderer getFontRenderer()
	{
		return IIClientUtils.fontRegular;
	}
}
