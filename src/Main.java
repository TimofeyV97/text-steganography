import controllers.AbstractCoder;
import controllers.AlphabetCoder;
import controllers.SpacesCoder;
import controllers.SpecialCharsCoder;
import model.CoderMode;
import model.TaskType;
import model.TextIOMode;
import model.exceptions.*;
import view.ConsoleView;

public class Main {

	public static void main(String[] args) {
		final ConsoleView consoleView = new ConsoleView();
		final AbstractCoder coder;
		final CoderMode coderMode;
		final TaskType taskType;
		final String text;

		try {
			coderMode = consoleView.chooseCoderMode();
		} catch (final InvalidChoiceException e) {
			System.out.println("Invalid mode.");
			return;
		}

		try {
			taskType = consoleView.chooseTaskType();
		} catch (final InvalidChoiceException e) {
			System.out.println("Invalid choice.");
			return;
		}

		try {
			text = consoleView.getText(taskType);
		} catch (final InvalidChoiceException e) {
			System.out.println("Invalid choice.");
			return;
		} catch (final FileParseException | TextInputException e) {
			System.out.println("Error reading text.");
			return;
		}

		switch (coderMode) {
			default:
			case ALPHABET:
				coder = new AlphabetCoder();
				break;

			case SPACES:
				coder = new SpacesCoder();
				break;

			case SPECIAL_CHARS:
				coder = new SpecialCharsCoder();
				break;
		}

		if (taskType == TaskType.ENCODE) {
			final String word;

			try {
				word = consoleView.readFromCLI("Please enter the word to encode: ");
			} catch (final TextInputException e) {
				System.out.println("Error reading text.");
				return;
			}

			try {
				consoleView.printTable(coder.getTable());
				coder.encode(text, word);
			} catch (final UnsupportedSymbolException e) {
				System.out.println(e.getMessage());
			}
		} else {
			final TextIOMode textIOMode;
			final int symbolsToRead;

			try {
				textIOMode = consoleView.chooseTextIOMode(taskType);
			} catch (final Exception e) {
				System.out.println("Error reading the length of the message.");
				return;
			}

			try {
				symbolsToRead = Integer.parseInt(
						consoleView.readFromCLI("Please enter the length of the encoded message: ")
				);
			} catch (final Exception e) {
				System.out.println("Error reading the length of the message.");
				return;
			}

			consoleView.printTable(coder.getTable());
			coder.decode(text, textIOMode, symbolsToRead);
		}
	}

}
