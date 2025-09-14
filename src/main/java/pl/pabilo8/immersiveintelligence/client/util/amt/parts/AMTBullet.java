package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;

/**
 * AMT type for drawing bullets/ammunition from II system
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.07.2022
 */
public class AMTBullet extends AMT
{
	AmmoCore core = null;
	CoreType coreType = null;
	float gunpowderPercentage = 0;
	@Nullable
	IIColor paintColor = null;
	@Nullable
	private IAmmoModel<?, ?> model;
	private BulletState state = BulletState.BULLET_UNUSED;

	public AMTBullet(String name, Vec3d originPos, @Nullable IAmmoModel<?, ?> model)
	{
		super(name, originPos);
		this.model = model;
	}

	public AMTBullet(String name, AMTModelHeader header, @Nullable IAmmoModel<?, ?> model)
	{
		super(name, header);
		this.model = model;
	}

	@Override
	protected void preDraw()
	{
		GlStateManager.translate(originPos.x, originPos.y, originPos.z);

		if(off!=null)
			GlStateManager.translate(-off.x, off.y, off.z);

		if(rot!=null)
		{
			GlStateManager.rotate((float)rot.y, 0, 1, 0);
			GlStateManager.rotate((float)rot.z, 0, 0, 1);
			GlStateManager.rotate((float)-rot.x, 1, 0, 0);
		}
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(model!=null)
		{
			switch(state)
			{
				case CASING:
					model.renderCasing(gunpowderPercentage, paintColor);
					break;
				case CORE:
					if(coreType!=null)
						model.renderCore(core, coreType);
					break;
				case BULLET_USED:
				case BULLET_UNUSED:
					if(coreType!=null)
						model.renderAmmoComplete(state==BulletState.BULLET_USED, paintColor, core, coreType);
					break;
			}
		}
	}

	@Override
	public void disposeOf()
	{

	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetItemStack("stack", stack -> this.withStack(stack, BulletState.BULLET_UNUSED));
		nbt.checkSetString("core", coreName -> this.core = AmmoRegistry.getCore(coreName));
		nbt.checkSetEnum("coreType", CoreType.class, coreType -> this.coreType = coreType);
		nbt.checkSetFloat("gunpowderPercentage", this::withGunpowderPercentage);
		nbt.checkSetColor("paintColor", paintColor -> this.paintColor = paintColor);
		nbt.checkSetString("ammoType", ammoTypeName -> {
			IAmmoTypeItem<?, ?> item = AmmoRegistry.getAmmoItem(ammoTypeName);
			if(item!=null)
				this.setModel(AmmoRegistry.getGenericModel(item));
		});
		nbt.checkSetEnum("state", BulletState.class, this::withState);

	}

	public void setModel(@Nullable IAmmoModel<?, ?> model)
	{
		this.model = model;
	}

	public AMTBullet withStack(ItemStack stack, BulletState state)
	{
		this.state = state;

		if(stack.getItem() instanceof IAmmoTypeItem)
		{
			IAmmoTypeItem<?, ?> b = (IAmmoTypeItem<?, ?>)stack.getItem();
			return withProperties(b.getCore(stack), b.getCoreType(stack), b.getPaintColor(stack));
		}
		if(stack.isEmpty())
			this.visible = false;

		return this;
	}

	public AMTBullet withProperties(AmmoCore core, CoreType coreType, @Nullable IIColor paintColor)
	{
		this.core = core;
		this.coreType = coreType;
		this.paintColor = paintColor;

		return this;
	}

	public AMTBullet withState(BulletState state)
	{
		this.state = state;
		return this;
	}

	public AMTBullet withGunpowderPercentage(float gunpowderPercentage)
	{
		this.state = BulletState.CASING;
		this.gunpowderPercentage = gunpowderPercentage;

		return this;
	}

	public BulletState getState()
	{
		return state;
	}

	public enum BulletState implements ISerializableEnum
	{
		CASING,
		CORE,
		BULLET_USED,
		BULLET_UNUSED
	}
}
