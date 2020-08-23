import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Main {
    private static String AES_KEY = "AES_KEY_HERE";

    public static void main(String[] args) {

        File inputFile = new File("/path/to/file.txt");
        File encryptedFile = new File("/path/to/file-enc.txt");
        File decryptedFile = new File("/path/to/file-dec.txt");

        //encrypt(AES_KEY, inputFile, encryptedFile);
        //decrypt(AES_KEY, encryptedFile, decryptedFile);

    }
    public static SecretKey getAESKeyFromPassword(char[] password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        // iterationCount = 65536
        // keyLength = 256
        KeySpec spec = new PBEKeySpec(password, salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        return secret;
    }
    public static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) {
        try
        {
            byte[] ivSpec = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec iv = new IvParameterSpec(ivSpec);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(cipherMode, getAESKeyFromPassword(AES_KEY.toCharArray(), AES_KEY), iv);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
    }
    public static void encrypt(String key, File inputFile, File outputFile) {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }
    public static void decrypt(String key, File inputFile, File outputFile) {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

}
