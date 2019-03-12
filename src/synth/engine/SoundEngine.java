package synth.engine;

import java.util.ArrayList;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import synth.io.InputInterface;
import synth.io.OutputInterface;
import synth.patch.Patch;
import synth.patch.PatchLoader;
import synth.postprocessing.EffectBox;
import synth.postprocessing.effects.Reverberation;

public class SoundEngine 
{
	private static final int NUM_CHANNELS_DEFAULT = 16;
	private static final int MAX_POLYPHONY_DEFAULT = 48;
	private static final String PATCH_BANK_LOCATION_DEFAULT = "C:\\Users\\Tomi\\Desktop\\p.ptch";
	
	private int sampleRate = 44100;

	private Renderer renderer;
	private OutputInterface out;
	private InputInterface in;
	
	private EffectBox effectProcessor;
	private EngineThread outThr;
	
	/**
	 * Synthesizer engine will be created using default settings. 
	 * That means that the sample rate will be set to 44100 and it will 
	 * load whatever patch bank is set as default
	 */
	public SoundEngine()
	{
		ArrayList<Patch> patchBank = PatchLoader.loadFromFile(PATCH_BANK_LOCATION_DEFAULT);
		this.renderer = new Renderer(this.sampleRate, NUM_CHANNELS_DEFAULT, MAX_POLYPHONY_DEFAULT, patchBank);
		
		Clock.initialize(sampleRate);
	}
	
	/**
	 * Synthesizer engine will be created using options passed to it.
	 * @param sampleRate - Sample rate the engine will work with
	 * @param patchBankLocation - Address of the patch bank that will be loaded
	 */
	public SoundEngine(int sampleRate, String patchBankLocation)
	{
		this.effectProcessor = new EffectBox();
		this.effectProcessor.addEffect(new Reverberation());
		
		this.sampleRate = sampleRate;
		ArrayList<Patch> patchBank = PatchLoader.loadFromFile(patchBankLocation);
		this.renderer = new Renderer(this.sampleRate, NUM_CHANNELS_DEFAULT, MAX_POLYPHONY_DEFAULT, patchBank);
		
		Clock.initialize(sampleRate);
	}
	
	/**
	 * The synthesizer will start looking for inputs and create outputs.
	 * @param in - interface from which the synthesis engine will receive MIDI messages
	 * @param out - interface to which output will be generated
	 */
	public void start(InputInterface in, OutputInterface out)
	{
		this.in = in;
		this.out = out;
		this.outThr = new EngineThread();
		this.outThr.start();
	}
	
	/**
	 * The synthesizer will stop looking for inputs and stop creating outputs
	 */
	public void stop()
	{
		this.outThr.interrupt();
	}
	
	private class EngineThread extends Thread
	{
		private static final int BUFFER_SIZE_DEFAULT = 512;
		
		private float[] buffer;		//Samples are rendered here before they get to the output
		private double currentTime = 0;		//TODO: CHANGE TO INTEGER!!!!
		private float deltaTime = 0;	//By how much current time changes with each iteration

		public void run()
		{
			this.buffer = new float[BUFFER_SIZE_DEFAULT];
			
			setDefaultTimingSettings();
			mainLoop();
		}
		
		private void mainLoop()
		{	
			while (!Thread.interrupted() && in.isActive())
			{
				this.processEvents();
				this.renderSamples();
				this.currentTime += this.deltaTime;
			}
		}
		
		private void processEvents()
		{
			MidiEvent event;
			while (in.isActive() && (event = in.getNextEvent((int) currentTime)) != null)
			{
				if (event.getMessage() instanceof ShortMessage)
					renderer.processMessage((ShortMessage) event.getMessage());
				else if (event.getMessage() instanceof MetaMessage)
					this.processMetaMessage((MetaMessage) event.getMessage());
			}
		}
		
		private void processMetaMessage(MetaMessage message)
		{		
			switch (message.getType())
			{
			case 0x51:
				int newTempo = 0;
				byte[] tempoData = message.getData();
				
				newTempo |= (short) tempoData[0] & 0xFF;
				newTempo <<= 8;
				newTempo |= (short) tempoData[1] & 0xFF;
				newTempo <<= 8;
				newTempo |= (short) tempoData[2] & 0xFF;
				
				this.deltaTime = BUFFER_SIZE_DEFAULT / (float) sampleRate * 1000.0f / (newTempo / in.getDivision() / 1000.0f);
				break;
			}
		}
		
		private void setDefaultTimingSettings()
		{
			this.deltaTime = BUFFER_SIZE_DEFAULT / (float) sampleRate * 1000.0f / (500.0f / in.getDivision());
		}
		
		/**
		 * Renders and outputs N samples. N is the size of the audio buffer
		 */
		
		private void renderSamples()
		{
			for (int i = 0; i < BUFFER_SIZE_DEFAULT; i++)
			{
				float sample = renderer.renderNextSample();
				
				this.buffer[i] = sample * 6500.0f;
				
				Clock.update();
			}
			out.writeOut(buffer);
		}
	}
}
