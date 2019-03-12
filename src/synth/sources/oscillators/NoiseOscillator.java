package synth.sources.oscillators;

public class NoiseOscillator extends Oscillator
{

	public NoiseOscillator(int sampleRate, float frequency) 
	{
		super(sampleRate, frequency);
	}

	@Override
	public float process(float input)
	{
		return (float) Math.random() * 2.0f - 1.0f;
	}

}
