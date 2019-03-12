package synth.sources;

import synth.fm.algorithm.Block;
import synth.sources.oscillators.NoiseOscillator;
import synth.sources.oscillators.Oscillator;
import synth.sources.oscillators.SawOscillator;
import synth.sources.oscillators.SineOscillator;
import synth.sources.oscillators.SquareOscillator;
import synth.sources.oscillators.TriangleOscillator;

public class WaveGenerator implements Block
{	
	public enum Waveform
	{
		SINE_WAVE,
		SQUARE_WAVE,
		TRIANGLE_WAVE,
		SAW_WAVE,
		NOISE
	}
	
	private Waveform waveform;
	private Oscillator oscillator;
	
	private int sampleRate;
	
	public WaveGenerator(int sampleRate)
	{
		this.waveform = Waveform.SINE_WAVE;
		this.oscillator = new SineOscillator(sampleRate, 440.0f);
		
		this.sampleRate = sampleRate;
	}
	
	@Override
	public float process(float input) 
	{
		return this.oscillator.process(input);
	}
	
	//---------------------- SETTERS ----------------------\\
	public void setWaveform(Waveform newWaveform)
	{
		float currentFrequency = this.oscillator.getFrequency();
		switch (newWaveform)
		{
		case SINE_WAVE:
			this.waveform = Waveform.SINE_WAVE;
			this.oscillator = new SineOscillator(this.sampleRate, currentFrequency);
			break;
		case SQUARE_WAVE:
			this.waveform = Waveform.SQUARE_WAVE;
			this.oscillator = new SquareOscillator(this.sampleRate, currentFrequency);
			break;
		case TRIANGLE_WAVE:
			this.waveform = Waveform.TRIANGLE_WAVE;
			this.oscillator = new TriangleOscillator(this.sampleRate, currentFrequency);
			break;
		case SAW_WAVE:
			this.waveform = Waveform.SAW_WAVE;
			this.oscillator = new SawOscillator(this.sampleRate, currentFrequency);
			break;
		case NOISE:
			this.waveform = Waveform.NOISE;
			this.oscillator = new NoiseOscillator(this.sampleRate, currentFrequency);
			break;
		}
	}
	
	public void setFrequency(float newFrequency)
	{
		this.oscillator.setFrequency(newFrequency);
	}

	//---------------------- GETTERS ----------------------\\
	public Waveform getWaveform()
	{
		return this.waveform;
	}
	
	public float getFrequency()
	{
		return this.oscillator.getFrequency();
	}
}
