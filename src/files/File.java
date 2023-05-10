package files;

public abstract class File {
    private Directory[] path;
    private Directory parent;
    private String name;

    /**
     * Sets the parent file to the specified parameter and sets up the path, inheriting from the parent.
     * @param parent parent file
     */
    public File(Directory parent, String name){
        this.name = name;
        this.parent = parent;
        this.path = new Directory[parent.getPath().length+1];
        System.arraycopy(parent.getPath(),0,this.path,0,parent.getPath().length);
        this.path[this.path.length-1] = parent;
    }

    /**
     * @return FileType enum of the file type
     */
    abstract FileType getType();

    /**
     * @return in-game path of file
     */
    public Directory[] getPath(){
        return path;
    };

    /**
     * @return parent file
     */
    public Directory getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }
}
