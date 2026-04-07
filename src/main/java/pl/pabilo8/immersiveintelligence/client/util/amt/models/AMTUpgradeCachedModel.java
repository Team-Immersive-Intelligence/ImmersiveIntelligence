package pl.pabilo8.immersiveintelligence.client.util.amt.models;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCachedMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTUpgradeModel.UpgradeStage;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIBooleanLine;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIShaderLine;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIVectorLine;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.07.2022
 */
public class AMTUpgradeCachedModel<T extends TileEntity & IUpgradableDevice> implements AMTRenderable
{
	private final Upgrade upgrade;
	//Construction
	private final IIAnimationCompiledMap constructionAnimation;
	private final AMTModel constructionModel, batchedConstructionModel;
	private final int steps;
	//Finished
	private boolean initialized = false;
	private final AMTCachedModelBuilder<T> modelBuilder;
	private AMTCachedModel<T> finishedModel;
	private final IIAnimation finishedAnimationRaw;
	private IIAnimationCachedMap finishedAnimation;

	private AMTUpgradeCachedModel(MachineCachedUpgradeModelBuilder<T> builder)
	{
		this.upgrade = builder.upgrade;

		//Create construction animation
		IIAnimation loaded = AMTLoader.loadAnimation(builder.animation);
		this.constructionAnimation = IIAnimationCompiledMap.create(builder.constructionModel, new IIAnimation(IIReference.RES_II.with("machine_upgrade/"+upgrade.getName()),
				Arrays.stream(loaded.groups)
						.map(g -> new IIAnimationGroup(g.groupName, g.position, g.scale, g.rotation, null, vecToAlpha(g.position), null))
						.toArray(IIAnimationGroup[]::new)));

		this.upgrade.withProgressStages(this.steps = this.constructionAnimation.size());

		//get only base level models (model), contained in animation
		this.constructionModel = new AMTModel(builder.constructionModel.stream()
				.filter(this.constructionAnimation::containsKey)
				.toArray(AMT[]::new));
		this.batchedConstructionModel = new AMTModel(builder.constructionModel.batch("batched"));
		this.modelBuilder = builder.cachedModelBuilder.withModel(builder.finishedModel);

		IIAnimationGroup[] groups = new AMTModel(DefaultVertexFormats.BLOCK, builder.finishedModel)
				.stream()
				.map(amt ->
						new IIAnimationGroup(amt.getName(), null, null, null,
								new IIBooleanLine(new float[]{0, 1}, new Boolean[]{false, true}),
								null, null)
				)
				.toArray(IIAnimationGroup[]::new);
		finishedAnimationRaw = new IIAnimation(IIReference.RES_II.with("machine_upgrade/"+upgrade.getName()), groups);
	}

	@Nullable
	private IIShaderLine vecToAlpha(IIVectorLine position)
	{
		if(position==null||position.values.length < 2)
			return null;

		return new IIShaderLine(Shaders.ALPHA,
				new float[]{0f, position.timeframes[0], position.timeframes[position.timeframes.length-1], 1f},
				new Float[][]{
						{0f},
						{0f},
						{1f},
						{1f}
				}
		);
	}

	public UpgradeStage apply(T machine, Tessellator tes, BufferBuilder buf, float partialTicks)
	{
		if(!initialized)
		{
			//Get the model from the builder result
			this.finishedModel = modelBuilder.getBuildResult();
			//Create the animation for displaying the finished model parts
			this.finishedAnimation = IIAnimationCachedMap.create(this.finishedModel, finishedAnimationRaw);
			this.initialized = true;
		}

		//Check if the machine has the upgrade installed
		if(machine.isUpgradeInstalled(upgrade))
		{
			//Make parts invisible
			this.finishedAnimation.apply(1f);
			return UpgradeStage.INSTALLED;
		}

		//Skip construction animation, if the machine does not have the upgrade installed
		if(machine.getCurrentUpgrade()!=upgrade)
		{
			this.finishedAnimation.apply(0f);
			return UpgradeStage.NOT_INSTALLED;
		}

		//calculate progress per part
		final int maxProgress = IIContent.UPGRADE_INSERTER.getProgressRequired();
		double maxClientProgress = UpgradeUtils.getMaxClientProgress(machine.getUpgradeInstallProgress(true), upgrade);

		double currentProgress = (int)Math.min(machine.getUpgradeInstallProgress(true)+((partialTicks*(Tools.wrenchUpgradeProgress*0.5f))), maxClientProgress);
		float install = (float)MathHelper.clamp(currentProgress/maxProgress, 0, 1);

		//Draw blueprint
		ShaderUtil.useBlueprint(0.35f, ClientUtils.mc().player.ticksExisted+partialTicks);
		batchedConstructionModel.render(tes, buf);
		ShaderUtil.releaseShader();

		constructionModel.defaultize();

		//Draw construction animation
		constructionAnimation.apply(install);
		constructionModel.render(tes, buf);

		return UpgradeStage.IN_PROGRESS;
	}

	@Override
	public void defaultize()
	{
		constructionModel.defaultize();
	}

	@Override
	public void render(Tessellator tes, BufferBuilder buf)
	{
		//do not
	}

	@Override
	public void disposeOf()
	{
		constructionModel.disposeOf();
	}


	public static class MachineCachedUpgradeModelBuilder<T extends TileEntity & IUpgradableDevice>
	{
		private Upgrade upgrade;
		private AMTModel constructionModel;
		private OBJModel finishedModel;
		private ResourceLocation animation;
		private AMTCachedModelBuilder<T> cachedModelBuilder;

		public MachineCachedUpgradeModelBuilder(AMTCachedModelBuilder<T> cachedModelBuilder)
		{
			this.cachedModelBuilder = cachedModelBuilder;
		}

		public MachineCachedUpgradeModelBuilder<T> withUpgrade(Upgrade upgrade)
		{
			this.upgrade = upgrade;
			return this;
		}

		public MachineCachedUpgradeModelBuilder<T> withConstructionModel(ResourceLocation modelLocation)
		{
			return withConstructionModel(AMTUtils.modelFromRes(modelLocation));
		}

		public MachineCachedUpgradeModelBuilder<T> withConstructionModel(OBJModel constructionModel)
		{
			if(finishedModel==null)
				finishedModel = constructionModel;
			return withConstructionModel(new AMTModel(DefaultVertexFormats.BLOCK, constructionModel));
		}

		public MachineCachedUpgradeModelBuilder<T> withConstructionModel(AMTModel constructionModel)
		{
			this.constructionModel = constructionModel;
			return this;
		}

		public MachineCachedUpgradeModelBuilder<T> withFinishedModel(OBJModel finishedModel)
		{
			this.finishedModel = finishedModel;
			return this;
		}

		public MachineCachedUpgradeModelBuilder<T> withAnimation(ResourceLocation animation)
		{
			this.animation = animation;
			return this;
		}

		public AMTUpgradeCachedModel<T> build()
		{
			if(cachedModelBuilder==null||upgrade==null||constructionModel==null||animation==null)
			{
				throw new IllegalStateException("Model Builder, Upgrade, Construction Model and Animation must be set");
			}
			return new AMTUpgradeCachedModel<>(this);
		}
	}
}
