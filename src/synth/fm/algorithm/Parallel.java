package synth.fm.algorithm;

import java.util.LinkedList;

public class Parallel implements Block
{
	private LinkedList<Block> systems;
	
	public Parallel()
	{
		this.systems = new LinkedList<>();
	}
	
	public void add(Block system)
	{
		this.systems.add(system);
	}
	
	@Override
	public float process(float input)
	{
		float temp = 0.0f;
		for (Block s : this.systems)
			temp += s.process(input);
		return temp;
	}
}
