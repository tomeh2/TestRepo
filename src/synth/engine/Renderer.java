package synth.engine;

import java.util.ArrayList;

import javax.sound.midi.ShortMessage;

import synth.patch.Patch;

/**
 * Passes messages to the respective channels, renders samples and produces output
 * @author Tomi
 *
 */

public class Renderer 
{
	private ArrayList<Patch> patchBank;
	private Channel[] channels;
	
	private int numOfChannels;
	
	/**
	 * Creates a renderer
	 * 
	 * @param sampleRate - self explanatory
	 * @param numOfChannels - number of channels which this renderer will create
	 * @param maxPolyphony - max polyphony per channel (each channel can play this many notes at the same time)
	 * @param initPatch - all channels will be initialized with this patch
	 */
	public Renderer(int sampleRate, int numOfChannels, int maxPolyphony, ArrayList<Patch> patchBank)
	{
		this.patchBank = patchBank;
		this.numOfChannels = numOfChannels;
		
		this.channels = new Channel[numOfChannels];
		for (int i = 0; i < numOfChannels; i++)
			this.channels[i] = new Channel(sampleRate, maxPolyphony, patchBank.get(0));
	}
	
	public float renderNextSample()
	{
		float sample = 0.0f;
		
		for (Channel c : this.channels)
			sample += c.getNextSample();
		return sample;
	}
	
	public void processMessage(ShortMessage message)
	{
		switch (message.getCommand())
		{
		case ShortMessage.NOTE_ON:
			this.channels[message.getChannel()].keyDown(message.getData1(), message.getData2());
			break;
		case ShortMessage.NOTE_OFF:
			this.channels[message.getChannel()].keyUp(message.getData1());
			break;
		case ShortMessage.PROGRAM_CHANGE:
			if (message.getData1() < this.patchBank.size())
				this.channels[message.getChannel()].setPatch(patchBank.get(message.getData1()));
			else
				System.err.println("New instrument's ID (" + message.getData1() + ") does not exist in the current patch bank!");
			break;
		case ShortMessage.CONTROL_CHANGE:
			if (message.getData1() == 0x07)
				this.channels[message.getChannel()].setChannelVolume(message.getData2());
			break;
		}
	}
	
	//----------------------------- SETTERS -----------------------------\\
	/**
	 * Changes a patch (instrument) of the given channel
	 * @param channel - Which channel this patch will be applied to
	 * @param patch - Patch that will be applied
	 */
	public void setPatch(int channel, int patchID)
	{
		this.channels[channel].setPatch(this.patchBank.get(patchID));
	}
	
	/**
	 * Sets the frequency ratio of the selected operator on the given channel
	 * @param channel - Which channel the operator is on
	 * @param operator - Which operator is being modified
	 * @param ratio - Frequency ratio with respect to the base frequency of the key
	 */
	public void setFrequencyRatio(int channel, int operator, float ratio)
	{
		if (channel < this.numOfChannels)
			this.channels[channel].setFrequencyRatio(operator, ratio);
	}
	
	/**
	 * Sets the output level of the selected operator on the given channel
	 * @param channel - Which channel the operator is on
	 * @param operator - Which operator is being modified
	 * @param level - Amplitude of the operator
	 */
	public void setOutputLevel(int channel, int operator, float level)
	{
		if (channel < this.numOfChannels)
			this.channels[channel].setOutputLevel(operator, level);
	}
	
	/**
	 * Sets the modulation sensitivity of the selected operator on the given channel
	 * @param channel - Which channel the operator is on
	 * @param operator - Which operator is being modified
	 * @param sensitivity - Modulation sensitivity
	 */
	public void setModulationSensitivity(int channel, int operator, float sensitivity)
	{
		if (channel < this.numOfChannels)
			this.channels[channel].setModulationSensitivity(operator, sensitivity);
	}
	
	/**
	 * Sets the envelope rate of the selected operator on the given channel
	 * @param channel - Which channel the operator is on
	 * @param operator - Which operator is being modified
	 * @param stage - 0: ATTACK | 1: DECAY | 2: SUSTAIN | 3: RELEASE
	 * @param rate - Rate of the segment
	 */
	public void setEnvelopeRate(int channel, int operator, int stage, int rate)
	{
		if (channel < this.numOfChannels)
			this.channels[channel].setEnvelopeRate(operator, stage, rate);
	}
	
	/**
	 * Sets the envelope level of the selected operator on the given channel
	 * @param channel - Which channel the operator is on
	 * @param operator - Which operator is being modified
	 * @param stage - 0: ATTACK | 1: DECAY | 2: SUSTAIN | 3: RELEASE
	 * @param rate - Envelope level
	 */
	public void setEnvelopeLevel(int channel, int operator, int stage, float level)
	{
		if (channel < this.numOfChannels)
			this.channels[channel].setEnvelopeLevel(operator, stage, level);
	}
	//----------------------------- GETTERS -----------------------------\\
	public int getNumOfChannels()
	{
		return this.numOfChannels;
	}
}
