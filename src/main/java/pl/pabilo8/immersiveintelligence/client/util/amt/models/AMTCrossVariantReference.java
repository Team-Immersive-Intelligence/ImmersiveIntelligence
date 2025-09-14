package pl.pabilo8.immersiveintelligence.client.util.amt.models;

import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;

import java.util.HashMap;
import java.util.Map;

/**
 * References an AMT part inside a {@link AMTCachedModel}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.02.2023
 */
public class AMTCrossVariantReference<T extends AMT>
{
	private final String name;
	private final AMTCachedModel<?> model;
	private final Map<AMTModel, T> references;

	public AMTCrossVariantReference(String name, AMTCachedModel<?> model)
	{
		this.name = name;
		this.model = model;
		references = new HashMap<>();
	}

	public T get()
	{
		AMTModel last = model.getLast();

		//Return already mapped
		if(references.containsKey(last))
			return references.get(last);
		if(last==null)
			return null;

		//Search and add to map if not
		@SuppressWarnings("unchecked")
		T part = (T)last.getPart(name);
		references.put(last, part);
		return part;
	}
}
