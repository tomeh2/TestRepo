package synth.sources.oscillators;

import synth.engine.Clock;

public class SquareOscillator extends Oscillator
{
	public SquareOscillator(int sampleRate, float frequency) 
	{
		super(sampleRate, frequency);
	}

	@Override
	public float process(float input) 
	{
		float sample = (float) Math.signum(Math.sin(Clock.getTime()));
		if (Clock.getTime() + input < Math.PI)
			sample = -1.0f;
		else
			sample = 1.0f;
		return sample;
	}
}
