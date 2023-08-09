package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;

import javax.annotation.Nullable;

/**
 * AMT type for drawing bullets/ammunition from II system
 *
 * @author Pabilo8
 * @since 26.07.2022
 */
public class AMTBullet extends AMT
{
	EnumCoreTypes coreType = null;
	float gunpowderPercentage = 0;
	int coreColour = 0xffffff, paintColour = -1;
	@Nullable
	private IBulletModel model;
	private BulletState state = BulletState.BULLET_UNUSED;

	public AMTBullet(String name, Vec3d originPos, @Nullable IBulletModel model)
	{
		super(name, originPos);
		this.model = model;
	}

	public AMTBullet(String name, IIModelHeader header, @Nullable IBulletModel model)
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
					model.renderCasing(gunpowderPercentage, paintColour);
					break;
				case CORE:
					if(coreType!=null)
						model.renderCore(coreColour, coreType);
					break;
				case BULLET_USED:
					if(coreType!=null)
						model.renderBulletUsed(coreColour, coreType, paintColour);
					break;
				case BULLET_UNUSED:
					if(coreType!=null)
						model.renderBulletUnused(coreColour, coreType, paintColour);
					break;
			}
			GlStateManager.color(1f, 1f, 1f, 1f);
			//TMT uses texture directly, not from main atlas
			ClientUtils.bindAtlas();
		}
	}

	@Override
	public void disposeOf()
	{

	}

	public void setModel(@Nullable IBulletModel model)
	{
		this.model = model;
	}

	public AMTBullet withStack(ItemStack stack, BulletState state)
	{
		this.state = state;

		if(stack.getItem() instanceof IAmmo)
		{
			IAmmo b = (IAmmo)stack.getItem();
			return withProperties(b.getCore(stack).getColour(), b.getCoreType(stack), b.getPaintColor(stack));
		}
		if(stack.isEmpty())
			this.visible = false;

		return this;
	}

	public AMTBullet withProperties(int coreColour, EnumCoreTypes coreType, int paintColour)
	{
		this.coreColour = coreColour;
		this.coreType = coreType;
		this.paintColour = paintColour;

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

	public enum BulletState
	{
		CASING,
		CORE,
		BULLET_USED,
		BULLET_UNUSED
	}
}
