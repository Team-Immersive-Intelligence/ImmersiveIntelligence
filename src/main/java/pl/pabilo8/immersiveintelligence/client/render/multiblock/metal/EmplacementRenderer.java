package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 06.09.2025
 */
@RegisteredTileRenderer(name = "multiblock/emplacement", clazz = TileEntityEmplacement.class)
public class EmplacementRenderer extends IIMultiblockRenderer<TileEntityEmplacement>
{
	private AMTCachedModel<TileEntityEmplacement> model;
	private IIAnimationCachedMap animationOpen;

	@Override
	public void drawAnimated(TileEntityEmplacement te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Apply mirroring and style customization (variant)
		applyStandardMirroring(te, true);
		model.getVariant(te, te.style);
		//Apply platform and door animation
		animationOpen.apply(te.door.getProgress(partialTicks));
		//Render the model
		model.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = AMTCachedModelBuilder.startTileEntityModel(TileEntityEmplacement.class)
				.withModel(model)
				.withHeader(IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/emplacement.obj.amt"))
				//Style Variants
				.withModel(te -> te==null||te.style.getStyle().equals("sandbags"),
						IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/variant_sandbags.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("bricks"),
						IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/variant_bricks.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("concrete"),
						IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/variant_concrete.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("wooden"),
						IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/variant_wooden.obj"))
				.withModel(te -> te!=null&&te.style.getStyle().equals("steel"),
						IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/variant_steel.obj"))
				.withModel(IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/weapon/light_howitzer.obj"))
				.withHeader(IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/weapon/light_howitzer.obj.amt"))
				.build();

		this.animationOpen = IIAnimationCachedMap.create(this.model, IIReference.RES_II.with("emplacement/open"));
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		super.registerSprites(map);
		for(Upgrade upgrade : Upgrade.getAllUpgrades())
		{
//			if(upgrade instanceof UpgradeEmplacementWeapon)

		}

		AMTLoader.preloadTexturesFromOBJ(IIReference.RES_BLOCK_MODEL.with("multiblock/emplacement/weapon/light_howitzer.obj"), map);
	}
}
