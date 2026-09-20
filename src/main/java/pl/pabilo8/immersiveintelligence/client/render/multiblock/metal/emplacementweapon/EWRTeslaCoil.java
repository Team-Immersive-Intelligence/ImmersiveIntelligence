package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBlendModeGroup;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.TeslaCoil;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponTeslaCoil;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRTeslaCoil extends EmplacementWeaponRenderer<EmplacementWeaponTeslaCoil>
{
	private IIAnimationCachedMap charge, chargeLoop;

	public EWRTeslaCoil()
	{
		super("tesla");
	}

	@Override
	public void reloadModels()
	{
		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_TESLA,
						MODEL_DIR.with("tesla_preview").withExtension(ResLoc.EXT_OBJ));
	}

	@Nonnull
	@Override
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(
				super.provideModel(header, style, upgrades),
				new AMTModel(
						new AMTBlendModeGroup("blend", header)
				)
		);
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.charge = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("charge"));
		this.chargeLoop = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("charge_loop"));
	}

	@Override
	public void apply(EmplacementWeaponTeslaCoil weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		float chargeProgress = AMTUtils.getAnimationProgress(weapon.chargeTicks, TeslaCoil.chargeTime,
				true, weapon.targetlessTicks >= 0, 1f, 1f, partialTicks);
		this.charge.apply(chargeProgress);
		if(chargeProgress >= 1f)
			this.chargeLoop.apply(AMTUtils.getDebugProgress(3f, partialTicks));
	}
}
