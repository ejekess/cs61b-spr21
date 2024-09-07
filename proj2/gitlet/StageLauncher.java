package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static gitlet.Utils.join;

public class StageLauncher {
    public static void add(Blob blob,String fileName) {
        /**If the current working version of the file is identical to the version in the current commit,
         *do not stage it to be added, and remove it from the staging area
         *if it is already there
         *(as can happen when a file is changed, added,
         * and then changed back to its original version).**/
        String prefix= blob.getBlobUID().substring(0,2);
        String other=blob.getBlobUID().substring(2);
        File preDir=join(Repository.OBJECT_DIR,prefix);
        File BlobFile=join(preDir,other);
        if(preDir.exists()&&BlobFile.exists())
            return;
        if(!preDir.exists())
        {
            preDir.mkdir();
        }
        try {
            BlobFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage;
        File file1 = join(Stage.Stage_DIR , "Stage.txt");
        if (file1.exists()) {
            stage = Utils.readObject(file1, Stage.class);
        }
        else {
            stage = new Stage();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                Utils.error("can't overwrite stage.class", "add");
            }
        }
        stage.add(fileName,blob.getBlobUID());
        Utils.writeObject(file1,stage);
        Utils.writeObject(BlobFile,blob);
    }





}
