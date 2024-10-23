package io;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class Jar {
	
	 
	public static String getURL() {
	try {		
		Class cls = Class.forName(Jar.class.getName());
		ProtectionDomain pDomain = cls.getProtectionDomain();
		CodeSource cSource = pDomain.getCodeSource();
		URL loc = cSource.getLocation();
		return loc.toString();
	} catch (Exception e) {
		e.printStackTrace();
	}
		return null;
	}
	
	/**
	 * In eclipse returns 
	 * @return
	 */
	public static String getName() {
		String name = getURL();
//		System.out.println(name);
		if(name == null){
			return null;
		}
//		System.out.println("JarName = " + name);
		name = name.substring(name.lastIndexOf("/") + 1);
		return name;
	}
	
	public static boolean isJar(){
		if(getName().length() == 0){
			return true;
		}
		return true;
	}
	
	public static void main(String[] args) {
		String name = getName();
		System.out.println(name);
	}
}
