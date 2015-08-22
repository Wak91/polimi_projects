package app.androbenchmark.util;

import android.util.Log;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * This class is meant to provide authentication for a
 * self-signed certificate used server-side, whose public
 * key is stored in the assets folder.
 *
 * Created by Matteo Ferroni on 19/05/14.
 */
public final class PubKeyManager implements X509TrustManager {

    private final String TAG = "PubKeyManager";
    private X509Certificate[] trustedCerts = null;

    public PubKeyManager(){
    	/*
        super();
        InputStream serverCertStream = null;
        try{
        	
            // Get pub key on creation
            serverCertStream = Application.getInstance().getAssets().open("server.cert");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(serverCertStream);

            // Note: use just a single certificate
            trustedCerts = new X509Certificate[1];
            trustedCerts[0] = cert;
            
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Public key not found on the device");
        } catch (IOException e) {
            Log.e(TAG, "I/O exception while opening the public key");

        } catch (CertificateException e) {
            Log.e(TAG, "Generic certificate exception");
        } finally {
            try {
                serverCertStream.close();
            } catch (Exception e) {
                Log.e(TAG, "Exception while closing the public key file");
            }
        }
        */
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() { return trustedCerts; }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
        // No client authentication
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        // Assert chain validity
        assert(chain != null);
        assert(chain.length > 0);

        boolean certificatesMatch = false;
        try{
            for(X509Certificate receivedCert : chain){
                if(receivedCert.equals(trustedCerts[0])){
                    // Note: use just a single certificate
                    certificatesMatch = true;
                    break;
                }
            }
        }catch (Exception e){
            Log.e(TAG, "Generic exception while checking trusted server");
        }

        if(!certificatesMatch)
            throw new CertificateException("Certificate doesn't match");
    }

}