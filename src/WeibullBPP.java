import java.io.File;

import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;

import io.NumberFormat;
import io.StringIO;

public class WeibullBPP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RandomDataGenerator rdg = new RandomDataGenerator();
//		int[] ns = { 1000, 5000, 10000 };
//		int[] cs = { 100, 500 };
		int[] ns = { 100000 };
		int[] cs = { 100 };
		int numProbs = 50;
		for (int n : ns) {
			for (int c : cs) {

				String dirName = "./WeibullNew/WC_" + c + "N_" + n + "/";
				new File(dirName).mkdirs();
				double[] data = new double[n];
				for (int count = 0; count < numProbs; count++) {
					StringBuilder sb = new StringBuilder();

					sb.append("" + n + "\r\n" + c + "\r\n");
					for (int i = 0; i < n; i++) {
						data[i] = rdg.nextWeibull(3, 45);// 0.454, 1.044
//				data[i] = rdg.nextWeibull(0.45, 45);//0.454, 1.044
//				data[i] = rdg.nextPoisson(50);
						if (data[i] < 1 || data[i] > 100) {
							i--;
							continue;
						}
						sb.append(Math.round(data[i]) + "\r\n");
					}

					StringIO.writeStringToFile(
							dirName + "WC_" + c + "N_" + n + "_" + NumberFormat.formatNumber(count, 2) + ".bpp",
							sb.toString(), false);
					System.out.println(count);
				}
			}
		}
	}

}
