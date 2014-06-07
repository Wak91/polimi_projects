package app.androbenchmark;

public class Bruteforce {
	
	
	private static final String SECRET= "ciaoo";
	
	private static final String[] lettere = {"a","b","c","d","e","f","g","h","i","l","m","n","o","p","q","r","s","t","u","v","z","0","1","2","3","4","5","6","7","8","9"};
	

	
	public static void pureJava(){

    	for (int i = 0; i < lettere.length; i++) {
    		for (int j = 0; j < lettere.length; j++) {
    			for (int k = 0; k < lettere.length; k++) {
    				for (int h = 0; h < lettere.length; h++) {
    					for (int l = 0; l < lettere.length; l++) {    						
    						//costruisco la parola
    						String create = lettere[i] + lettere[j] + lettere[k] + lettere[h] + lettere[l];
    						//se e uguale esco
    						if(create.equals(SECRET)){
    							return;
    						}
    						
    					}
    				}
    			}
    		}
		}

	}
	
	public native static void pureJni();
	
	
	

}
