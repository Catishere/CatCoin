package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    private String data;
    private String id;
    private byte[] hash;
    private byte[] prevHash;
    private Long transactions = 0L;
    private Long proofOfWork = 0L;

    public Block(String data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getProofOfWork() {
        return proofOfWork;
    }

    public void setProofOfWork(Long proofOfWork) {
        this.proofOfWork = proofOfWork;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setPrevHash(byte[] prevHash) {
        this.prevHash = prevHash;
    }

    public byte[] serialize() {
        int it = 0;
        byte[] dataBytes = data.getBytes();

        byte[] serializedObj = new byte[dataBytes.length + prevHash.length + Long.BYTES];
        for (byte b : dataBytes) {
            serializedObj[it] = b;
            it++;
        }
        for (byte b : prevHash) {
            serializedObj[it] = b;
            it++;
        }

        for (byte b : ByteUtils.longToBytes(proofOfWork)) {
            serializedObj[it] = b;
            it++;
        }

        return serializedObj;
    }
    public byte[] calculateHash() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = md.digest(this.serialize());

        if (this.hash == null) {
            this.hash = hash;
            this.id = ByteUtils.byteToHexString(this.hash);
        }
        return hash;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
