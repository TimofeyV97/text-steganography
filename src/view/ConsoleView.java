package view;

import controllers.FileHelper;
import model.CoderMode;
import model.TaskType;
import model.TextIOMode;
import model.exceptions.FileParseException;
import model.exceptions.InvalidChoiceException;
import model.exceptions.TextInputException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsoleView {

	private final BufferedReader bufferedReader;

	private final FileHelper fileHelper;

	public ConsoleView() {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		fileHelper = new FileHelper();
	}

	public TaskType chooseTaskType() throws InvalidChoiceException {
		System.out.print("Please enter task:\n1. Encode\n2. Decode\n(1/2): ");
		final int type;

		try {
			type = Integer.parseInt(bufferedReader.readLine());
		} catch (final Exception exception) {
			throw new InvalidChoiceException();
		}

		switch (type) {
			case 1:
				return TaskType.ENCODE;

			case 2:
				return TaskType.DECODE;

			default:
				throw new InvalidChoiceException();
		}
	}

	public CoderMode chooseCoderMode() throws InvalidChoiceException {
		System.out.print("Please enter mode:\n1. Aplhabet\n2. Spaces\n3. Special chars\n(1/2/3): ");
		final int mode;

		try {
			mode = Integer.parseInt(bufferedReader.readLine());
		} catch (final Exception exception) {
			throw new InvalidChoiceException();
		}

		switch (mode) {
			case 1:
				return CoderMode.ALPHABET;

			case 2:
				return CoderMode.SPACES;

			case 3:
				return CoderMode.SPECIAL_CHARS;

			default:
				throw new InvalidChoiceException();
		}
	}

	public TextIOMode chooseTextIOMode(final TaskType taskType) throws InvalidChoiceException {
		System.out.print(
				taskType == TaskType.ENCODE
						? "Get text from:\n1. File\n2. CLI\n(1/2): "
						: "Write text to:\n1. File\n2. CLI\n(1/2): "
		);

		final int mode;

		try {
			mode = Integer.parseInt(bufferedReader.readLine());
		} catch (final Exception exception) {
			throw new InvalidChoiceException();
		}

		switch (mode) {
			case 1:
				return TextIOMode.FILE;

			case 2:
				return TextIOMode.CLI;

			default:
				throw new InvalidChoiceException();
		}
	}

	public String getText(final TaskType taskType) throws FileParseException, TextInputException,
			InvalidChoiceException {
		return taskType == TaskType.ENCODE
				? readTextToEncode()
				: readFromFile().collect(Collectors.joining("\n"));
	}

	public String readFromCLI(final String prompt) throws TextInputException {
		System.out.print(prompt);

		try {
			return bufferedReader.readLine();
		} catch (final Exception exception) {
			throw new TextInputException();
		}
	}

	public void printTable(final Map<Character, String> table) {
		System.out.println("Alphabet table:");
		table.forEach((key, value) -> System.out.println(key + " | " + value));
		System.out.println();
	}

	private String readTextToEncode() throws FileParseException, TextInputException, InvalidChoiceException {
		return chooseTextIOMode(TaskType.ENCODE) == TextIOMode.FILE
				? readFromFile().collect(Collectors.joining("\n"))
				: readFromCLI("Please enter your text: ");
	}

	private Stream<String> readFromFile() throws FileParseException {
		System.out.print("Please enter the file path: ");

		try {
			return fileHelper.readFile(bufferedReader.readLine());
		} catch (final Exception exception) {
			throw new FileParseException();
		}
	}

}
