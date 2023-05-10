package files;

public class Image extends File{
    private String imagePath;
    @Override
    FileType getType() {
        return FileType.IMAGE;
    }

    /**
     * Stores a String as the image file's image path, and sets parent.
     * @param parent parent file.
     * @param imagePath Image path to be saved
     */
    public Image(Directory parent, String name, String imagePath){
        super(parent, name);
        this.imagePath = imagePath;
    }

    /**
     * @return the image path of this image file
     */
    public String getImagePath() {
        return imagePath;
    }
}
