package synth.io.devices;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import synth.io.OutputInterface;
import synth.io.codec.Encoder;

public class AudioOut implements OutputInterface
{
	private SourceDataLine sdl;
	
	public AudioOut(int sampleRate, int bufferSize)
	{
		AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, true);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		
		try 
		{
			sdl = (SourceDataLine) AudioSystem.getLine(info);
			sdl.open(format, bufferSize);
		}
		catch (LineUnavailableException e) 
		{
			System.err.println("Error in AudioOut::AudioOut -> " + e.getMessage());
		}

		sdl.start();
		
		for (AudioFormat f : info.getFormats())
			System.out.println(f.getSampleRate());
	}
	
	public void writeOut(float[] buffer)
	{
		byte[] data = Encoder.toSamples16(buffer);
		sdl.write(data, 0, data.length);
	}
}
