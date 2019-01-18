import de.agitos.encfs4j.EncryptedFileSystemProvider;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Main {
    public static void main(String [] args) throws IOException {
        String rootDir = "C:\\Users\\nklap\\IdeaProjects\\encfs4j\\src\\main\\java";
        rootDir = rootDir.replaceAll("\\\\", "/");
        // According to
        // https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher

        FileSystemProvider provider = new EncryptedFileSystemProvider();
        Map<String, Object> env = new HashMap<String, Object>();
        env.put(EncryptedFileSystemProvider.CIPHER_ALGORITHM, "AES");
        env.put(EncryptedFileSystemProvider.CIPHER_ALGORITHM_MODE, "CTR");
        env.put(EncryptedFileSystemProvider.CIPHER_ALGORITHM_PADDING,
                "NoPadding");
        // 128 bit key
        env.put(EncryptedFileSystemProvider.SECRET_KEY, "f31BmUS)&?O!19W:".getBytes());
        env.put(EncryptedFileSystemProvider.FILESYSTEM_ROOT_URI, "file:///"
                + rootDir.replaceAll("\\\\", "/"));
        // env.put(EncryptedFileSystemProvider.REVERSE_MODE, "true");

        URI uri = URI.create("enc:///");
        FileSystem fs = provider.newFileSystem(uri, env);

        Path path = Paths.get(URI.create("enc:///"+rootDir.replaceAll("\\\\", "/")+"/filer"));
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
//
//        OutputStream outStream = Files.newOutputStream(path);
//        outStream.write("hello world".getBytes());
//        outStream.close();

        InputStream inStream = Files.newInputStream(path);
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

        StringBuilder buf = new StringBuilder();
        String line = null;
        while ((line = in.readLine()) != null) {
            buf.append(line);
        }
        inStream.close();

        System.out.print(buf.toString());
    }
}
