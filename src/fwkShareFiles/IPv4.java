package fwkShareFiles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: IPv4</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * Classe permettant de représenter une addresse IPv4</p>
 * @author OGOULOLA Marlin Yannick
 * @society PlutoSoft-Benin
 * 
 */

public class IPv4 {	
	
	/**pattern représentant la composition d'une adresse ip**/
	private static Pattern pattern = Pattern.compile("^\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b$");
	
	/**
	 * Permert de savoir la validité d'une addresse ip
	 * @param ip
	 * @return le verdicte
	 */
	public static boolean isIPv4(String ip)
	{
		Matcher matcher = pattern.matcher(ip);
		
		if(!matcher.find())
		{
			return false;
		}
		else
		{
			return true;
		}		
	}
}