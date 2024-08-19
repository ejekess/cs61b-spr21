package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Stage implements Dumpable{
    @Override
    public void dump() {

    }
    public static final File Stage_DIR=Utils.join(Repository.GITLET_DIR,"\\StageArea");

    private List<Blob> blobs;

    public List<Blob> getBlobs() {
        return blobs;
    }
    public Stage()
    {
      blobs=new LinkedList<>();
    }
   public void add(Blob blob)
   {
       blobs.add(blob);
   }



}
