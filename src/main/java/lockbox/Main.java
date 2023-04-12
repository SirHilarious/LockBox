package lockbox;

import lockbox.data.FileEncryption;
import lockbox.screen.LoginProcess;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static final File FILE_PATH = new File("C:\\Test\\Authentication.json"); // TODO: Change this to your file path
    public static final File ENCRYPTED_FILE_PATH = new File(FILE_PATH + ".enc"); // TODO: Change this to your encrypted file path

    public static void main(String[] args) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        System.out.println("Starting LockBox!");
        System.out.println("Checking if the File already exists");

        if (FILE_PATH.exists()) {
            System.out.println("File already exists");
        } else {
            System.out.println("File does not exist");
            System.out.println("Creating the File");
        }

        System.out.println(FILE_PATH);
        System.out.println(ENCRYPTED_FILE_PATH);

        if (FILE_PATH.exists()) {
            System.out.println("File already exists");
            System.out.println("Encrypting the File");
            FileEncryption.encryptFile(FILE_PATH, ENCRYPTED_FILE_PATH);
            System.out.println("File encrypted");
        }

        System.out.println("Starting the Login Process");
        LoginProcess.launch(LoginProcess.class, args);
        System.out.println("Login Process Started");
    }
}