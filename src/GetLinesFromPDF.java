import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GetLinesFromPDF extends PDFTextStripper {

  static List<String> wordsToLookFor = new ArrayList<String>();
  static HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
  static List<String> wordsToSkip = new ArrayList<>(Arrays.asList(
      "for", "may", "m,", "was", "their", "from", "with", "or", "are", ".", "more", "our", "we", "this",
      "&", "3", "not", "an", "w", "you", "your", "?", "by", "were", "2023).", "know", "�", "program", "1",
      "do", "what", "et", "e", "t", "i", "h", "n",
      "the", "of", "on", "that", "as", "and", "in", "to", "a", "as", "be", "is", "it", ".", "", "0", "__", "j", "had",
      "which", "all", "but", "about", "us", "can", "found", "than", "must", "has", "have", "its", "they", "who", "?",
      "there", "any", "every", "other", "one", "+", "these", "at", "if", "how", "1.", "2."));

  public GetLinesFromPDF() throws IOException {
  }

  /**
   * @throws IOException If there is an error parsing the document.
   */
  public static void main(String[] args) throws IOException {
    String folderPath = "./bin";

    // Create a File object representing the folder
    File folder = new File(folderPath);

    // Get a list of all files in the folder
    File[] files = folder.listFiles();
    for (File file : files) {
      setUpData();
      System.out.println("Reading in from: " + file.getName());
      readPDF(file.getAbsolutePath());
    }
  }

  public static void readPDF(String fileName) throws IOException {
    PDDocument document = null;
    try {
      document = Loader.loadPDF(new File(fileName));

      PDFTextStripper stripper = new GetLinesFromPDF();
      stripper.setSortByPosition(true);
      stripper.setStartPage(0);
      stripper.setEndPage(document.getNumberOfPages());

      Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
      // At this point we're callign the overridden function below
      stripper.writeText(document, dummy);
    } finally {

      for (String word : wordsToLookFor) {
        System.out.println(word + "\t" + wordCount.get(word));
      }
      // List<Map.Entry<String, Integer>> entryList = new
      // ArrayList<>(wordCount.entrySet());

      // // Sort the list based on the word count (value) in descending order
      // entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

      // // Print the sorted list
      // for (int i = 0; i < 5; i++) {
      // String word = entryList.get(i).getKey();
      // System.out.println(word + "\t" + wordCount.get(word));
      // }
      // wordCount.clear();
      if (document != null) {
        document.close();
      }
    }
  }

  public static void setUpData() {
    if (wordsToLookFor.isEmpty()) {
      wordsToLookFor.add("her");
      wordsToLookFor.add("him");
      wordsToLookFor.add("woman");
      wordsToLookFor.add("man");
      wordsToLookFor.add("the");
      wordsToLookFor.add("white");
      wordsToLookFor.add("race");
      wordsToLookFor.add("gender");
      wordsToLookFor.add("sex");
      wordsToLookFor.add("sexual");
      wordsToLookFor.add("black");
    }
    for (String word : wordsToLookFor) {
      wordCount.put(word, 0);
    }
  }

  /**
   * Override the default functionality of PDFTextStripper.writeString()
   */
  @Override
  protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
    // you may process the line here itself, as and when it is obtained
    for (String word : str.split(" ")) {
      word = word.toLowerCase();
      // if (!wordsToSkip.contains(word.toLowerCase()) && word.length() > 1) {
      if (wordsToLookFor.contains(word)) {
        if (wordCount.containsKey(word)) {
          wordCount.put(word, wordCount.get(word) + 1);
        } else {
          wordCount.put(word, 1);
        }
      } else if (word.equalsIgnoreCase("women")) {
        word = "woman";
        if (wordCount.containsKey(word)) {
          wordCount.put(word, wordCount.get(word) + 1);
        } else {
          wordCount.put(word, 1);
        }
      } else if (word.equalsIgnoreCase("men")) {
        word = "man";
        if (wordCount.containsKey(word)) {
          wordCount.put(word, wordCount.get(word) + 1);
        } else {
          wordCount.put(word, 1);
        }
      }
    }
  }
}