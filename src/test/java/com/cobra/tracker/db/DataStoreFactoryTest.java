package com.cobra.tracker.db;

import com.cobra.tracker.BaseTest;
import com.cobra.tracker.db.repo.DataStoreFactory;
import com.cobra.tracker.db.repo.DiffResultsStore;
import com.cobra.tracker.db.repo.InventoryStore;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class DataStoreFactoryTest extends BaseTest {

    @Test
    public void createInventoryStore() {
        InventoryStore store = DataStoreFactory.createInventoryStore();
        File f = new File(store.getInventoryDir());
        assertTrue(f.exists());
    }

    @Test
    public void createDiffResultsStore() {
        DiffResultsStore store = DataStoreFactory.createDiffResultsStore();
        File f = new File(store.getDiffResultsDir());
        assertTrue(f.exists());
    }

}