package files;

import render3D.Mesh;
import render3D.MeshBuilder;

public enum FileType{
    TEXT{
        @Override
        public Mesh getMesh() {
            return new MeshBuilder().readOBJ(
                    ".\\assets\\textfile3.obj",
                    ".\\assets\\textfilepalette.png");
        }
    },
    IMAGE {
        @Override
        public Mesh getMesh() {
            return new MeshBuilder().readOBJ(
                    ".\\assets\\imagefile.obj",
                    ".\\assets\\imagefilepalette.png");
        }
    },
    DIRECTORY {
        @Override
        public Mesh getMesh() {
            return new MeshBuilder().readOBJ(
                    ".\\assets\\folder (1).obj",
                    ".\\assets\\folderpalette.png");
        }
    },
    ZIP {
        @Override
        public Mesh getMesh() {
            return new MeshBuilder().readOBJ(
                    ".\\assets\\folder (1).obj",
                    ".\\assets\\folderpalette.png");
        }
    };
    public abstract Mesh getMesh();
}