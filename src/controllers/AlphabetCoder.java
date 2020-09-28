package controllers;

import model.TextIOMode;
import model.exceptions.UnsupportedSymbolException;
import java.util.*;

public class AlphabetCoder extends AbstractCoder {

	@Override
	public void encode(final String text, final String word) throws UnsupportedSymbolException {
		checkIsValidWord(word);

		final StringBuilder resultText = new StringBuilder();
		int textLetterIndex = 0;
		int wordLetterIndex = 0;
		int encodedBits = BITS;
		String bitsStr = table.get(word.charAt(wordLetterIndex));

		while (true) {
			if (textLetterIndex >= text.length()) {
				System.out.println("Reached the end of the text to encode.");
				return;
			}

			if (encodedBits == BITS && wordLetterIndex >= word.length()) {
				System.out.println("Reached the end of the word.");
				break;
			}

			char symbolFromFile = text.charAt(textLetterIndex);

			if (ENG_LETTERS.contains(symbolFromFile)) {
				if (encodedBits == BITS) {
					final char symbolFromWord = word.charAt(wordLetterIndex);

					bitsStr = table.get(symbolFromWord);
					wordLetterIndex++;
					encodedBits = 0;

					System.out.printf("Trying to encode %c = %s\n", symbolFromWord, bitsStr);
				}

				final boolean bitFromLetter = "1".equals(String.valueOf(bitsStr.charAt(encodedBits)));

				if (bitFromLetter) {
					symbolFromFile = RUS_LETTERS.get(ENG_LETTERS.indexOf(symbolFromFile));
				}

				System.out.printf("Read '%c', bit [%d]\n", symbolFromFile, (bitFromLetter ? 1 : 0));
				encodedBits++;
			}

			resultText.append(symbolFromFile);
			textLetterIndex++;
		}

		final String encodedText = resultText.append(text.substring(textLetterIndex)).toString();

		fileHelper.writeToFile("encoded.txt", encodedText);
		compareVolumeAfterEncoding(text, encodedText);
	}

	@Override
	public void decode(final String text, final TextIOMode textIOMode, final Integer encodedMessageLength) {
		final StringBuilder decodedByteStr = new StringBuilder();
		final StringBuilder decodedResult = new StringBuilder();
		int lettersToDecodeRead = 0;
		int bitsRead = 0;
		int fileLetterIndex = 0;

		while (lettersToDecodeRead < encodedMessageLength) {
			if (fileLetterIndex >= text.length()) {
				System.out.println("Reached the end of the text to decode.");
				return;
			}

			char symbolFromFile = text.charAt(fileLetterIndex);

			if (ENG_LETTERS.contains(symbolFromFile)) {
				decodedByteStr.append("0");
				System.out.printf("Read '%c', bit [0], byte [%s]\n", symbolFromFile, decodedByteStr);
				bitsRead++;
			} else if (RUS_LETTERS.contains(symbolFromFile)) {
				decodedByteStr.append("1");
				System.out.printf("Read '%c', bit [1], byte [%s]\n", symbolFromFile, decodedByteStr);
				bitsRead++;
			}

			if (bitsRead == BITS) {
				final Character letter = table.entrySet().stream()
						.filter(entry -> decodedByteStr.toString().equals(entry.getValue()))
						.map(Map.Entry::getKey)
						.findFirst()
						.orElse(null);

				if (Objects.nonNull(letter)) {
					System.out.printf("Decoded letter %c = [%s]\n", letter, decodedByteStr);

					decodedResult.append(letter);
					lettersToDecodeRead++;
					bitsRead = 0;
					decodedByteStr.setLength(0);
				}
			}

			fileLetterIndex++;
		}

		if (textIOMode == TextIOMode.FILE) {
			fileHelper.writeToFile("decoded.txt", decodedResult.toString());
		} else {
			System.out.println("Decoded word: " + decodedResult);
		}
	}

}
