package synth.sources.oscillators;

import synth.engine.Clock;

public class SawOscillator extends Oscillator
{
	public SawOscillator(int sampleRate, float frequency) 
	{
		super(sampleRate, frequency);
	}

	@Override
	public float process(float input)
	{
		float sample = (float) (-2.0f / Math.PI) * (float) Math.atan(1.0f / Math.tan((Clock.getTime() / this.getSampleRate() * Math.PI) / (1.0f / this.getFrequency())));
		return sample;
	}

}
