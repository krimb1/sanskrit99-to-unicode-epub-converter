package org.dontexist.kb.service;

import org.dontexist.kb.service.epuboperations.EpubReaderService;
import org.dontexist.kb.service.epuboperations.EpubReaderServiceFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EpubReaderServiceFactoryTest {

    public static final String EPUB_FILE_FOR_READING = "src/test/resources/org/dontexist/kb/test.epub";

    @Autowired
    private EpubReaderServiceFactory epubReaderServiceFactory;

    @Test
    public void createEpubReaderServiceTest() throws Exception {
        File epubFile = new File(EPUB_FILE_FOR_READING);
        EpubReaderService service = epubReaderServiceFactory.createEpubReaderService(epubFile);
        System.out.println(service.getClass());
    }

}
