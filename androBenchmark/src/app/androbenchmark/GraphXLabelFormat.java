package app.androbenchmark;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

class GraphXLabelFormat extends Format {

	private final String[] LABELS = {"" , "Java", "JNI", "Renderscript", ""};
	
	@Override
	public StringBuffer format(Object object, StringBuffer buffer,
			FieldPosition field) {
		 
		int parsedInt = Math.round(Float.parseFloat(object.toString()));
	        String labelString = LABELS[parsedInt];

	        buffer.append(labelString);
	        return buffer;	
	}

	@Override
	public Object parseObject(String string, ParsePosition position) {
        return java.util.Arrays.asList(LABELS).indexOf(string);	}

}