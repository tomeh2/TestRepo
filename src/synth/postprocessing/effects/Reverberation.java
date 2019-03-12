package synth.postprocessing.effects;

import synth.postprocessing.Effect;

public class Reverberation implements Effect 
{
	private float[] delayLine;
	
	public Reverberation()
	{
		this.delayLine = new float[512];
	}
	
	@Override
	public void process(float[] samples) 
	{
	}

}
