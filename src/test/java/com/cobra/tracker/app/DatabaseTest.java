package com.cobra.tracker.app;

import com.cobra.tracker.util.CobraException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void create() throws CobraException {
        Database db = new Database("test.db");
    }

}