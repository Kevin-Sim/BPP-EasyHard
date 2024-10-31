import java.util.Random;

import ea.Individual;
import ea.Parameters;
import ea.Parameters.Init;
import io.NumberFormat;
import io.StringIO;

public class GenerateRandom {

	public static void main(String[] args) {
		Random rnd = Parameters.random;
		for (int i = 0; i < 10000; i++) {
			Parameters.BIN_CAPACITY = rnd.nextInt(50, 5000);
			Parameters.INIT_METHOD = Init.UNIFORM;
			if(rnd.nextBoolean()) {
				Parameters.INIT_METHOD = Init.GAUSSIAN;
			}
			Parameters.MIN_ITEM_SIZE = rnd.nextInt(10, Parameters.BIN_CAPACITY / 3);
			Parameters.MAX_ITEM_SIZE = rnd.nextInt(Parameters.BIN_CAPACITY / 2, Parameters.BIN_CAPACITY);;
			Parameters.NUM_ITEMS = rnd.nextInt(40, 10000);
			Individual ind = new Individual(false);
			StringIO.writeStringToFile("./New folder//New folder//" + Parameters.getPrefix() + "_" + NumberFormat.formatNumber(i, 4) + ".bpp",
					ind.problem.toString(), false);
			if(i % 100 == 0) {
				System.out.println(i);
			}
		}
	}

}
