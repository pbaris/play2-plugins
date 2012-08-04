package pbaris.play.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import play.mvc.Http.Request;

/**
 * A helper class for printing in a table format any instance 
 * of a {@link java.util.Map} or {@link play.mvc.Http.Request}
 * <br/><br/>
 * For example:
 * <p><blockquote><pre>
 *     PrintUtil.print(myMap);
 *     +--------------------------------------------+
 *     |                    Map                     |
 *     +--------------------------------------------+
 *     | aLongerKey          : one more value again |
 *     | key1                : a value              |
 *     | key2                : an other value       |
 *     | longKey             : one more value       |
 *     | theLongerstKeyOfAll : the last value       |
 *     +--------------------------------------------+
 * </pre></blockquote>
 * 
 * <p><blockquote><pre>
 *     PrintUtil.print(myHttpRequest);
 *     +---------------------------------------------------+
 *     |                  Http.Request                     |
 *     +---------------------------------------------------+
 *     | aLongerKey          : [1]                         |
 *     | key1                : [1, 2, 3]                   |
 *     | key2                : [alpha, beta, gamma, delta] |
 *     | longKey             : [value]                     |
 *     | theLongerstKeyOfAll : [yes, no, no, yes, no]      |
 *     +---------------------------------------------------+
 * </pre></blockquote>
 * 
 * @author Panos Bariamis
 * @since	0.0.1
 */
public class PrintUtil {

	protected final static String FRAME_START = "+";
	protected final static String FRAME_END = "+";
	protected final static String FRAME_HBORDER = "-";
	protected final static String FRAME_VBORDER = "|";
	protected final static String SEPARATOR = "_____SEP_____";
	
	private PrintUtil() {}
	
	/**
	 * Prints a {@link java.util.Map} in a table format
	 * 
	 * @param	map
	 * 			The map to be print
	 */
	public static void print(Map<String, ?> map) {
		if (map == null) return;
		
		Set<String> keys = map.keySet();
		if (keys.size() > 0) {
			List<String> list = new ArrayList<String>();
			for (String key : keys) {
				list.add(" " + key + SEPARATOR + map.get(key) + " ");
			}
			printFrame("Map", list);
		}
	}
	
	/**
	 * Prints the parameters of a {@link play.mvc.Http.Request} in a table format
	 * 
	 * @param	request
	 * 			The request to be print
	 */
	public static void print(Request request) {
		Map<String, String[]> map = request.queryString();
		if (map == null) return;
		
		Set<String> keys = map.keySet();
		if (keys.size() > 0) {
			List<String> list = new ArrayList<String>();
			for (String key : keys) {
				list.add(" " + key + SEPARATOR + Arrays.asList(map.get(key)) + " ");
			}
			printFrame("Http.Request", list);
		}
	}
	
	private static void printFrame(String title, List<String> list) {
		Collections.sort(list);
		String[] messages = list.toArray(new String[] {});
		String[] msgKeys = new String[messages.length];
		String[] msgVals = new String[messages.length];

		int index = 0;
		for (String message : messages) {
			String[] t = message.split(SEPARATOR);
			try {
				msgKeys[index] = t[0];
				msgVals[index++] = t[1];
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}

		int longest = findLongest(msgKeys);
		index = 0;
		for (String msgkey : msgKeys) {
			messages[index] = msgkey + compoundSpaces(longest, msgkey) + " : "
					+ msgVals[index++];
		}

		longest = findLongest(messages);
		longest = title.length() > longest ? title.length() + 2 : longest;
		String hborder = "";
		for (int i = 0; i < longest; i++) {
			hborder += FRAME_HBORDER;
		}

		StringBuilder result = new StringBuilder();
		result.append(FRAME_START).append(hborder).append(FRAME_END + "\n");
		result.append(FRAME_VBORDER).append(frameTitle(title, longest))
				.append(FRAME_VBORDER + "\n");
		result.append(FRAME_START).append(hborder).append(FRAME_END + "\n");
		for (String message : messages) {
			result.append(FRAME_VBORDER).append(message)
					.append(compoundSpaces(longest, message))
					.append(FRAME_VBORDER + "\n");
		}
		result.append(FRAME_START).append(hborder).append(FRAME_END);
		System.out.println(result);
	}

	private static int findLongest(String[] strs) {
		int result = 0;
		for (String str : strs) {
			if (str.length() > result) {
				result = str.length();
			}
		}
		return result;
	}

	private static String compoundSpaces(int longest, String str) {
		String result = "";
		for (int i = str.length(); i < longest; i++) {
			result += " ";
		}
		return result;
	}

	private static String frameTitle(String title, int longest) {
		char[] spaces = new char[longest];
		for (int i = 0; i < longest; i++) {
			spaces[i] = ' ';
		}
		String temp = new String(spaces);
		int length = title.length();
		int start = longest / 2 - length / 2;

		StringBuilder result = new StringBuilder();
		result.append(temp.substring(0, start)).append(title)
				.append(temp.substring(start + length));

		return result.toString();
	}
}
