package secure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
public class RSAUtils {
    public static String path = "D:\\IdeaProjects\\PixelAppServer\\key";

    public static final String RSA = "RSA";

    public static KeyPair generateKey() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(1024);
            keyPair = keyPairGenerator.genKeyPair();
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }


    public static void SaveKeyPair(KeyPair keyPair)   {
        try {
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // Store Public Key.
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                    publicKey.getEncoded());
            FileOutputStream fos = new FileOutputStream(path + "\\public.key");
            fos.write(x509EncodedKeySpec.getEncoded());
            fos.close();

            // Store Private Key.
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                    privateKey.getEncoded());
            fos = new FileOutputStream(path + "\\private.key");
            fos.write(pkcs8EncodedKeySpec.getEncoded());
            fos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private static void dumpKeyPair(KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        System.out.println("Public Key: " + getHexString(pub.getEncoded()));

        PrivateKey priv = keyPair.getPrivate();
        System.out.println("Private Key: " + getHexString(priv.getEncoded()));
    }

    private static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static KeyPair LoadKeyPair()  {
        // Read Public Key.
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        try {
            File filePublicKey = new File(path + "\\public.key");
            FileInputStream fis = new FileInputStream(path + "\\public.key");
            byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
            fis.read(encodedPublicKey);
            fis.close();

            // Read Private Key.
            File filePrivateKey = new File(path + "\\private.key");
            fis = new FileInputStream(path + "\\private.key");
            byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
            fis.read(encodedPrivateKey);
            fis.close();

            // Generate KeyPair.
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                    encodedPublicKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                    encodedPrivateKey);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return new KeyPair(publicKey, privateKey);
    }
}