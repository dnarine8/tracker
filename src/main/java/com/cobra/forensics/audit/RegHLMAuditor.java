package com.cobra.forensics.audit;

import com.cobra.forensics.db.model.WindowsGenericForensicData;
import com.cobra.forensics.db.repo.InventoryStore;
import com.cobra.forensics.util.CobraException;

import java.io.*;

public class RegHLMAuditor extends GenericWindowsAuditor {
        public static final String TYPE = "HKLM";
        private static final String KEY = "Name";
        private static final String CMD = "reg.ps1";

    public RegHLMAuditor() {
            super(TYPE, KEY, CMD,TYPE);
        }




}
