package files;

public class Image extends File{
    private String imagePath;
    @Override
    FileType getType() {
        return FileType.IMAGE;
    }

    /**
     * @return the image path of this image file
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Stores a String as the image file's image path.
     * @param imagePath Image path to be saved
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
