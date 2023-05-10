package files;

public class Directory extends File{
    @Override
    FileType getType() {
        return FileType.DIRECTORY;
    }
    private File[] children = {};

    /**
     * @return File[] list of children
     */
    public File[] getChildren() {
        return children;
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
