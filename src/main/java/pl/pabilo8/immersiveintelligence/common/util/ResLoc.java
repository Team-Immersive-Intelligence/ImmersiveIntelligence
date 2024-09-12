package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

/**
 * Pattern based, more programmer friendly extension of {@link net.minecraft.util.ResourceLocation}
 *
 * @author Pabilo8
 * @since 13.02.2023
 */
public class ResLoc extends ResourceLocation
{
	//--- Extensions ---//
	public static final String EXT_OBJ = ".obj";
	public static final String EXT_MTL = ".mtl";
	public static final String EXT_OBJAMT = ".obj.amt";
	public static final String EXT_JSON = ".json";
	public static final String EXT_PNG = ".png";
	public static final String EXT_FX_AMT = ".fx.amt";
	public static final String EXT_AMT = ".amt";

	//--- Instance Variables ---//
	private final String extension;
	private final int extensionIndex;

	private ResLoc(String domain, String path, String extension, int extensionIndex)
	{
		super(domain, path);
		this.extension = extension;
		this.extensionIndex = extensionIndex;
	}

	private ResLoc(String domain, String path)
	{
		super(domain, path);
		this.extensionIndex = path.lastIndexOf('.');
		this.extension = extensionIndex==-1?"": path.substring(extensionIndex);
	}

	/**
	 * @return file type extension of this {@link ResLoc}
	 */
	public String getExtension()
	{
		return extension;
	}

	/**
	 * @param domain new domain
	 * @return new {@link ResLoc} based on this, but with another domain
	 */
	public ResLoc withDomain(String domain)
	{
		return new ResLoc(domain, this.resourcePath, this.extension, this.extensionIndex);
	}

	public ResLoc asDirectory()
	{
		int i = this.resourcePath.lastIndexOf('/');
		if(i==-1)
			return this;
		return new ResLoc(this.resourceDomain, this.resourcePath.substring(0, i)+"/");
	}

	/**
	 * @param extension new extension
	 * @return new {@link ResLoc} based on this, but with another file extension
	 */
	public ResLoc withExtension(String extension)
	{
		if(extensionIndex==-1)
			return new ResLoc(resourceDomain, resourcePath+extension, extension, resourcePath.length());
		return new ResLoc(resourceDomain, resourcePath.substring(0, extensionIndex)+extension, extension, extensionIndex);
	}

	/**
	 * @param domain domain of this ResLoc
	 * @return a base {@link ResLoc}
	 */
	public static ResLoc root(String domain)
	{
		return new ResLoc(domain, "");
	}

	public static ResLoc of(ResLoc blueprint, Object... elements)
	{
		if(!blueprint.resourcePath.contains("%"))
		{
			StringBuilder builder = new StringBuilder(blueprint.resourcePath);
			for(Object e : elements)
				builder.append(e);

			return new ResLoc(blueprint.resourceDomain, builder.toString());
		}

		return new ResLoc(blueprint.resourceDomain,
				String.format(blueprint.resourcePath, elements));
	}

	public static ResLoc of(ResourceLocation res)
	{
		return new ResLoc(res.getResourceDomain(), res.getResourcePath());
	}

	public ResLoc with(Object... elements)
	{
		return of(this, elements);
	}

	public ResLoc replace(String replace, String with)
	{
		return new ResLoc(resourceDomain, resourcePath.replace(replace, with));
	}

	public ModelResourceLocation getModelResLoc()
	{
		return new ModelResourceLocation(this.resourceDomain+":"+this.resourcePath);
	}
}
