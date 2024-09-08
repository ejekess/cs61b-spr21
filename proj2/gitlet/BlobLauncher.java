package gitlet;

import java.io.File;

public class BlobLauncher {

    public static Blob FiletoBlob(File file)
    {
        Blob blob=new Blob();
        blob.setContent(Utils.readContents(file));
        blob.setBlobName(file.getAbsolutePath());
        blob.setBlobUID(Utils.sha1(Utils.serialize(blob)));
        return blob;
    }





}
