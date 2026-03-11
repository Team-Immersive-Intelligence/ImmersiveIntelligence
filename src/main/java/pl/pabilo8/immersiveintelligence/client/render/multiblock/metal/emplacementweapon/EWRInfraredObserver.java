package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponInfraredObserver;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.02.2026
 */
@SideOnly(Side.CLIENT)
public class EWRInfraredObserver extends EmplacementWeaponRenderer<EmplacementWeaponInfraredObserver>
{
	private IIAnimationCachedMap install, uninstall, rotatePitch;

	public EWRInfraredObserver()
	{
		super("infrared_observer");
	}

	@Override
	public void reloadModels()
	{
		super.reloadModels();
		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER, MODEL_DIR.with("infrared_observer_preview.obj"));
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.install = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("install"));
		this.uninstall = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("uninstall"));

		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
	}

	@Override
	public void apply(EmplacementWeaponInfraredObserver weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		assert weapon.setup!=null;
		if(weapon.setup.getState())
			this.uninstall.apply(weapon.setup.getProgress(partialTicks));
		else
			this.install.apply(weapon.setup.getProgress(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
	}
}
