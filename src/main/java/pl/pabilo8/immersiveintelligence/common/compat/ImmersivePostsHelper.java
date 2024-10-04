package pl.pabilo8.immersiveintelligence.common.compat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialRod.MaterialsRod;
import pl.pabilo8.immersiveintelligence.common.util.block.IIIStateMappings.IIISingleMetaStateMappings;
import twistedgate.immersiveposts.ImmersivePosts;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.enums.EnumPostMaterial;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class ImmersivePostsHelper extends IICompatModule
{
	private BlockFence brassFence;
	private BlockFence tungstenFence;
	private BlockFence zincFence;
	private BlockFence platinumFence;
	private BlockFence duraluminiumFence;

	@Override
	public String getName()
	{
		return "ImmersivePosts";
	}

	/**
	 * <b>Potentially</b> Dangerous alternative prefix `immersiveposts` for name `*insert metal name*`, expected `immersiveintelligence`.
	 * This could be a intended override, but in <s>most cases</s> <b>no cases at all</b> indicates a broken mod.
	 * ( ^^ )
	 */
	@Override
	public void preInit()
	{
		brassFence = createFence("brass");
		tungstenFence = createFence("tungsten");
		zincFence = createFence("zinc");
		platinumFence = createFence("platinum");
		duraluminiumFence = createFence("duraluminium");

		try
		{
			IIContent.itemMaterialRod.setMetaUnhidden(MaterialsRod.ZINC, MaterialsRod.PLATINUM);

			Constructor<EnumPostMaterial> constructor = EnumPostMaterial.class.getDeclaredConstructor(String.class, int.class, String.class, Block.class, boolean.class, boolean.class);
			constructor.setAccessible(true);

			EnumPostMaterial postMatBrass = addPostMaterial("brass", brassFence);
			EnumPostMaterial postMatTungsten = addPostMaterial("tungsten", tungstenFence);
			EnumPostMaterial postMatZinc = addPostMaterial("zinc", zincFence);
			EnumPostMaterial postMatPlatinum = addPostMaterial("platinum", platinumFence);
			EnumPostMaterial postMatDuraluminium = addPostMaterial("duraluminium", duraluminiumFence);

			BlockPost brassPost = createMetalPost(postMatBrass);
			BlockPost tungstenPost = createMetalPost(postMatTungsten);
			BlockPost zincPost = createMetalPost(postMatZinc);
			BlockPost platinumPost = createMetalPost(postMatPlatinum);
			BlockPost duraluminiumPost = createMetalPost(postMatDuraluminium);

			IILogger.info("It's fine, everything's fine. (no really, don't report it to me or TwistedGate)");

			// TODO: 28.07.2021 either poke Twisted about adding an api (on 1.12? are you serious?) or do the asm
			/*
			ClassReader reader = new ClassReader(EnumPostMaterial.class.getName());
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
			Method m = EnumPostMaterial.class.getDeclaredMethod("getPostStateFrom", ItemStack.class);

			MethodVisitor methodVisitor = writer.visitMethod(Opcodes.ACC_PUBLIC| Opcodes.ACC_STATIC,
					"getPostStateFrom",
					Type.getMethodDescriptor(m),
					null,
					null
			);
			 */

		} catch(NoSuchMethodException e) // |IOException
		{
			IILogger.info("Couldn't add Immersive Posts Compat :<");
		}
	}

	@Override
	public void registerRecipes()
	{
		OreDictionary.registerOre("fenceBrass", new ItemStack(brassFence));
		OreDictionary.registerOre("fenceTungsten", new ItemStack(tungstenFence));
		OreDictionary.registerOre("fenceZinc", new ItemStack(zincFence));
		OreDictionary.registerOre("fencePlatinum", new ItemStack(platinumFence));
		OreDictionary.registerOre("fenceDuraluminium", new ItemStack(duraluminiumFence));
	}

	@Override
	public void init()
	{

	}

	@Override
	public void postInit()
	{

	}

	@Optional.Method(modid = "immersiveposts")
	private static BlockPost createMetalPost(EnumPostMaterial postMat)
	{
		return new BlockPost(Material.IRON, postMat);
	}

	private static BlockFence createFence(String name)
	{
		return new BlockMetalFence(name);
	}

	public static class BlockMetalFence extends BlockFence implements IIISingleMetaStateMappings
	{
		public final String rawName;
		public ItemBlock itemBlock;

		public BlockMetalFence(String name)
		{
			this(name, Material.IRON);
		}

		private BlockMetalFence(String name, Material mat)
		{
			super(mat, mat.getMaterialMapColor());
			this.rawName = name;
			//setRegistryName(new ResourceLocation(ImmersiveIntelligence.MODID, name));
			setUnlocalizedName(ImmersiveIntelligence.MODID+".fence_"+name);
			setHardness(3.0F);
			setResistance(15.0F);
			setCreativeTab(ImmersivePosts.creativeTab);

			IIContent.BLOCKS.add(this);
			itemBlock = (ItemBlock)new ItemBlock(this).setUnlocalizedName(ImmersiveIntelligence.MODID+".fence_"+name);
			IIContent.ITEMS.add(itemBlock);
		}

		@Override
		public String getMappingsName()
		{
			return "fence/"+rawName;
		}
	}

	@Nullable
	@Deprecated
	@Optional.Method(modid = "immersiveposts")
	public static EnumPostMaterial addPostMaterial(String name, Block block)
	{
		return EnumHelper.addEnum(EnumPostMaterial.class, name.toUpperCase(),
				new Class[]{
						String.class, Block.class, boolean.class, boolean.class
				},
				name+"post", block, true, true
		);
	}
}
