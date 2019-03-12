package synth.sources.oscillators;

import synth.engine.Clock;

public class SineOscillator extends Oscillator
{
	public SineOscillator(int sampleRate, float frequency)
	{
		super(sampleRate, frequency);
	}

	@Override
	public float process(float input) 
	{	
		float sample = (float) (Math.sin((twoPi * this.getFrequency() *
				(Clock.getTime() / (double) this.getSampleRate()) + input)));
		return sample;
	}
}