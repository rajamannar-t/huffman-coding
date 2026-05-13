# Huffman Coding Project

This project implements a simple Huffman coding encoder and decoder in Java.

## Project Structure

- `Huffman.java` - main Java program that:
  - reads input text from `message.txt`
  - calculates character frequencies
  - builds a Huffman tree
  - writes the Huffman table to `huffmanTable.txt`
  - writes the encoded bit string to `encoded_data.txt`
  - decodes the bit string and writes the result to `target_message.txt`
- `message.txt` - input text to encode
- `huffmanTable.txt` - generated Huffman code table and frequencies
- `encoded_data.txt` - generated encoded bit string
- `target_message.txt` - decoded output from the encoded data

## Usage

1. Make sure you have Java installed (Java 8 or newer).
2. Place your input text in `message.txt`.
3. Compile the program:

```bash
javac Huffman.java
```

4. Run the program:

```bash
java Huffman
```

5. Output files will be generated:
   - `huffmanTable.txt`
   - `encoded_data.txt`
   - `target_message.txt`

## Notes

- `Huffman.java` currently reads the complete file from `message.txt` as a single message.
- The decoder reads `encoded_data.txt` and uses the generated Huffman tree to reconstruct the original message.
- If you want to test with different input, update `message.txt` and rerun the program.
