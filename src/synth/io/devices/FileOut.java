package synth.io.devices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import synth.io.OutputInterface;
import synth.io.codec.Encoder;

/**
 * Contains methods that will write the output of the synthesizer into the specified file rather then
 * an audio device.
 * 
 * @author Tomi
 *
 */
public class FileOut implements OutputInterface
{
	private FileOutputStream fos;

	public FileOut(String filename)
	{
		try 
		{
			this.fos = new FileOutputStream(new File(filename));
			
			System.out.println("Successfully created the output device! Output is to a file");
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("Error in FileOut::FileOut line 23\n" + e.getMessage());
		}
	}
	
	@Override
	public void writeOut(float[] samples) 
	{
		try {
			this.fos.write(Encoder.toSamples16(samples));
			this.fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
