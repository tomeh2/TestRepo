package synth.fm.algorithm;

import synth.fm.Operator;

public class Algorithm 
{
	private Operator[] operators;
	private Block structure;
	
	public Algorithm(Operator[] operators, Block structure)
	{
		this.operators = operators;
		this.structure = structure;
	}
	
	public float getNextSample()
	{
		return this.structure.process(0.0f);
	}
	
	//---------------------- GETTERS ----------------------\\
	public Operator getOperator(int id)
	{
		if (id < 0 || id > operators.length)
			System.err.println("Operator " + id + " does not exist!");
		return this.operators[id];
	}
	
	public Operator[] getOperators()
	{
		return this.operators;
	}
}
