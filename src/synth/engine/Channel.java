package synth.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import synth.fm.algorithm.AlgorithmFactory;
import synth.patch.Patch;
import synth.sources.WaveGenerator.Waveform;

public class Channel 
{
	//Detects inactive voices still in activeVoices map and returns them to freeVoices.
	//Executes every n samples as determined by the SWEEP_FREQUENCY variable (Default: 4000)
	private static final int SWEEP_PERIOD = 4000;
	private int samplesPassed = 0;
	
	private Stack<Voice> freeVoices;
	private HashMap<Integer, Voice> activeVoices;
	private Patch patch;
	
	private int sampleRate;
	private int maxPolyphony;
	private float channelVolume;
	
	public Channel(int sampleRate, int maxPolyphony, Patch patch)
	{
		this.patch = patch;
		this.sampleRate = sampleRate;
		this.maxPolyphony = maxPolyphony;
		this.channelVolume = 1.0f;
		this.activeVoices = new HashMap<>();
		this.freeVoices = new Stack<>();
		
		setPatch(patch);
	}

	private synchronized void performSweep()
	{
		Iterator<Map.Entry<Integer, Voice>> it = this.activeVoices.entrySet().iterator();
		while (it.hasNext())
		{
			 Map.Entry<Integer, Voice> entry = it.next();
			 Voice v = entry.getValue();

			if (!v.isActive())
			{
				it.remove();
				this.freeVoices.push(v);
			}
		}
	}
	
	public synchronized float getNextSample()
	{	
		float sample = 0.0f;
		for (Map.Entry<Integer, Voice> i : this.activeVoices.entrySet())
			sample += i.getValue().getNextSample();

		//Perform sweep on activeVoices
		if (samplesPassed == SWEEP_PERIOD)
		{
			performSweep();
			this.samplesPassed = 0;
		}
		samplesPassed++;
		
		return sample * this.channelVolume;
	}
	
	public synchronized void keyDown(int keyNum, int velocity)
	{	
		if (this.activeVoices.get(keyNum) == null && !this.freeVoices.isEmpty())
		{	
			Voice v = this.freeVoices.pop();
			v.trigger(keyNum, velocity);
			this.activeVoices.put(keyNum, v);
		}
		
		if (this.activeVoices.get(keyNum) != null)
			this.activeVoices.get(keyNum).trigger(keyNum, velocity);
	}
	
	public synchronized void keyUp(int keyNum)
	{	
		if (this.activeVoices.get(keyNum) != null)
			this.activeVoices.get(keyNum).release();
	}
	
	//---------------------- SETTERS ----------------------\\
	public void setPatch(Patch patch)
	{
		this.patch = patch;
		
		this.freeVoices.clear();
		this.activeVoices.clear();
		
		for (int i = 0; i < this.maxPolyphony; i++)
		{
			Voice v = new Voice(AlgorithmFactory.createFromExpression(
					this.sampleRate, patch.getOperatorsPerVoice(), patch.getAlgorithm()));
			
			for (int j = 0; j < patch.getOperatorsPerVoice(); j++)
			{
				v.getOperator(j).setWaveform(patch.getWaveform(j));
				v.getOperator(j).setFrequencyRatio(patch.getFrequencyRatio(j));
				v.getOperator(j).setOutputLevel(patch.getOutputLevel(j));
				v.getOperator(j).setModulationSensitivity(patch.getModulationSensitivity(j));
				
				for (int k = 0; k < 4; k++)
				{
					v.getOperator(j).getEnvelopeGenerator().setRate(k, patch.getEnvelopeRatios(j)[k]);
					v.getOperator(j).getEnvelopeGenerator().setLevel(k, patch.getEnvelopeLevels(j)[k]);
				}
			}
			this.freeVoices.push(v);
		}
	}
	
	public void setWaveform(int operatorId, Waveform newWaveform)
	{
		for (Voice v : this.freeVoices)
			v.getOperator(operatorId).setWaveform(newWaveform);
	}
	
	public void setFrequencyRatio(int operatorId, float newRatio)
	{
		for (Voice v : this.freeVoices)
			v.getOperator(operatorId).setFrequencyRatio(newRatio);
	}
	
	public void setOutputLevel(int operatorId, float newLevel)
	{
		for (Voice v : this.freeVoices)
			v.getOperator(operatorId).setOutputLevel(newLevel);
	}
	
	public void setModulationSensitivity(int operatorId, float newSensitivity)
	{
		for (Voice v : this.freeVoices)
			v.getOperator(operatorId).setModulationSensitivity(newSensitivity);
	}
	
	/**
	 * Sets the rate for one of the ADSR stages
	 * Which stage is being modified depends on the stage variable
	 * 0 - ATTACK
	 * 1 - DECAY
	 * 2 - SUSTAIN
	 * 3 - RELEASE
	 * @param operatorId - Which operator this changes are applied to
	 * @param stage - Which phase is modified [0, 3]
	 * @param rate - New rate for this phase [1, 100]
	 */
	public void setEnvelopeRate(int operatorId, int stage, int rate)
	{
		for (Voice v : this.freeVoices)
			v.getOperator(operatorId).getEnvelopeGenerator().setRate(stage, rate);
	}
	
	/**
	 * Sets the level for one of the ADSR stages
	 * Which stage is being modified depends on the stage variable
	 * 0 - ATTACK
	 * 1 - DECAY
	 * 2 - SUSTAIN
	 * 3 - RELEASE
	 * @param operatorId - Which operator this changes are applied to
	 * @param stage - Which phase is modified [0, 3]
	 * @param envLevel - New level for this phase [0, 1]
	 */
	public void setEnvelopeLevel(int operatorId, int stage, float envLevel)
	{
		for (Voice v : this.freeVoices)
			v.getOperator(operatorId).getEnvelopeGenerator().setLevel(stage, envLevel);
	}

	/**
	 * Sets the overall volume of this channel
	 * @param volume - a number between 0 - 127
	 */
	public void setChannelVolume(int volume)
	{
		if (volume >= 0 && volume <= 127)
			this.channelVolume = volume / 127.0f;
		else
			System.err.println("Channel volume value out of range. VOLUME = " + volume);
	}
	//---------------------- GETTERS ----------------------\\
	public Patch getPatch()
	{
		return this.patch;
	}
	
	public int getMaxPolyphony()
	{
		return this.maxPolyphony;
	}
}
