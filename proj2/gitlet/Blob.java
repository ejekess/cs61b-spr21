package gitlet;

import java.io.File;

public class Blob implements  Dumpable{
    public static final File BLOB_DIR=Utils.join(Repository.GITLET_DIR,"\\Objects\\Blobs");





    public static final int STATUS_ADD=1;
    public static final int STATUS_REMOVE=2;

    public static final int STATUS_MODIFY=3;

    @Override
    public void dump() {

    }


    private String BlobUID;


    /** to avoid files which has different name and the same content
     * to have the same sha1UID**/
    private String BlobName;
    private int  BlobStatus;
    private byte[] content;

    public void setBlobUID(String blobUID) {
        BlobUID = blobUID;
    }

    public void setBlobName(String blobName) {
        BlobName = blobName;
    }

    public void setBlobStatus(int blobStatus) {
        BlobStatus = blobStatus;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getBlobUID() {
        return BlobUID;
    }

    public int getBlobStatus() {
        return BlobStatus;
    }

    public byte[] getContent() {
        return content;
    }








}
