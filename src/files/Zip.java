package files;

public class Zip extends Directory{
    private String password;

    public Zip(Directory parent, String name){
        super(parent, name);
        generatePassword();
    }

    /**
     * @return the zip file's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Generates a password for the zip file.
     */
    private void generatePassword() {
        int length = 4;

    }
}
