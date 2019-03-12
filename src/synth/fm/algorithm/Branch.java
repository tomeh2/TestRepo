package synth.fm.algorithm;

import java.util.LinkedList;

public class Branch implements Block
{
	private LinkedList<Block> systems;
	
	public Branch()
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
		float output = 0.0f;
		float temp = this.systems.get(0).process(input);
		for (int i = 1; i < this.systems.size(); i++)
			output += this.systems.get(i).process(temp);
		return output;
	}
}
