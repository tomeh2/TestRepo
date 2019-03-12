package synth.fm.algorithm;

public class Feedback implements Block
{
	private Block system;
	
	private float lastOutput = 0.0f;
	
	public Feedback(Block system) 
	{
		this.system = system;
	}
	
	@Override
	public float process(float input)
	{
		float temp = system.process(lastOutput);
		this.lastOutput = temp;
		return temp;
	}
}
