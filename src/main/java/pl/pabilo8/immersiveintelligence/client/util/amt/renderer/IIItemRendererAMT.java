package pl.pabilo8.immersiveintelligence.client.util.amt.renderer;

import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement_OBJ;
import blusunrize.immersiveengineering.client.models.IESmartObjModel;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelDualPerspective;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static blusunrize.immersiveengineering.client.ClientUtils.mc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.09.2022
 */
public abstract class IIItemRendererAMT<I extends Item> extends TileEntityItemStackRenderer implements IReloadableModelContainer<IIItemRendererAMT<I>>
{
	@Nonnull
	protected final I item;
	@Nonnull
	private final ItemModelReplacement_OBJ replacementModel;
	@Nonnull
	protected ResLoc headerRes;
	private boolean unCompiled = true;
	@Nullable
	private IESmartObjModel model;

	public IIItemRendererAMT(@Nonnull I item, ResLoc modelRes)
	{
		this(item, modelRes, false);
	}

	public IIItemRendererAMT(@Nonnull I item, ResLoc modelRes, boolean dualPerspective)
	{
		this.item = item;
		this.headerRes = modelRes.withExtension(ResLoc.EXT_OBJAMT);

		//Set TEISR and register custom model
		item.setTileEntityItemStackRenderer(this);
		this.replacementModel = new ImmersiveModelRegistry.ItemModelReplacement_OBJ(modelRes.withExtension(ResLoc.EXT_OBJ).toString(), true);
		ItemModelReplacement replacement = setTransforms(this.replacementModel);
		if(dualPerspective)
			IIModelRegistry.INSTANCE.registerDualCustomItemModel(item, modelRes.getResourceDomain(), replacement);
		else
			IIModelRegistry.INSTANCE.registerCustomItemModel(item, modelRes.getResourceDomain(), replacement);

		//Register to the list of renderers for automatic reloading
		RegisteredItemRenderer annotation = IIUtils.getAnnotation(RegisteredItemRenderer.class, this);
		if(annotation!=null)
			this.subscribeToList(annotation.name());
	}

	protected final ItemModelReplacement parseTransforms(ItemModelReplacement_OBJ model, @Nullable AMTModelHeader header)
	{
		if(header!=null)
			header.applyTransforms(model);
		return model;
	}

	@Override
	public final void renderByItem(@Nonnull ItemStack stack, float partialTicks)
	{
		//Get model values
		World w = IESmartObjModel.tempEntityStatic!=null?IESmartObjModel.tempEntityStatic.world: null;
		IBakedModel model = mc().getRenderItem().getItemModelWithOverrides(stack, w, IESmartObjModel.tempEntityStatic);
		IBakedModel renderModel = model instanceof ModelDualPerspective?((ModelDualPerspective)model).getPerspectiveModel(): model;

		if(unCompiled)
		{
			//IOBJModelCallback<ItemStack> callback = (IOBJModelCallback<ItemStack>)stack.getItem();
			if(renderModel instanceof IESmartObjModel)
			{
				this.model = ((IESmartObjModel)renderModel);
				nullifyModels();

				//load header
				compileModels(this.model.getModel(), AMTLoader.loadHeader(headerRes));
				this.unCompiled = false;
			}
		}
		else
		{
			assert this.model!=null;

			//setup
			Tessellator tes = Tessellator.getInstance();
			GlStateManager.pushMatrix();
			GlStateManager.disableCull();

			//draw the model with proper transform
			draw(stack,
					((renderModel instanceof IESmartObjModel)?((IESmartObjModel)renderModel): this.model).lastCameraTransform,
					tes.getBuffer(), tes, mc().getRenderPartialTicks()
			);

			//finish
			GlStateManager.enableCull();
			GlStateManager.popMatrix();
		}
	}

	//--- Model and Texture loading ---//

	@Override
	public final void reloadModels()
	{
		unCompiled = true;
		//reset transforms | allows easy debugging
		setTransforms(this.replacementModel);
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		AMTLoader.preloadTexturesFromMTL(headerRes.withExtension(ResLoc.EXT_MTL), map);
	}

	//--- Abstract Methods ---//

	protected abstract ItemModelReplacement setTransforms(ItemModelReplacement_OBJ model);


	/**
	 * @param stack        ItemStack to be rendered
	 * @param transform    Item Transform Type, dependent on place
	 * @param buf          Buffer, by default provided by the Tessellator
	 * @param tes          Tessellator drawing the models, by default the vanilla one
	 * @param partialTicks partial time of drawing
	 */
	public abstract void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks);

	/**
	 * Load the {@link AMT} and prepare {@link IIAnimationCompiledMap} here.
	 *
	 * @param model the model providing AMT groups
	 */
	public abstract void compileModels(OBJModel model, AMTModelHeader header);

	/**
	 * Called when cached models, animations should be unloaded/reloaded
	 */
	protected abstract void nullifyModels();

	//--- Utility Methods ---//

	protected final boolean is1stPerson(TransformType transform)
	{
		switch(transform)
		{
			case FIRST_PERSON_RIGHT_HAND:
			case FIRST_PERSON_LEFT_HAND:
				return true;
			case THIRD_PERSON_RIGHT_HAND:
			case THIRD_PERSON_LEFT_HAND:
				return Graphics.AMTHandDisplayMode==2;
			default:
				return false;
		}
	}

	protected final boolean is3rdPerson(TransformType transform)
	{
		return transform==TransformType.THIRD_PERSON_RIGHT_HAND||transform==TransformType.THIRD_PERSON_LEFT_HAND;
	}

	protected float getItemEquipTime(EnumHand hand, float partialTicks)
	{
		ItemRenderer renderer = mc().entityRenderer.itemRenderer;
		//Mainhand
		if(hand==EnumHand.MAIN_HAND)
			return (renderer.prevEquippedProgressMainHand+(renderer.equippedProgressMainHand-renderer.prevEquippedProgressMainHand)*partialTicks);
		//Offhand
		return (renderer.prevEquippedProgressOffHand+(renderer.equippedProgressOffHand-renderer.prevEquippedProgressOffHand)*partialTicks);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface RegisteredItemRenderer
	{
		String name();
	}
}
