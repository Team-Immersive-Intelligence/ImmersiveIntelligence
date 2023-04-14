package pl.pabilo8.immersiveintelligence.client.util.amt;

import java.util.HashMap;
import java.util.Map;

/**
 * References an AMT part inside a {@link AMTModelCache}
 *
 * @author Pabilo8
 * @since 17.02.2023
 */
public class AMTCrossVariantReference<T extends AMT>
{
	private final String name;
	private final AMTModelCache<?> model;
	private final Map<AMT[], T> references;

	public AMTCrossVariantReference(String name, AMTModelCache<?> model)
	{
		this.name = name;
		this.model = model;
		references = new HashMap<>();
	}

	public T get()
	{
		AMT[] last = model.getLast();

		//Return already mapped
		if(references.containsKey(last))
			return references.get(last);

		//Search and add to map if not
		T part = (T)IIAnimationUtils.getPart(last, name);
		references.put(last, part);
		return part;
	}
}
