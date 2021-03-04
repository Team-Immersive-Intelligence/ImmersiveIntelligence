package pl.pabilo8.immersiveintelligence.common.ammunition_system.emplacement_weapons;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.EmplacementRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

public class EmplacementWeaponCPDS extends EmplacementWeapon
{
	/**
	 * CPDS Q&A
	 *
	 * Q: Is CPDS a real life thing?
	 * A: Not really, it's based on CIWS, but in II it performs a counter-projectile role with anti-aircraft as secondary task
	 *
	 * Q: What does CPDS stand for?
	 * A: Counter-Projectile Defense System
	 *
	 * Q: Why gatling?
	 * A: It's the most high-tech II can get ^^ Historically, gatling guns were used since the US Civil War, so yes, they existed in interwar/ww2
	 * Decided to choose it because of the unique design
	 *
	 * Q: Isn't it OP? it's 8 barrels
	 * A: It can't shoot at ground targets, only air, so yes, but it isn't invincible
	 */

	@Override
	public String getName()
	{
		return "cpds";
	}

	@Override
	public IngredientStack[] getIngredientsRequired()
	{
		return new IngredientStack[]{
				new IngredientStack(new ItemStack(IEContent.itemMaterial, 4, 15)),
				new IngredientStack("blockSteel", 1),
				new IngredientStack("plateSteel", 4),
				new IngredientStack(new ItemStack(IIContent.blockMetalDevice, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta())),
				new IngredientStack("engineElectricSmall", 2)
		};
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		//Only pitch, no yaw rotation
		nextPitch = pitch;
		float p = pitch-this.pitch;
		this.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, this.pitchTurnSpeed);
		this.pitch = this.pitch%180;
	}

	public boolean isSetUp(boolean door)
	{
		return true;
	}

	@Override
	public void tick()
	{

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
	public boolean canShoot(TileEntityEmplacement te)
	{
		return te.isDoorOpened;
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
		GlStateManager.pushMatrix();
		float p, pp, y, yy;
		p = this.nextPitch-this.pitch;
		y = this.nextYaw-this.yaw;
		pp = pitch+Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, 1)*partialTicks*pitchTurnSpeed;
		yy = yaw+Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, 1)*partialTicks*yawTurnSpeed;

		//setupProgress = ((te.getWorld().getTotalWorldTime()+partialTicks)%100)/100f;
		ClientUtils.bindTexture(EmplacementRenderer.textureCPDS);

		for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.baseModel)
			mod.render();

		GlStateManager.translate(0, 24/16f, 3/16f);
		GlStateManager.rotate(pp, 1, 0, 0);
		GlStateManager.rotate(yy, 0, 1, 0);
		for(ModelRendererTurbo mod : EmplacementRenderer.modelInfraredObserver.observerModel)
			mod.render();

		GlStateManager.popMatrix();
	}
}
