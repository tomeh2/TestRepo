package synth.helpers;

public class StringHelper
{
	/**
	 * Converts a given string into an array of integers.
	 * STRING FORMAT: "x,y,...,z" 
	 * x,y,z - Any whole number
	 * Numbers must be separated by a colon
	 * 
	 * @param str - String which will be parsed
	 * @return - an array of integers containing numbers from a string
	 */
	public static int[] stringToIntArray(String str)
	{
		int[] array;
		
		// Just to make sure all tabs and spaces are removed
		str.trim();
		str = str.replace("\t", "");
		
		String[] numbers = str.split(",");
		array = new int[numbers.length]; 
		
		for (int i = 0; i < numbers.length; i++)
			array[i] = Integer.parseInt(numbers[i]);
		
		return array;
	}
	
	/**
	 * Converts a given string into an array of floats.
	 * STRING FORMAT: "x,y,...,z" 
	 * x,y,z - Any floating point number
	 * Numbers must be separated by a colon
	 * 
	 * @param str - String which will be parsed
	 * @return - an array of integers containing numbers from a string
	 */
	public static float[] stringToFloatArray(String str)
	{
		float[] array;
		
		// Just to make sure all tabs and spaces are removed
		str.trim();
		str = str.replace("\t", "");
		
		String[] numbers = str.split(",");
		array = new float[numbers.length]; 
		
		for (int i = 0; i < numbers.length; i++)
			array[i] = Float.parseFloat(numbers[i]);
		
		return array;
	}
}
