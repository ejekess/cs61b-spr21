package gitlet;

import java.io.File;
import java.io.IOException;

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

   /*the Sha1ID of HEADCommit**/
    public static final File HEAD_FILE=join(GITLET_DIR,"refs","HEAD");


    /**  mark the path the branch now tracked**/
    public  static String BRANCH_USED=BRANCHS_DIR.getPath()+"/master";;

    public static String HEAD;




    /* TODO: fill in the rest of this class. */
    public  static  void InitRepository()
    {
          if(!GITLET_DIR.exists())
          {
              GITLET_DIR.mkdir();

              Commit Initial=new Commit("initial commit",null);



              //TODO:make the relevant folder

              REFS_DIR.mkdir();


              /**store initial branch path**/

              System.out.println(BRANCH_USED);
              Utils.writeContents(HEAD_FILE,BRANCH_USED);


              //write the initial branch to file
              BRANCHS_DIR.mkdir();
              File file2=new File(BRANCH_USED);

              HEAD=Initial.getUID();
              try {
                  file2.createNewFile();
                  HEAD_FILE.createNewFile();
                  Utils.writeContents(file2,HEAD);
              } catch (IOException e) {
                  throw new GitletException("failed to create initial branch master");
              }




              Stage.Stage_DIR.mkdir();

              File file3=join(GITLET_DIR,"Objects");
              file3.mkdir();
              Blob.BLOB_DIR.mkdir();
              Commit.COMMIT_DIR.mkdir();
              Initial.commitIn();
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

        Blob blob = new Blob();
        blob.forStageADD(file);

        Stage stage = new Stage();
        stage.add(blob);
    }
}




