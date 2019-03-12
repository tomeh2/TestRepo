package synth.postprocessing.effects;

import synth.postprocessing.Effect;

public class TestEffect implements Effect 
{
	@Override
	public void process(float[] samples) 
	{
		for (int i = 0; i < samples.length; i++)
			samples[i] = 0.0f;
	}
}
