package synth.io.devices;

import java.util.LinkedList;
import java.util.Queue;

import javax.sound.midi.MidiEvent;

import synth.io.FileLoader;
import synth.io.InputInterface;
import synth.io.data.MidiFile;

public class FileIn implements InputInterface
{
	private MidiFile midiFile;
	private Queue<MidiEvent> midiStream;
	
	public FileIn(String filename)
	{
		this.midiFile = FileLoader.loadMidiFromFile(filename);
		this.midiStream = new LinkedList<>();
		
		for (MidiEvent e : this.midiFile.getMidiEvents())
			this.midiStream.add(e);
	}
	
	public MidiEvent getNextEvent(int timestamp)
	{
		if (this.midiStream.peek() != null)
		{
			if (this.midiStream.peek().getTick() <= timestamp)
				return this.midiStream.poll();
		}				
		return null;
	}
	
	public int getDivision()
	{
		return this.midiFile.getDivision();
	}
	
	public boolean isActive()
	{
		return !this.midiStream.isEmpty();
	}
}