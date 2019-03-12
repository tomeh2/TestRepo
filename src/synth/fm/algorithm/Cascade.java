package synth.fm.algorithm;

import java.util.LinkedList;

public class Cascade implements Block
{
	private LinkedList<Block> systems;
	
	public Cascade()
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
		float temp = input;
		for (Block s : this.systems)
			temp = s.process(temp);
		return temp;
	}
}
