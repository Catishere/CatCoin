package blockchain;

import utils.ByteUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockChain {

    private static final BlockChain instance = new BlockChain();

    private BlockChain(){}

    public static BlockChain getInstance(){
        return instance;
    }

    private LinkedList<Block> blockChain = new LinkedList<>();
    private LinkedList<Block> blockQueue = new LinkedList<>();

    public void addBlock(Block block) {

        if (blockChain.size() > 0)
            block.setPrevHash(blockChain.getLast().getHash());
        else
            block.setPrevHash(new byte[32]);

        block.calculateHash();
        blockQueue.add(block);
    }

    public boolean verifyProof(byte[] hash) {
        String hashString = ByteUtils.byteToHexString(hash);
        int difficulty = 3;
        for (int i = 0; i < difficulty; i++) {
            if (hashString.charAt(i) != '0')
                return  false;
        }
        return true;
    }

    public Block findBlockInBlockQueue(String blockId) {
        for (Block block : blockQueue) {
            if (block.getId().equals(blockId))
                return block;
        }
        return null;
    }

    public Block findBlockInBlockChain(String blockId) {
        for (Block block : blockChain) {
            if (block.getId().equals(blockId))
                return block;
        }
        return null;
    }

    public String mineBlock(String blockId, Long proof) {
        for (Block block : blockQueue) {
            if (block.getId().equals(blockId))
            {
                block.setProofOfWork(proof);
                if (verifyProof(block.calculateHash())) {
                    blockChain.add(block);
                    blockQueue.remove(block);
                    return "Success";
                }
                return "Proof doesn't match";
            }
        }
        return "Not Found";
    }

    public String serializeBlock(Block block)
    {
        return "{\"hash\":" +
                Arrays.toString(block.serialize()) +
                ",\"id\":\"" +
                block.getId() +
                "\"}";
    }

    public String serializeBlockList(String header, List<Block> list)
    {
        return "{\"" + header + "\":[" +
                list.stream()
                        .map(this::serializeBlock)
                        .collect(Collectors.joining(",")) +
                "]}";
    }

    public String serializeBlockQueue() {
        return serializeBlockList("queue", blockQueue);
    }

    public String serializeBlockChain()  {
        return serializeBlockList("chain", blockChain);
    }


    public List<Block> getBlockChain() {
        return blockChain;
    }

    public LinkedList<Block> getBlockQueue() {
        return blockQueue;
    }
}
