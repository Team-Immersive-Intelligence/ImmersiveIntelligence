package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement_OBJ;
import blusunrize.immersiveengineering.client.models.IESmartObjModel;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static blusunrize.immersiveengineering.client.ClientUtils.mc;

/**
 * @author Pabilo8
 * @since 17.09.2022
 */
public abstract class IIItemRendererAMT<I extends Item> extends TileEntityItemStackRenderer implements IReloadableModelContainer<IIItemRendererAMT<I>>
{
	private boolean unCompiled = true;
	@Nonnull
	private final ItemModelReplacement_OBJ replacementModel;
	@Nullable
	private IESmartObjModel model;
	@Nonnull
	protected ResLoc headerRes;
	@Nonnull
	protected final I item;

	public IIItemRendererAMT(@Nonnull I item, ResLoc modelRes)
	{
		this.item = item;
		this.headerRes = modelRes.withExtension(ResLoc.EXT_OBJAMT);
		IIModelRegistry.INSTANCE.registerCustomItemModel(item, modelRes.getResourceDomain(),
				setTransforms(this.replacementModel = new ImmersiveModelRegistry.ItemModelReplacement_OBJ(modelRes.withExtension(ResLoc.EXT_OBJ).toString(), true)));
	}

	public void setHeaderRes(ResLoc modelRes)
	{
		headerRes = modelRes;
	}

	protected final ItemModelReplacement parseTransforms(ItemModelReplacement_OBJ model, @Nullable IIModelHeader header)
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

		if(unCompiled)
		{
			//IOBJModelCallback<ItemStack> callback = (IOBJModelCallback<ItemStack>)stack.getItem();
			if(model instanceof IESmartObjModel)
			{
				this.model = ((IESmartObjModel)model);
				nullifyModels();

				//load header
				compileModels(this.model.getModel(), IIAnimationLoader.loadHeader(headerRes));
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
					((model instanceof IESmartObjModel)?((IESmartObjModel)model): this.model).lastCameraTransform,
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
		IIAnimationLoader.preloadTexturesFromMTL(headerRes.withExtension(ResLoc.EXT_MTL), map);
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
	public abstract void compileModels(OBJModel model, IIModelHeader header);

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

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface RegisteredItemRenderer
	{
		String name();
	}
}
