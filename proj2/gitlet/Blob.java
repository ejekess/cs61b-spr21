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

    private int  BlobStatus;
    private byte[] content;

    public String getBlobUID() {
        return BlobUID;
    }

    public int getBlobStatus() {
        return BlobStatus;
    }

    public byte[] getContent() {
        return content;
    }

    public void forStageADD(File file)
    {
        content=Utils.readContents(file);
        BlobUID=Utils.sha1(this);
        BlobStatus=STATUS_ADD;
    }





}
