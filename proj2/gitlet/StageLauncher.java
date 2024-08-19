package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StageLauncher {
    public static void add(Blob blob) {
        /**If the current working version of the file is identical to the version in the current commit,
         *do not stage it to be added, and remove it from the staging area
         *if it is already there
         *(as can happen when a file is changed, added,
         * and then changed back to its original version).**/
       Commit head=Repository.getHEADCommit();
        List<String> blobs1 = head.getBlobsUID();

        if (blobs1 != null) {
            for (int i = 0; i < blobs1.size(); i++) {
                if (blobs1.get(i).equals(blob.getBlobUID())) {
                    return;
                }
            }
        }

        Stage stage;
        File file1 = new File(Stage.Stage_DIR + "\\Stage.txt");
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


        for (Blob stageBlob : stage.getBlobs()) {
            if (stageBlob.getBlobUID().equals(blob.getBlobUID())) {
              //  System.out.println("this file has no change!");
                return;
            }
        }

        stage.add(blob);
        Utils.writeObject(file1,stage);
    }



    public void addFile(List<Blob> blobs){

    }





}
