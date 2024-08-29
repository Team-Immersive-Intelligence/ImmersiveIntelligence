package pl.pabilo8.immersiveintelligence.client.util.amt;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradableTool;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 17.09.2022
 */
public abstract class IIUpgradableItemRendererAMT<I extends ItemIIUpgradableTool> extends IIItemRendererAMT<I>
{
	/**
	 * Resource location blueprint of weapon item models
	 */
	protected static final ResLoc RES_MODEL_WEAPON = ResLoc.of(IIReference.RES_ITEM_MODEL, "weapons/%1$s/%1$s");
	/**
	 * Resource location for animations of this model
	 */
	protected final ResLoc animationRes;
	/**
	 * Resource location of the directory, in which the model is stored
	 */
	protected final ResLoc directoryRes;

	public final HashMap<Predicate<EasyNBT>, String> UPGRADE_PARTS = new HashMap<>();
	private final HashMap<Predicate<EasyNBT>, IIAnimationCachedMap> UPGRADES = new HashMap<>();

	protected AMTModelCache<ItemStack> model;
	IIAnimationCachedMap upgradeVisibility;

	public IIUpgradableItemRendererAMT(@Nonnull I item, ResLoc modelRes)
	{
		super(item, modelRes);
		animationRes = ResLoc.of(IIReference.RES_II, item.itemName, "/");
		directoryRes = modelRes.asDirectory();
	}

	@Override
	protected void nullifyModels()
	{
		IIAnimationUtils.disposeOf(model);
	}

	protected final void showUpgrades(ItemStack stack, EasyNBT nbt)
	{
		//Get upgrade tag
		EasyNBT upgrades = nbt.getEasyCompound("upgrades");
		//Make upgrade parts hidden by default
		upgradeVisibility.apply(0);

		//Show upgrades
		if(!upgrades.isEmpty())
			UPGRADES.forEach((key, value) -> {
				if(key.test(upgrades))
					value.apply(0f);
			});
	}

	protected final OBJModel[] listUpgradeModels()
	{
		return UPGRADE_PARTS.values().stream()
				.map(s -> ResLoc.of(directoryRes, "upgrades/", s).withExtension(ResLoc.EXT_OBJ))
				.map(IIAnimationUtils::modelFromRes)
				.toArray(OBJModel[]::new);
	}

	protected final void loadUpgrades(OBJModel model, ResourceLocation location)
	{
		UPGRADES.clear();
		UPGRADE_PARTS.forEach((pred, str) -> UPGRADES.put(pred, IIAnimationCachedMap.create(this.model,
				new ResourceLocation(location.getResourceDomain(), location.getResourcePath()+str)))
		);
		upgradeVisibility = IIAnimationCachedMap.createVisibilityAnimation(this.model, model);
	}

	//--- Utility Methods ---//

	protected final boolean isScopeZooming(TransformType transform, ItemStack stack)
	{
		return is1stPerson(transform)&&CameraHandler.zoom!=null&&CameraHandler.zoom.getZoomProgress(stack, null)==1;
	}

	//--- Public Methods ---//

	public void addUpgradePart(Predicate<EasyNBT> predicate, String name)
	{
		UPGRADE_PARTS.put(predicate, name);
	}
}
