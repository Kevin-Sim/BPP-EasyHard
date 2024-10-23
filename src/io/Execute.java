package io;



import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public class Execute {

	public static void openFile(String fileName){
		try {
			Desktop.getDesktop().open(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param appName
	 *            The executable file name or file name associated with a
	 *            program
	 * @param appPath
	 *            The path to appName directory terminating in a "\"
	 * @param waitForReturn
	 *            for executable files waits for the application to be exited
	 *            before continuing
	 * @param args
	 *            command line parameters to be passes to the executable file
	 *            appName
	 * 
	 *            Attempts to start a process using the command prompt opened
	 *            using cmd /c which terminates the prompt after completion of
	 *            the process. Once the command prompt is open a string of the
	 *            following format is attempted to be initiated.
	 *            
	 *            start "window title" /D "appPath" "appName" arg0 arg1 ....argN
	 *            
	 *            which changes the working directory to that specified in appPath
	 *            (if appPath is specified) and attempts to execute appName using
	 *            the supplied arguments array args[] = arg1...argN
	 *            
	 */
	public static void startApp(String appName, String appPath,
			boolean waitForReturn, String[] args) {

		ArrayList<String> paramsArrayList = new ArrayList<String>();
		paramsArrayList.add("cmd");
		paramsArrayList.add("/c");
		paramsArrayList.add("start");
		paramsArrayList.add("\"Kev's Java App Starter\"");
		if (appPath != null) {
			paramsArrayList.add("/D");
			paramsArrayList.add("\"" + appPath + "\"");
		}

		paramsArrayList.add("\"" + appName + "\"");

		if (args != null) {
			for (String str : args) {
				paramsArrayList.add(str);
			}
		}
		String[] paramsArray = new String[paramsArrayList.size()];
		for (int i = 0; i < paramsArrayList.size(); i++) {
			paramsArray[i] = paramsArrayList.get(i);
		}

		String temp = "";
		for(String str : paramsArray){
			temp += str + " ";
		}
//		JOptionPane.showMessageDialog(null, paramsArray);
//		System.out.print(temp);

		ProcessBuilder pb = new ProcessBuilder(paramsArray);

		Map<String, String> env = pb.environment();
		String pathToAdd = env.get("Path");

		if (appPath != null) {
			pathToAdd += ";" + appPath;
			env.put("Path", pathToAdd);
		}

		try {
			// below from except pb.start(); not needed for longer runs
			pb.redirectErrorStream(true); // merge stdout, stderr of
			// process
			Process p = pb.start();
			InputStreamReader isr = new InputStreamReader(p.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String line;
			if (waitForReturn) {
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				@SuppressWarnings("unused")
				int rc = p.waitFor();
				// TODO error handling for non-zero rc
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
