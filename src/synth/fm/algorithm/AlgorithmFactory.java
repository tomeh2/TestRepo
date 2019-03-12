package synth.fm.algorithm;

import java.util.ArrayList;
import java.util.Collections;

import synth.fm.Operator;

/**
 * n-Operator Algorithm Generator.
 * @author Tomi
 *
 */

public class AlgorithmFactory 
{
	public static Algorithm createFromExpression(int sampleRate, int numOfOperators, String expression)
	{
		Operator[] operators = new Operator[numOfOperators];
		for (int i = 0; i < numOfOperators; i++)
			operators[i] = new Operator(sampleRate);
		
		Block structure = constructFromExpression(operators, expression);
		
		return new Algorithm(operators, structure);
	}
	
	private static Block constructFromExpression(Operator[] operators, String expression)
	{
		char command = expression.charAt(0);
		expression = expression.substring(2, expression.length() - 1);
		ArrayList<String> arguments = split(expression);

		switch (command)
		{
		case 'c':									//CASCADE
			Cascade c = new Cascade();
			for (String s : arguments)
				if (isDigit(s))
					c.add(operators[Integer.parseInt(s) - 1]);
				else
					c.add(constructFromExpression(operators, s));
			return c;
		case 'p':									//PARALLEL
			Parallel p = new Parallel();
			for (String s : arguments)
				if (isDigit(s))
					p.add(operators[Integer.parseInt(s) - 1]);
				else
					p.add(constructFromExpression(operators, s));
			return p;
		case 'b':									//BRANCH
			Branch b = new Branch();
			for (String s : arguments)
				if (isDigit(s))
					b.add(operators[Integer.parseInt(s) - 1]);
				else
					b.add(constructFromExpression(operators, s));
			return b;
		case 'f':									//FEEDBACK
			String s = arguments.get(0);
			if (isDigit(s))
				return new Feedback(operators[Integer.parseInt(s) - 1]);
			else
				return new Feedback(constructFromExpression(operators, s));
		default:
			System.err.println("ALGORITHM BUILDER: Unknown command: " + command);
		}
		return null;
	}
	//-------------------- PARSER --------------------\\
	private static ArrayList<String> split(String expression)
	{
		int nestingDepth = 0;
		
		ArrayList<String> arguments = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < expression.length(); i++)
		{
			char temp = expression.charAt(i);
			
			if (temp == ' ')
				continue;
			if (temp == '(')
				nestingDepth++;
			if (temp == ')')
				nestingDepth--;
			
			if (i == expression.length() - 1 && nestingDepth == 0)
			{
				builder.append(expression.charAt(i));
				arguments.add(builder.toString());
				builder.delete(0, builder.length());
			}
			if (temp == ',' && nestingDepth == 0)
			{
				arguments.add(builder.toString());
				builder.delete(0, builder.length());
			}
			else
				builder.append(temp);
		}
		Collections.reverse(arguments);
		return arguments;
	}
	
	private static boolean isDigit(String s)
	{
		try
		{
			Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}
}
