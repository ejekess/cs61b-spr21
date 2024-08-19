package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.util.List;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit  implements Dumpable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */

    public static final File COMMIT_DIR=Utils.join(Repository.GITLET_DIR,"\\Objects\\Commits");
    private String message;
    private String timestamp;//format:Thu Nov 9 17:01:33 2017 -0800



    private String UID;

    public List<String> getBlobsUID() {
        return BlobsUID;
    }

    private  List<String> parentUID;//the most last commit,use the file name
    private List<String> BlobsUID;

    /* TODO: fill in the rest of this class. */

    public Commit(String message, List<String> parentUID) {
        this.message = message;
        if(parentUID==null)
        {
            timestamp="Thu Jan 1 00:00:00 1970 -0800";
        }
        this.parentUID=parentUID;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<String> getParentUID() {
        return parentUID;
    }

    public void setBlobsUID(List<String> blobsUID) {
        BlobsUID = blobsUID;
    }


    public void commitIn()
    {
         if(!COMMIT_DIR.exists())
         {
             System.out.println("Not in an initialized Gitlet directory.");
         }

         UID=Utils.sha1(this);
         File file=new File(COMMIT_DIR+"\\"+UID+".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeObject(file,this);
    }

    public String getUID() {
        return UID;
    }

    @Override
    public void dump() {
        return;
    }
}
