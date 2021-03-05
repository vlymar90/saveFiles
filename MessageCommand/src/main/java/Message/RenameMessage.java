package Message;

public class RenameMessage implements Message  {
   private String oldPath;
   private String newPath;

    public RenameMessage(String oldPath, String newPath) {
        this.oldPath = oldPath;
        this.newPath = newPath;
    }

    public String getOldPath() {
        return oldPath;
    }

    public String getNewPath() {
        return newPath;
    }
}
