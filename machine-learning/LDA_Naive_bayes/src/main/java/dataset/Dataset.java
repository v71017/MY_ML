package dataset;

import java.util.List;

public class Dataset {
    private BagOfWords bow;
    private Vocabularies vocabs;
    
    public Dataset(String bagOfWordsFileName, String vocabsFileName) throws Exception {
        bow = new BagOfWords(bagOfWordsFileName);
        vocabs = new Vocabularies(vocabsFileName);
    }
    
    public Dataset(BagOfWords bow) {
        this.bow = bow;
        this.vocabs = null;
    }
    
    public BagOfWords getBow() {
        return bow;
    }

    public int getNumDocs() {
        return bow.getNumDocs();
    }

    public int getDocLength(int docID) {
        return bow.getDocLength(docID);
    }

    public List<Integer> getWords(int docID) {
        return bow.getWords(docID);
    }

    public int getNumVocabs() {
        return bow.getNumVocabs();
    }

    public int getNumNNZ() {
        return bow.getNumNNZ();
    }

    public int getNumWords() {
        return bow.getNumWords();
    }
    
    public Vocabulary get(int id) {
        return vocabs.get(id);
    }
    
    public int size() {
        return vocabs.size();
    }
    
    public Vocabularies getVocabularies() {
        return vocabs;
    }
    
    public List<Vocabulary> getVocabularyList() {
        return vocabs.getVocabularyList();
    }
}
