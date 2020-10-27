package pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

public class EmplacementWeaponAutocannon extends EmplacementWeapon
{
	float flaps = 0;
	NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);

	@Override
	public String getName()
	{
		return "autocannon";
	}

	@Override
	public IngredientStack[] getIngredientsRequired()
	{
		return new IngredientStack[]{
				new IngredientStack(new ItemStack(IEContent.itemMaterial, 4, 15)),
				new IngredientStack("blockSteel", 1),
				new IngredientStack("plateSteel", 4),
				new IngredientStack(new ItemStack(CommonProxy.block_metal_device, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta())),
				new IngredientStack("engineElectricSmall", 2)
		};
	}

	@Override
	public boolean isReloaded()
	{
		return reloadDelay==0;
	}

	@Override
	public boolean hasAmmunitionInTempStorage()
	{
		return false;
	}

	@Override
	public boolean canReloadFrom(TileEntityEmplacement te)
	{
		return false;
	}

	@Override
	public int reloadFrom(TileEntityEmplacement te)
	{
		// TODO: 27.10.2020 check for inventory
		return reloadDelay = 240;
	}

	@Override
	public void tick()
	{
		if(reloadDelay > 0)
			reloadDelay -= 1;
	}

	@Override
	public void aimAt(float pitch, float yaw)
	{
		super.aimAt(pitch, yaw);
		if(this.pitch < -20&&this.pitch > -75)
			flaps = Math.min(flaps+0.075f, 1);
		else
			flaps = Math.max(flaps-0.075f, 0);
	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("yaw", yaw);
		tag.setFloat("pitch", pitch);
		return tag;
	}

	@Override
	public boolean willShoot(TileEntityEmplacement te)
	{
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		yaw = tagCompound.getFloat("yaw");
		pitch = tagCompound.getFloat("pitch");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(TileEntityEmplacement te, float partialTicks)
	{

		float reloadAnim = 1;
		if(!isReloaded())
			reloadAnim = Math.min(1, (this.reloadDelay+partialTicks)/(float)Autocannon.reloadTime);

		GlStateManager.pushMatrix();
		float p, pp, y, yy;
		double cannonAnim;
		p = this.nextPitch-this.pitch;
		y = this.nextYaw-this.yaw;
		float flaps = this.flaps;
		if(this.pitch < -20&&this.pitch > -75)
			flaps = Math.min(flaps+(0.075f*partialTicks), 1);
		else
			flaps = Math.max(flaps-(0.075f*partialTicks), 0);

		if(te.progress < Emplacement.lidTime)
			cannonAnim = 0;
		else
			cannonAnim = Math.abs((((te.getWorld().getTotalWorldTime()+partialTicks)%6)/6d)-0.5)/0.5;

		pp = pitch+Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1)*partialTicks;
		yy = yaw+Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1)*partialTicks;

		double b1 = 0, b2 = 0;
		if(isShooting())
		{
			if(cannonAnim < 0.5f)
			{
				b1 = (float)(cannonAnim/0.5f);
				if(willShoot(te))
					b2 = 1f-(cannonAnim/0.5f);
			}
			else
			{
				b1 = 1f-((cannonAnim-0.5f)/0.5f);
				b2 = ((cannonAnim-0.5f)/0.5f);
			}
		}

		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel1Model)
			mod.rotationPointZ = (float)(-8f*b1);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel2Model)
			mod.rotationPointZ = (float)(-8f*b2);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel3Model)
			mod.rotationPointZ = (float)(-8f*b1);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel4Model)
			mod.rotationPointZ = (float)(-8f*b2);

		ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.baseModel)
			mod.render();
		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.turretModel)
			mod.render();

		EmplacementRenderer.modelAutocannon.turretTopFlapsModel[0].rotateAngleY = -flaps*1.55f;
		EmplacementRenderer.modelAutocannon.turretTopFlapsModel[1].rotateAngleY = flaps*1.55f;
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.turretTopFlapsModel)
			mod.render();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 1.125f, 0);
		GlStateManager.rotate(pp, 1, 0, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.gunModel)
			mod.render();

		GlStateManager.pushMatrix();

		if(reloadAnim > 0.3-0.1)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineRightBottomModel)
				mod.render();
		if(reloadAnim > 0.5-0.1)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineRightTopModel)
				mod.render();
		if(reloadAnim > 0.7-0.1)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineLeftBottomModel)
				mod.render();
		if(reloadAnim > 0.9-0.1)
			for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineLeftTopModel)
				mod.render();

		GlStateManager.popMatrix();


		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel1Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel2Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel3Model)
			mod.render();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.barrel4Model)
			mod.render();

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.625f, 1f, -0.88125f);

		float ins_y = 0f, ins_p1 = 15f, ins_p2 = 175f, ins_progress = 0f;
		Runnable r = () -> {
			ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
		};

		if(reloadAnim > 0.1&&reloadAnim < 0.9)
		{
			if(((reloadAnim-0.1)%0.2)/0.2 < 0.5)
			{
				double p_anim = (((reloadAnim-0.1)%0.2)/0.2)/0.5f;
				if(p_anim < 0.25)
				{
					ins_y = (float)(90*(p_anim/0.25));
				}
				else if(p_anim < 0.5)
				{
					ins_y = 90f;
					ins_p1 = 15+(float)(50*((p_anim-0.25)/0.25));
				}
				else if(p_anim < 0.75)
				{
					ins_y = 90f;
					ins_p1 = 15+(float)(50*(1f-((p_anim-0.5)/0.25)));
				}
				else
				{
					if(reloadAnim < 0.5)
						ins_y = 90f-(float)(90*((p_anim-0.75)/0.25));
					else
					{
						ins_y = 90f-((float)(25*((p_anim-0.75)/0.25)));
						ins_p2 = 175f-((float)(70*((p_anim-0.75)/0.25)));
						ins_p1 = 15f+((float)(25*((p_anim-0.75)/0.25)));
					}
				}

				if(p_anim > 0.5)
					if(reloadAnim > 0.5)
						r = () -> {
							ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
							GlStateManager.rotate(-55, 1, 0, 0);
							GlStateManager.translate(0, 0, 0.0625f);
							for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineLeftModel)
								mod.render();
						};
					else
						r = () -> {
							ClientUtils.bindTexture(EmplacementRenderer.textureAutocannon);
							GlStateManager.rotate(-55, 1, 0, 0);
							GlStateManager.translate(0, 0, 0.0625f);
							for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.magazineRightModel)
								mod.render();
						};
			}
			//right
			else if(reloadAnim < 0.3)
			{
				double p_anim = (reloadAnim-0.1)/0.2;
				//-25, 15,145, 1f
				if(p_anim < 0.25)
				{
					//ins_p1 = 15f, ins_p2 = 175f
					ins_y = (float)(-25*(p_anim/0.25));
				}
				else if(p_anim < 0.5)
				{
					ins_y = -25;
					ins_p1 = 15f+(float)(10*((p_anim-0.25)/0.25));
					ins_p2 = 175f-(float)(30*((p_anim-0.25)/0.25));
				}
				else if(p_anim < 0.75)
				{
					ins_y = -25;
					ins_p1 = 25f-(float)(10*((p_anim-0.5)/0.25));
					ins_p2 = 145f+(float)(30*((p_anim-0.5)/0.25));
				}
				else
				{
					ins_y = (float)(-25*(1f-((p_anim-0.75)/0.25)));
				}

			}
			else if(reloadAnim < 0.5)
			{
				double p_anim = (reloadAnim-0.3)/0.2;
				if(p_anim < 0.25)
				{
					//ins_p1 = 15f, ins_p2 = 175f
					ins_y = (float)(-25*(p_anim/0.25));
				}
				else if(p_anim < 0.5)
				{
					ins_y = -25;
					ins_p1 = 15f+(float)(15*((p_anim-0.25)/0.25));
					ins_p2 = 175f-(float)(45*((p_anim-0.25)/0.25));
				}
				else if(p_anim < 0.75)
				{
					ins_y = -25;
					ins_p1 = 30f-(float)(15*((p_anim-0.5)/0.25));
					ins_p2 = 130f+(float)(45*((p_anim-0.5)/0.25));
				}
				else
				{
					ins_y = (float)(-25*(1f-((p_anim-0.75)/0.25)));
				}
			}
			//left
			else if(reloadAnim < 0.7)
			{
				double p_anim = (reloadAnim-0.5)/0.2;
				ins_y = 65f;
				ins_p2 = 105;
				ins_p1 = 40;
				if(p_anim < 0.2)
				{
					ins_y = (float)(65f-(12.5f*(p_anim/0.2f)));
					ins_p1 = (float)(40+(50f*(p_anim/0.2f)));
					ins_p2 = (float)(105+(15f*(p_anim/0.2f)));
				}
				else if(p_anim < 0.4)
				{
					ins_y = 52.5f;
					ins_p1 = 90;
					ins_p2 = 120;
				}
				else if(p_anim < 0.6)
				{
					ins_y = (float)(52.5f+(37.5f*((p_anim-0.4)/0.2f)));
					ins_p1 = (float)(90-(50f*((p_anim-0.4)/0.2f)));
					ins_p2 = (float)(120-(15f*((p_anim-0.4)/0.2f)));
				}
				else if(p_anim < 0.8)
				{
					ins_p1 = (float)(40-(25f*((p_anim-0.6)/0.2f)));
					ins_p2 = (float)(105+(70f*((p_anim-0.6)/0.2f)));
					ins_y = 90;
				}
				else
				{
					ins_p1 = 15f;
					ins_p2 = 175f;
					ins_y = (float)(90f*(1f-((p_anim-0.8)/0.2f)));
				}
			}
			else if(reloadAnim < 0.9)
			{
				double p_anim = (reloadAnim-0.7)/0.2;
				ins_y = 65f;
				ins_p2 = 105;
				ins_p1 = 40;
				if(p_anim < 0.2)
				{
					ins_y = (float)(65f-(12.5f*(p_anim/0.2f)));
					ins_p1 = (float)(40+(20f*(p_anim/0.2f)));
				}
				else if(p_anim < 0.4)
				{
					ins_y = 52.5f;
					ins_p1 = 65;
				}
				else if(p_anim < 0.6)
				{
					ins_y = (float)(52.5f+(37.5f*((p_anim-0.4)/0.2f)));
					ins_p1 = (float)(65-(25f*((p_anim-0.4)/0.2f)));
				}
				else if(p_anim < 0.8)
				{
					ins_p1 = (float)(40-(25f*((p_anim-0.6)/0.2f)));
					ins_p2 = (float)(105+(70f*((p_anim-0.6)/0.2f)));
					ins_y = 90;
				}
				else
				{
					ins_p1 = 15f;
					ins_p2 = 175f;
					ins_y = (float)(90f*(1f-((p_anim-0.8)/0.2f)));
				}
			}
		}

		/*

		 */

		EmplacementRenderer.renderInserter(ins_y, ins_p1, ins_p2, ins_progress, r);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		for(ModelRendererTurbo mod : EmplacementRenderer.modelAutocannon.ammoBoxLidModel)
		{
			if(reloadAnim < 0.1)
				mod.rotateAngleZ = (reloadAnim/0.1f)*-4.125f;
			else if(reloadAnim < 0.9f)
				mod.rotateAngleZ = -4.125f;
			else if(reloadAnim < 1f)
				mod.rotateAngleZ = (1f-((reloadAnim-0.9f)/0.1f))*-4.125f;
			else
				mod.rotateAngleZ = 0;
			mod.render();
		}
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}
}
