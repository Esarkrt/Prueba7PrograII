package prueba7prograii;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Contador {

    private final File archivo;

    public Contador(File archivo) {
        this.archivo = archivo;
    }

    public int siguiente() throws IOException {
        try (RandomAccessFile rcods = new RandomAccessFile(archivo, "rw")) {
            if (rcods.length() == 0) {
                rcods.writeInt(1);
            }
            rcods.seek(0);
            int code = rcods.readInt();
            rcods.seek(0);
            rcods.writeInt(code + 1);
            return code;
        }
    }

    public void asegurarMinimo(int minimo) throws IOException {
        try (RandomAccessFile rcods = new RandomAccessFile(archivo, "rw")) {
            int actual = 1;
            if (rcods.length() > 0) {
                rcods.seek(0);
                actual = rcods.readInt();
            }
            if (actual < minimo) {
                rcods.seek(0);
                rcods.writeInt(minimo);
            }
        }
    }
}
