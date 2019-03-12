package synth.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import synth.io.data.MidiFile;

public class FileLoader 
{
	public static MidiFile loadMidiFromFile(String filename)
	{
		Sequence seq = loadSequenceFromFile(filename);
		List<MidiEvent> events = loadEventsFromSequence(seq);
		sortEventsByTimestamp(events);
		
		return new MidiFile(events, seq.getResolution());
	}

	private static Sequence loadSequenceFromFile(String filename)
	{
		Sequence seq = null;
		try 
		{
			seq = MidiSystem.getSequence(new FileInputStream(filename));
		} 
		catch (InvalidMidiDataException | IOException e) 
		{
			System.out.println("Could not load the file! Cause: " + e.getMessage());
		}
		return seq;
	}
	
	private static List<MidiEvent> loadEventsFromSequence(Sequence seq)
	{
		List<MidiEvent> events = new ArrayList<MidiEvent>();
		for (Track t : seq.getTracks())
		{
			for (int i = 0; i < t.size(); i++)
			{
				events.add(t.get(i));
			}
		}
		return events;
	}
	
	private static void sortEventsByTimestamp(List<MidiEvent> events)
	{
		Collections.sort(events, new Comparator<MidiEvent>()
		{
			@Override
			public int compare(MidiEvent o1, MidiEvent o2) {
				if (o1.getTick() < o2.getTick())
					return -1;
				else if (o1.getTick() > o2.getTick())
					return 1;
				else
					return 0;
			}
		});
	}
}
