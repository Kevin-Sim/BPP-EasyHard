import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;

import io.NumberFormat;
import io.StringIO;

public class WeibullBPP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RandomDataGenerator rdg = new RandomDataGenerator();
		double[] data = new double[5000];
		for (int count = 0; count < 1000; count ++) {
			StringBuilder sb = new StringBuilder();
		
			sb.append("5000\r\n100\r\n");
			for(int i = 0; i < 5000; i++) {
				data[i] = rdg.nextWeibull(3, 45);//0.454, 1.044
//				data[i] = rdg.nextPoisson(50);
				if(data[i] < 1 || data[i] > 100) {
					i--;
					continue;
				}
				sb.append(Math.round(data[i]) + "\r\n");
				
			}
			StringIO.writeStringToFile("./WeibullNew/Weibel_3_45_" + NumberFormat.formatNumber(count, 4) + ".bpp", sb.toString(), false);
			System.out.println(count);
		}
	}

}
