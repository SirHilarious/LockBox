package lockbox.data;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileDecryption {
    public static void main(String[] args) throws Exception {
        decryptFile("C:\\Test\\Authentication.json.enc");
    }

    public static void decryptFile(String encryptedFilePath) throws Exception {
        // Read the key from the key file
        String appData = System.getenv("APPDATA");
        Path lockBoxPath = Paths.get(appData, "LockBox");
        Path keyFilePath = lockBoxPath.resolve("key.txt");
        Path keyFile = Paths.get(String.valueOf(keyFilePath));
        byte[] encodedKey = Files.readAllBytes(keyFile);
        SecretKey key = new SecretKeySpec(encodedKey, "AES");
        System.out.println("Key: " + key);

        // Read the encrypted data from the file
        Path encryptedFile = Paths.get(encryptedFilePath);
        byte[] encryptedBytes = Files.readAllBytes(encryptedFile);
        byte[] iv = Arrays.copyOfRange(encryptedBytes, 0, 16);
        byte[] data = Arrays.copyOfRange(encryptedBytes, 16, encryptedBytes.length);

        // Decrypt the data
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        System.out.println("Cipher: " + cipher);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] outputBytes = cipher.doFinal(data);
        System.out.println("Decrypted data: " + Arrays.toString(outputBytes));

        // Write the decrypted data to a file
        String outputFilePath = encryptedFilePath.replace(".enc", "");
        Path outputFile = Paths.get(outputFilePath);
        Files.write(outputFile, outputBytes);
        System.out.println("Decrypted data written to file");

        // Delete the encrypted file and key file
        System.out.println("Deleting encrypted file and key file");
        Files.delete(encryptedFile);
        Files.delete(keyFile);
        System.out.println("Encrypted file and key file deleted");
    }
}