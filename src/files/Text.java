package files;

public class Text extends File{
    private String text = "";
    @Override
    FileType getType() {
        return FileType.TEXT;
    }

    /**
     * @return text stored in file
     */
    public String getText() {
        return text;
    }

    /**
     * Sets stored text to parameter
     * @param text String to be stored
     */
    public void setText(String text) {
        this.text = text;
    }

}
