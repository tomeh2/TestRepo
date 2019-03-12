

import java.util.ArrayList;

import synth.engine.SoundEngine;
import synth.io.devices.AudioOut;
import synth.io.devices.FileIn;
import synth.io.devices.FileOut;
import synth.io.devices.KeyboardIn;
import synth.patch.Patch;
import synth.patch.PatchLoader;

public class Main
{
	private static String s1 = "C:\\Users\\Tomi\\Desktop\\MIDI's\\";
	private static String s2 = "E:\\Stuff\\";
	
	public static void main(String[] args) throws Exception
	{
		/*
		Scanner s = new Scanner(System.in);
		System.out.println("Sample Rate: ");
		int sr = Integer.parseInt(s.nextLine());
		System.out.println("BufferSize: ");
		int buf = Integer.parseInt(s.nextLine());
		System.out.println("PatchBank: ");
		String patch = s.nextLine();
		System.out.println("MIDI File: ");
		String midif = s.nextLine();*/
		
		ArrayList<Patch> p = PatchLoader.loadFromFile("C:\\Users\\Tomi\\Desktop\\p.ptch");
		System.out.println(p.get(0).getName());
		System.out.println(p.get(1).getName());
		System.out.println(p.get(2).getName());
		
		SoundEngine synth = new SoundEngine(88200, "C:\\Users\\Tomi\\Desktop\\p.ptch");
		/*
		if (midif.equals(""))
			synth.start(new KeyboardIn(), new AudioOut(sr, buf));
		else
			synth.start(new FileIn(midif), new FileOut("D:\\song"));*/
		
		//synth.start(new FileIn(s1 + "EDITED.mid"), new AudioOut(88200, 6144));
		synth.start(new FileIn("C:\\Users\\Tomi\\Desktop\\fur-elise.mid"), new FileOut("D:\\test.test"));
		//synth.start(new KeyboardIn(), new AudioOut(88200, 2048));
		//synth.start(new KeyboardIn(), new FileOut("D:\\test.test"));
	}
}





















































