package pl.pabilo8.immersiveintelligence.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker for items registering OreDict in batch fashion<br>
 * When registering, adds item subtype name to the end. <br>
 * i.e. <pre>@IBatchOredictRegister(oredict="ingot")</pre> with subtype <pre>steel</pre> is registered as <pre>ingotSteel</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.TYPE_USE})
public @interface IBatchOredictRegister
{
	String[] oreDict() default {};
}
