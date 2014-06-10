package org.dontexist.kb.integration;

import junitx.framework.FileAssert;
import org.apache.commons.io.FileUtils;
import org.dontexist.kb.SpringDriver;
import org.dontexist.kb.service.epuboperations.ParentEpubReaderServiceImpl;
import org.dontexist.kb.service.epuboperations.ZipEpubReaderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

abstract class AbstractEpubConverterIT {

    @Autowired
    protected SpringDriver springDriver;

    // TODO autowire instead
    protected ParentEpubReaderServiceImpl epubReaderService = new ParentEpubReaderServiceImpl();

    // --------------- HELPER METHODS -----------------

    protected void verify(File inputfile, File actualOutputFile, File expectedOutputFile) throws IOException, Exception {
        actualOutputFile.deleteOnExit();
        // StringBuilder convertedFileAsString = (StringBuilder)
        // ReflectionTestUtils.invokeMethod(springDriver,
        // "convertFileToUnicode",
        // inputfile);
        StringBuilder convertedFileAsString = epubReaderService.convertFileAsOneStringToUnicode(FileUtils.readFileToString(inputfile));
        FileUtils.writeStringToFile(actualOutputFile, convertedFileAsString.toString());
        FileAssert.assertEquals(expectedOutputFile, actualOutputFile);
    }

}
