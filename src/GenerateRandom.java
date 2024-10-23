import ea.Individual;
import ea.Parameters;
import io.NumberFormat;
import io.StringIO;

public class GenerateRandom {

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			Individual ind = new Individual();
			StringIO.writeStringToFile(Parameters.PREFIX + "NoEvo" + "_" + NumberFormat.formatNumber(i, 4) + ".bpp",
					ind.problem.toString(), false);
		}
	}

}
