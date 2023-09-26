package spell;

public class Trie implements ITrie {
    private INode rootNode;
    private int nodeCount;
    private int wordCount;

    public Trie() {
        rootNode = new Node();
        nodeCount = 1;
        wordCount = 0;
    }

    @Override
    public void add(String word) {
        INode currNode = rootNode;
        for (char c : word.toCharArray()) {
            if (currNode.getChildren()[c - 'a'] == null) {
                currNode.getChildren()[c - 'a'] = new Node();
                nodeCount++;
            }
            currNode = currNode.getChildren()[c - 'a'];
        }
        if (currNode.getValue() == 0) {
            wordCount++;
        }
        currNode.incrementValue();
    }

    @Override
    public INode find(String word) {
        INode currNode = rootNode;
        for (char c : word.toCharArray()) {
            if (currNode.getChildren()[c - 'a'] == null) {
                return null;
            }
            currNode = currNode.getChildren()[c - 'a'];
        }
        if (currNode.getValue() > 0) {
            return currNode;
        }
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder words = new StringBuilder();
        StringBuilder word = new StringBuilder();
        toStringHelper(rootNode, word, words);
        return words.toString();
    }

    private void toStringHelper(INode n, StringBuilder word, StringBuilder words) {
        if (n.getValue() > 0) {
            words.append(word);
            words.append('\n');
        }

        INode[] children = n.getChildren();

        for (int i = 0; i < 26; i++) {
            if (children[i] != null) {
                word.append((char)('a' + i));
                toStringHelper(children[i], word, words);
                word.deleteCharAt(word.length() - 1);
            }
        }
    }

    @Override
    public int hashCode() {
        int mod = 1;
        INode[] children = rootNode.getChildren();
        for (int i = 0; i < 26; i++) {
            if (children[i] != null) {
                mod += i;
            }
        }
        return mod * nodeCount * wordCount;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trie)) return false;
        Trie otherTrie = (Trie) o;
        if (wordCount != otherTrie.wordCount || nodeCount != otherTrie.nodeCount) return false;
        return equalsHelper(rootNode, otherTrie.rootNode);
    }

    private boolean equalsHelper(INode trieNode, INode otherNode) {
        if (trieNode.getValue() != otherNode.getValue()) return false;

        INode[] trieChildren = trieNode.getChildren();
        INode[] otherChildren = otherNode.getChildren();

        for (int i = 0; i < 26; i++) {
            if ((trieChildren[i] == null && otherChildren[i] != null) ||  (trieChildren[i] != null && otherChildren[i] == null)) return false;
        }

        for (int i = 0; i < 26; i++) {
            if (trieChildren[i] != null) {
                if (!equalsHelper(trieChildren[i], otherChildren[i])) return false;
            }
        }

        return true;
    }
}

