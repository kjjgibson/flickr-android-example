package com.example.flickrexample.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.flickrexample.model.FlickrImage;

import java.util.Objects;

@Entity(tableName = "flickrImages")
public class FlickrImageEntity implements FlickrImage {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String flickrId;
    private String title;
    private String server;
    private String secret;
    private String searchTerm;
    private int farm;
    private int pageNumber;

    public FlickrImageEntity() {
    }

    @Ignore
    public FlickrImageEntity(String flickrId, String title, String server, String secret, String searchTerm, int farm, int pageNumber) {
        this.flickrId = flickrId;
        this.title = title;
        this.server = server;
        this.secret = secret;
        this.searchTerm = searchTerm;
        this.farm = farm;
        this.pageNumber = pageNumber;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getFlickrId() {
        return flickrId;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public int getFarm() {
        return this.farm;
    }

    @Override
    public String getSecret() {
        return this.secret;
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public String getSearchTerm() {
        return this.searchTerm;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFlickrId(String flickrId) {
        this.flickrId = flickrId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlickrImageEntity that = (FlickrImageEntity) o;
        return id == that.id &&
                Objects.equals(flickrId, that.flickrId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flickrId);
    }
}
