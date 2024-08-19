package gitlet;

import java.io.File;

public class BlobLauncher {

    public static Blob forStageADD(File file)
    {
        Blob blob=new Blob();
        blob.setContent(Utils.readContents(file));
        blob.setBlobUID(Utils.sha1(Utils.serialize(blob)));
        blob.setBlobName(file.getName());
        blob.setBlobStatus(Blob.STATUS_ADD);
        return blob;
    }

}
