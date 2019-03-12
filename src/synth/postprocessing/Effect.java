package synth.postprocessing;

/**
 * This interface is pretty much the same as the Block interface. The only reason this one actually exists
 * is to prevent algorithm related classes to be mixed with the effects.
 * @author Tomi
 *
 */

public interface Effect 
{
	public void process(float[] samples);
}
