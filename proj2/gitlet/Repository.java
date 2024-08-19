package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        if(!Utils.restrictedDelete(join(Stage.Stage_DIR,"Stage.txt")))
        {
            System.out.println("can't clear StageArea.");
        }
        
    }


    public  static Commit getHEADCommit()
    {
        String CommitID = Repository.HEADSHA1;
        File CommitTracked = Utils.join(Commit.COMMIT_DIR, (CommitID + ".txt"));
        //System.out.println(CommitTracked.getPath());
        if (!CommitTracked.exists()) {
            System.out.println("can't find head commit file.");
            return null;
        }

        Commit head = Utils.readObject(CommitTracked, Commit.class);
        return head;
    }
}




