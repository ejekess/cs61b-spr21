package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Stage implements Dumpable{
    @Override
    public void dump() {

    }
    public static final File Stage_DIR=Utils.join(Repository.GITLET_DIR,"\\StageArea");

    private static List<Blob> blobs;

    public List<Blob> getBlobs() {
        return blobs;
    }

    public void add(Blob blob) {
        /**If the current working version of the file is identical to the version in the current commit,
         *do not stage it to be added, and remove it from the staging area
         *if it is already there
         *(as can happen when a file is changed, added,
         * and then changed back to its original version).**/
         String CommitID=Repository.HEAD;
         File CommitTracked=Utils.join(Commit.COMMIT_DIR,(CommitID+".txt"));

         if(!CommitTracked.exists())
         {
             System.out.println("can't find head commit file.");
             return;
         }

         Commit head=Utils.readObject(CommitTracked,Commit.class);
         List<String> blobs1=head.getBlobsUID();
        for (int i = 0; i < blobs1.size(); i++) {
            if(blobs1.get(i).equals(blob.getBlobUID()))
            {
                return;
            }
        }

        blobs.add(blob);
        File file1=new File(Stage_DIR+"\\Stage.class");
        try {
            file1.createNewFile();
        } catch (IOException e) {
            Utils.error("can't overwrite stage.class","add")
        }
        Utils.writeObject(file1,this);
    }


    public void add(List<Blob> blobs){

    }



}
