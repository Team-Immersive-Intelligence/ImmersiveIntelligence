package pl.pabilo8.immersiveintelligence.common.asm;

import com.google.common.collect.Maps;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author BluSunrize - 20.07.2017
 *
 * @author Pabilo8
 * @since 23.09.2020
 */
public class IIClassTransformer implements IClassTransformer
{
	private static Map<String, MethodTransformer[]> transformerMap = Maps.newHashMap();

	static
	{
		//Allows to change player/mob animation (used by items)
		transformerMap.put("net.minecraft.client.model.ModelBiped", new MethodTransformer[]{
				new MethodTransformer("setRotationAngles", "func_78087_a", "(FFFFFFLnet/minecraft/entity/Entity;)V", methodNode ->
				{
					Iterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
					while(iterator.hasNext())
					{
						AbstractInsnNode anode = iterator.next();
						if(anode.getOpcode()==Opcodes.RETURN)
						{
							InsnList newInstructions = new InsnList();
							newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
							newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 7));
							newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "pl/pabilo8/immersiveintelligence/client/ClientEventHandler", "handleBipedRotations", "(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/entity/Entity;)V", false));
							methodNode.instructions.insertBefore(anode, newInstructions);
						}
					}
				})
		});

		// TODO: 26.11.2020 night vision
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(basicClass!=null&&transformerMap.containsKey(transformedName))
		{
			MethodTransformer[] transformers = transformerMap.get(transformedName);
			ClassReader reader = new ClassReader(basicClass);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);

			for(MethodNode method : node.methods)
				for(MethodTransformer methodTransformer : transformers)
					if((methodTransformer.functionName.equals(method.name)||methodTransformer.srgName.equals(method.name))&&methodTransformer.functionDesc.equals(method.desc))
						methodTransformer.function.accept(method);

			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
			node.accept(writer);
			return writer.toByteArray();
		}
		return basicClass;
	}

	private static class MethodTransformer
	{
		final String functionName;
		final String srgName;
		final String functionDesc;
		final Consumer<MethodNode> function;

		private MethodTransformer(String funcName, String srgName, String funcDesc, Consumer<MethodNode> function)
		{
			this.functionName = funcName;
			this.srgName = srgName;
			this.functionDesc = funcDesc;
			this.function = function;
		}
	}
}