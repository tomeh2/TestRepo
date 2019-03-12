package synth.io.devices;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.swing.JFrame;

import synth.io.InputInterface;

public class KeyboardIn extends JFrame implements InputInterface
{
	private static final long serialVersionUID = 1L;

	private Queue<MidiEvent> eventQueue;
	
	private boolean[] activeKeys;
	
	public KeyboardIn()
	{
		this.activeKeys = new boolean[256];
		this.eventQueue = new LinkedList<>();
		this.setSize(500, 500);
		this.setVisible(true);
		this.addKeyListener(new KeyCapturer());
	}
	
	@Override
	public MidiEvent getNextEvent(int timestamp) 
	{
		if (this.eventQueue.peek() != null)
			return this.eventQueue.poll();
		return null;
	}

	@Override
	public int getDivision() 
	{
		return 0;
	}
	
	private class KeyCapturer implements KeyListener
	{
		@Override
		public void keyTyped(KeyEvent e)
		{
			//Unused
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			if (!activeKeys[e.getKeyCode()])
			{
				eventQueue.add(this.constructEvent(0x90, e.getKeyCode()));
			}
			activeKeys[e.getKeyCode()] = true;
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
			if (activeKeys[e.getKeyCode()])
			{
				eventQueue.add(this.constructEvent(0x80, e.getKeyCode()));
			}
			activeKeys[e.getKeyCode()] = false;
		}
		
		private MidiEvent constructEvent(int command, int key)
		{
			ShortMessage m = new ShortMessage();
			
			try
			{
				m.setMessage(command, 1, key, 127);
			}
			catch (InvalidMidiDataException e1)
			{
				System.err.println("Error in KeyboardIn::KeyCapturer::constructEvent -> " + e1.getMessage());
			}
			
			MidiEvent e = new MidiEvent(m, 0);
			return e;
		}
	}
}
