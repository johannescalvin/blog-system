package tech.freecode.commonmark.ext.url.accessibility;

public class LinuxLocalFile implements LocalFile{
    @Override
    public String getAbsolutePath(String fileBase, String relativePath) {
        String path = fileBase;
        while(path.endsWith("/")){
            path = path.substring(0,path.length()-1);
        }

        while (relativePath.startsWith("../")){
            int index = path.lastIndexOf("/");
            if (index == -1 || index == 0){
                return null;
            }
            path = path.substring(0,index);
            relativePath = relativePath.substring(3);
        }

        if (relativePath.startsWith("./")){
            relativePath.substring(2);
        }

        path = path + "/" + relativePath;

        return path;
    }
}
