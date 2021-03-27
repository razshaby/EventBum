package com.raz.eventbum.objects;

public class ImageInfo {
    String name;
    String albumName;
    String url;
    String uploader;
    long time;


    public ImageInfo() {
    }

    public ImageInfo(String name, String albumName, String url, String uploader) {
        this.name = name;
        this.albumName = albumName;
        this.url = url;
        this.uploader = uploader;
        setTime();
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public long getTime() {
        return time;
    }

    public void setTime() {
        this.time = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "name='" + name + '\'' +
                ", albumName='" + albumName + '\'' +
                ", url='" + url + '\'' +
                ", uploader='" + uploader + '\'' +
                ", time=" + time +
                '}';
    }
}
