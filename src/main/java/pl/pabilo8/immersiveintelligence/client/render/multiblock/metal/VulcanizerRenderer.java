package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTParticle;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVulcanizer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Renders the Vulcanizer with alternating mould cycles and recipe model variants.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.10.2026
 * @ii-approved 0.3.1
 * @since 21.06.2019
 */
@RegisteredTileRenderer(name = "multiblock/vulcanizer", clazz = TileEntityVulcanizer.class)
public class VulcanizerRenderer extends IIMultiblockRenderer<TileEntityVulcanizer>
{
	private AMTCachedModel<TileEntityVulcanizer> model;
	private final IIAnimation[] work = new IIAnimation[2], loading = new IIAnimation[2];
	private final Map<AMTModel, IIAnimationCompiledMap[]> animations = new IdentityHashMap<>();

	@Override
	public void drawAnimated(TileEntityVulcanizer te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		AMTModel variant = model.getVariant(te, getRecipeModel(te));
		IIAnimationCompiledMap[] maps = animations.computeIfAbsent(variant, this::compileAnimations);

		//Defaultize
		variant.defaultize();
		if(te.processQueue.isEmpty())
			maps[te.nextWork2?1: 0].apply(0);
		else
		{
			//The oldest cycle controls unloading and the shared turntable.
			for(int i = te.processQueue.size()-1; i >= 0; i--)
			{
				IIMultiblockProcess<VulcanizerRecipe> process = te.processQueue.get(i);
				maps[te.getWorkAnimation(process)].apply(MathHelper.clamp(te.getProductionProgress(process, partialTicks), 0, 1));
			}

			//The newest cycle controls the shared rollers and feed strips.
			IIMultiblockProcess<VulcanizerRecipe> newest = te.processQueue.get(te.processQueue.size()-1);
			maps[2+te.getWorkAnimation(newest)].apply(MathHelper.clamp(te.getProductionProgress(newest, partialTicks), 0, 1));
		}

		applyStandardMirroring(te, true);
		variant.render(tes, buf);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		AMTModel variant = model.getBase();
		variant.defaultize();
		variant.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = AMTCachedModelBuilder.startTileEntityModel(TileEntityVulcanizer.class)
				.withModel(model)
				.withHeader(AMTLoader.loadHeader(model))
				.withHeaderProvider(te -> {
					ResourceLocation recipeModel = getRecipeModel(te);
					return recipeModel==null?null: AMTLoader.loadHeader(ResLoc.of(recipeModel).withExtension(ResLoc.EXT_OBJAMT));
				})
				.withModelProvider((te, header) -> {
					AMTModel mold = null, base = new AMTModel(
							new AMTParticle("smoke1", header),
							new AMTParticle("smoke2", header),
							new AMTParticle("smoke3", header),
							new AMTParticle("heating_left1", header),
							new AMTParticle("heating_left2", header),
							new AMTParticle("heating_right1", header),
							new AMTParticle("heating_right2", header)
					);
					ResourceLocation recipeModel = getRecipeModel(te);
					if(recipeModel!=null)
						mold = new AMTModel(DefaultVertexFormats.BLOCK, AMTUtils.modelFromRes(recipeModel), header, null);
					return new AMTModel(mold, base).getParts();
				})
				.build();

		for(int i = 0; i < 2; i++)
		{
			work[i] = AMTLoader.loadAnimation(IIReference.RES_II.with("vulcanizer/work"+(i+1)));
			IIAnimationGroup[] groups = Arrays.stream(work[i].groups)
					.filter(group -> group.groupName.startsWith("roller")||group.groupName.startsWith("latex_strip"))
					.toArray(IIAnimationGroup[]::new);
			loading[i] = new IIAnimation(work[i].res, groups);
		}
	}

	private IIAnimationCompiledMap[] compileAnimations(AMTModel variant)
	{
		return new IIAnimationCompiledMap[]{
				IIAnimationCompiledMap.create(variant, work[0]),
				IIAnimationCompiledMap.create(variant, work[1]),
				IIAnimationCompiledMap.create(variant, loading[0]),
				IIAnimationCompiledMap.create(variant, loading[1])
		};
	}

	@Nullable
	private ResourceLocation getRecipeModel(@Nullable TileEntityVulcanizer te)
	{
		return te==null||te.processQueue.isEmpty()?null: te.processQueue.get(0).recipe.model;
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		IIMultiblockRecipe.streamRecipes(VulcanizerRecipe.class)
				.map(recipe -> recipe.model)
				.distinct()
				.forEach(res -> AMTLoader.preloadTexturesFromOBJ(res, map));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		this.model = AMTUtils.disposeOf(model);
		this.animations.clear();
	}
}
