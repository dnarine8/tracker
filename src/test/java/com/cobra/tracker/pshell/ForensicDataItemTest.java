package com.cobra.tracker.pshell;

import com.cobra.tracker.util.CobraException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ForensicDataItemTest {

    @Test
    public void create() throws CobraException {
        String key = "key";
        String [] names = new String[] {"key","field"};
        String [] values = new String[] {"keyval","fieldval"};
        String [] invalidValues = new String[] {"keyval"};


        // invalid input
        assertThrows(CobraException.class, () -> new ForensicDataItem(null,names,values));
        assertThrows(CobraException.class, () -> new ForensicDataItem(key,null,values));
        assertThrows(CobraException.class, () -> new ForensicDataItem(key,names,null));
        assertThrows(CobraException.class, () -> new ForensicDataItem("junk",names,values));
        assertThrows(CobraException.class, () -> new ForensicDataItem("junk",names,invalidValues));

        // valid input
        ForensicDataItem forensicData = new ForensicDataItem(key,names,values);
        assertTrue(forensicData.key().equals(key));
        ForensicDataItem forensicData2 = null;
        assertNotEquals(forensicData, forensicData2);

        forensicData2 = new ForensicDataItem(key,names,values);
        assertEquals(forensicData, forensicData2);

        String [] names2 = new String[] {"key"};
        String [] values2 = new String[] {"keyval"};
        forensicData2 = new ForensicDataItem(key,names2,invalidValues);
        assertNotEquals(forensicData, forensicData2);

        names2 = new String[] {"key","field"};
        values2 = new String[] {"keyval",null};
        assertNotEquals(forensicData, forensicData2);

        System.out.println(forensicData);

    }

}