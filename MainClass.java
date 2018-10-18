import java.io.IOException;

public class MainClass {
	
	private static void testHello() throws IOException {
		CompressorLibrary.writeCFile("PS 3\\inputs\\Hello.txt");
		CompressorLibrary.readCFile("compressedHello.txt");
	}
	
	private static void testConstitution() throws IOException {
		CompressorLibrary.writeCFile("USConstitution.txt");
		CompressorLibrary.readCFile("compressedUSConstitution.txt");
	}
	
	private static void testWP() throws IOException {
		CompressorLibrary.writeCFile("WarAndPeace.txt");
		CompressorLibrary.readCFile("compressedWarAndPeace.txt");
	}
	
	private static void testPoem() throws IOException {
		CompressorLibrary.writeCFile("Poem");
		CompressorLibrary.readCFile("compressedPoem.txt");
	}
	
	public static void main(String[] args) throws IOException {
		testHello();
		//testPoem();
		//testConstitution();
		//testWP();
	}
}
