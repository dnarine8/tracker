package com.cobra.forensics.pshell;

import com.cobra.forensics.db.model.WindowsGenericForensicData;
import com.cobra.forensics.util.CobraException;
import org.junit.Test;

import static org.junit.Assert.*;

public class WindowsGenericForensicDataTest {

//    @Test
    public void create() throws CobraException {
        String key = "key";
        String [] names = new String[] {"key","field"};
        String [] values = new String[] {"keyval","fieldval"};
        String [] invalidValues = new String[] {"keyval"};


        // invalid input
        assertThrows(CobraException.class, () -> new WindowsGenericForensicData(null,names,values));
        assertThrows(CobraException.class, () -> new WindowsGenericForensicData(key,null,values));
        assertThrows(CobraException.class, () -> new WindowsGenericForensicData(key,names,null));
        assertThrows(CobraException.class, () -> new WindowsGenericForensicData("junk",names,values));
        assertThrows(CobraException.class, () -> new WindowsGenericForensicData("junk",names,invalidValues));

        // valid input
        WindowsGenericForensicData forensicData = new WindowsGenericForensicData(key,names,values);
        assertEquals(forensicData.key(), key);
        WindowsGenericForensicData forensicData2 = null;
        assertNotEquals(forensicData, forensicData2);

        forensicData2 = new WindowsGenericForensicData(key,names,values);
        assertEquals(forensicData, forensicData2);

        String [] names2 = new String[] {"key"};
        String [] values2 = new String[] {"keyval"};
        forensicData2 = new WindowsGenericForensicData(key,names2,invalidValues);
        assertNotEquals(forensicData, forensicData2);

        names2 = new String[] {"key","field"};
        values2 = new String[] {"keyval",null};
        assertNotEquals(forensicData, forensicData2);

        System.out.println(forensicData);

    }

}