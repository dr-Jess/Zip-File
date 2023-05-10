package files;

public class Directory extends File{
    @Override
    FileType getType() {
        return FileType.DIRECTORY;
    }
    private File[] children = {};

    public Directory(String name){
        super(name);
    }

    public Directory(Directory parent, String name){
        super(parent, name);
    }

    /**
     * @return File[] list of children
     */
    public File[] getChildren() {
        return children;
    }

    public void removeChild(File child){
        for(int i=0;i<children.length;i++){
            if(children[i]==child){
                File[] newChildren = new File[children.length-1];
                System.arraycopy(children,0,newChildren,0,i);
                System.arraycopy(children,i+1,newChildren,i,children.length-i-1);
                children = newChildren;
                return;
            }
        }
    }

    /**
     * Adds a child to the list of children a directory holds
     * @param child child to be added
     */
    public void addChild(File child) {
        File[] newChildren = new File[children.length+1];
        System.arraycopy(children,0,newChildren,0,children.length);
        newChildren[newChildren.length-1] = child;
        children = newChildren;
    }
}
