package synth.engine;

/**
 * It is basically 0 - 2*PI counter. Used to keep the oscillators in phase, it is necessary otherwise artifacting may occur
 * @author Tomi
 *
 */
public class Clock 
{
	private static int currentTime;
	
	public static void initialize(int sampleRate)
	{
		currentTime = 0;
	}
	
	public static void update()
	{
		currentTime += 1;
		if (currentTime >= 4410000)
			currentTime -= 4410000;
	}
	
	public static float getTime()
	{
		return currentTime;
	}
}
