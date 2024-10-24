public class TextJustifier {

    private static String leftJustify(String[] line, int wordCount, int maxWidth) {
        StringBuilder leftJustifiedLine = new StringBuilder();

        for (int i = 0; i < wordCount; i++) {

            leftJustifiedLine.append(line[i]);

            if (i < wordCount - 1) {
                leftJustifiedLine.append(' ');
            }
        }

        while (leftJustifiedLine.length() < maxWidth) {
            leftJustifiedLine.append(' ');
        }
        return leftJustifiedLine.toString();
    }

    private static String justify(String[] line, int wordCount, int lineLength, int maxWidth) {
        if (wordCount == 1) {
            return leftJustify(line, wordCount, maxWidth);
        }

        int totalSpaces = maxWidth - lineLength;
        int spacesBetweenWords = totalSpaces / (wordCount - 1);
        int extraSpaces = totalSpaces % (wordCount - 1);

        StringBuilder justifiedLine = new StringBuilder();

        for (int i = 0; i < wordCount; i++) {
            justifiedLine.append(line[i]);

            if (i < wordCount - 1) {

                for (int j = 0; j < spacesBetweenWords; j++) {
                    justifiedLine.append(' ');
                }

                if (extraSpaces > 0) {
                    justifiedLine.append(' ');
                    extraSpaces--;
                }
            }
        }

        return justifiedLine.toString();
    }

    public static String[] justifyText(String[] words, int maxWidth) {

        if(words.length == 0){
            return words;
        }

        String[] result = new String[words.length];
        int resultIndex = 0, wordIndex = 0;

        String[] currentLine = new String[words.length];
        int currentLineLength = 0;
        int currentWordCount = 0;

        while (wordIndex < words.length) {
            String word = words[wordIndex];

            if (currentLineLength + word.length() + currentWordCount > maxWidth) {

                result[resultIndex++] = justify(currentLine, currentWordCount, currentLineLength, maxWidth);

                currentLine = new String[words.length];
                currentLineLength = 0;
                currentWordCount = 0;
            }

            currentLine[currentWordCount++] = word;
            currentLineLength += word.length();
            wordIndex++;
        }

        result[resultIndex++] = leftJustify(currentLine, currentWordCount, maxWidth);

        String[] finalResult = new String[resultIndex];
        System.arraycopy(result, 0, finalResult, 0, resultIndex);
        return finalResult;
    }

    public static void main(String[] args) {

        String[] words1 = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."};
        int maxWidth1 = 11;
        String[] result1 = justifyText(words1, maxWidth1);
        for (String line : result1) {
            System.out.println("\"" + line + "\"");
        }

        System.out.print('\n');

        String[] words2 = {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};
        int maxWidth2 = 20;
        String[] result2 = justifyText(words2, maxWidth2);
        for (String line : result2) {
            System.out.println("\"" + line + "\"");
        }

        System.out.print('\n');

        String[] words3 = {"Hello", "my", "friend!", "I", "am", "in", "London."};
        int maxWidth3 = 10;
        String[] result3 = justifyText(words3, maxWidth3);
        for (String line : result3) {
            System.out.println("\"" + line + "\"");
        }
    }
}

