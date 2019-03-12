package synth.sources;

/**
 * Generates an envelope for a note. Realized as a state machine
 * @author Tomi
 *
 */

public class EnvelopeGenerator 
{
	private float[] envData;	//Holds envelope data. Rates and state change levels
	
	private int sampleRate;
	private float level;
	
	/*
	 * State variable tells us in which state this envelope generator currently is.
	 * States are as follows:
	 * 0 - ATTACK
	 * 1 - DECAY
	 * 2 - SUSTAIN
	 * 3 - RELEASE
	 * 4 - IDLE
	 */
	private int state;
	
	/**
	 * Initializes the envelope generator with the default values. The note will play at the same loudness
	 * the whole time. No scaling. Reference key for scaling is 44.
	 * @param sampleRate
	 */
	public EnvelopeGenerator(int sampleRate)
	{
		this.sampleRate = sampleRate;
		this.state = 4;

		this.envData = new float[8];

		for (int i = 0; i < 4; i++)
			this.envData[i] = (float) Math.tan(Math.PI / 2.01f) / sampleRate;
		for (int i = 4; i < 8; i++)
			this.envData[i] = 1.0f;
	}
	//--------------------- ENVELOPE GENERATION ---------------------\\
	public float getNextValue()
	{
		if (this.state == 4)
			return 0.0f;
		return calculateNextAmplitude();
	}
	
	private float calculateNextAmplitude()
	{
		switch (this.state)
		{
		case 0:
			level += this.envData[0] * 5.0f;
			if (level >= this.envData[4])
			{
				this.state = 1;
				level = this.envData[4];
			}
			break;
		case 1:
			level -= this.envData[1] * 10.0f;
			if (level <= this.envData[5])
			{
				this.state = 2;
				level = this.envData[5];
			}
			break;
		case 2:
			level -= this.envData[2] * 2.0f;
			if (level <= this.envData[6])
			{
				level = this.envData[6];
			}
			break;
		case 3:
			level -= this.envData[3] * 20.0f;
			if (level <= this.envData[7])
			{
				this.state = 4;
				level = this.envData[7];
			}
			break;
		}
		return level;
	}
	
	//--------------------- SETTERS ---------------------\\
	/**
	 * Sets the rate for one of the ADSR stages
	 * Which stage is being modified depends on the stage variable
	 * 0 - ATTACK
	 * 1 - DECAY
	 * 2 - SUSTAIN
	 * 3 - RELEASE
	 * @param stage - Which phase is modified
	 * @param rate - New rate for this stage
	 */
	public void setRate(int stage, int rate)
	{
		if (stage >= 0 && stage <= 3)
			if (rate >= 1 && rate <= 100)
				this.envData[stage] = (float) Math.tan(Math.PI / (-0.98979798f * rate + 100.98979798f))
						/ this.sampleRate;
			else
				System.err.println("Invalid rate for the envelope (Must be between 1 - 100) | Current: " + rate);
		else
			System.err.println("Invalid stage (Must be 0, 1, 2, 3) | Current: " + stage);
	}
	
	/**
	 * Sets the level for one of the ADSR stages
	 * Which stage is being modified depends on the stage variable
	 * 0 - ATTACK
	 * 1 - DECAY
	 * 2 - SUSTAIN
	 * 3 - RELEASE
	 * @param stage - Which stage is modified
	 * @param rate - New level for this stage
	 */
	public void setLevel(int stage, float level)
	{
		if (stage >= 0 && stage <= 3)
			if (level >= 0.0f && level <= 1.0f)
				this.envData[stage + 4] = level;
			else
				System.err.println("Invalid level for the envelope (Must be between 0 - 1) | Current: " + level);
		else
			System.err.println("Invalid stage (Must be 0, 1, 2, 3) | Current: " + stage);
	}
	
	/**
	 * An envelope generator is active if it is currently in one of these states: ATTACK, DELAY, SUSTAIN, RELEASE
	 * An envelope generator is not active if it is currently in IDLE state
	 * @return Boolean value on whether the envelope generator is active or not
	 */
	public boolean isActive()
	{
		return state != 4;
	}
	
	public void trigger(int key)
	{
		this.state = 0;
	}
	
	public void release()
	{
		this.state = 3;
	}
	
	//UNIT TEST
	public static void main(String[] args)
	{
		//TEST CODE
				EnvelopeGenerator e = new EnvelopeGenerator(44100);

				e.setRate(4, 100);
				e.setLevel(-1, 1.01f);
				
				e.trigger(1);
				e.trigger(44);
	}
}
