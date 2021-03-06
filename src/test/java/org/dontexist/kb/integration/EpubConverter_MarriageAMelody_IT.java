package org.dontexist.kb.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-context.xml")
public class EpubConverter_MarriageAMelody_IT extends AbstractEpubConverterIT {

    private final boolean deleteOutputFile = true;

    @Test
    public void testConvertFileAsOneStringToUnicode_Unchanged1() throws Exception {
        // this file contains no eligible characters (Sanskrit99 or Palladio IT)
        // to convert. it should be unchanged by the conversion process, except
        // for &ndash;
        File inputfile = new File("src/test/resources/org/dontexist/kb/part0005.html");
        File actualOutputFile = new File("src/test/resources/org/dontexist/kb/part0005.html.out");
        File expectedOutputFile = inputfile;
        verify(inputfile, actualOutputFile, expectedOutputFile, deleteOutputFile);
    }

    @Test
    public void testConvertFileAsOneStringToUnicode_Convert1() throws Exception {
        // sample from "Marriage A Melody" containing Sanskrit99 and PalladioIT
        File inputfile = new File("src/test/resources/org/dontexist/kb/part0006.html");
        File actualOutputFile = new File("src/test/resources/org/dontexist/kb/part0006.html.out");
        File expectedOutputFile = new File("src/test/resources/org/dontexist/kb/part0006.html.expected");
        verify(inputfile, actualOutputFile, expectedOutputFile, deleteOutputFile);
    }

}
