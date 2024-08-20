package gitlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));


    /** The .gitlet directory. */

    /**directory structure:
     * .gitlet:
     *     -staged file:
     *           -addition
     *           -remove
     *
     *     -objects:
     *           -commits:
     *                -for gitlet log
     *
     *           -Blobs:
     *               -the every version of commit files context
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * another file
     * **/



    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /**
     * file storing all head of branch
     * **/


    public  static  final File REFS_DIR=join(GITLET_DIR,"refs");
    public static final File BRANCHS_DIR=join(GITLET_DIR,"refs","heads");

   /*the used branch filepath**/
    public static final File HEAD_FILE=join(GITLET_DIR,"refs","HEADSHA1");


    /**  mark the path the branch now tracked**/
    public  static String BRANCH_USED;

    /**HEADSHA1 commit sha1**/
    public static String HEADSHA1;

    static{
        if(HEAD_FILE.exists())
        {
            BRANCH_USED=Utils.readContentsAsString(HEAD_FILE);
            HEADSHA1=Utils.readContentsAsString(new File(BRANCH_USED));
            //System.out.println(HEADSHA1);
        }

        else
        {
            BRANCH_USED=BRANCHS_DIR.getPath()+"\\master";
        }
    }




    /* TODO: fill in the rest of this class. */
    public  static  void InitRepository()
    {
          if(!GITLET_DIR.exists())
          {
              GITLET_DIR.mkdir();

              Commit Initial=new Commit("initial commit",null);



              //TODO:make the relevant folder

              REFS_DIR.mkdir();


              Stage.Stage_DIR.mkdir();
              File file3=join(GITLET_DIR,"Objects");
              file3.mkdir();
              Blob.BLOB_DIR.mkdir();
              Commit.COMMIT_DIR.mkdir();


              /**store initial branch path**/

              Utils.writeContents(HEAD_FILE,BRANCH_USED);


              //write the initial branch to file
              BRANCHS_DIR.mkdir();
              File file2=new File(BRANCH_USED);
              commitIn(Initial);
              HEADSHA1=Initial.getUID();

              try {
                  file2.createNewFile();
                  HEAD_FILE.createNewFile();
                  Utils.writeContents(file2,HEADSHA1);
              } catch (IOException e) {
                  throw new GitletException("failed to create initial branch master");
              }





          }
          else
          {
              System.out.println("A Gitlet version-control system already exists in the current directory.");
          }
    }


    public static void add(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        File file = join(Repository.CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        Blob blob=BlobLauncher.forStageADD(file);
        StageLauncher.add(blob);
    }

    public static void commitIn(Commit commit)
    {
        if(!Commit.COMMIT_DIR.exists())
        {
            System.out.println("Not in an initialized Gitlet directory.");
        }
        commit.UID=Utils.sha1(Utils.serialize(commit));
        File file=new File(Commit.COMMIT_DIR+"\\"+commit.UID+".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeObject(file,commit);
    }

    public static void Commit(String message) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        File file=join(Stage.Stage_DIR,"Stage.txt");
        if(!file.exists()) {
            System.out.println("No changes added to the commit.");
            return;
   }
        Stage stage=Utils.readObject(file,Stage.class);



        Commit headCommit=getHEADCommit();
        List<String> ParentUID=headCommit.parentUID;
        if(ParentUID!=null) {
            ParentUID.add(headCommit.getUID());
        }
        else
        {
            ParentUID=new ArrayList<>();
            ParentUID.add(headCommit.getUID());
        }
        Commit NewCommit=new Commit(message,ParentUID);
        NewCommit.setBlobsUID(stage);
        commitIn(NewCommit);
        
        Utils.writeContents(new File(BRANCH_USED),NewCommit.getUID());
        for (File listFile : Stage.Stage_DIR.listFiles()) {
            listFile.delete();
        }
        
    }


    public  static Commit getHEADCommit()
    {

        String CommitID = Repository.HEADSHA1;
        //System.out.println(BRANCH_USED);
        //System.out.println(HEADSHA1);
        File CommitTracked = Utils.join(Commit.COMMIT_DIR, (CommitID + ".txt"));
        //System.out.println(CommitTracked.getPath());
        if (!CommitTracked.exists()) {
            System.out.println("can't find head commit file.");
            return null;
        }

        Commit head = Utils.readObject(CommitTracked, Commit.class);
        return head;
    }

    public static void remove(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        boolean flag1=true,flag2=true;
        Stage stage;
        Commit commit=getHEADCommit();
        File StageArea=join(Stage.Stage_DIR,"Stage.class");
        if(StageArea.exists()) {
             stage = Utils.readObject(StageArea, Stage.class);
                File file = join(Repository.CWD, fileName);
                if (!file.exists()) {
                    System.out.println("File does not exist.");
                }
                Blob fileblob=BlobLauncher.forStageADD(file);
                flag1=remove(stage,fileName,commit,fileblob);
                writeObject(StageArea,stage);
        }
        else//file not in   addition StageArea
        {
                stage=new Stage();
                File file = join(Repository.CWD, fileName);
                if (!file.exists()) {
                    System.out.println("File does not exist.");
                }
                Blob fileblob=BlobLauncher.forStageADD(file);
                flag2=RemoveFromCommit(commit,fileblob,stage);
                writeObject(StageArea,stage);
        }
       if(flag1==false&&flag2==false) {
           throw Utils.error("No reason to remove the file.", "rm");
        }
    }

    private static  boolean remove(Stage stage, String s,Commit commit,Blob fileblob) {
            List<Blob> blobs=stage.getBlobs();

            if(blobs!=null)
            {
                //Unstage the file if it is currently staged for addition.
                Iterator<Blob> iterator = blobs.iterator();
                while(iterator.hasNext()){
                    Blob temp = iterator.next();
                    if(temp.getBlobUID().equals(fileblob.getBlobUID())){
                        if(temp.getBlobStatus()==Blob.STATUS_ADD)
                        {
                            iterator.remove();
                            return true;
                        }
                    }
                }
                return false;
            }
            //file not in  addition StageArea
            else
            {
               return  RemoveFromCommit(commit,fileblob,stage);
            }
    }

    /**
     *If file to be removed not in StagedArea,then conclude
     *  if the file is tracked in the current commit, stage it for removal and
     *  remove the file from the working directory if the user
     *  has not already done so
     */
    public  static  boolean RemoveFromCommit(Commit commit,Blob fileblob,Stage stage)
    {

        if(commit.getBlobsUID()!=null)
        {
            Iterator<String> iterator = commit.getBlobsUID().iterator();
            while(iterator.hasNext()) {
                String next = iterator.next();

                /**delete Blobs file and version in head commit**/
                if (fileblob.getBlobUID().equals(next)) {
                    File file1=join(Blob.BLOB_DIR,(fileblob.getBlobUID()+".txt"));
                    Blob Rmblob=Utils.readObject(file1,Blob.class);
                    file1.delete();
                    Rmblob.setBlobStatus(Blob.STATUS_REMOVE);
                    stage.add(Rmblob);
                    iterator.remove();
                    return true;
                }
            }
        }
        return  false;
    }

    /**
     *
     *  following the first parent commit links,
     *  ignoring any second parents found in merge commits.
     *
     */

    //TODO:now ignore the branch commit and merge commits
    public static void Log() {
           Commit HeadCommit=getHEADCommit();
        File file;
        //TODO :Commit dumb method
        System.out.println("===");
        System.out.println(HeadCommit.getUID());
        System.out.println(HeadCommit.getTimestamp());
        System.out.println(HeadCommit.getMessage());
        for (String s : HeadCommit.getParentUID()) {
            file=Utils.join(Commit.COMMIT_DIR,(s+".txt"));
            Commit commit=Utils.readObject(file,Commit.class);
            System.out.println("===");
            System.out.println(HeadCommit.getUID());
            System.out.println(HeadCommit.getTimestamp());
            System.out.println(HeadCommit.getMessage());
        }
    }
}




