package com.dbychkov.words;


import com.dbychkov.data.datastore.DatabaseFlashcardDataStore;
import com.dbychkov.data.greendao.DaoSession;
import com.dbychkov.data.greendao.FlashcardEntity;
import com.dbychkov.data.greendao.FlashcardEntityDao;

import static org.mockito.Mockito.*;
import org.junit.*;
import java.util.regex.Pattern;
import static org.junit.*;
import static org.junit.Assert.*;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



/**
 * Created by brian on 5/6/16.
 */
public class FlashcardDataStoreTest {
    @SmallTest
    public void testAddFlashCard() {
        DaoSession mockedDaoSession =  mock(DaoSession.class);
        FlashcardEntity mockedFlashcardEntity= mock(FlashcardEntity.class);
        DatabaseFlashcardDataStore flashcardStore =
                new DatabaseFlashcardDataStore(mockedDaoSession);
        flashcardStore.addFlashcard(mockedFlashcardEntity);
        assertEquals()






    }


}
