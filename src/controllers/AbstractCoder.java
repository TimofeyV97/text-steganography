package controllers;

import model.TextIOMode;
import model.exceptions.UnsupportedSymbolException;
import view.ConsoleView;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCoder {

	protected static final Character [] ALPHABET = new Character [] {
			'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т',
			'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',
			'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т',
			'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
	};

	protected static final List<Character> RUS_LETTERS = Arrays.asList('К', 'А', 'М', 'О', 'Н', 'В', 'Е', 'Р', 'Х',
			'С', 'Т', 'о', 'р', 'у', 'х', 'а', 'е', 'с');

	protected static final List<Character> ENG_LETTERS = Arrays.asList('K', 'A', 'M', 'O', 'H', 'B', 'E', 'P', 'X',
			'C', 'T', 'o', 'p', 'y', 'x', 'a', 'e', 'c');

	protected static final Integer BITS = 7;

	protected final Map<Character, String> table;

	protected final ConsoleView consoleView;

	protected final FileHelper fileHelper;

	public AbstractCoder() {
		this.consoleView = new ConsoleView();
		this.fileHelper = new FileHelper();
		table = new HashMap<>();

		for (byte i = 0; i < ALPHABET.length; i++) {
			table.put(ALPHABET[i], String.format("%7s", Integer.toBinaryString(i)).replaceAll(" ", "0"));
		}
	}

	public Map<Character, String> getTable() {
		return this.table;
	}

	public abstract void encode(final String text, final String word) throws UnsupportedSymbolException;

	public abstract void decode(final String text, final TextIOMode textIOMode, final Integer encodedMessageLength);

	protected void compareVolumeAfterEncoding(final String initial, final String result) {
		System.out.printf(
				"Text volume before encoding: [%d]\nText volume after encoding: [%d]\n",
				initial.length(),
				result.length()
		);
	}

	protected void checkIsValidWord(final String word) throws UnsupportedSymbolException {
		for (final Character letter : word.toCharArray()) {
			if (!table.containsKey(letter)) {
				throw new UnsupportedSymbolException("Unsupported symbol: " + letter);
			}
		}
	}

}
