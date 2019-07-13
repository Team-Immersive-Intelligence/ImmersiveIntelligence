package pl.pabilo8.immersiveintelligence.api;

/**
 * Created by Pabilo8 on 02-07-2019.
 */
public interface IBooleanAnimatedPartsBlock
{
	void onAnimationChangeClient(boolean state, int part);

	void onAnimationChangeServer(boolean state, int part);
}
