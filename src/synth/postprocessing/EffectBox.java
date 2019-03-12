package synth.postprocessing;

import java.util.Iterator;
import java.util.LinkedList;

public class EffectBox
{
	private LinkedList<Effect> effects;
	
	public EffectBox()
	{
		this.effects = new LinkedList<>();
	}
	
	/**
	 * Adds the specified effect to the end of the list
	 * @param e
	 */
	public void addEffect(Effect e)
	{
		this.effects.add(e);
	}
	
	public void processEffects(float[] samples)
	{
		if (this.effects.size() != 0)
		{
			Iterator<Effect> it = this.effects.iterator();
			while (it.hasNext())
			{
				it.next().process(samples);
			}
		}
	}
}
