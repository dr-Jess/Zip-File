package files;

public class Text extends File{
    private String text = "";
    @Override
    FileType getType() {
        return FileType.TEXT;
    }

    /**
     * Stores a String as the text file's text, and sets parent.
     * @param parent parent file.
     * @param text Text to be saved
     */
    public Text(Directory parent, String name, String text){
        super(parent, name);
        this.text = text;
    }

    /**
     * @return text stored in file
     */
    public String getText() {
        return text;
    }

}
