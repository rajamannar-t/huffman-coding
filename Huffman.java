import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
 class HuffmanTree {
    public HuffmanTree left;  // left node of the tree
    public HuffmanTree right; // right node of the tree
    public int freq;
    public char letter;

    public HuffmanTree(int freq, char letter) {
        this.freq = freq;
        this.letter = letter;
        this.left = null;
        this.right = null;
    }
}
 class PriorityQueue {
    private List<HuffmanTree> A = new ArrayList<>();

    // Heapify down
    private void heapifyDown(int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;

        if (left < A.size() && A.get(left).freq < A.get(i).freq) {
            smallest = left;
        }

        if (right < A.size() && A.get(right).freq < A.get(smallest).freq) {
            smallest = right;
        }

        if (smallest != i) {
            Collections.swap(A, i, smallest);
            heapifyDown(smallest);
        }
    }

    // Heapify up
    private void heapifyUp(int i) {
        int parent = (i - 1) / 2;
        if (i > 0 && A.get(parent).freq >= A.get(i).freq) {
            Collections.swap(A, i, parent);
            heapifyUp(parent);
        }
    }

    public int size() {
        return A.size();
    }

    public boolean empty() {
        return A.isEmpty();
    }

    public void push(HuffmanTree key) {
        A.add(key);
        int index = A.size() - 1;
        heapifyUp(index);
    }

    public void pop() {
        if (A.isEmpty()) return;
        A.set(0, A.get(A.size() - 1));
        A.remove(A.size() - 1);
        heapifyDown(0);
    }

    public HuffmanTree top() {
        if (!A.isEmpty()) {
            return A.get(0);
        }
        return new HuffmanTree(0, '#');
    }
}
 class Pair<A, B> {
    public A first;
    public B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
public class Huffman {
    static Map<Character, Pair<Integer, String>> freq = new HashMap<>();
    static HuffmanTree root;

    public static void messageDecode(HuffmanTree root) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("encoded_data.txt"));
        String encodedString = reader.readLine();
        reader.close();
        
        StringBuilder ans = new StringBuilder();
        HuffmanTree node = root;
        for (char c : encodedString.toCharArray()) {
            if (c == '0') node = node.left;
            else node = node.right;

            if (node.left == null && node.right == null) {
                ans.append(node.letter);
                node = root;
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("target_message.txt"));
        writer.write(ans.toString());
        writer.newLine();
        writer.close();
    }
    public static void findCodes(HuffmanTree root, List<Integer> arr, int top) throws IOException {
    // Traverse left subtree
    if (root.left != null) {
        arr.add(0); // Add 0 for left direction
        findCodes(root.left, arr, top + 1);
        arr.remove(arr.size() - 1); // Remove last element after traversal
    }

    // Traverse right subtree
    if (root.right != null) {
        arr.add(1); // Add 1 for right direction
        findCodes(root.right, arr, top + 1);
        arr.remove(arr.size() - 1); // Remove last element after traversal
    }

    // When reaching a leaf node, record its binary code
    if (root.left == null && root.right == null) {
        BufferedWriter file = new BufferedWriter(new FileWriter("huffmanTable.txt", true));
        file.write(root.letter + " " + root.freq + " ");
        
        // Writing the binary representation (the entire array contents)
        for (int bit : arr) {
            file.write(Integer.toString(bit));
        }

        // Storing the binary code for the character in the map
        freq.put(root.letter, new Pair<>(root.freq, arr.stream().map(Object::toString).collect(Collectors.joining())));
        
        file.newLine();
        file.close();
    }
}

   
    public static HuffmanTree buildTree(PriorityQueue pq) {
        while (pq.size() != 1) {
            HuffmanTree left = pq.top();
            pq.pop();
            HuffmanTree right = pq.top();
            pq.pop();
            HuffmanTree node = new HuffmanTree(left.freq + right.freq, '\0');
            node.left = left;
            node.right = right;
            pq.push(node);
        }
        return pq.top();
    }

    public static void messageEncode(String message) throws IOException {
        PriorityQueue pq = new PriorityQueue();
        for (Map.Entry<Character, Pair<Integer, String>> entry : freq.entrySet()) {
            HuffmanTree node = new HuffmanTree(entry.getValue().first, entry.getKey());
            pq.push(node);
        }
    
        BufferedWriter file = new BufferedWriter(new FileWriter("huffmanTable.txt"));
        file.write(Integer.toString(pq.size()));
        file.newLine();
        file.close();
    
        root = buildTree(pq);
    
        // Use a List<Integer> for dynamic resizing
        List<Integer> arr = new ArrayList<>();
        findCodes(root, arr, 0);
    
        file = new BufferedWriter(new FileWriter("encoded_data.txt"));
        for (char c : message.toCharArray()) {
            String codedString = freq.get(c).second;
            file.write(codedString);
        }
        file.newLine();
        file.close();
    }
    

    public static void findFrequency(String message) {
        for (char c : message.toCharArray()) {
            freq.put(c, new Pair<>(freq.getOrDefault(c, new Pair<>(0, "")).first + 1, ""));
        }

        for (Map.Entry<Character, Pair<Integer, String>> entry : freq.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().first);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("message.txt"));
        StringBuilder message = new StringBuilder();
        String line;
        while ((line = file.readLine()) != null) {
            message.append(line);
        }
        file.close();

        System.out.println(message);

        findFrequency(message.toString());
        messageEncode(message.toString());
        messageDecode(root);
    }
}