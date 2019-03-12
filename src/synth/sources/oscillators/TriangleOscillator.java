package synth.sources.oscillators;

import synth.engine.Clock;

public class TriangleOscillator extends Oscillator
{

	public TriangleOscillator(int sampleRate, float frequency) 
	{
		super(sampleRate, frequency);
	}

	@Override
	public float process(float input) 
	{
		float sample = (float) (2.0f / Math.PI * Math.asin(Math.sin(Clock.getTime() * 5.0f * this.getFrequency() + input)));
		return sample;
	}

}
