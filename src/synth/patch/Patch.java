package synth.patch;

import synth.sources.WaveGenerator.Waveform;

public class Patch 
{
	private String name;
	private String algorithmExpression;
	private int operatorsPerVoice;
	private int voicesPerKey;
	private Waveform[] waveforms;
	private float[] frequencyRatios;
	private float[] outputLevels;
	private float[] modSensitivities;
	private float[][] envelopeLevels;
	private int[][] envelopeRates;
	
	/**
	 * Creates an empty patch.
	 */
	public Patch(int operatorsPerVoice, int voicesPerKey, String algorithmExpression)
	{
		this.name = "NoName";
		this.operatorsPerVoice = operatorsPerVoice;
		this.algorithmExpression = algorithmExpression;
		this.voicesPerKey = voicesPerKey;
		
		this.waveforms = new Waveform[operatorsPerVoice];
		this.frequencyRatios = new float[operatorsPerVoice];
		this.outputLevels = new float[operatorsPerVoice];
		this.modSensitivities = new float[operatorsPerVoice];
		this.envelopeLevels = new float[operatorsPerVoice][];
		this.envelopeRates = new int[operatorsPerVoice][];
	}
	
	//------------------------- SETTERS -------------------------\\
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setWaveform(int operator, Waveform waveform)
	{
		if (operator < this.operatorsPerVoice)
			this.waveforms[operator] = waveform;
	}
	
	public void setFrequencyRatio(int operator, float ratio)
	{
		if (operator < this.operatorsPerVoice)
			this.frequencyRatios[operator] = ratio;
	}
	
	public void setOutputLevel(int operator, float level)
	{
		if (operator < this.operatorsPerVoice)
			this.outputLevels[operator] = level;
	}
	
	public void setModulationSensitivity(int operator, float modSensitivity)
	{
		if (operator < this.operatorsPerVoice)
			this.modSensitivities[operator] = modSensitivity;
	}
	
	public void setEnvelopeLevels(int operator, float[] envLevels)
	{
		if (operator < this.operatorsPerVoice)
			this.envelopeLevels[operator] = envLevels;
	}
	
	public void setEnvelopeRates(int operator, int[] envRates)
	{
		if (operator < this.operatorsPerVoice)
			this.envelopeRates[operator] = envRates;
	}
	
	//------------------------- GETTERS -------------------------\\
	public String getName()
	{
		return this.name;
	}
	
	public int getVoicesPerKey()
	{
		return this.voicesPerKey;
	}
	
	public String getAlgorithm()
	{
		return this.algorithmExpression;
	}
	
	public int getOperatorsPerVoice()
	{
		return this.operatorsPerVoice;
	}
	
	public Waveform getWaveform(int operator)
	{
		if (operator < this.operatorsPerVoice)
			return this.waveforms[operator];
		return null;
	}
	
	public float getFrequencyRatio(int operator)
	{
		if (operator < this.operatorsPerVoice)
			return this.frequencyRatios[operator];
		return 0.0f;
	}
	
	public float getOutputLevel(int operator)
	{
		if (operator < this.operatorsPerVoice)
			return this.outputLevels[operator];
		return 0.0f;
	}
	
	public float getModulationSensitivity(int operator)
	{
		if (operator < this.operatorsPerVoice)
			return this.modSensitivities[operator];
		return 0.0f;
	}
	
	public float[] getEnvelopeLevels(int operator)
	{
		if (operator < this.operatorsPerVoice)
			return this.envelopeLevels[operator];
		return null;
	}
	
	public int[] getEnvelopeRatios(int operator)
	{
		if (operator < this.operatorsPerVoice)
			return this.envelopeRates[operator];
		return null;
	}
}
