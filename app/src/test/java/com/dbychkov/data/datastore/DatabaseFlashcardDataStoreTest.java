package com.dbychkov.data.datastore;

import com.dbychkov.data.greendao.DaoSession;
import com.dbychkov.data.greendao.FlashcardEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by brian on 5/6/16.
 * Its very important to test the data access layer to make sure that CRUD ops are done correctly
 * and data integrity is preserved
 * This class tests the DatabaseFlashcardDataStore but DatabaseLessonDataStore,
 * FlashcardDataRepository and LessonDataRepository should be tested in very similar way
 */
public class DatabaseFlashcardDataStoreTest {

    DatabaseFlashcardDataStore databaseFlashcardDataStore;

    /**
     * init a databaseflashcarddatastore with injected mock object
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        databaseFlashcardDataStore = new DatabaseFlashcardDataStore(mock(DaoSession.class));
    }

    /**
     * init a new databaseflashcardatastore
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        databaseFlashcardDataStore = new DatabaseFlashcardDataStore(mock(DaoSession.class));
    }


    @Test
    public void testAddFlashcard() throws Exception {
        FlashcardEntity flashcardEntity = new FlashcardEntity(1L);
        databaseFlashcardDataStore.addFlashcard(flashcardEntity);
        FlashcardEntity result =
                databaseFlashcardDataStore.getFlashcardById(1L).toBlocking().singleOrDefault(null);
        assertEquals(flashcardEntity, result);
    }

    @Test
    public void testRemoveFlashcard() throws Exception {
        FlashcardEntity flashcardEntity = new FlashcardEntity(1L);
        databaseFlashcardDataStore.addFlashcard(flashcardEntity);
        FlashcardEntity result =
                databaseFlashcardDataStore.getFlashcardById(1L).toBlocking().singleOrDefault(null);
        if (result == null) {
            fail("can't remove flashcard from empty data store");
        } else {
            databaseFlashcardDataStore.removeFlashcard(1L);
            result = databaseFlashcardDataStore.getFlashcardById(1L).toBlocking().singleOrDefault(null);
            assertEquals(result, null);
        }
    }

    @Test
    public void testBulkInsertFlashcards() throws Exception {
        FlashcardEntity flashcardEntity1 = new FlashcardEntity(1L);
        FlashcardEntity flashcardEntity2 = new FlashcardEntity(2L);
        List<FlashcardEntity> flashList = new ArrayList<>();
        flashList.add(flashcardEntity1);
        flashList.add(flashcardEntity2);
        databaseFlashcardDataStore.bulkInsertFlashcards(flashList);
        List<FlashcardEntity> result =
                databaseFlashcardDataStore.getAllFlashcards().toBlocking().singleOrDefault(null);
        if (result == null) {
            fail();
        } else {
            assertArrayEquals(flashList.toArray(), result.toArray());
        }
    }

    @Test
    public void testGetFlashcardById() throws Exception {
        FlashcardEntity flashcardEntity = new FlashcardEntity(1L);
        databaseFlashcardDataStore.addFlashcard(flashcardEntity);
        FlashcardEntity result =
                databaseFlashcardDataStore.getFlashcardById(1L).toBlocking().singleOrDefault(null);
        assertEquals(result, flashcardEntity);
    }

    @Test
    public void testUpdateFlashcard() throws Exception {
        FlashcardEntity flashcardEntity = new FlashcardEntity(1L);
        flashcardEntity.setStatus(3);
        databaseFlashcardDataStore.updateFlashcard(flashcardEntity);
        FlashcardEntity result =
                databaseFlashcardDataStore.getFlashcardById(1L).toBlocking().singleOrDefault(null);
        assertEquals(flashcardEntity.getStatus(), result.getStatus());
    }

    @Test
    public void testGetAllFlashcards() throws Exception {
        FlashcardEntity flashcardEntity1 = new FlashcardEntity(1L);
        FlashcardEntity flashcardEntity2 = new FlashcardEntity(2L);
        List<FlashcardEntity> flashList = new ArrayList<>();
        flashList.add(flashcardEntity1);
        flashList.add(flashcardEntity2);
        databaseFlashcardDataStore.bulkInsertFlashcards(flashList);
        List<FlashcardEntity> result =
                databaseFlashcardDataStore.getAllFlashcards().toBlocking().singleOrDefault(null);
        if (result == null) {
            fail();
        } else {
            assertArrayEquals(flashList.toArray(), result.toArray());
        }
    }

    @Test
    public void testGetFlashcardsFromLesson() throws Exception {
        FlashcardEntity flashcardEntity1 = new FlashcardEntity(1L);
        FlashcardEntity flashcardEntity2 = new FlashcardEntity(2L);
        flashcardEntity1.setLessonId(1L);
        flashcardEntity2.setLessonId(1L);
        List<FlashcardEntity> flashList = new ArrayList<>();
        flashList.add(flashcardEntity1);
        flashList.add(flashcardEntity2);
        databaseFlashcardDataStore.bulkInsertFlashcards(flashList);
        List<FlashcardEntity> result =
                databaseFlashcardDataStore.getAllFlashcards().toBlocking().singleOrDefault(null);
        if (result.isEmpty()) {
            fail("cannot test getFlashcardsFromLesson since FlashcardEntities couldn't be added");
        } else {
            result =
                    databaseFlashcardDataStore.getFlashcardsFromLesson(1L).toBlocking()
                            .singleOrDefault(null);
            Long e1LessonId = null;
            Long e2LessonId = null;
            for (FlashcardEntity entity : result) {
                if (entity.getId() == flashcardEntity1.getId()) {
                    e1LessonId = entity.getLessonId();
                }
                if (entity.getId() == flashcardEntity2.getId()) {
                    e2LessonId = entity.getLessonId();
                }
            }
            assertEquals(true, e1LessonId == e2LessonId && e1LessonId == 1L);
        }
    }

    @Test
    public void testClearProgressForLesson() throws Exception {
        FlashcardEntity flashcardEntity1 = new FlashcardEntity(1L);
        FlashcardEntity flashcardEntity2 = new FlashcardEntity(2L);
        flashcardEntity1.setLessonId(1L);
        flashcardEntity2.setLessonId(1L);
        List<FlashcardEntity> flashList = new ArrayList<>();
        flashList.add(flashcardEntity1);
        flashList.add(flashcardEntity2);
        databaseFlashcardDataStore.bulkInsertFlashcards(flashList);
        List<FlashcardEntity> result =
                databaseFlashcardDataStore.getAllFlashcards().toBlocking().singleOrDefault(null);
        if (result.isEmpty()) {
            fail("cannot test clearProgressForLesson since FlashcardEntities couldn't be added");
        } else {
            databaseFlashcardDataStore.clearProgressForLesson(1L);
            result = databaseFlashcardDataStore.getFlashcardsFromLesson(1L)
                        .toBlocking().singleOrDefault(null);
            boolean notCleared = false;
            for (FlashcardEntity entity : result) {
                if (entity.getStatus() != 0) {
                    notCleared = true;
                }
            }
            assertEquals(notCleared, false);
        }
    }

    @Test
    public void testGetUnlearntFlashcardsFromLesson() throws Exception {
        FlashcardEntity flashcardEntity1 = new FlashcardEntity(1L);
        FlashcardEntity flashcardEntity2 = new FlashcardEntity(1L);
        FlashcardEntity flashcardEntity3 = new FlashcardEntity(1L);
        flashcardEntity1.setLessonId(1L);
        flashcardEntity2.setLessonId(1L);
        flashcardEntity2.setLessonId(1L);
        flashcardEntity1.setStatus(0);
        flashcardEntity2.setStatus(0);
        flashcardEntity3.setStatus(1);
        List<FlashcardEntity> flashList = new ArrayList<>();
        flashList.add(flashcardEntity1);
        flashList.add(flashcardEntity2);
        flashList.add(flashcardEntity3);
        databaseFlashcardDataStore.bulkInsertFlashcards(flashList);
        List<FlashcardEntity> result =
                databaseFlashcardDataStore.getAllFlashcards().toBlocking().singleOrDefault(null);
        if (result.isEmpty()) {
            fail("cannot test getUnlearentFlashcardsFromLesson since FlashcardEntities couldn't " +
                    "be added");
        } else {
            result = databaseFlashcardDataStore.getUnlearntFlashcardsFromLesson(1L)
                            .toBlocking().singleOrDefault(null);
            boolean notAllUnlearnt = false;
            for (FlashcardEntity entity : result) {
                if (entity.getStatus() != 0 || entity.getLessonId() != 1L) {
                    notAllUnlearnt = true;
                }
            }
            assertEquals(notAllUnlearnt, false);
        }
    }
}