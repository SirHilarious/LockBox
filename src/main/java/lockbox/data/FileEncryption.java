package lockbox.data;

import javax.crypto.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class FileEncryption {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        encryptFile(new File("C:\\Test\\Authentication.json"), new File("C:\\Test\\Authentication.json.enc"));
    }

    public static void encryptFile(File filePath, File encryptedFilePath) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        // Input file
        String inputFilePath = String.valueOf(filePath);
        Path inputFile = Paths.get(inputFilePath);
        System.out.println("Input file path: " + inputFilePath);

        // Output file
        String outputFilePath = String.valueOf(encryptedFilePath);
        Path outputFile = Paths.get(outputFilePath);
        System.out.println("Output file path: " + outputFilePath);

        // Generate a random key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        System.out.println("Key: " + key);

        // Write the key to a file
        String appData = System.getenv("APPDATA");
        Path lockBoxPath = Paths.get(appData, "LockBox");
        Path keyFilePath = lockBoxPath.resolve("key.txt");
        System.out.println("Key file path: " + keyFilePath);

        try {
            System.out.println("Writing key to file...");
            Files.createDirectories(lockBoxPath);
            System.out.println("Key directory created");
            Files.write(keyFilePath, key.getEncoded(), StandardOpenOption.CREATE_NEW);
            System.out.println("Key written to file");
        } catch (IOException e) {
            // Handle the Exception
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // Encrypt the file
        System.out.println("Encrypting file...");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        System.out.println("Cipher: " + cipher);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        byte[] inputBytes = Files.readAllBytes(inputFile);
        byte[] outputBytes = cipher.doFinal(inputBytes);
        System.out.println("Encrypted data: " + Arrays.toString(outputBytes));

        // Write the encrypted data to a file
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.out.println("Output stream: " + outputStream);
        outputStream.write(iv);
        outputStream.write(outputBytes);
        byte[] finalOutputBytes = outputStream.toByteArray();
        Files.write(outputFile, finalOutputBytes);
        System.out.println("Encrypted data written to file");

        // Delete the original file
        System.out.println("Deleting original file");
        Files.delete(inputFile);
        System.out.println("Input file deleted");
    }
}