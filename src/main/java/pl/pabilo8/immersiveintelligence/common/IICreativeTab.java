package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDecoration.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIRadioExplosives.ItemBlockRadioExplosives;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 2019-05-07
 */
public class IICreativeTab extends CreativeTabs
{
	public static List<Fluid> fluidBucketMap = new ArrayList<>();

	public IICreativeTab(String name)
	{
		super(name);
	}

	@Override
	@Nonnull
	public ItemStack getTabIconItem()
	{
		return IIContent.blockMetalDecoration.getStack(IIBlockTypes_MetalDecoration.COIL_DATA, 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list)
	{
		super.displayAllRelevantItems(list);

		addExampleBullets(list);

		for(Fluid fluid : fluidBucketMap)
			addFluidBucket(fluid, list);
	}

	public void addFluidBucket(Fluid fluid, NonNullList<ItemStack> list)
	{
		UniversalBucket bucket = ForgeModContainer.getInstance().universalBucket;
		ItemStack stack = new ItemStack(bucket);
		FluidStack fs = new FluidStack(fluid, bucket.getCapacity());
		IFluidHandlerItem fluidHandler = new FluidBucketWrapper(stack);
		if(fluidHandler.fill(fs, true)==fs.amount)
			list.add(fluidHandler.getContainer());
	}

	public void addExampleBullets(NonNullList<ItemStack> list)
	{
		//add generic artillery ammo
		for(IAmmo bullet : new IAmmo[]{IIContent.itemAmmoArtillery, IIContent.itemAmmoLightArtillery, IIContent.itemAmmoMortar})
			for(String[] core : new String[][]{{"core_brass", "canister"}, {"core_tungsten", "piercing"}, {"core_steel", "shaped"}})
				for(String explosive : new String[]{"tnt", "rdx", "hmx"})
					list.add(bullet.getBulletWithParams(core[0], core[1], explosive, "tracer_powder"));

		//add custom artillery ammo examples
		list.add(IIContent.itemAmmoArtillery.getBulletWithParams("core_brass", "canister", "hmx", "white_phosphorus").setStackDisplayName("Phosphorgranate mk. 1"));
		list.add(IIContent.itemAmmoArtillery.getBulletWithParams("core_brass", "canister", "fluid_napalm").setStackDisplayName("Napalmgranate mk. 1"));
		list.add(IIContent.itemAmmoArtillery.getBulletWithParams("core_brass", "canister", "nuke").setStackDisplayName("Geburtstagsgranate mk.1"));

		//add grenades
		list.add(IIContent.itemGrenade.getBulletWithParams("core_brass", "canister", "tnt").setStackDisplayName("Stielhandgranate mk.1"));
		list.add(IIContent.itemGrenade.getBulletWithParams("core_brass", "canister", "white_phosphorus").setStackDisplayName("Phosphorhandgranate mk.1"));
		list.add(IIContent.itemGrenade.getBulletWithParams("core_brass", "canister", "rdx").setStackDisplayName("Sprenghandgranate mk.1"));
		list.add(IIContent.itemGrenade.getBulletWithParams("core_brass", "canister", "hmx").setStackDisplayName("Sprenghandgranate mk.2"));
		list.add(IIContent.itemGrenade.getBulletWithParams("core_brass", "canister", "gas_chlorine").setStackDisplayName("Chemhandgranate mk.1"));
		//firework grenade
		ItemStack grenade_firework = IIContent.itemGrenade.getBulletWithParams("core_brass", "canister", "firework").setStackDisplayName("Feuerwerkhandgranate mk.1");
		IIContent.itemGrenade.setComponentNBT(grenade_firework, EasyNBT.parseNBT("{Explosion:{Type:0b,Colors:[I;3887386]}}"));

		//railgun grenades
		list.add(IIContent.itemRailgunGrenade.getBulletWithParams("core_brass", "canister", "gas_chlorine")
				.setStackDisplayName("Schienenkanone Chemgranate mk.1"));
		list.add(IIContent.itemRailgunGrenade.getBulletWithParams("core_brass", "canister", "rdx")
				.setStackDisplayName("Schienenkanone Sprenggranate mk.1"));
		//firework railgun grenade
		list.add(grenade_firework);

		//revolver bullets
		list.add(IIContent.itemAmmoRevolver.getBulletWithParams("core_brass", "canister", "white_phosphorus")
				.setStackDisplayName("Flammpatrone mk.1"));
		list.add(IIContent.itemAmmoRevolver.getBulletWithParams("core_tungsten", "piercing")
				.setStackDisplayName("Wolframpatrone mk.1"));
		ItemStack bullet_tracer = IIContent.itemAmmoRevolver.getBulletWithParams("core_brass", "canister", "tracer_powder")
				.setStackDisplayName("Markierungspatrone mk.1");
		IIContent.itemAmmoRevolver.setComponentNBT(bullet_tracer, EasyNBT.parseNBT("{colour:3887386}"));
		list.add(bullet_tracer);

		ItemStack bullet1, bullet2, bullet3, bullet4, bullet5;

		list.add(bullet1 = IIContent.itemAmmoMachinegun.getBulletWithParams("core_brass", "softpoint", "tnt").setStackDisplayName("Sprengpatrone mk.1"));
		list.add(bullet2 = IIContent.itemAmmoMachinegun.getBulletWithParams("core_tungsten", "piercing", "shrapnel_tungsten").setStackDisplayName("Wolframpatrone mk.1"));
		list.add(bullet3 = IIContent.itemAmmoMachinegun.getBulletWithParams("core_steel", "piercing").setStackDisplayName("Stahlpatrone mk.1"));
		list.add(bullet4 = IIContent.itemAmmoMachinegun.getBulletWithParams("core_brass", "softpoint", "white_phosphorus").setStackDisplayName("Phosphorpatrone mk.1"));
		list.add(IIContent.itemBulletMagazine.getMagazine(Magazines.MACHINEGUN, bullet1, bullet2, bullet3, bullet4));

		list.add(IIContent.itemBulletMagazine.getMagazine(Magazines.SUBMACHINEGUN,
				IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_brass", "softpoint", "tnt").setStackDisplayName("Sprengpatrone mk.1"),
				IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_tungsten", "piercing", "shrapnel_tungsten").setStackDisplayName("Wolframpatrone mk.1"),
				IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_steel", "piercing").setStackDisplayName("Stahlpatrone mk.1"),
				IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_brass", "softpoint", "white_phosphorus").setStackDisplayName("Phosphorpatrone mk.1"))
		);

		list.add(bullet5 = IIContent.itemAmmoMachinegun.getBulletWithParams("core_uranium", "piercing", "shrapnel_uranium").setStackDisplayName("Uraniumpatrone mk.1"));
		list.add(IIContent.itemBulletMagazine.getMagazine(Magazines.MACHINEGUN, bullet5));

		//add tracer magazines
		for(Magazines magazine : Magazines.values())
			if(!IIContent.itemBulletMagazine.isMetaHidden(magazine.getMeta()))
				list.add(getColorMagazine(magazine, 65327, 16711680, 25343, 16772608));

		assert IIContent.blockRadioExplosives.itemBlock instanceof ItemBlockMineBase;
		assert IIContent.blockTellermine.itemBlock instanceof ItemBlockMineBase;
		assert IIContent.blockTripmine.itemBlock instanceof ItemBlockMineBase;

		list.add(((ItemBlockMineBase)IIContent.blockRadioExplosives.itemBlock)
				.getBulletWithParams("core_brass", "canister", "tnt").setStackDisplayName("Radio-Sprengstoff TNT mk.1"));
		list.add(((ItemBlockMineBase)IIContent.blockRadioExplosives.itemBlock)
				.getBulletWithParams("core_brass", "canister", "white_phosphorus").setStackDisplayName("Radio-Sprengstoff Phosphor mk.1"));

		list.add(((ItemBlockMineBase)IIContent.blockTellermine.itemBlock)
				.getBulletWithParams("core_brass", "softpoint", "tnt").setStackDisplayName("Tellermine mk.1"));
		list.add(((ItemBlockMineBase)IIContent.blockTripmine.itemBlock)
				.getBulletWithParams("core_brass", "softpoint", "tnt").setStackDisplayName("SD-Mine mk.1"));
		list.add(IIContent.itemNavalMine.getBulletWithParams("core_brass", "softpoint", "rdx").setStackDisplayName("Seemine mk.1"));
	}

	ItemStack getColorMagazine(Magazines magazine, int... colors)
	{
		ItemStack[] bullets = new ItemStack[colors.length];
		for(int i = 0; i < colors.length; i++)
		{
			//get bullet
			ItemStack stack = magazine.ammo.getBulletWithParams("core_steel", "softpoint", "tracer_powder")
					.setStackDisplayName(getGermanColorName(colors[i])+"markierungspatrone");
			//tracer color
			magazine.ammo.setComponentNBT(stack, EasyNBT.parseNBT("{colour: %s}", colors[i]));

			//paint color
			magazine.ammo.setPaintColour(stack, colors[i]);

			//set in array
			bullets[i] = stack;
		}

		return IIContent.itemBulletMagazine.getMagazine(magazine, bullets);
	}

	/**
	 * Deutsche Qualität
	 */
	private String getGermanColorName(int color)
	{
		switch(IIUtils.getRGBTextFormatting(color))
		{
			case WHITE:
				return "Weiß";
			case ORANGE:
				return "Orange";
			case MAGENTA:
				return "Magenta";
			case LIGHT_BLUE:
				return "Hellblau";
			case YELLOW:
				return "Gelb";
			case LIME:
				return "Lime";
			case PINK:
				return "Rosa";
			case GRAY:
				return "Grau";
			case SILVER:
				return "Silber";
			case CYAN:
				return "Cyan";
			case PURPLE:
				return "Purpur";
			case BLUE:
				return "Blau";
			case BROWN:
				return "Braun";
			case GREEN:
				return "Grün";
			case RED:
				return "Rot";
			default:
			case BLACK:
				return "Schwarz";
		}
	}
}
