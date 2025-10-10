package pl.pabilo8.immersiveintelligence.common.util;

import java.lang.annotation.Annotation;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 05.10.2025
 */
public class MissingAnnotationException extends RuntimeException
{
	public MissingAnnotationException(Object nonAnnotated, Class<? extends Annotation> annotation)
	{
		super("Class "+nonAnnotated.getClass().getName()+" is missing @"+annotation.getName()+" annotation!");
	}
}
