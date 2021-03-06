package org.dontexist.kb.integration;

import junitx.framework.FileAssert;
import org.apache.commons.io.FileUtils;
import org.dontexist.kb.SpringDelegateDriver;
import org.dontexist.kb.service.converter.UnicodeConverterHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

abstract class AbstractEpubConverterIT {

    @Autowired
    protected SpringDelegateDriver springDelegateDriver;

    @Autowired
    protected UnicodeConverterHelper unicodeConverterHelper;

    // --------------- HELPER METHODS -----------------

    protected void verify(File inputfile, File actualOutputFile, File expectedOutputFile, boolean deleteOutputFile) throws IOException, Exception {
        if (deleteOutputFile) {
            actualOutputFile.deleteOnExit();
        }
        StringBuilder convertedFileAsString = unicodeConverterHelper.convertFileAsOneStringToUnicode(FileUtils.readFileToString(inputfile));
        FileUtils.writeStringToFile(actualOutputFile, convertedFileAsString.toString());
        FileAssert.assertEquals(expectedOutputFile, actualOutputFile);
    }

}
