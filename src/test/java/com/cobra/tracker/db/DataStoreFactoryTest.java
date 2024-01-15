package com.cobra.tracker.db;

import com.cobra.tracker.util.CobraException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class DataStoreFactoryTest {

    @Test
    public void createInventoryStore() throws CobraException {
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