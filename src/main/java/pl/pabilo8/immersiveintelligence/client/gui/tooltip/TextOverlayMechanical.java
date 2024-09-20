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
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8
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
				IIItemUtil.isTachometer(player.getHeldItem(EnumHand.MAIN_HAND));
	}

	@ParametersAreNonnullByDefault
	@Nullable
	@Override
	public String[] getText(EntityPlayer player, RayTraceResult mouseOver, @Nullable TileEntity te, @Nullable Entity entityHit)
	{
		assert te!=null;

		//TODO: 03.08.2024 expose capability on all sides
		IRotaryEnergy energy = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mouseOver.sideHit.getOpposite());
		assert energy!=null;

		float intTorque = energy.getTorque();
		float extTorque = energy.getOutputTorque();
		float intSpeed = energy.getRotationSpeed();
		float extSpeed = energy.getOutputRotationSpeed();

		if(intTorque!=extTorque&&intSpeed!=extSpeed)
			return new String[]{
					IIReference.COLOR_PRUSSIAN_BLUE.getHexCol("\u2296 "+I18n.format(IIReference.INFO_KEY+"tachometer.internal_torque", intTorque)),
					IIReference.COLOR_PRUSSIAN_BLUE.getHexCol("\u29c1 "+I18n.format(IIReference.INFO_KEY+"tachometer.internal_speed", intSpeed)),
					IIReference.CHARICON_TORQUE+I18n.format(IIReference.INFO_KEY+"tachometer.external_torque", extTorque),
					IIReference.CHARICON_SPEED+I18n.format(IIReference.INFO_KEY+"tachometer.external_speed", extSpeed)
			};
		else
			return new String[]{
					IIReference.CHARICON_TORQUE+I18n.format(IIReference.INFO_KEY+"tachometer.torque", intTorque),
					IIReference.CHARICON_SPEED+I18n.format(IIReference.INFO_KEY+"tachometer.speed", intSpeed)
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
