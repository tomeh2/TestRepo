package synth.patch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import synth.helpers.StringHelper;
import synth.sources.WaveGenerator.Waveform;

public class PatchLoader 
{
	/**
	 * Loads a file containing patch data.
	 * @param filename - Patch bank file
	 * @return - ArrayList that contains patches
	 */
	public static ArrayList<Patch> loadFromFile(String filename)
	{
		BufferedReader data = loadFile(filename);
		Properties header = loadHeader(data);
		
		checkCompatibility(header);
		
		return loadPatches(data);
	}
	
	/**
	 * Checks if the patch file is compatible with the synthesizer's version.
	 * @param header - Properties object that contains header information
	 * @return - is this patch bank file compatible with the current version
	 */
	private static boolean checkCompatibility(Properties header)
	{
		if (!header.getProperty("engine_version").equals("0.1a"))
		{
			System.out.println("Patch bank version seems to be different from the synthesis engine version.\n" +
							   "There is no guarantee that the patch bank will work properly.\n" +
							   "Patch version: " + header.getProperty("engine_version") + "\n" +
							   "Engine version: 0.1a");
			return false;
		}
		return true;
	}
	
	/**
	 * Loads the files header. The header contains info about synth's version and the name of the patch bank.
	 * @param data - Reader object that is connected to the valid patch file
	 * @return - Properties object that contains all header information
	 */
	private static Properties loadHeader(BufferedReader data)
	{
		Properties p = new Properties();
		try 
		{
			String line;
			while (!(line = data.readLine()).equals("end_prop"))
			{
				String[] property = line.split("=");
				p.setProperty(property[0], property[1]);
			}
		} 
		catch (IOException e) 
		{
			System.err.println("Error in PatchLoader::loadHeader -> " + e.getMessage());
		}
		return p;
	}
	
	/**
	 * Extracts patch data into Patch objects, and returns them in an array list. Buffered reader's pointer MUST be positioned before the first
	 * patch in order for this to work properly!
	 */
	private static ArrayList<Patch> loadPatches(BufferedReader data)
	{
		int debug = 0;
		
		ArrayList<Patch> patches = new ArrayList<>();
		
		String line;
		try 
		{
			while (!(line = data.readLine()).equals("end_file"))
			{
				if (line.equals("{"))
				{
					patches.add(loadPatch(data));
					debug++;
				}
			}
		} 
		catch (IOException e) 
		{
			System.err.println("Error in PatchLoader::loadPatches -> \n" + e.getMessage());
		}
		System.out.println("Loaded " + debug + " patches");
		
		return patches;
	}
	
	/**
	 * Loads operator data for the patch, creates the Patch object and returns it
	 * @param data - Reader object pointing before the start of where patch data is stored
	 * @return - Patch object which contains loaded data
	 */
	private static Patch loadPatch(BufferedReader data)
	{
		Properties patchData = extractPatchData(data);
		return createPatchFromData(patchData);
	}
	
	private static Properties extractPatchData(BufferedReader data)
	{
		Properties patchData = new Properties();

		try 
		{
			String line;
			int operatorNum = 0;
			int nestingDepth = 1;
			while (nestingDepth > 0)
			{
				//Read next line and remove all tabs
				line = data.readLine();
				line = line.replace("\t", "");
				
				if (line.equals("{"))
				{
					nestingDepth++;
					operatorNum++;
					continue;
				}
				else if (line.equals("}"))
				{
					nestingDepth--;
					continue;
				}

				if (operatorNum > 0)
				{
					String[] property = line.split("=");
					patchData.setProperty(property[0] + operatorNum, property[1]);
				}
				else
				{
					String[] property = line.split("=");
					patchData.setProperty(property[0], property[1]);
				}
			}
		} 
		catch (IOException e)
		{
			System.err.println("Error in PatchLoader::extractPatchData\n" + e.getMessage());
			return null;
		}
		return patchData;
	}
	
	private static Patch createPatchFromData(Properties patchData)
	{
		Patch patch = new Patch(Integer.parseInt(patchData.getProperty("opv")),
								Integer.parseInt(patchData.getProperty("vpk")),
								patchData.getProperty("alg"));
		
		patch.setName(patchData.getProperty("name"));
		
		for (int i = 0; i < Integer.parseInt(patchData.getProperty("opv")); i++)
		{
			if (patchData.getProperty("waveform" + (i + 1)).equals("SIN"))
				patch.setWaveform(i, Waveform.SINE_WAVE);
			else if (patchData.getProperty("waveform" + (i + 1)).equals("SQR"))
				patch.setWaveform(i, Waveform.SQUARE_WAVE);
			else if (patchData.getProperty("waveform" + (i + 1)).equals("TRI"))
				patch.setWaveform(i, Waveform.TRIANGLE_WAVE);
			else if (patchData.getProperty("waveform" + (i + 1)).equals("SAW"))
				patch.setWaveform(i, Waveform.SAW_WAVE);
			else if (patchData.getProperty("waveform" + (i + 1)).equals("NOISE"))
				patch.setWaveform(i, Waveform.NOISE);
			
			patch.setFrequencyRatio(i, Float.parseFloat(patchData.getProperty("freq_ratio" + (i + 1))));
			patch.setOutputLevel(i, Float.parseFloat(patchData.getProperty("out_lvl" + (i + 1))));
			patch.setModulationSensitivity(i, Float.parseFloat(patchData.getProperty("mod_sens" + (i + 1))));
			patch.setEnvelopeLevels(i, StringHelper.stringToFloatArray(patchData.getProperty("env_lvl" + (i + 1))));
			patch.setEnvelopeRates(i, StringHelper.stringToIntArray(patchData.getProperty("env_rate" + (i + 1))));
		}
		return patch;
	}
	
	/**
	 * Creates the Reader object and connects it to the file given by filename
	 * @param filename
	 * @return
	 */
	private static BufferedReader loadFile(String filename)
	{
		try 
		{
			return new BufferedReader(new FileReader(new File(filename)));
		} 
		catch (FileNotFoundException e)
		{
			System.err.println("File: " + filename + " does not exist!");
		}
		return null;
	}
	
	public static void main(String[] args)
	{		
		Properties properties = new Properties();
		properties.setProperty("engine_version", "1.0");
		properties.setProperty("patch_bank_name", "Test Patches");
		/*
		try {
			FileOutputStream f = new FileOutputStream(new File("C:\\Users\\Tomi\\Desktop\\p.ptch"));
			properties.store(f, null);
			
			byte[] b = {'v', 'd', 'a', 'g'};
			f.write(b);
			f.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
