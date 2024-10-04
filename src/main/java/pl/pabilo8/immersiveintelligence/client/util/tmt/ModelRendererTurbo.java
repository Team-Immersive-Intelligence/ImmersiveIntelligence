package pl.pabilo8.immersiveintelligence.client.util.tmt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * <p>
 * <h1>History</h1>
 * II's old Rendering System borrowed from Flan's Mod.
 * A piece of history, if you could call code that.
 * <br><br>
 * It goes back to when I (Pabilo8) started my modding adventure by adding new vehicles to Flan's Mod, then improving the main mod itself.
 * I really took a liking to the rendering system there, because it was simple and had a great model editor – SMP Toolbox.
 * And so, when time came, I decided to use it in my own mod Immersive Intelligence, and it was a great choice, at least for the time.
 * <br><br>
 * But as time went on, I realized the flaws and low performance of it compared to what could have been possible, then the final nail to the coffin – SMP Toolbox stopped working on modern systems.
 * This leaded me to create my own rendering system, which is now called {@link pl.pabilo8.immersiveintelligence.client.util.amt.AMT}.
 * <br><br>
 * However, many models in Immersive Intelligence still use this system, and despite the efforts, it will take some time to convert them all to the new system.
 * </p>
 * <p>
 * <h1>Original Description by GaryCXJk</h1>
 * An extension to the ModelRenderer class. It basically is a copy to ModelRenderer,
 * however, it contains various new methods to make your models.
 * <br><br>
 * Since the ModelRendererTurbo class gets loaded during startup, the models made
 * can be very complex. This is why I can afford to add, for example, Wavefront OBJ
 * support or have the addSprite method, methods that add a lot of vertices and
 * polygons.
 * </p>
 *
 * @author GaryCXJk
 * @author Pabilo8
 */
@Deprecated
public class ModelRendererTurbo extends ModelRenderer
{
	/**
	 * Vertices of this TMT
	 */
	private PositionTextureVertex[] vertices;
	/**
	 * Faces of this TMT
	 */
	private TexturedPolygon[] faces;
	/**
	 * Texture Offsets of this TMT
	 */
	private int textureOffsetX, textureOffsetY;
	/**
	 * Whether this TMT has been compiled into GL Display Lists
	 */
	private boolean compiled;
	/**
	 * GL Display List ID
	 */
	public boolean forcedRecompile;
	/**
	 * Array of compiled GL display lists for each texture group
	 */
	private int[] displayListArray;
	/**
	 * Map of texture groups
	 */
	private final Map<String, TextureGroup> textureGroup;
	/**
	 * Current texture group
	 */
	private TextureGroup currentTextureGroup;
	/**
	 * Whether this TMT is mirrored (X-axis)
	 */
	public boolean mirror;
	/**
	 * Whether this TMT is mirrored (Y-axis)
	 */
	public boolean flip;
	/**
	 * Whether this TMT and its children are visible
	 */
	public boolean showModel;
	/**
	 * Whether this uses the vanilla {@link #offsetX}, {@link #offsetY} and {@link #offsetZ} fields for element offset
	 */
	public boolean hasOffset = false;
	/**
	 * List of child models
	 */
	public ArrayList<ModelRenderer> childModels;

	public static final int MR_FRONT = 0;
	public static final int MR_BACK = 1;
	public static final int MR_LEFT = 2;
	public static final int MR_RIGHT = 3;
	public static final int MR_TOP = 4;
	public static final int MR_BOTTOM = 5;

	private static final float pi = (float)Math.PI;

	public ModelRendererTurbo(ModelBase modelbase)
	{
		super(modelbase);
		flip = false;
		compiled = false;
		mirror = false;
		showModel = true;
		vertices = new PositionTextureVertex[0];
		faces = new TexturedPolygon[0];
		forcedRecompile = false;
		textureGroup = new HashMap<>();
		textureGroup.put("0", new TextureGroup());
		currentTextureGroup = textureGroup.get("0");
	}

	/**
	 * Creates a new ModelRenderTurbo object. It requires the coordinates of the
	 * position of the texture.
	 *
	 * @param modelbase
	 * @param textureX  the x-coordinate on the texture
	 * @param textureY  the y-coordinate on the texture
	 */
	public ModelRendererTurbo(ModelBase modelbase, int textureX, int textureY)
	{
		this(modelbase, textureX, textureY, 64, 32);
	}

	/**
	 * Creates a new ModelRenderTurbo object. It requires the coordinates of the
	 * position of the texture, but also allows you to specify the width and height
	 * of the texture, allowing you to use bigger textures instead.
	 *
	 * @param modelbase
	 * @param textureX
	 * @param textureY
	 * @param textureU
	 * @param textureV
	 */
	public ModelRendererTurbo(ModelBase modelbase, int textureX, int textureY, int textureU, int textureV)
	{
		this(modelbase);
		textureOffsetX = textureX;
		textureOffsetY = textureY;
		textureWidth = textureU;
		textureHeight = textureV;
	}

	private TexturedPolygon addPolygonReturn(PositionTextureVertex[] verts, int u1, int v1, int u2, int v2, float q1, float q2, float q3, float q4)
	{
		if(verts.length < 3)
			return null;
		float uOffs = 1.0F/(textureWidth*10.0F);
		float vOffs = 1.0F/(textureHeight*10.0F);
		if(verts.length < 4)
		{
			float xMin = -1;
			float yMin = -1;
			float xMax = 0;
			float yMax = 0;

			for(PositionTextureVertex vert : verts)
			{
				float xPos = vert.texturePositionX;
				float yPos = vert.texturePositionY;
				xMax = Math.max(xMax, xPos);
				xMin = (xMin < -1?xPos: Math.min(xMin, xPos));
				yMax = Math.max(yMax, yPos);
				yMin = (yMin < -1?yPos: Math.min(yMin, yPos));
			}
			float uMin = u1/textureWidth+uOffs;
			float vMin = v1/textureHeight+vOffs;
			float uSize = (u2-u1)/textureWidth-uOffs*2;
			float vSize = (v2-v1)/textureHeight-vOffs*2;

			float xSize = xMax-xMin;
			float ySize = yMax-yMin;
			for(int i = 0; i < verts.length; i++)
			{
				float xPos = verts[i].texturePositionX;
				float yPos = verts[i].texturePositionY;
				xPos = (xPos-xMin)/xSize;
				yPos = (yPos-yMin)/ySize;
				verts[i] = verts[i].setTexturePosition(uMin+(xPos*uSize), vMin+(yPos*vSize));
			}
		}
		else
		{
			verts[0] = verts[0].setTexturePosition((u2/textureWidth-uOffs)*q1, (v1/textureHeight+vOffs)*q1, q1);
			verts[1] = verts[1].setTexturePosition((u1/textureWidth+uOffs)*q2, (v1/textureHeight+vOffs)*q2, q2);
			verts[2] = verts[2].setTexturePosition((u1/textureWidth+uOffs)*q3, (v2/textureHeight-vOffs)*q3, q3);
			verts[3] = verts[3].setTexturePosition((u2/textureWidth-uOffs)*q4, (v2/textureHeight-vOffs)*q4, q4);
		}
		return new TexturedPolygon(verts);
	}

	private TexturedPolygon addPolygonReturn(PositionTextureVertex[] verts, int u1, int v1, int u2, int v2)
	{
		if(verts.length < 3)
			return null;
		float uOffs = 1.0F/(textureWidth*10.0F);
		float vOffs = 1.0F/(textureHeight*10.0F);
		if(verts.length < 4)
		{
			float xMin = -1;
			float yMin = -1;
			float xMax = 0;
			float yMax = 0;

			for(PositionTextureVertex vert : verts)
			{
				float xPos = vert.texturePositionX;
				float yPos = vert.texturePositionY;
				xMax = Math.max(xMax, xPos);
				xMin = (xMin < -1?xPos: Math.min(xMin, xPos));
				yMax = Math.max(yMax, yPos);
				yMin = (yMin < -1?yPos: Math.min(yMin, yPos));
			}
			float uMin = u1/textureWidth+uOffs;
			float vMin = v1/textureHeight+vOffs;
			float uSize = (u2-u1)/textureWidth-uOffs*2;
			float vSize = (v2-v1)/textureHeight-vOffs*2;

			float xSize = xMax-xMin;
			float ySize = yMax-yMin;
			for(int i = 0; i < verts.length; i++)
			{
				float xPos = verts[i].texturePositionX;
				float yPos = verts[i].texturePositionY;
				xPos = (xPos-xMin)/xSize;
				yPos = (yPos-yMin)/ySize;
				verts[i] = verts[i].setTexturePosition(uMin+(xPos*uSize), vMin+(yPos*vSize));
			}
		}
		else
		{
			verts[0] = verts[0].setTexturePosition(u2/textureWidth-uOffs, v1/textureHeight+vOffs);
			verts[1] = verts[1].setTexturePosition(u1/textureWidth+uOffs, v1/textureHeight+vOffs);
			verts[2] = verts[2].setTexturePosition(u1/textureWidth+uOffs, v2/textureHeight-vOffs);
			verts[3] = verts[3].setTexturePosition(u2/textureWidth-uOffs, v2/textureHeight-vOffs);
		}
		return new TexturedPolygon(verts);
	}

	/**
	 * Adds a rectangular shape. Basically, you can make any eight-pointed shape you want,
	 * as the method requires eight vector coordinates.
	 *
	 * @param v  a float array with three values, the x, y and z coordinates of the vertex
	 * @param v1 a float array with three values, the x, y and z coordinates of the vertex
	 * @param v2 a float array with three values, the x, y and z coordinates of the vertex
	 * @param v3 a float array with three values, the x, y and z coordinates of the vertex
	 * @param v4 a float array with three values, the x, y and z coordinates of the vertex
	 * @param v5 a float array with three values, the x, y and z coordinates of the vertex
	 * @param v6 a float array with three values, the x, y and z coordinates of the vertex
	 * @param v7 a float array with three values, the x, y and z coordinates of the vertex
	 * @param w  the width of the shape, used in determining the texture
	 * @param h  the height of the shape, used in determining the texture
	 * @param d  the depth of the shape, used in determining the texture
	 */
	public void addRectShape(float[] v, float[] v1, float[] v2, float[] v3, float[] v4, float[] v5, float[] v6, float[] v7, int w, int h, int d)
	{
		float[] var1 = new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		addRectShape(v, v1, v2, v3, v4, v5, v6, v7, w, h, d, var1);
	}

	/**
	 * Adds a rectangular shape. Basically, you can make any eight-pointed shape you want,
	 * as the method requires eight vector coordinates. Also does some special texture mapping.
	 *
	 * @param v      a float array with three values, the x, y and z coordinates of the vertex
	 * @param v1     a float array with three values, the x, y and z coordinates of the vertex
	 * @param v2     a float array with three values, the x, y and z coordinates of the vertex
	 * @param v3     a float array with three values, the x, y and z coordinates of the vertex
	 * @param v4     a float array with three values, the x, y and z coordinates of the vertex
	 * @param v5     a float array with three values, the x, y and z coordinates of the vertex
	 * @param v6     a float array with three values, the x, y and z coordinates of the vertex
	 * @param v7     a float array with three values, the x, y and z coordinates of the vertex
	 * @param w      the width of the shape, used in determining the texture
	 * @param h      the height of the shape, used in determining the texture
	 * @param d      the depth of the shape, used in determining the texture
	 * @param qParam Array containing the q parameters in the order xBack, xBottom, xFront, xTop, yBack, yFront, yLeft, yRight, zBottom, zLeft, zRight, zTop
	 */
	public void addRectShape(float[] v, float[] v1, float[] v2, float[] v3, float[] v4, float[] v5, float[] v6, float[] v7, int w, int h, int d, float[] qParam)
	{
		PositionTextureVertex[] verts = new PositionTextureVertex[8];
		TexturedPolygon[] poly = new TexturedPolygon[6];
		PositionTextureVertex positionTexturevertex = new PositionTextureVertex(v[0], v[1], v[2], 0.0F, 0.0F);
		PositionTextureVertex positionTexturevertex1 = new PositionTextureVertex(v1[0], v1[1], v1[2], 0.0F, 8F);
		PositionTextureVertex positionTexturevertex2 = new PositionTextureVertex(v2[0], v2[1], v2[2], 8F, 8F);
		PositionTextureVertex positionTexturevertex3 = new PositionTextureVertex(v3[0], v3[1], v3[2], 8F, 0.0F);
		PositionTextureVertex positionTexturevertex4 = new PositionTextureVertex(v4[0], v4[1], v4[2], 0.0F, 0.0F);
		PositionTextureVertex positionTexturevertex5 = new PositionTextureVertex(v5[0], v5[1], v5[2], 0.0F, 8F);
		PositionTextureVertex positionTexturevertex6 = new PositionTextureVertex(v6[0], v6[1], v6[2], 8F, 8F);
		PositionTextureVertex positionTexturevertex7 = new PositionTextureVertex(v7[0], v7[1], v7[2], 8F, 0.0F);
		verts[0] = positionTexturevertex;
		verts[1] = positionTexturevertex1;
		verts[2] = positionTexturevertex2;
		verts[3] = positionTexturevertex3;
		verts[4] = positionTexturevertex4;
		verts[5] = positionTexturevertex5;
		verts[6] = positionTexturevertex6;
		verts[7] = positionTexturevertex7;
		poly[0] = addPolygonReturn(new PositionTextureVertex[]{
						positionTexturevertex5, positionTexturevertex1, positionTexturevertex2, positionTexturevertex6
				}, textureOffsetX+d+w, textureOffsetY+d, textureOffsetX+d+w+d, textureOffsetY+d+h,
				1F, qParam[7], qParam[10]*qParam[7], qParam[10]);
		poly[1] = addPolygonReturn(new PositionTextureVertex[]{
						positionTexturevertex, positionTexturevertex4, positionTexturevertex7, positionTexturevertex3
				}, textureOffsetX, textureOffsetY+d, textureOffsetX+d, textureOffsetY+d+h,
				qParam[9]*qParam[6], qParam[9], 1F, qParam[6]);
		poly[2] = addPolygonReturn(new PositionTextureVertex[]{
						positionTexturevertex5, positionTexturevertex4, positionTexturevertex, positionTexturevertex1
				}, textureOffsetX+d, textureOffsetY, textureOffsetX+d+w, textureOffsetY+d,
				1F, qParam[8], qParam[1]*qParam[8], qParam[1]);
		poly[3] = addPolygonReturn(new PositionTextureVertex[]{
						positionTexturevertex2, positionTexturevertex3, positionTexturevertex7, positionTexturevertex6
				}, textureOffsetX+d+w, textureOffsetY, textureOffsetX+d+w+w, textureOffsetY+d,
				qParam[3], qParam[3]*qParam[11], qParam[11], 1F);
		poly[4] = addPolygonReturn(new PositionTextureVertex[]{
						positionTexturevertex1, positionTexturevertex, positionTexturevertex3, positionTexturevertex2
				}, textureOffsetX+d, textureOffsetY+d, textureOffsetX+d+w, textureOffsetY+d+h,
				qParam[0], qParam[0]*qParam[4], qParam[4], 1F);
		poly[5] = addPolygonReturn(new PositionTextureVertex[]{
						positionTexturevertex4, positionTexturevertex5, positionTexturevertex6, positionTexturevertex7
				}, textureOffsetX+d+w+d, textureOffsetY+d, textureOffsetX+d+w+d+w, textureOffsetY+d+h,
				qParam[2]*qParam[5], qParam[2], 1F, qParam[5]);
		if(mirror^flip)
			for(TexturedPolygon aPoly : poly)
				aPoly.flipFace();

		copyTo(verts, poly);
	}

	/**
	 * Adds a new box to the model.
	 *
	 * @param x the starting x-position
	 * @param y the starting y-position
	 * @param z the starting z-position
	 * @param w the width (over the x-direction)
	 * @param h the height (over the y-direction)
	 * @param d the depth (over the z-direction)
	 */
	@Override
	public ModelRendererTurbo addBox(float x, float y, float z, int w, int h, int d)
	{
		addBox(x, y, z, w, h, d, 0.0F);
		return this;
	}

	/**
	 * Adds a new box to the model.
	 *
	 * @param x         the starting x-position
	 * @param y         the starting y-position
	 * @param z         the starting z-position
	 * @param w         the width (over the x-direction)
	 * @param h         the height (over the y-direction)
	 * @param d         the depth (over the z-direction)
	 * @param expansion the expansion of the box. It increases the size in each direction by that many.
	 */
	@Override
	public void addBox(float x, float y, float z, int w, int h, int d, float expansion)
	{
		addBox(x, y, z, w, h, d, expansion, 1F);
	}

	/**
	 * Adds a new box to the model.
	 *
	 * @param x         the starting x-position
	 * @param y         the starting y-position
	 * @param z         the starting z-position
	 * @param w         the width (over the x-direction)
	 * @param h         the height (over the y-direction)
	 * @param d         the depth (over the z-direction)
	 * @param expansion the expansion of the box. It increases the size in each direction by that many. It's independent from the scale.
	 * @param scale
	 */
	public void addBox(float x, float y, float z, int w, int h, int d, float expansion, float scale)
	{
		float scaleX = w*scale;
		float scaleY = h*scale;
		float scaleZ = d*scale;

		float x1 = x+scaleX;
		float y1 = y+scaleY;
		float z1 = z+scaleZ;

		float expX = expansion+scaleX-w;
		float expY = expansion+scaleY-h;
		float expZ = expansion+scaleZ-d;

		x -= expX;
		y -= expY;
		z -= expZ;
		x1 += expansion;
		y1 += expansion;
		z1 += expansion;
		if(mirror)
		{
			float xTemp = x1;
			x1 = x;
			x = xTemp;
		}

		float[] v = {x, y, z};
		float[] v1 = {x1, y, z};
		float[] v2 = {x1, y1, z};
		float[] v3 = {x, y1, z};
		float[] v4 = {x, y, z1};
		float[] v5 = {x1, y, z1};
		float[] v6 = {x1, y1, z1};
		float[] v7 = {x, y1, z1};
		addRectShape(v, v1, v2, v3, v4, v5, v6, v7, w, h, d);
	}

	/**
	 * Adds a trapezoid-like shape. It's achieved by expanding the shape on one side.
	 * You can use the static variables <code>MR_RIGHT</code>, <code>MR_LEFT</code>,
	 * <code>MR_FRONT</code>, <code>MR_BACK</code>, <code>MR_TOP</code> and
	 * <code>MR_BOTTOM</code>.
	 *
	 * @param x           the starting x-position
	 * @param y           the starting y-position
	 * @param z           the starting z-position
	 * @param w           the width (over the x-direction)
	 * @param h           the height (over the y-direction)
	 * @param d           the depth (over the z-direction)
	 * @param scale       the "scale" of the box. It only increases the size in each direction by that many.
	 * @param bottomScale the "scale" of the bottom
	 * @param dir         the side the scaling is applied to
	 */
	public void addTrapezoid(float x, float y, float z, int w, int h, int d, float scale, float bottomScale, int dir)
	{
		float f4 = x+w;
		float f5 = y+h;
		float f6 = z+d;
		x -= scale;
		y -= scale;
		z -= scale;
		f4 += scale;
		f5 += scale;
		f6 += scale;

		int m = (mirror?-1: 1);
		if(mirror)
		{
			float f7 = f4;
			f4 = x;
			x = f7;
		}

		float[] v = {x, y, z};
		float[] v1 = {f4, y, z};
		float[] v2 = {f4, f5, z};
		float[] v3 = {x, f5, z};
		float[] v4 = {x, y, f6};
		float[] v5 = {f4, y, f6};
		float[] v6 = {f4, f5, f6};
		float[] v7 = {x, f5, f6};

		switch(dir)
		{
			case MR_RIGHT:
				v[1] -= bottomScale;
				v[2] -= bottomScale;
				v3[1] += bottomScale;
				v3[2] -= bottomScale;
				v4[1] -= bottomScale;
				v4[2] += bottomScale;
				v7[1] += bottomScale;
				v7[2] += bottomScale;
				break;
			case MR_LEFT:
				v1[1] -= bottomScale;
				v1[2] -= bottomScale;
				v2[1] += bottomScale;
				v2[2] -= bottomScale;
				v5[1] -= bottomScale;
				v5[2] += bottomScale;
				v6[1] += bottomScale;
				v6[2] += bottomScale;
				break;
			case MR_FRONT:
				v[0] -= m*bottomScale;
				v[1] -= bottomScale;
				v1[0] += m*bottomScale;
				v1[1] -= bottomScale;
				v2[0] += m*bottomScale;
				v2[1] += bottomScale;
				v3[0] -= m*bottomScale;
				v3[1] += bottomScale;
				break;
			case MR_BACK:
				v4[0] -= m*bottomScale;
				v4[1] -= bottomScale;
				v5[0] += m*bottomScale;
				v5[1] -= bottomScale;
				v6[0] += m*bottomScale;
				v6[1] += bottomScale;
				v7[0] -= m*bottomScale;
				v7[1] += bottomScale;
				break;
			case MR_TOP:
				v[0] -= m*bottomScale;
				v[2] -= bottomScale;
				v1[0] += m*bottomScale;
				v1[2] -= bottomScale;
				v4[0] -= m*bottomScale;
				v4[2] += bottomScale;
				v5[0] += m*bottomScale;
				v5[2] += bottomScale;
				break;
			case MR_BOTTOM:
				v2[0] += m*bottomScale;
				v2[2] -= bottomScale;
				v3[0] -= m*bottomScale;
				v3[2] -= bottomScale;
				v6[0] += m*bottomScale;
				v6[2] += bottomScale;
				v7[0] -= m*bottomScale;
				v7[2] += bottomScale;
				break;
		}

		float[] qValues = new float[]{
				Math.abs((v[0]-v1[0])/(v3[0]-v2[0])),
				Math.abs((v[0]-v1[0])/(v4[0]-v5[0])),
				Math.abs((v4[0]-v5[0])/(v7[0]-v6[0])),
				Math.abs((v3[0]-v2[0])/(v7[0]-v6[0])),

				Math.abs((v[1]-v3[1])/(v1[1]-v2[1])),
				Math.abs((v4[1]-v7[1])/(v5[1]-v6[1])),
				Math.abs((v[1]-v3[1])/(v4[1]-v7[1])),
				Math.abs((v1[1]-v2[1])/(v5[1]-v6[1])),

				Math.abs((v[2]-v4[2])/(v1[2]-v5[2])),
				Math.abs((v[2]-v4[2])/(v3[2]-v7[2])),
				Math.abs((v1[2]-v5[2])/(v2[2]-v6[2])),
				Math.abs((v3[2]-v7[2])/(v2[2]-v6[2]))
		};

		addRectShape(v, v1, v2, v3, v4, v5, v6, v7, w, h, d);
	}

	/**
	 * Adds a trapezoid-like shape. It's achieved by expanding the shape on one side.
	 * You can use the static variables <code>MR_RIGHT</code>, <code>MR_LEFT</code>,
	 * <code>MR_FRONT</code>, <code>MR_BACK</code>, <code>MR_TOP</code> and
	 * <code>MR_BOTTOM</code>.
	 *
	 * @param x       the starting x-position
	 * @param y       the starting y-position
	 * @param z       the starting z-position
	 * @param w       the width (over the x-direction)
	 * @param h       the height (over the y-direction)
	 * @param d       the depth (over the z-direction)
	 * @param scale   the "scale" of the box. It only increases the size in each direction by that many.
	 * @param bScale1 the "scale" of the bottom - Top
	 * @param bScale2 the "scale" of the bottom - Bottom
	 * @param bScale3 the "scale" of the bottom - Left
	 * @param bScale4 the "scale" of the bottom - Right
	 * @param dir     the side the scaling is applied to
	 */
	public void addFlexBox(float x, float y, float z, int w, int h, int d, float scale, float bScale1, float bScale2, float bScale3, float bScale4, int dir)
	{
		float f4 = x+w;
		float f5 = y+h;
		float f6 = z+d;
		x -= scale;
		y -= scale;
		z -= scale;
		f4 += scale;
		f5 += scale;
		f6 += scale;

		int m = (mirror?-1: 1);
		if(mirror)
		{
			float f7 = f4;
			f4 = x;
			x = f7;
		}

		float[] v = {x, y, z};
		float[] v1 = {f4, y, z};
		float[] v2 = {f4, f5, z};
		float[] v3 = {x, f5, z};
		float[] v4 = {x, y, f6};
		float[] v5 = {f4, y, f6};
		float[] v6 = {f4, f5, f6};
		float[] v7 = {x, f5, f6};

		switch(dir)
		{
			case MR_RIGHT:
				v[1] -= bScale1;
				v[2] -= bScale3;
				v3[1] += bScale2;
				v3[2] -= bScale3;
				v4[1] -= bScale1;
				v4[2] += bScale4;
				v7[1] += bScale2;
				v7[2] += bScale4;
				break;
			case MR_LEFT:
				v1[1] -= bScale1;
				v1[2] -= bScale3;
				v2[1] += bScale2;
				v2[2] -= bScale3;
				v5[1] -= bScale1;
				v5[2] += bScale4;
				v6[1] += bScale2;
				v6[2] += bScale4;
				break;
			case MR_FRONT:
				v[0] -= m*bScale4;
				v[1] -= bScale1;
				v1[0] += m*bScale3;
				v1[1] -= bScale1;
				v2[0] += m*bScale3;
				v2[1] += bScale2;
				v3[0] -= m*bScale4;
				v3[1] += bScale2;
				break;
			case MR_BACK:
				v4[0] -= m*bScale4;
				v4[1] -= bScale1;
				v5[0] += m*bScale3;
				v5[1] -= bScale1;
				v6[0] += m*bScale3;
				v6[1] += bScale2;
				v7[0] -= m*bScale4;
				v7[1] += bScale2;
				break;
			case MR_TOP:
				v[0] -= m*bScale1;
				v[2] -= bScale3;
				v1[0] += m*bScale2;
				v1[2] -= bScale3;
				v4[0] -= m*bScale1;
				v4[2] += bScale4;
				v5[0] += m*bScale2;
				v5[2] += bScale4;
				break;
			case MR_BOTTOM:
				v2[0] += m*bScale2;
				v2[2] -= bScale3;
				v3[0] -= m*bScale1;
				v3[2] -= bScale3;
				v6[0] += m*bScale2;
				v6[2] += bScale4;
				v7[0] -= m*bScale1;
				v7[2] += bScale4;
				break;
		}

		float[] qValues = new float[]{
				Math.abs((v[0]-v1[0])/(v3[0]-v2[0])),
				Math.abs((v[0]-v1[0])/(v4[0]-v5[0])),
				Math.abs((v4[0]-v5[0])/(v7[0]-v6[0])),
				Math.abs((v3[0]-v2[0])/(v7[0]-v6[0])),

				Math.abs((v[1]-v3[1])/(v1[1]-v2[1])),
				Math.abs((v4[1]-v7[1])/(v5[1]-v6[1])),
				Math.abs((v[1]-v3[1])/(v4[1]-v7[1])),
				Math.abs((v1[1]-v2[1])/(v5[1]-v6[1])),

				Math.abs((v[2]-v4[2])/(v1[2]-v5[2])),
				Math.abs((v[2]-v4[2])/(v3[2]-v7[2])),
				Math.abs((v1[2]-v5[2])/(v2[2]-v6[2])),
				Math.abs((v3[2]-v7[2])/(v2[2]-v6[2]))
		};

		addRectShape(v, v1, v2, v3, v4, v5, v6, v7, w, h, d);
	}

	/**
	 * Adds a trapezoid-like shape. It's achieved by expanding the shape on one side.
	 * You can use the static variables <code>MR_RIGHT</code>, <code>MR_LEFT</code>,
	 * <code>MR_FRONT</code>, <code>MR_BACK</code>, <code>MR_TOP</code> and
	 * <code>MR_BOTTOM</code>.
	 *
	 * @param x       the starting x-position
	 * @param y       the starting y-position
	 * @param z       the starting z-position
	 * @param w       the width (over the x-direction)
	 * @param h       the height (over the y-direction)
	 * @param d       the depth (over the z-direction)
	 * @param scale   the "scale" of the box. It only increases the size in each direction by that many.
	 * @param bScale1 the "scale" of the bottom - Top
	 * @param bScale2 the "scale" of the bottom - Bottom
	 * @param bScale3 the "scale" of the bottom - Left
	 * @param bScale4 the "scale" of the bottom - Right
	 * @param fScale1 the "scale" of the top - Left
	 * @param fScale2 the "scale" of the top - Right
	 * @param dir     the side the scaling is applied to
	 */
	public void addFlexTrapezoid(float x, float y, float z, int w, int h, int d, float scale, float bScale1, float bScale2, float bScale3, float bScale4, float fScale1, float fScale2, int dir)
	{
		float f4 = x+w;
		float f5 = y+h;
		float f6 = z+d;
		x -= scale;
		y -= scale;
		z -= scale;
		f4 += scale;
		f5 += scale;
		f6 += scale;

		int m = (mirror?-1: 1);
		if(mirror)
		{
			float f7 = f4;
			f4 = x;
			x = f7;
		}

		float[] v = {x, y, z};
		float[] v1 = {f4, y, z};
		float[] v2 = {f4, f5, z};
		float[] v3 = {x, f5, z};
		float[] v4 = {x, y, f6};
		float[] v5 = {f4, y, f6};
		float[] v6 = {f4, f5, f6};
		float[] v7 = {x, f5, f6};


		switch(dir)
		{
			case MR_RIGHT:
				v[2] -= fScale1;
				v1[2] -= fScale1;
				v4[2] += fScale2;
				v5[2] += fScale2;

				v[1] -= bScale1;
				v[2] -= bScale3;
				v3[1] += bScale2;
				v3[2] -= bScale3;
				v4[1] -= bScale1;
				v4[2] += bScale4;
				v7[1] += bScale2;
				v7[2] += bScale4;
				break;
			case MR_LEFT:
				v[2] -= fScale1;
				v1[2] -= fScale1;
				v4[2] += fScale2;
				v5[2] += fScale2;

				v1[1] -= bScale1;
				v1[2] -= bScale3;
				v2[1] += bScale2;
				v2[2] -= bScale3;
				v5[1] -= bScale1;
				v5[2] += bScale4;
				v6[1] += bScale2;
				v6[2] += bScale4;
				break;
			case MR_FRONT:
				v1[1] -= fScale1;
				v5[1] -= fScale1;
				v2[1] += fScale2;
				v6[1] += fScale2;

				v[0] -= m*bScale4;
				v[1] -= bScale1;
				v1[0] += m*bScale3;
				v1[1] -= bScale1;
				v2[0] += m*bScale3;
				v2[1] += bScale2;
				v3[0] -= m*bScale4;
				v3[1] += bScale2;
				break;
			case MR_BACK:
				v1[1] -= fScale1;
				v5[1] -= fScale1;
				v2[1] += fScale2;
				v6[1] += fScale2;

				v4[0] -= m*bScale4;
				v4[1] -= bScale1;
				v5[0] += m*bScale3;
				v5[1] -= bScale1;
				v6[0] += m*bScale3;
				v6[1] += bScale2;
				v7[0] -= m*bScale4;
				v7[1] += bScale2;
				break;
			case MR_TOP:
				v1[2] -= fScale1;
				v2[2] -= fScale1;
				v5[2] += fScale2;
				v6[2] += fScale2;

				v[0] -= m*bScale1;
				v[2] -= bScale3;
				v1[0] += m*bScale2;
				v1[2] -= bScale3;
				v4[0] -= m*bScale1;
				v4[2] += bScale4;
				v5[0] += m*bScale2;
				v5[2] += bScale4;
				break;
			case MR_BOTTOM:
				v1[2] -= fScale1;
				v2[2] -= fScale1;
				v5[2] += fScale2;
				v6[2] += fScale2;

				v2[0] += m*bScale2;
				v2[2] -= bScale3;
				v3[0] -= m*bScale1;
				v3[2] -= bScale3;
				v6[0] += m*bScale2;
				v6[2] += bScale4;
				v7[0] -= m*bScale1;
				v7[2] += bScale4;
				break;
		}

		addRectShape(v, v1, v2, v3, v4, v5, v6, v7, w, h, d);
	}

	/**
	 * Adds a box with float width, height and depth. Who knows what it will do to the texture.
	 *
	 * @param x the starting x-positions
	 * @param y the starting y-position
	 * @param z the starting z-position
	 * @param w the width (over the x-direction)
	 * @param h the height (over the y-direction)
	 * @param d the depth (over the z-direction)
	 */
	public void addBox(float x, float y, float z, float w, float h, float d)
	{
		int rw = MathHelper.ceil(w);
		int rh = MathHelper.ceil(h);
		int rd = MathHelper.ceil(d);
		w -= rw;
		h -= rh;
		d -= rd;
		addShapeBox(x, y, z, rw, rh, rd, 0F,
				0F, 0F, 0F,
				w, 0F, 0F,
				w, 0F, d,
				0F, 0F, d,
				0F, h, 0F,
				w, h, 0F,
				w, h, d,
				0F, h, d);
	}

	/**
	 * Adds a trapezoid-like shape. It's achieved by expanding the shape on one side.
	 * You can use the static variables <code>MR_RIGHT</code>, <code>MR_LEFT</code>,
	 * <code>MR_FRONT</code>, <code>MR_BACK</code>, <code>MR_TOP</code> and
	 * <code>MR_BOTTOM</code>.
	 *
	 * @param x        the starting x-position
	 * @param y        the starting y-position
	 * @param z        the starting z-position
	 * @param w        the width (over the x-direction)
	 * @param h        the height (over the y-direction)
	 * @param d        the depth (over the z-direction)
	 * @param scale    the "scale" of the box. It only increases the size in each direction by that many.
	 * @param x0,y0,z0 - x7,y7,z7 the modifiers of the box corners. each corner can changed seperat by x/y/z values
	 */
	public void addShapeBox(float x, float y, float z, int w, int h, int d, float scale, float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float x5, float y5, float z5, float x6, float y6, float z6, float x7, float y7, float z7)
	{
		float f4 = x+w;
		float f5 = y+h;
		float f6 = z+d;
		x -= scale;
		y -= scale;
		z -= scale;
		f4 += scale;
		f5 += scale;
		f6 += scale;

		int m = (mirror?-1: 1);
		if(mirror)
		{
			float f7 = f4;
			f4 = x;
			x = f7;
		}

		float[] v = {x-x0, y-y0, z-z0};
		float[] v1 = {f4+x1, y-y1, z-z1};
		float[] v2 = {f4+x5, f5+y5, z-z5};
		float[] v3 = {x-x4, f5+y4, z-z4};
		float[] v4 = {x-x3, y-y3, f6+z3};
		float[] v5 = {f4+x2, y-y2, f6+z2};
		float[] v6 = {f4+x6, f5+y6, f6+z6};
		float[] v7 = {x-x7, f5+y7, f6+z7};

		float[] qValues = new float[]{
				Math.abs((v[0]-v1[0])/(v3[0]-v2[0])),
				Math.abs((v[0]-v1[0])/(v4[0]-v5[0])),
				Math.abs((v4[0]-v5[0])/(v7[0]-v6[0])),
				Math.abs((v3[0]-v2[0])/(v7[0]-v6[0])),

				Math.abs((v[1]-v3[1])/(v1[1]-v2[1])),
				Math.abs((v4[1]-v7[1])/(v5[1]-v6[1])),
				Math.abs((v[1]-v3[1])/(v4[1]-v7[1])),
				Math.abs((v1[1]-v2[1])/(v5[1]-v6[1])),

				Math.abs((v[2]-v4[2])/(v1[2]-v5[2])),
				Math.abs((v[2]-v4[2])/(v3[2]-v7[2])),
				Math.abs((v1[2]-v5[2])/(v2[2]-v6[2])),
				Math.abs((v3[2]-v7[2])/(v2[2]-v6[2]))
		};

		addRectShape(v, v1, v2, v3, v4, v5, v6, v7, w, h, d);
	}


	/**
	 * Creates a shape from a 2D vector shape.
	 *
	 * @param x                  the starting x position
	 * @param y                  the starting y position
	 * @param z                  the starting z position
	 * @param coordinates        an array of coordinates that form the shape
	 * @param depth              the depth of the shape
	 * @param shapeTextureWidth  the width of the texture of one side of the shape
	 * @param shapeTextureHeight the height of the texture the shape
	 * @param sideTextureWidth   the width of the texture of the side of the shape
	 * @param sideTextureHeight  the height of the texture of the side of the shape
	 * @param direction          the direction the starting point of the shape is facing
	 */
	public void addShape3D(float x, float y, float z, Coord2D[] coordinates, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, int direction)
	{
		addShape3D(x, y, z, coordinates, depth, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, direction, null);
	}

	/**
	 * Creates a shape from a 2D vector shape.
	 *
	 * @param x                  the starting x position
	 * @param y                  the starting y position
	 * @param z                  the starting z position
	 * @param coordinates        an array of coordinates that form the shape
	 * @param depth              the depth of the shape
	 * @param shapeTextureWidth  the width of the texture of one side of the shape
	 * @param shapeTextureHeight the height of the texture the shape
	 * @param sideTextureWidth   the width of the texture of the side of the shape
	 * @param sideTextureHeight  the height of the texture of the side of the shape
	 * @param direction          the direction the starting point of the shape is facing
	 * @param faceLengths        An array with the length of each face. Used to set
	 *                           the texture width of each face on the side manually.
	 */
	public void addShape3D(float x, float y, float z, Coord2D[] coordinates, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, int direction, float[] faceLengths)
	{
		addShape3D(x, y, z, new Shape2D(coordinates), depth, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, direction, faceLengths);
	}

	/**
	 * Creates a shape from a 2D vector shape.
	 *
	 * @param x                  the starting x position
	 * @param y                  the starting y position
	 * @param z                  the starting z position
	 * @param coordinates        an ArrayList of coordinates that form the shape
	 * @param depth              the depth of the shape
	 * @param shapeTextureWidth  the width of the texture of one side of the shape
	 * @param shapeTextureHeight the height of the texture the shape
	 * @param sideTextureWidth   the width of the texture of the side of the shape
	 * @param sideTextureHeight  the height of the texture of the side of the shape
	 * @param direction          the direction the starting point of the shape is facing
	 */
	public void addShape3D(float x, float y, float z, ArrayList<Coord2D> coordinates, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, int direction)
	{
		addShape3D(x, y, z, coordinates, depth, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, direction, null);
	}

	/**
	 * Creates a shape from a 2D vector shape.
	 *
	 * @param x                  the starting x position
	 * @param y                  the starting y position
	 * @param z                  the starting z position
	 * @param coordinates        an ArrayList of coordinates that form the shape
	 * @param depth              the depth of the shape
	 * @param shapeTextureWidth  the width of the texture of one side of the shape
	 * @param shapeTextureHeight the height of the texture the shape
	 * @param sideTextureWidth   the width of the texture of the side of the shape
	 * @param sideTextureHeight  the height of the texture of the side of the shape
	 * @param direction          the direction the starting point of the shape is facing
	 * @param faceLengths        An array with the length of each face. Used to set
	 *                           the texture width of each face on the side manually.
	 */
	public void addShape3D(float x, float y, float z, ArrayList<Coord2D> coordinates, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, int direction, float[] faceLengths)
	{
		addShape3D(x, y, z, new Shape2D(coordinates), depth, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, direction, faceLengths);
	}

	/**
	 * Creates a shape from a 2D vector shape.
	 *
	 * @param x                  the starting x position
	 * @param y                  the starting y position
	 * @param z                  the starting z position
	 * @param shape              a Shape2D which contains the coordinates of the shape points
	 * @param depth              the depth of the shape
	 * @param shapeTextureWidth  the width of the texture of one side of the shape
	 * @param shapeTextureHeight the height of the texture the shape
	 * @param sideTextureWidth   the width of the texture of the side of the shape
	 * @param sideTextureHeight  the height of the texture of the side of the shape
	 * @param direction          the direction the starting point of the shape is facing
	 */
	public void addShape3D(float x, float y, float z, Shape2D shape, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, int direction)
	{
		addShape3D(x, y, z, shape, depth, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, direction, null);
	}

	/**
	 * Creates a shape from a 2D vector shape.
	 *
	 * @param x                  the starting x position
	 * @param y                  the starting y position
	 * @param z                  the starting z position
	 * @param shape              a Shape2D which contains the coordinates of the shape points
	 * @param depth              the depth of the shape
	 * @param shapeTextureWidth  the width of the texture of one side of the shape
	 * @param shapeTextureHeight the height of the texture the shape
	 * @param sideTextureWidth   the width of the texture of the side of the shape
	 * @param sideTextureHeight  the height of the texture of the side of the shape
	 * @param direction          the direction the starting point of the shape is facing
	 * @param faceLengths        An array with the length of each face. Used to set
	 *                           the texture width of each face on the side manually.
	 */
	public void addShape3D(float x, float y, float z, Shape2D shape, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, int direction, float[] faceLengths)
	{
		float rotX = 0;
		float rotY = 0;
		float rotZ = 0;
		switch(direction)
		{
			case MR_LEFT:
				rotY = pi/2;
				break;
			case MR_RIGHT:
				rotY = -pi/2;
				break;
			case MR_TOP:
				rotX = pi/2;
				break;
			case MR_BOTTOM:
				rotX = -pi/2;
				break;
			case MR_FRONT:
				rotY = pi;
				break;
			case MR_BACK:
				break;
		}
		addShape3D(x, y, z, shape, depth, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, rotX, rotY, rotZ, faceLengths);
	}

	public void addShape3D(float x, float y, float z, Shape2D shape, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, int direction, float[] faceLengths, boolean flip)
	{
		addShape3D(x, y, z, shape, depth, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, direction, faceLengths);
		this.doMirror(false, true, false);
	}

	/**
	 * Creates a shape from a 2D vector shape.
	 *
	 * @param x                  the starting x position
	 * @param y                  the starting y position
	 * @param z                  the starting z position
	 * @param shape              a Shape2D which contains the coordinates of the shape points
	 * @param depth              the depth of the shape
	 * @param shapeTextureWidth  the width of the texture of one side of the shape
	 * @param shapeTextureHeight the height of the texture the shape
	 * @param sideTextureWidth   the width of the texture of the side of the shape
	 * @param sideTextureHeight  the height of the texture of the side of the shape
	 * @param rotX               the rotation around the x-axis
	 * @param rotY               the rotation around the y-axis
	 * @param rotZ               the rotation around the z-axis
	 */
	public void addShape3D(float x, float y, float z, Shape2D shape, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, float rotX, float rotY, float rotZ)
	{
		addShape3D(x, y, z, shape, depth, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, rotX, rotY, rotZ, null);
	}

	public void addShape3D(float x, float y, float z, Shape2D shape, float depth, int shapeTextureWidth, int shapeTextureHeight, int sideTextureWidth, int sideTextureHeight, float rotX, float rotY, float rotZ, float[] faceLengths)
	{
		Shape3D shape3D = shape.extrude(x, y, z, rotX, rotY, rotZ, depth, textureOffsetX, textureOffsetY, textureWidth, textureHeight, shapeTextureWidth, shapeTextureHeight, sideTextureWidth, sideTextureHeight, faceLengths);

		if(flip)
			for(int idx = 0; idx < shape3D.faces.length; idx++)
				shape3D.faces[idx].flipFace();

		copyTo(shape3D.vertices, shape3D.faces);
	}

	/**
	 * Sets a new position for the texture offset.
	 *
	 * @param x the x-coordinate of the texture start
	 * @param y the y-coordinate of the texture start
	 */
	@Override
	public ModelRendererTurbo setTextureOffset(int x, int y)
	{
		textureOffsetX = x;
		textureOffsetY = y;
		return this;
	}

	/**
	 * Sets the position of the shape, relative to the model's origins. Note that changing
	 * the offsets will not change the pivot of the model.
	 *
	 * @param x the x-position of the shape
	 * @param y the y-position of the shape
	 * @param z the z-position of the shape
	 */
	public void setPosition(float x, float y, float z)
	{
		rotationPointX = x;
		rotationPointY = y;
		rotationPointZ = z;
	}

	/**
	 * Mirrors the model in any direction.
	 *
	 * @param x whether the model should be mirrored in the x-direction
	 * @param y whether the model should be mirrored in the y-direction
	 * @param z whether the model should be mirrored in the z-direction
	 */
	public void doMirror(boolean x, boolean y, boolean z)
	{
		for(TexturedPolygon face : faces)
		{
			PositionTextureVertex[] verts = face.vertexPositions;
			for(PositionTextureVertex vert : verts)
				vert.vector3D = new Vec3d(vert.vector3D.x*(x?-1: 1), vert.vector3D.y*(y?-1: 1), vert.vector3D.z*(z?-1: 1));
			if(x^y^z)
				face.flipFace();
		}
	}

	/**
	 * Sets whether the shape is mirrored or not. This has effect on the way the textures
	 * get displayed. When working with addSprite, addPixel and addObj, it will be ignored.
	 *
	 * @param isMirrored a boolean to define whether the shape is mirrored
	 */
	public void setMirrored(boolean isMirrored)
	{
		mirror = isMirrored;
	}

	/**
	 * Sets whether the shape's faces are flipped or not. When GL_CULL_FACE is enabled,
	 * it won't render the back faces, effectively giving you the possibility to make
	 * "hollow" shapes. When working with addSprite and addPixel, it will be ignored.
	 *
	 * @param isFlipped a boolean to define whether the shape is flipped
	 */
	public void setFlipped(boolean isFlipped)
	{
		flip = isFlipped;
	}

	/**
	 * Clears the current shape. Since all shapes are stacked into one shape, you can't
	 * just replace a shape by overwriting the shape with another one. In this case you
	 * would need to clear the shape first.
	 */
	public void clear()
	{
		vertices = new PositionTextureVertex[0];
		faces = new TexturedPolygon[0];
		//transformGroup.clear();
		//currentGroup = transformGroup.get("0");
	}

	/**
	 * Copies an array of vertices and polygons to the current shape. This mainly is
	 * used to copy each shape to the main class, but you can just use it to copy
	 * your own shapes, for example from other classes, into the current class.
	 *
	 * @param verts the array of vertices you want to copy
	 * @param poly  the array of polygons you want to copy
	 */
	public void copyTo(PositionTextureVertex[] verts, TexturedPolygon[] poly)
	{
		copyTo(verts, poly, true);
	}

	public void copyTo(PositionTextureVertex[] verts, TexturedPolygon[] poly, boolean copyGroup)
	{
		vertices = Arrays.copyOf(vertices, vertices.length+verts.length);
		faces = Arrays.copyOf(faces, faces.length+poly.length);

		System.arraycopy(verts, 0, vertices, vertices.length-verts.length, verts.length);

		for(int idx = 0; idx < poly.length; idx++)
		{
			faces[faces.length-poly.length+idx] = poly[idx];
			if(copyGroup)
				currentTextureGroup.addPoly(poly[idx]);
		}
	}

	/**
	 * Copies an array of vertices and quads to the current shape. This method
	 * converts quads to polygons and then calls the main copyTo method.
	 *
	 * @param verts the array of vertices you want to copy
	 * @param quad  the array of quads you want to copy
	 */
	public void copyTo(PositionTextureVertex[] verts, TexturedQuad[] quad)
	{
		TexturedPolygon[] poly = new TexturedPolygon[quad.length];
		for(int idx = 0; idx < quad.length; idx++)
			poly[idx] = new TexturedPolygon((PositionTextureVertex[])quad[idx].vertexPositions);

		copyTo(verts, poly);
	}

	/**
	 * Renders the shape.
	 *
	 * @param worldScale the scale of the shape. Usually is 0.0625.
	 */
	@Override
	public void render(float worldScale)
	{
		render(worldScale, false);
	}

	public void render()
	{
		render(0.0625f);
	}


	/**
	 * Renders the shape
	 *
	 * @param worldScale     The scale of the shape
	 * @param oldRotateOrder Whether to use the old rotate order (ZYX) instead of the new one (YZX)
	 */
	public void render(float worldScale, boolean oldRotateOrder)
	{
		if(!showModel)
			return;
		if(!compiled||forcedRecompile)
			compileDisplayList(worldScale);
		if(rotateAngleX!=0.0F||rotateAngleY!=0.0F||rotateAngleZ!=0.0F)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(rotationPointX*worldScale, rotationPointY*worldScale, rotationPointZ*worldScale);
			if(!oldRotateOrder&&rotateAngleY!=0.0F)
				GL11.glRotatef(rotateAngleY*57.29578F, 0.0F, 1.0F, 0.0F);
			if(rotateAngleZ!=0.0F)
				GL11.glRotatef((oldRotateOrder?-1: 1)*rotateAngleZ*57.29578F, 0.0F, 0.0F, 1.0F);
			if(oldRotateOrder&&rotateAngleY!=0.0F)
				GL11.glRotatef(-rotateAngleY*57.29578F, 0.0F, 1.0F, 0.0F);
			if(rotateAngleX!=0.0F)
				GL11.glRotatef(rotateAngleX*57.29578F, 1.0F, 0.0F, 0.0F);
			if(hasOffset)
				GL11.glTranslatef(offsetX*worldScale, offsetY*worldScale, offsetZ*worldScale);

			callDisplayList();
			if(childModels!=null)
				for(ModelRenderer childModel : childModels)
					childModel.render(worldScale);
			GL11.glPopMatrix();
		}
		else if(rotationPointX!=0.0F||rotationPointY!=0.0F||rotationPointZ!=0.0F)
		{
			GL11.glTranslatef(rotationPointX*worldScale, rotationPointY*worldScale, rotationPointZ*worldScale);
			callDisplayList();
			if(childModels!=null)
				for(ModelRenderer childModel : childModels)
					childModel.render(worldScale);
			GL11.glTranslatef(-rotationPointX*worldScale, -rotationPointY*worldScale, -rotationPointZ*worldScale);
		}
		else
		{
			callDisplayList();
			if(childModels!=null)
				for(ModelRenderer childModel : childModels)
					childModel.render(worldScale);
		}
	}

	@Override
	public void renderWithRotation(float f)
	{
		if(!showModel)
			return;
		if(!compiled)
			compileDisplayList(f);
		GL11.glPushMatrix();
		GL11.glTranslatef(rotationPointX*f, rotationPointY*f, rotationPointZ*f);
		if(rotateAngleY!=0.0F)
			GL11.glRotatef(rotateAngleY*57.29578F, 0.0F, 1.0F, 0.0F);
		if(rotateAngleX!=0.0F)
			GL11.glRotatef(rotateAngleX*57.29578F, 1.0F, 0.0F, 0.0F);
		if(rotateAngleZ!=0.0F)
			GL11.glRotatef(rotateAngleZ*57.29578F, 0.0F, 0.0F, 1.0F);
		callDisplayList();
		GL11.glPopMatrix();
	}

	public void addChild(ModelRendererTurbo renderer)
	{
		if(this.childModels==null)
			this.childModels = new ArrayList<>();

		this.childModels.add(renderer);
	}

	@Override
	public void postRender(float f)
	{
		if(!showModel)
			return;
		if(!compiled||forcedRecompile)
			compileDisplayList(f);
		if(rotateAngleX!=0.0F||rotateAngleY!=0.0F||rotateAngleZ!=0.0F)
		{
			GL11.glTranslatef(rotationPointX*f, rotationPointY*f, rotationPointZ*f);
			if(rotateAngleZ!=0.0F)
				GL11.glRotatef(rotateAngleZ*57.29578F, 0.0F, 0.0F, 1.0F);
			if(rotateAngleY!=0.0F)
				GL11.glRotatef(rotateAngleY*57.29578F, 0.0F, 1.0F, 0.0F);
			if(rotateAngleX!=0.0F)
				GL11.glRotatef(rotateAngleX*57.29578F, 1.0F, 0.0F, 0.0F);
		}
		else if(rotationPointX!=0.0F||rotationPointY!=0.0F||rotationPointZ!=0.0F)
			GL11.glTranslatef(rotationPointX*f, rotationPointY*f, rotationPointZ*f);
	}

	private void callDisplayList()
	{
		TextureManager renderEngine = ClientUtils.mc().renderEngine;
		Collection<TextureGroup> textures = textureGroup.values();
		Iterator<TextureGroup> itr = textures.iterator();
		for(int i = 0; itr.hasNext(); i++)
		{
			TextureGroup curTexGroup = itr.next();
			curTexGroup.loadTexture();
			GL11.glCallList(displayListArray[i]);
		}

	}

	public void compileDisplayList(float worldScale)
	{
		Collection<TextureGroup> textures = textureGroup.values();

		Iterator<TextureGroup> itr = textures.iterator();
		displayListArray = new int[textureGroup.size()];
		for(int i = 0; itr.hasNext(); i++)
		{
			displayListArray[i] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(displayListArray[i], GL11.GL_COMPILE);
			TmtTessellator tessellator = TmtTessellator.instance;

			TextureGroup usedGroup = itr.next();
			for(int j = 0; j < usedGroup.poly.size(); j++)
				usedGroup.poly.get(j).draw(tessellator, worldScale);

			GL11.glEndList();
		}

		compiled = true;
	}

	public void setRotationAngle(float x, float y, float z)
	{
		this.rotateAngleX = x;
		this.rotateAngleY = y;
		this.rotateAngleZ = z;
	}

	public boolean isCompiled()
	{
		return compiled;
	}
}
