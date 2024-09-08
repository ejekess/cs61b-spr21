package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static gitlet.Utils.*;


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
    public static final File OBJECT_DIR=new File(GITLET_DIR,"Object");

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
              OBJECT_DIR.mkdir();
              Stage.Stage_DIR.mkdir();


              File StageArea=join(Stage.Stage_DIR,"Stage.txt");
              if(!StageArea.exists()) {
                  try {
                      StageArea.createNewFile();
                  } catch (IOException e) {
                      throw new RuntimeException(e);
                  }
              }
              /**store initial branch path**/

              Utils.writeContents(HEAD_FILE,BRANCH_USED);

              //write the initial branch to file
              BRANCHS_DIR.mkdir();
              File file2=new File(BRANCH_USED);
              commitIn(Initial,null);
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

        Blob blob=BlobLauncher.FiletoBlob(file);

        StageLauncher.add(blob,fileName);
    }

    public static void commitIn(Commit commit, Stage stage)
    {
        if(!GITLET_DIR.exists())
        {
            System.out.println("Not in an initialized Gitlet directory.");
        }
        commit.UID=Utils.sha1(Utils.serialize(commit));

        String prefix=commit.UID.substring(0,2);
        String other=commit.UID.substring(2);

        File file=join(OBJECT_DIR,prefix);
        File commitFile=join(file,other);
        if(!file.exists())
        {
            file.mkdir();
        }
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       //add BlobID in StageArea to  commit
        File preDir;
        File BlobFile;
        if(stage!=null) {
            if(stage.getBlobsMap().isEmpty()) {
                System.out.println("No changes added to the commit.");
                return;
            }
            for (String s : stage.getBlobsMap().keySet()) {
                String blobUID=stage.getBlobsMap().get(s);
                commit.BlobsMap.put(s,blobUID);
            }
            stage.getBlobsMap().clear();
        }
        else
        {
            stage=new Stage();
        }



        Utils.writeObject(join(Stage.Stage_DIR,"Stage.txt"),stage);
        Utils.writeObject(commitFile,commit);

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
        ParentUID.add(headCommit.getUID());
        Commit NewCommit=new Commit(message,ParentUID);
        commitIn(NewCommit,stage);
        HEADSHA1=NewCommit.getUID();
        
        Utils.writeContents(new File(BRANCH_USED),NewCommit.getUID());
    }


    public  static Commit getHEADCommit()
    {
        String CommitID = Repository.HEADSHA1;
        String prefix=CommitID.substring(0,2);
        String other=CommitID.substring(2);
        File prefixFile = Utils.join(OBJECT_DIR, prefix);

        if(!prefixFile.exists())
        {
            throw error("can't find Head Commit");
        }
        File CommitFile=Utils.join(prefixFile,other);
        if (!CommitFile.exists()) {
            throw error("can't find Head Commit");
        }
        Commit head = Utils.readObject(CommitFile, Commit.class);
        return head;
    }


    //TODO:
    public static void remove(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        Stage stage;
        Commit commit = getHEADCommit();
        File file = join(Repository.CWD, fileName);

        File StageArea = join(Stage.Stage_DIR, "Stage.txt");

        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        Blob fileblob = BlobLauncher.FiletoBlob(file);
        stage = Utils.readObject(StageArea, Stage.class);


        // Unstage the file if it is currently staged for addition.
       if(stage.getBlobsMap().remove(fileName)!=null) {
           writeObject(StageArea, stage);
           return;
       }
        //If the file is tracked in the current commit
        if(commit.getBlobsMap().containsKey(fileName)) {
            Utils.restrictedDelete(fileName);
                  //stage it for removal
                   stage.getBlobsMap().put(fileName,"rm"+fileblob.getBlobUID());
                  writeObject(StageArea,stage);
                  return;
        }

        throw error("No reason to remove the file.","rm");
    }

    /**
     *
     *  following the first parent commit links,
     *  ignoring any second parents found in merge commits.
     *
     */

    //TODO:now ignore the branch commit and merge commits
    public static void Log() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
           Commit HeadCommit=getHEADCommit();
           File prexFile,CommitFile;

        System.out.println("===");
        HeadCommit.dump();
        for (int i = HeadCommit.getParentUID().size()-1; i >=0;i--) {
            String prefix=HeadCommit.getParentUID().get(i).substring(0,2);
            String other=HeadCommit.getParentUID().get(i).substring(2);
            prexFile=Utils.join(OBJECT_DIR,prefix);
            CommitFile=Utils.join(prexFile,other);
            Commit commit=Utils.readObject(CommitFile,Commit.class);
            System.out.println("===");
            commit.dump();
        }

    }


    /**
     * the method to find Blob of File in Blobs
     * @param arg
     * @param Blobs
     */

    public static void CheckoutFile(String arg,Map<String,String> Blobs)
    {

        if(!Blobs.containsKey(arg))
        {
            System.out.println("File does not exist in that commit.");
            return;
        }

        String blobID=Blobs.get(arg);
        File preFile,BlobFile;
        Blob tempblob;
        String prefix;
        String  other;
            prefix=blobID.substring(0,2);
            other=blobID.substring(2);
            preFile=join(OBJECT_DIR,prefix);
            BlobFile=join(preFile,other);

            if(BlobFile.exists())
            {
            Blob   blob=Utils.readObject(BlobFile,Blob.class);
                    File targetFile=new File(blob.getBlobPath());
                    if(targetFile.exists()) {
                        if (targetFile.isDirectory()) {
                            throw Utils.error("can't checkout a directory", "checkout --");
                        }
                        else {
                            writeContents(targetFile,blob.getContent());
                        }
                    }
                    else
                    {
                        try {
                            targetFile.createNewFile();
                            writeContents(targetFile, blob.getContent());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return;
                }
        System.out.println("File does not exist in that commit.");
    }

    /**
     * Takes the version of the file as it exists in the head commit and puts it
     * in the working directory, overwriting the version of the file
     * that’s already there if there is one.
     * The new version of the file is not staged.
     * @param arg
     */
    public static void CheckOut1(String arg) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
       Commit headCommit=getHEADCommit();
       Map<String,String> Blobs=headCommit.getBlobsMap();
       CheckoutFile(arg,Blobs);
    }


    /**
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     */
    public static void CheckOut2(String commitID,String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        String prefix=commitID.substring(0,2);
        String other=commitID.substring(2);
        File preFile=join(OBJECT_DIR,prefix);
        if(!preFile.isDirectory())
        {
            System.out.println("No commit with that id exists.");
            return;
        }
        for (File file : preFile.listFiles()) {
            if(file.getName().startsWith(other))
            {
                Commit TargetCommit=Utils.readObject(file,Commit.class);
                Map<String,String> BlobsMap=TargetCommit.getBlobsMap();
                CheckoutFile(fileName,BlobsMap);
                return;
            }
        }

        System.out.println("No commit with that id exists.");
    }

    public static void CheckOut3(String BranchName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        File file=join(BRANCHS_DIR,BranchName);
        if(!file.exists())
        {
            System.out.println("No such branch exists.");
            return;
        }


        BRANCH_USED=file.getPath();
        Utils.writeContents(HEAD_FILE,BRANCH_USED);
        HEADSHA1=Utils.readContentsAsString(file);
    }

    public static void NewBranch(String BranchName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        File NewBranch=join(BRANCHS_DIR,BranchName);
        if(NewBranch.exists())
        {
            System.out.println("A branch with that name already exists.");
            return;
        }

        try {
            NewBranch.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Commit HeadCommit=getHEADCommit();
        String BranchSha1=HeadCommit.getUID();
        Utils.writeContents(NewBranch,BranchSha1);
    }


    public static void removeBranch(String BranchName) {
        File file=join(BRANCHS_DIR,BranchName);
        if(!file.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if(BRANCH_USED.endsWith(BranchName))
        {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        file.delete();
    }

    public static void reset(String CommitID) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        String prefix=CommitID.substring(0,2);
        String other=CommitID.substring(2);
        File preFile=join(OBJECT_DIR,prefix),BlobFile;
        if(!preFile.exists())
        {
            System.out.println("No commit with that id exists.");
        }
       Commit commit = null;
        for (File file : preFile.listFiles()) {
            if (file.getName().startsWith(other)) {
                commit = Utils.readObject(file, Commit.class);
                break;
            }
        }

        if(commit==null)
        {
            System.out.println("No commit with that id exists.");
            return;
        }

        if(commit.getUID().equals(HEADSHA1))
        {
            System.out.println("this is current commit ,no need to reset!");
            return;
        }


         HEADSHA1=commit.getUID();


        /** Removes tracked files that are not present in that commit*/
        for (String s : plainFilenamesIn(CWD)) {
            if(!commit.getBlobsMap().containsKey(s)) {
                //TODO:stay untracked files
                Blob blob=BlobLauncher.FiletoBlob(join(s));
                preFile=join(OBJECT_DIR,blob.getBlobUID().substring(0,2));
                BlobFile=join(preFile,blob.getBlobUID().substring(2));
                if(!preFile.exists())
                {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
                if(!BlobFile.exists())
                {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
                restrictedDelete(s);
            }


        }

       Map<String,String>  blobMap=commit.getBlobsMap();
        for (String key: blobMap.keySet()) {
                String blobID=blobMap.get(key);
                prefix=blobID.substring(0,2);
                other=blobID.substring(2);
                preFile=join(OBJECT_DIR,prefix);
                BlobFile=join(preFile,other);
                if(BlobFile.exists())
                {
                    Blob blob=readObject(BlobFile,Blob.class);
                   File file=join(blob.getBlobPath());
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    writeContents(file,blob.getContent());
                }
        }

        /**moves the current branch’s head to that commit node.**/
        writeContents(join(BRANCH_USED),HEADSHA1);
        /**The staging area is cleared**/
        writeObject(join(Stage.Stage_DIR,"Stage.txt"),new Stage());


    }
    public static void getStatus() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        System.out.println("=== Branches ===");
        String Branch=join(BRANCH_USED).getName();
        System.out.println("*"+Branch);
        for (File file : BRANCHS_DIR.listFiles()) {
            if(file.getName().equals(Branch))
            {
                continue;
            }
            System.out.println(file.getName());
        }

        File stageArea=join(Stage.Stage_DIR,"Stage.txt");
        Stage stage=readObject(stageArea,Stage.class);
        Commit headcommit=getHEADCommit();
        File prefile,Blobfile,file;
        List<String>  addFile=new ArrayList<>();
        List<String>  rmFile=new ArrayList<>();
        List<String>  MnSFile=new ArrayList<>();
        List<String>  UtFile=new ArrayList<>();
        for (String s :Utils.plainFilenamesIn(CWD)) {

            /**the file’s BlobUID in CWD*/
            file=join(s);
            Blob blob=BlobLauncher.FiletoBlob(file);
            prefile=join(OBJECT_DIR,blob.getBlobUID().substring(0,2));
            Blobfile=join(prefile,blob.getBlobUID().substring(2));
            if(stage.getBlobsMap().containsKey(file.getName())) {
                String BlobUID=stage.getBlobsMap().get(file.getName());
                if (BlobUID.startsWith("rm")) {
                    rmFile.add(file.getName());
                } else {
                   /** Staged for addition, but with different contents than in the working directory; */
                    if(!prefile.exists()||!Blobfile.exists())
                    {
                        MnSFile.add(file.getName()+"(modified)");
                    }
                    else
                    {    addFile.add(file.getName());    }
                }
                stage.getBlobsMap().remove(file.getName());
            }
            else
            {
                /**
                 * Tracked in the current commit, changed in the working directory, but not staged; or
                 * Staged for addition, but with different contents than in the working directory; or
                 * Staged for addition, but deleted in the working directory; or
                 * Not staged for removal, but tracked in the current commit and deleted from the working directory.
                 */
               if(headcommit.getBlobsMap().containsKey(file.getName()))
               {
                   headcommit.getBlobsMap().remove(file.getName());
                   /**Tracked in the current commit, changed in the working directory, but not staged; or*/
                   if(!prefile.exists()||!Blobfile.exists())
                   {
                       MnSFile.add(file.getName()+"(modified)");
                   }
                   headcommit.getBlobsMap().remove(file.getName());
               }

                /**
                 * The final category ("Untracked Files") is for files present
                 * in the working directory
                 * but neither staged for addition nor tracked.
                 */
                else
               {
                   if(!prefile.exists()||!Blobfile.exists())
                   {
                       UtFile.add(file.getName());
                   }
               }

            }
        }

        /** Staged for addition, but deleted in the working directory;*/
        for (String s : stage.getBlobsMap().keySet()) {
            if(stage.getBlobsMap().get(s).startsWith("rm"))
            {
                rmFile.add(s);

                headcommit.getBlobsMap().remove(s);
            }
            else
               MnSFile.add(s+"(deleted)");
        }

        /**Not staged for removal, but tracked in the current commit and deleted from the working directory.*/
        for (String s : headcommit.getBlobsMap().keySet()) {
                MnSFile.add(s + "(deleted)");
        }

        System.out.println();
        System.out.println("=== Staged Files ===");
            for (String s : addFile) {
                System.out.println(s);
            }
        System.out.println();
        System.out.println("=== Removed Files ===");
            for (String s : rmFile) {
                System.out.println(s);
            }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String s : MnSFile) {
            System.out.println(s);
        }

        System.out.println();
        System.out.println("=== Untracked Files ===");
        for (String s : UtFile) {
            System.out.println(s);
        }
        System.out.println();
    }



    public  static  void Merge(String BranchName) {
        if (BRANCH_USED.endsWith(BranchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        File file = join(BRANCHS_DIR, BranchName);
        if (!file.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        String Branchsha1 = Utils.readContentsAsString(file);
        String prefix = Branchsha1.substring(0, 2);
        String other = Branchsha1.substring(2);

        File preDir = join(OBJECT_DIR, prefix);
        if (!preDir.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        File CommitFile = join(preDir, other);
        if (!CommitFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        Commit headcommit = getHEADCommit();
        Commit branchcommit = readObject(CommitFile, Commit.class);
        List<String> headParentUID = headcommit.getParentUID();
        List<String> branchParentUID = branchcommit.getParentUID();
        String SplitCommitSha1 = FindAncestor(0, 0, headParentUID, branchParentUID);
        preDir = join(OBJECT_DIR, SplitCommitSha1.substring(0, 2));
        CommitFile = join(preDir, SplitCommitSha1.substring(2));
        File BlobFile;

        Commit SplitCommon = readObject(CommitFile, Commit.class);

        Map<String, String> headMap = headcommit.getBlobsMap();
        Map<String, String> splitMap =SplitCommon.getBlobsMap();
        Map<String, String> branchMap = branchcommit.getBlobsMap();

        Set<String> keys = splitMap.keySet();
        for (String key : keys) {
            String FileUID = splitMap.get(key);
            if (branchMap.containsKey(key) && headMap.containsKey(key)) {
                /**modify in brach not head ->branch*/
                if (headMap.containsValue(FileUID) && !branchMap.containsValue(FileUID)) {
                    headMap.remove(key);

                    /**overwrite file*/
                    preDir=join(OBJECT_DIR,branchMap.get(key).substring(0,2));
                    BlobFile=join(preDir,branchMap.get(key).substring(2));
                    Blob blob1=readObject(BlobFile,Blob.class);

                    File file1=join(CWD,key);
                    if(!file1.exists())
                    {
                        try {
                            file1.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    writeContents(file1,blob1.getContent());
                }
                /**modify in head not branch ->head*/
                else if (!headMap.containsKey(key) && branchMap.containsKey(key)) {
                    branchMap.remove(key);

                    /**overwrite file*/
                    preDir=join(OBJECT_DIR,headMap.get(key).substring(0,2));
                    BlobFile=join(preDir,headMap.get(key).substring(2));
                    Blob blob1=readObject(BlobFile,Blob.class);

                    File file1=join(CWD,key);
                    if(!file1.exists())
                    {
                        try {
                            file1.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    writeContents(file1,blob1.getContent());
                }
                /**both modify--merge*/
                else if (!headMap.containsKey(key) && !branchMap.containsKey(key)) {
                    //TO DO:
                    headMap.remove(key);
                    branchMap.remove(key);
                    preDir=join(OBJECT_DIR,headMap.get(key).substring(0,2));
                    BlobFile=join(preDir,headMap.get(key).substring(2));
                    Blob blob1=readObject(BlobFile,Blob.class);
                    preDir=join(OBJECT_DIR,branchMap.get(key).substring(0,2));
                    BlobFile=join(preDir,branchMap.get(key).substring(2));
                    Blob blob2=readObject(BlobFile, Blob.class);
                    String bytes="<<<<<<< HEAD\r\n"+new String(blob1.getContent(), StandardCharsets.UTF_8)
                            +"=======\r\n"+new String(blob1.getContent(), StandardCharsets.UTF_8)+"\r\n>>>>>>>";

                    File file1=join(CWD,key);
                    writeContents(file1,bytes);
                }

            } else if (branchMap.containsKey(key)) {
                /** not present in head and unmodify in other->remove*/
                if (branchMap.containsValue(FileUID)) {
                    branchMap.remove(key);
                    preDir=join(OBJECT_DIR,branchMap.get(key).substring(0,2));
                    BlobFile=join(preDir,branchMap.get(key).substring(2));

                    File file1=join(CWD,key);
                    file1.delete();
                }
            } else if (headMap.containsValue(key)) {
                /** not present in other and unmodify in head->remove*/
                if (headMap.containsValue(FileUID)) {
                    headMap.remove(key);
                    preDir=join(OBJECT_DIR,headMap.get(key).substring(0,2));
                    BlobFile=join(preDir,headMap.get(key).substring(2));

                    File file1=join(CWD,key);
                    file1.delete();

                }
            }
        }

        Set<String> branchFile = branchMap.keySet();
        for (String key : branchFile) {
            headMap.put(key, branchMap.get(key));
        }
    }

    public  static  String FindAncestor(int i,int j,List<String> headBlobUID,List<String>  branchBlobUID)
    {
        if(i==-1||j==-1)
        {
            return null;
        }
        if(headBlobUID.get(i).equals(branchBlobUID.get(j)))
        {
            return headBlobUID.get(i);
        }

            return (FindAncestor(i-1,j,headBlobUID,branchBlobUID)==null)?FindAncestor(i, j-1, headBlobUID, branchBlobUID):FindAncestor(i-1, j, headBlobUID, branchBlobUID);
    }
}




