package com.example.nytimesmostpopular.business.objects;


import java.io.File;

public class ParamFile {
    private String key;
    private File file;

    public ParamFile(String key, File file)
    {
        this.key=key;
        this.file=file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
