package files;

public enum FileType {
    DIRECTORY{
        public String get3DPath(){
            return "";
        }
    },
    TEXT{
        public String get3DPath(){
            return "";
        }
    },
    IMAGE{
        public String get3DPath(){
            return "";
        }
    };

    /**
     * @return assets filepath for the 3D object
     */
    abstract String get3DPath();
}
