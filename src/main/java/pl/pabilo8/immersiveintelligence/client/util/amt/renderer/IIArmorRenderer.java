package pl.pabilo8.immersiveintelligence.client.util.amt.renderer;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Renders cached AMT armor at the pivots of a biped model.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.11.2026
 */
public abstract class IIArmorRenderer<I extends Item> extends ModelBiped implements IReloadableModelContainer<IIArmorRenderer<I>>
{
	protected static final ResLoc ARMOR_MODELS = IIReference.RES_II.with("models/armor/");
	private static final Map<Item, IIArmorRenderer<?>> RENDERERS = new HashMap<>();
	private static final ModelBiped REST_POSE = new ModelBiped();

	protected final I item;
	@Nullable
	protected AMTCachedModel<ItemStack> model;
	protected final ResLoc modelLocation;

	private ItemStack stack = ItemStack.EMPTY;
	private ModelBiped defaultPose = this;
	@Nullable
	private ModelBiped posedBiped;

	protected IIArmorRenderer(I item, ResLoc modelLocation)
	{
		this.item = item;
		this.modelLocation = modelLocation.withExtension(ResLoc.EXT_OBJ);

		RegisteredArmorRenderer annotation = IIUtils.getAnnotation(RegisteredArmorRenderer.class, this);
		if(annotation!=null)
		{
			RENDERERS.put(item, this);
			subscribeToList(annotation.name());
		}
	}

	/**
	 * Gets the armor renderer for the specified stack.
	 */
	@Nullable
	public static IIArmorRenderer<?> get(ItemStack stack)
	{
		return RENDERERS.get(stack.getItem());
	}

	/**
	 * Sets the stack and the armor pose for the next render.
	 */
	public ModelBiped prepare(ItemStack stack, @Nullable ModelBiped pose)
	{
		this.stack = stack;
		this.defaultPose = pose==null?this: pose;
		this.posedBiped = null;
		return this;
	}

	@Override
	public void setModelAttributes(ModelBase model)
	{
		super.setModelAttributes(model);
		posedBiped = model instanceof ModelBiped?(ModelBiped)model: null;
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		reloadModels();
		AMTLoader.preloadTexturesFromOBJ(modelLocation, map);
	}

	@Override
	public void reloadModels()
	{
		this.model = AMTCachedModelBuilder.startItemModel()
				.withModel(modelLocation)
				.withHeader(modelLocation.withExtension(ResLoc.EXT_OBJAMT))
				.build();
	}

	/**
	 * Used to load the model variant for the stack and wearer.
	 *
	 * @implNote Overriding classes should apply model animations here.
	 */
	protected AMTModel getModelVariant(AMTCachedModel<ItemStack> cache, ItemStack stack, EntityLivingBase wearer, float partialTicks)
	{
		AMTModel variant = cache.getVariant(stack);
		variant.defaultize();
		return variant;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbAmount, float age, float yaw, float pitch, float scale)
	{
		if(!(entity instanceof EntityLivingBase))
			return;
		EntityLivingBase wearer = (EntityLivingBase)entity;
		float partialTicks = ClientUtils.timer().renderPartialTicks;
		ModelBiped biped = posedBiped;
		if(biped==null)
		{
			biped = defaultPose;
			if(biped!=this)
			{
				biped.setModelAttributes(this);
				biped.setLivingAnimations(wearer, limbSwing, limbAmount, partialTicks);
			}
			biped.setRotationAngles(limbSwing, limbAmount, age, yaw, pitch, scale, entity);
		}
		//Render all parts of this stack, including parts outside its equipment slot.
		renderParts(stack, wearer, biped, partialTicks, scale, false);
	}

	/**
	 * Renders the armor on a biped in its current pose.
	 */
	public void renderOnBiped(ItemStack stack, EntityLivingBase wearer, ModelBiped biped, float partialTicks, float scale)
	{
		renderParts(stack, wearer, biped, partialTicks, scale, true);
	}

	private void renderParts(ItemStack stack, EntityLivingBase wearer, ModelBiped biped, float partialTicks, float scale, boolean useBipedVisibility)
	{
		if(model==null)
			reloadModels();
		ClientUtils.bindAtlas();
		Tessellator tessellator = Tessellator.getInstance();
		for(AMT part : getModelVariant(model, stack, wearer, partialTicks))
		{
			//Apply pivot from player model
			Function<ModelBiped, ModelRenderer> getPivot = switch(part.getName())
			{
				case "head" -> b -> b.bipedHead;
				case "body" -> b -> b.bipedBody;
				case "left_arm" -> b -> b.bipedLeftArm;
				case "right_arm" -> b -> b.bipedRightArm;
				case "left_leg", "left_foot" -> b -> b.bipedLeftLeg;
				case "right_leg", "right_foot" -> b -> b.bipedRightLeg;
				default -> null;
			};
			if(getPivot==null)
				continue;
			ModelRenderer pivot = getPivot.apply(biped);
			if(pivot.isHidden||useBipedVisibility&&!pivot.showModel)
				continue;
			ModelRenderer rest = getPivot.apply(REST_POSE);

			GlStateManager.pushMatrix();
			if(biped.isChild)
			{
				boolean head = pivot==biped.bipedHead;
				float childScale = head?0.75f: 0.5f;
				GlStateManager.scale(childScale, childScale, childScale);
				GlStateManager.translate(0, (head?16: 24)*scale, 0);
			}
			else if(wearer.isSneaking())
				GlStateManager.translate(0, 0.2f, 0);

			//Apply the current biped pivot in ModelRenderer rotation order.
			GlStateManager.translate(pivot.offsetX+pivot.rotationPointX*scale, pivot.offsetY+pivot.rotationPointY*scale,
					pivot.offsetZ+pivot.rotationPointZ*scale);
			if(pivot.rotateAngleZ!=0)
				GlStateManager.rotate(pivot.rotateAngleZ*180f/(float)Math.PI, 0, 0, 1);
			if(pivot.rotateAngleY!=0)
				GlStateManager.rotate(pivot.rotateAngleY*180f/(float)Math.PI, 0, 1, 0);
			if(pivot.rotateAngleX!=0)
				GlStateManager.rotate(pivot.rotateAngleX*180f/(float)Math.PI, 1, 0, 0);

			//Convert the feet-centred editor coordinates to the local biped pivot.
			float objScale = 16*scale;
			GlStateManager.scale(objScale, -objScale, -objScale);
			GlStateManager.translate(-rest.rotationPointX/16f, (rest.rotationPointY-24)/16f, rest.rotationPointZ/16f);
			part.render(tessellator, tessellator.getBuffer());
			GlStateManager.popMatrix();
		}
	}

	/**
	 * Registers an armor renderer for sprite loading and model reloads.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 12.11.2026
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface RegisteredArmorRenderer
	{
		String name();
	}
}
