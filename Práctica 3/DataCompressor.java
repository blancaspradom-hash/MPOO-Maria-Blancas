public class DataCompressor {

    public static String compress(String data) {
        if (data == null || data.isEmpty()) {
            return "";
        }

        StringBuilder compressed = new StringBuilder();
        int currentRunLength = 1;

        for (int i = 1; i < data.length(); i++) {
            char currentCharacter = data.charAt(i);
            char previousCharacter = data.charAt(i - 1);

            if (currentCharacter != previousCharacter || currentRunLength == 9) {
                compressed.append(currentRunLength);
                compressed.append(previousCharacter);
                currentRunLength = 0;
            }

            currentRunLength++;
        }

        compressed.append(currentRunLength);
        compressed.append(data.charAt(data.length() - 1));

        return compressed.toString();
    }

    public static void main(String[] args) {
        System.out.println(compress("AAAAAAAAAAAAABBCCCCDD")); 
        System.out.println(compress("AAAAAAAAAA"));           
        System.out.println(compress("ABCDE"));                
        System.out.println(compress("AAAAAABBBAA"));          
    }
}