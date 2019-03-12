package synth.io;

import javax.sound.midi.MidiEvent;

/**
 * An interface that defines what every input device should support. 
 * 
 * isActive is a method that tells the engine whether the given stream is done and 
 * should it continue reading from it
 * @author Tomi
 *
 */
public interface InputInterface
{
	public MidiEvent getNextEvent(int timestamp);
	public int getDivision();		//Division is used if the input is from a MIDI file. Otherwise return 0.
	public boolean isActive();
}
