package synth.io;

/**
 * Specifies the writeOut function that every output device must contain.
 * It is a function that takes some number of samples from the synthesizer and writes
 * them to the corresponding output. Where and how it writes them depends on the
 * implementation of the writeOut function
 * @author Tomi
 *
 */
public interface OutputInterface
{
	public void writeOut(float[] samples);
}
