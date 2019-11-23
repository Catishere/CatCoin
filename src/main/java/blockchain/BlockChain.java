package blockchain;

import com.google.gson.Gson;
import utils.ByteUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlockChain {

    private static final BlockChain instance = new BlockChain();

    private BlockChain(){
        Block genesis = new Block("Totally legit blockchain");
        genesis.calculateHash();
        blockChain.add(genesis);

        transactionPool.add(new Transaction("wiki", "bo23bi", 10.0));
        transactionPool.add(new Transaction("asd", "23bobi", 20.0));
        transactionPool.add(new Transaction("sssda", "bo2bi", 30.0));
        transactionPool.add(new Transaction("s", "bo2bi", 440.0));
        transactionPool.add(new Transaction("wiki", "b12obi", 410.0));
        transactionPool.add(new Transaction("asdasdasd", "bo3bi", 410.0));
    }

    public static BlockChain getInstance(){
        return instance;
    }

    private LinkedList<Block> blockChain = new LinkedList<>();
    private List<Transaction> transactionPool = new ArrayList<>();
    private int difficulty = 3;

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public List<Transaction> getTransactionPool() {
        return transactionPool;
    }

    public void setTransactionPool(List<Transaction> transactionPool) {
        this.transactionPool = transactionPool;
    }

    public boolean verifyProof(byte[] hash) {
        System.out.println(ByteUtils.byteToHexString(hash));
        String hashString = ByteUtils.byteToHexString(hash);
        for (int i = 0; i < difficulty; i++) {
            if (hashString.charAt(i) != '0')
                return  false;
        }
        return true;
    }

    public boolean addBlock(Block block) {
        if (verifyProof(block.calculateHash())) {
            blockChain.add(block);
            return true;
        }
        return false;
    }

    public List<Block> getBlockChain() {
        return blockChain;
    }

    public void addTransaction(Transaction transaction) {
        transactionPool.add(transaction);
    }
}
