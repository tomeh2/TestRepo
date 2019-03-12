package synth.fm;

import synth.fm.algorithm.Block;
import synth.sources.EnvelopeGenerator;
import synth.sources.WaveGenerator;
import synth.sources.WaveGenerator.Waveform;

public class Operator implements Block
{
	private WaveGenerator waveGenerator;
	private EnvelopeGenerator envGenerator;
	
	private float baseFrequency;
	private float detuneFrequency;
	private float frequencyRatio;
	private float outputLevel;
	private float modulationSensitivity;
	
	public Operator(int sampleRate)
	{	 
		this.envGenerator = new EnvelopeGenerator(sampleRate);
		this.waveGenerator = new WaveGenerator(sampleRate);
		
		this.baseFrequency = 440.0f;
		this.detuneFrequency = 0.0f;
		this.frequencyRatio = 1.0f;
		this.outputLevel = 1.0f;
		this.modulationSensitivity = 1.0f;
	}
	
	@Override
	public float process(float input) 
	{
		if (this.envGenerator.isActive())
			return this.waveGenerator.process(input * this.modulationSensitivity)
					* this.envGenerator.getNextValue() * outputLevel;
		else
			return 0.0f;
	}
	
	public void trigger(int key)
	{			
		this.envGenerator.trigger(key);
	}
	
	public void release()
	{
		this.envGenerator.release();
	}
	//---------------------- SETTERS ----------------------\\	
	public void setWaveform(Waveform newWaveform)
	{
		this.waveGenerator.setWaveform(newWaveform);
	}
	
	public void setFrequency(float newFrequency)
	{
		this.baseFrequency = newFrequency;
		this.waveGenerator.setFrequency((this.baseFrequency * this.frequencyRatio) + this.detuneFrequency);
	}
	
	public void setDetuneFrequency(float detuneFrequency)
	{
		this.detuneFrequency = detuneFrequency;
		this.waveGenerator.setFrequency((this.baseFrequency * this.frequencyRatio) + this.detuneFrequency);
	}
	
	public void setFrequencyRatio(float newRatio)
	{
		this.frequencyRatio = newRatio;
		this.waveGenerator.setFrequency((this.baseFrequency * this.frequencyRatio) + this.detuneFrequency);
	}
	
	public void setOutputLevel(float newLevel)
	{
		if (newLevel < 0.0f || newLevel > 1.0f)
			System.err.println("Output level invalid: " + newLevel);
		else
			this.outputLevel = newLevel;
	}
	
	public void setModulationSensitivity(float newSensitivity)
	{
		if (newSensitivity < 0.0f || newSensitivity > 100.0f)
			System.err.println("Modulatio sensitivity out of range: " + newSensitivity);
		else
			this.modulationSensitivity = newSensitivity;
	}

	//---------------------- GETTERS ----------------------\\
	public boolean isActive()
	{
		return this.envGenerator.isActive();
	}
	
	/**
	 * Returns the envelope generation that is used by the operator. Allows modification of envelope
	 * generators properties
	 * @return Envelope generator in use by the operator
	 */
	public EnvelopeGenerator getEnvelopeGenerator()
	{
		return this.envGenerator;
	}
	
	public Waveform getWaveform()
	{
		return this.waveGenerator.getWaveform();
	}
	
	public float getFrequency()
	{
		return this.baseFrequency;
	}
	
	public float getDetuneFrequency()
	{
		return this.detuneFrequency;
	}
	
	public float getFrequencyRatio()
	{
		return this.frequencyRatio;
	}
	
	public float getOutputLevel()
	{
		return this.outputLevel;
	}
	
	public float getModulationSensitivity()
	{
		return this.modulationSensitivity;
	}
}
