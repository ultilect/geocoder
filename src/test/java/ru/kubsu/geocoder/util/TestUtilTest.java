package ru.kubsu.geocoder.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TestUtilTest {

   @Test
   void unitTest() {
        assertEquals(3, TestUtil.sum(1,2));
        assertEquals(-10, TestUtil.sum(-15, 5));
        assertEquals(-5, TestUtil.sum(-10,5));
        assertEquals(0, TestUtil.sum(-1000, 1000));
   }

}
