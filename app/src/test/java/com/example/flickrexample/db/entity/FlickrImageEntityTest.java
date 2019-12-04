package com.example.flickrexample.db.entity;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class FlickrImageEntityTest {

    @Test
    public void equals_WithTwoDifferentIds_ReturnsFalse() {
        FlickrImageEntity entity1 = new FlickrImageEntity("ID", "Title", "server", "secret", "Kittens", 10, 1);
        entity1.setId(1);

        FlickrImageEntity entity2 = new FlickrImageEntity("ID", "Title", "server", "secret", "Kittens", 10, 1);
        entity2.setId(2);

        assertFalse(entity1.equals(entity2));
    }

    @Test
    public void equals_WithTwoDifferentFlickrIds_ReturnsFalse() {
        FlickrImageEntity entity1 = new FlickrImageEntity("ID1", "Title", "server", "secret", "Kittens", 10, 1);
        entity1.setId(1);

        FlickrImageEntity entity2 = new FlickrImageEntity("ID2", "Title", "server", "secret", "Kittens", 10, 1);
        entity2.setId(1);

        assertFalse(entity1.equals(entity2));
    }

    @Test
    public void equals_WithTheSameIdsAndFlickrIds_ReturnsFalse() {
        FlickrImageEntity entity1 = new FlickrImageEntity("ID", "Title 1", "server", "secret", "Kittens", 10, 1);
        entity1.setId(1);

        FlickrImageEntity entity2 = new FlickrImageEntity("ID", "Title 2", "server", "secret", "Kittens", 10, 1);
        entity2.setId(1);

        assertEquals(entity1, entity2);
    }
}
