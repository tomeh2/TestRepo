package synth.sources.oscillators;

import synth.fm.algorithm.Block;

/**
 * o = sin(w1 * t + f[t]) -> f[t] is a function that performs Phase Modulation on the current oscillator
 * and o is the output of the oscillator at time-step t
 * @author Tomi
 *
 */
public abstract class Oscillator implements Block
{
	protected static float twoPi = (float) (2.0f * Math.PI);
	
	private int sampleRate;
	private float frequency;
	
	protected Oscillator(int sampleRate, float frequency)
	{
		this.sampleRate = sampleRate;
		setFrequency(frequency);
	}
	
	@Override
	public abstract float process(float input); 
	
	//---------------------- SETTERS ----------------------\\
	public void setFrequency(float newFrequency)
	{
			this.frequency = newFrequency;
	}

	//---------------------- GETTERS ----------------------\\
	public float getFrequency()
	{
		return this.frequency;
	}
	
	public int getSampleRate()
	{
		return this.sampleRate;
	}
}
