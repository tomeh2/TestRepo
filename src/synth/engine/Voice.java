package synth.engine;

import synth.fm.Operator;
import synth.fm.algorithm.Algorithm;

public class Voice 
{
	//Contains frequencies for keys in the zeroth octave
	private final static float[] keyFreqs = {16.35f, 17.32391444f, 18.354048f, 19.44543649f, 20.60172231f,
									   		 21.82676447f, 23.12465142f, 24.49971475f, 25.9565436f,
									   		 27.5f, 29.13523509f, 30.86770633f};
	
	private Algorithm algorithm;
	
	private int activeKey;
	private float velocityModifier;

	public Voice(Algorithm algorithm)
	{
		this.algorithm = algorithm;
	}

	public float getNextSample()
	{		
		float sample = this.algorithm.getNextSample() * this.velocityModifier;
		return sample;
	}
	
	public void trigger(int keyNum, int velocity)
	{
		if (keyNum >= 0 || keyNum <= 88)
		{
			this.activeKey = keyNum;
			this.velocityModifier = velocity / 127.0f;
			
			int mul = (int) Math.floor(Math.pow(2, keyNum / 12));
			int key = keyNum % 12;
			for (Operator o : this.algorithm.getOperators())
			{
				o.setFrequency(keyFreqs[key] * mul);
				o.trigger(keyNum);
			}
		}
		else
			System.err.println("Key number out of range (0 - 88)");
	}
	
	public void release()
	{
		for (Operator o : this.algorithm.getOperators())
			o.release();
	}
	
	public boolean isActive()
	{
		boolean act = false;
		for (Operator o : this.algorithm.getOperators())
		{
			act |= o.isActive();
		}
		return act;
	}
	//---------------------- SETTERS ----------------------\\
	public void setDetuneFrequency(float detuneFrequency)
	{
		for (Operator o : this.algorithm.getOperators())
			o.setDetuneFrequency(detuneFrequency);
	}
	
	//---------------------- GETTERS ----------------------\\
	public Operator getOperator(int id)
	{
		return this.algorithm.getOperator(id);
	}
	
	public Operator[] getOperators()
	{
		return this.algorithm.getOperators();
	}
	
	public int getActiveKey()
	{
		return this.activeKey;
	}
}
