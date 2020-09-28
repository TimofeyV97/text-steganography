package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class FileHelper {

	public Stream<String> readFile(final String path) throws IOException {
		return Files.lines(Paths.get(path));
	}

	public void writeToFile(final String filePath, final String text) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(filePath, false));
			writer.write(text);

			System.out.printf("Text saved to the file '%s'.\n", filePath);
		} catch (final IOException e) {
			System.out.println("Error writing text to the file.");
		} finally {
			if (Objects.nonNull(writer)) {
				try {
					writer.close();
				} catch (final IOException exception) {
					System.out.println("Error closing file.");
				}
			}
		}
	}


}
