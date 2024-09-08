package gitlet;

import java.io.File;

public class Blob implements  Dumpable{


    @Override
    public void dump() {

    }


    private String BlobUID;


    /** to avoid files which has different name and the same content
     * to have the same sha1UID**/
    private String BlobPath;
     private byte[] content;

    public void setBlobUID(String blobUID) {
        BlobUID = blobUID;
    }

    public void setBlobName(String blobName) {
        BlobPath = blobName;
    }


    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getBlobUID() {
        return BlobUID;
    }


    public byte[] getContent() {
        return content;
    }

    public String getBlobPath() {
        return BlobPath;
    }
}
