package synth.io.data;

import java.util.List;

import javax.sound.midi.MidiEvent;

public class MidiFile 
{
	private List<MidiEvent> midiEvents;
	
	private int division;
	
	public MidiFile(List<MidiEvent> events, int division)
	{
		this.midiEvents = events;
		this.division = division;
	}
	
	public List<MidiEvent> getMidiEvents()
	{
		return this.midiEvents;
	}
	
	public int getDivision()
	{
		return this.division;
	}
}
