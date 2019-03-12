package synth.io.codec;

public class Encoder 
{
	public static byte toSample8(float sample)
	{
		byte data = (byte) Math.round(sample);
		
		return data;
	}
	
	public static byte[] toSample16(float sample)
	{
		int sampleInt = Math.round(sample);
		byte[] data = new byte[2];
		
		data[0] |= sampleInt;
		data[1] |= sampleInt >> 8;
		
		return data;
	}
	
	public static byte[] toSample24(float sample)
	{
		int sampleInt = Math.round(sample);
		byte[] data = new byte[3];

		data[0] |= sampleInt;
		data[1] |= sampleInt >> 8;
		data[2] |= sampleInt >> 16;
		
		return data;
	}
	
	public static byte[] toSamples8(float[] samples)
	{
		byte[] data = new byte[samples.length];
		
		for (int i = 0; i < samples.length; i++)
			data[i] = toSample8(samples[i]);
			
		return data;
	}
	
	public static byte[] toSamples16(float[] samples)
	{
		byte[] data = new byte[samples.length * 2];
		byte[] temp = new byte[2];
		
		int j = 0;
		for (int i = 0; i < data.length; i += 2)
		{	
			temp = toSample16(samples[j++]);
			data[i] = temp[1];
			data[i + 1] = temp[0];
		}
			
		return data;
	}
	
	public static byte[] toSamples24(float[] samples)
	{
		byte[] data = new byte[samples.length * 3];
		byte[] temp = new byte[3];
		
		int j = 0;
		for (int i = 0; i < data.length; i += 3)
		{
			temp = toSample24(samples[j++]);
			data[i] = temp[2];
			data[i + 1] = temp[1];
			data[i + 2] = temp[0];
		}
			
		return data;
	}
}
