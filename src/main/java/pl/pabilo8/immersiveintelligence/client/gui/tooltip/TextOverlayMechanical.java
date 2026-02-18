package pl.pabilo8.immersiveintelligence.client.gui.tooltip;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.10.2022
 */
public class TextOverlayMechanical extends TextOverlayBase
{
	@ParametersAreNonnullByDefault
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		if(mouseOver.typeOfHit!=Type.BLOCK)
			return false;

		return te!=null&&te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mouseOver.sideHit.getOpposite())&&
				IIItemUtils.isTachometer(player.getHeldItem(EnumHand.MAIN_HAND));
	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		assert te!=null;
		IRotaryEnergy energy = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mouseOver.sideHit.getOpposite());
		assert energy!=null;

		float intTorque = energy.getTorque();
		float extTorque = energy.getOutputTorque();
		float intSpeed = energy.getRotationSpeed();
		float extSpeed = energy.getOutputRotationSpeed();

		if(intTorque!=extTorque&&intSpeed!=extSpeed)
			return new String[]{
					IIReference.COLOR_ENGINEERS_BLUE.getHexCol(IIReference.CHARICON_TORQUE+" "+I18n.format(IIReference.INFO_KEY_TORQUE_IN, intTorque)),
					IIReference.COLOR_ENGINEERS_BLUE.getHexCol(IIReference.CHARICON_SPEED+" "+I18n.format(IIReference.INFO_KEY_SPEED_IN, intSpeed)),
					IIReference.COLOR_ENGINEERS_BLUE.getHexCol(IIReference.CHARICON_TORQUE+" "+I18n.format(IIReference.INFO_KEY_TORQUE_OUT, extTorque)),
					IIReference.COLOR_ENGINEERS_BLUE.getHexCol(IIReference.CHARICON_SPEED+" "+I18n.format(IIReference.INFO_KEY_SPEED_OUT, extSpeed)),
			};
		else
			return new String[]{
					IIReference.CHARICON_TORQUE+" "+I18n.format(IIReference.INFO_KEY_TORQUE_BOTH, intTorque, extTorque),
					IIReference.CHARICON_SPEED+" "+I18n.format(IIReference.INFO_KEY_SPEED_BOTH, intSpeed, intSpeed)
			};
	}

	@Override
	public IIColor getDefaultFontColor()
	{
		return IIReference.COLOR_IMMERSIVE_ORANGE;
	}

	@Nonnull
	@Override
	public FontRenderer getFontRenderer()
	{
		return IIClientUtils.fontRegular;
	}
}
