package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    private String message;
    private String timestamp;//format:Thu Nov 9 17:01:33 2017 -0800



    String UID;

    public Map<String,String> getBlobsMap() {
        return BlobsMap;
    }

    List<String> parentUID;//the most last commit,use the file name
     Map<String ,String> BlobsMap;


    public Commit(String message, List<String> ParentUID) {
        this.message = message;
        BlobsMap=new HashMap<>();
        if(ParentUID==null)
        {
            timestamp="Thu Jan 1 00:00:00 1970 -0800";
            this.parentUID=new ArrayList<>();
        }
        else
        {
            //get current time
            Date date = new Date();


            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);

            formatter.format("%ta %tb %td %tH:%tM:%tS %tY  %tz", date, date, date, date, date, date,date,date);

            timestamp=sb.toString();
            this.parentUID=ParentUID;
        }

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

//    public void addBlobsFile(Stage stage) {
//        BlobsUID=new ArrayList<>();
//        File prexFile,CommitFile;
//        String prefix,other;
//        for (Blob blob : stage.getBlobs()) {
//            if(blob.getBlobStatus()!=Blob.STATUS_REMOVE) {
//                BlobsUID.add(blob.getBlobUID());
//                prefix=blob.getBlobUID().substring(0,2);
//                other=blob.getBlobUID().substring(2);
//                prexFile = Utils.join(Repository.OBJECT_DIR, prefix);
//                CommitFile=Utils.join(prexFile,other);
//                if(!prexFile.exists())
//                {
//                 prexFile.mkdir();
//                }
//                try {
//                    CommitFile.createNewFile();
//                    Utils.writeObject(CommitFile, blob);
//                } catch (IOException e) {
//                    throw Utils.error("Can't save Blobs", "commit");
//                }
//            }
//        }
//    }




    public String getUID() {
        return UID;
    }

    @Override
    public void dump() {
        System.out.println("commit "+UID);
        System.out.println("Date: "+timestamp);
        System.out.println(message);
        System.out.println();
    }
}
