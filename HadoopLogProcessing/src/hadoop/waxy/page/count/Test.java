package hadoop.waxy.page.count;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	
	public static void main(String args[]) 
	{
		
		String line ="130.113.69.66 - - [18/Mar/2003:09:39:16 -0800] \"GET /archive/2002/06/12/the_onio.shtml HTTP/1.1\" 200 13671 \"http://www.google.ca/search?q=June+2002+%22the+onion%22&ie=UTF-8&oe=UTF-8&hl=en&meta=\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.0.3705)\"";
		String word="";
		String data="";
		
		/*
		StringTokenizer tokenizer = new StringTokenizer(line);
		
		int i=0;
		while (tokenizer.hasMoreTokens()) {
			word = tokenizer.nextToken();
			
			Pattern pat = Pattern.compile("\\b(http://|https://)\\w+/\\b");
			
			Matcher mat = pat.matcher("http://www.google.com/asdsadsadsadsadskjfhdsfjsdhfds/asdsadas");
			
			System.out.println("token "+ i + ") " +word);
			i++;
		}
		*/
		
		
		DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy" , Locale.ENGLISH);
		DateFormat formatter2 = new SimpleDateFormat("dd MMMM yyyy" , Locale.ENGLISH);

		int error=0;
		
		Date begin=null;
		Date end=null;
		
		try {
			begin = formatter.parse("03 Dec 2003");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			end = formatter2.parse("20 Apr 2003");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		System.out.println("ok");
		
		
		
	}

}
