package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCrossVariantReference;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeaponGunBase;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.UpgradeEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Renderer (model laoder, animation applier) for an emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @implNote Do not bind to a specific TE/weapon instance.
 * @since 19.02.2026
 */
@SideOnly(Side.CLIENT)
public abstract class EmplacementWeaponRenderer<W extends EmplacementWeapon> implements IReloadableModelContainer<EmplacementWeaponRenderer<W>>
{
	protected static final ResLoc MODEL_DIR = IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/weapon/");
	private final ResLoc MODEL_LOC;
	protected final ResLoc ANIMATIONS_DIR;
	protected final String name;

	public EmplacementWeaponRenderer(String name)
	{
		this.name = name;
		this.MODEL_LOC = MODEL_DIR.with(name).withExtension(ResLoc.EXT_OBJ);
		this.ANIMATIONS_DIR = IIReference.RES_II.with("emplacement/weapon/"+name+"/");
		subscribeToList("emplacement_weapon/"+name);
		//noinspection DataFlowIssue
		EmplacementRenderer.registerWeaponRenderer(((UpgradeEmplacementWeapon<?>)
				Upgrade.getUpgradeByID(IIReference.RES_II.with("emplacement/"+name))).createWeapon().getClass(), this);
	}

	/**
	 * @param header
	 * @param style    current style of the emplacement tile entity
	 * @param upgrades
	 * @return the model to be cached and used for rendering the weapon
	 * @implNote styling is optional, by default the model has no styling applied
	 */
	@Nonnull
	public AMTModel provideModel(AMTModelHeader header, String style, List<Upgrade> upgrades)
	{
		return new AMTModel(DefaultVertexFormats.BLOCK, this.MODEL_LOC);
	}

	public AMTModelHeader provideHeader()
	{
		return AMTLoader.loadHeader(this.MODEL_LOC.withExtension(ResLoc.EXT_OBJAMT));
	}

	public abstract void loadAnimations(AMTCachedModel<TileEntityEmplacement> model);

	@Override
	public void reloadModels()
	{
		Upgrade upgrade = Upgrade.getUpgradeByID(IIReference.RES_II.with("emplacement/"+name));
		if(upgrade!=null)
			UpgradeTechTree.getTreeFor(TileEntityEmplacement.class)
					.withUpgradeModelLocation(upgrade, MODEL_LOC);
	}

	/**
	 * Renders the weapon model with animations applied for a given weapon instance.
	 *
	 * @param weapon       the weapon
	 * @param model
	 * @param buf          buffer builder
	 * @param tes          tessellator
	 * @param partialTicks partial ticks for animation interpolation
	 */
	public abstract void apply(W weapon, AMTCachedModel<TileEntityEmplacement> model, BufferBuilder buf, Tessellator tes, float partialTicks);

	protected void reloadSprites(TextureMap map)
	{
		AMTLoader.preloadTexturesFromOBJ(MODEL_LOC, map);
	}

	@Override
	public final void registerSprites(TextureMap map)
	{
		IReloadableModelContainer.super.registerSprites(map);
		reloadSprites(map);
	}

	protected final void applyAmmoItem(EmplacementWeaponGunBase<?> weapon, BulletState state, AMTCrossVariantReference<AMTBullet> reference)
	{
		List<ItemStack> renderAmmo = weapon.getLoadedAmmo();
		//noinspection SequencedCollectionMethodCanBeUsed
		ItemStack ammoStack = renderAmmo.isEmpty()?ItemStack.EMPTY: renderAmmo.get(0);
		if(!ammoStack.isEmpty())
		{
			AMTBullet amtBullet = reference.get();
			if(amtBullet!=null)
				amtBullet.withStack(ammoStack, state);
		}
	}

	/**
	 * Applies the ammo items to the bullets.
	 *
	 * @param weapon           emplacement weapon
	 * @param state            state the bullets are in
	 * @param bulletReferences model part references
	 */
	protected final void applyAmmoItems(EmplacementWeaponGunBase<?> weapon, BulletState state, List<AMTCrossVariantReference<AMTBullet>> bulletReferences)
	{
		final NonNullList<ItemStack> ammoList = weapon.getLoadedAmmo();
		applyAmmoItems(weapon, ammoList, state, bulletReferences);
	}

	/**
	 * Applies the ammo items to the bullets.
	 *
	 * @param weapon           emplacement weapon
	 * @param state            state the bullets are in
	 * @param bulletReferences model part references
	 */
	protected final void applyAmmoItems(EmplacementWeaponGunBase<?> weapon, final NonNullList<ItemStack> ammoList, BulletState state, List<AMTCrossVariantReference<AMTBullet>> bulletReferences)
	{
		final int size = ammoList.size();
		int slot = 0, bulletIndex = 0;
		for(AMTCrossVariantReference<AMTBullet> bulletReference : bulletReferences)
		{
			ItemStack ammoStack = (ammoList.isEmpty()||slot >= size)?ItemStack.EMPTY: ammoList.get(slot);
			if(bulletIndex+1 >= ammoStack.getCount())
			{
				slot += 1;
				bulletIndex = 0;
			}
			else
				bulletIndex++;

			AMTBullet amtBullet = bulletReference.get();
			if(amtBullet!=null)
			{
				amtBullet.setVisible(true);
				amtBullet.withStack(ammoStack, state);
			}
		}
	}
}
