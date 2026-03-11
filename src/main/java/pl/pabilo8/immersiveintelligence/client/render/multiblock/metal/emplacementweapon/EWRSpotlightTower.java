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
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponSpotlightTower;

@SideOnly(Side.CLIENT)
public class EWRSpotlightTower extends EmplacementWeaponRenderer<EmplacementWeaponSpotlightTower>
{
	private IIAnimationCachedMap install, uninstall, rotateYaw, rotatePitch, fire;

	public EWRSpotlightTower()
	{
		super("spotlight_tower");
	}

	@Override
	public void reloadModels()
	{
		UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
				.withUpgradeModelLocation(IIContent.UPGRADE_EMPLACEMENT_SPOTLIGHT_TOWER, MODEL_DIR.with("spotlight_tower_preview.obj"));
	}

	@Override
	public void loadAnimations(AMTCachedModel<TileEntityEmplacement> model)
	{
		this.install = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("install"));
		this.uninstall = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("uninstall"));
		this.rotateYaw = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_yaw"));
		this.rotatePitch = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("rotate_pitch"));
		this.fire = IIAnimationCachedMap.create(model, ANIMATIONS_DIR.with("fire"));
	}

	@Override
	public void apply(EmplacementWeaponSpotlightTower weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		assert weapon.setup!=null;
		if(weapon.setup.getState())
			this.uninstall.apply(weapon.setup.getProgress(partialTicks));
		else
			this.install.apply(weapon.setup.getProgress(partialTicks));

		this.rotateYaw.apply(weapon.aim.getYawNormalized(partialTicks));
		this.rotatePitch.apply(weapon.aim.getPitchNormalized(partialTicks));
		this.fire.apply(weapon.shootDelay);
	}
}
