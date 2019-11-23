package blockchain;

import com.google.gson.Gson;
import utils.ByteUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    private String data;
    private String id;
    private byte[] hash;
    private byte[] prevHash = new byte[32];
    private Long transactions = 0L;
    private Long proofOfWork = 0L;

    public Block(String data) {
        this.data = data;
    }

    public Block() {

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

        byte[] serializedObj = new byte[dataBytes.length + 32 + Long.BYTES];
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

        System.out.println(new Gson().toJson(serializedObj));
        return serializedObj;
    }

    public byte[] calculateHash() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = new byte[0];
        if (md != null) {
            hash = md.digest(this.serialize());
        }

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

    public String getData() {
        return data;
    }
}
