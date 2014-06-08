package org.dontexist.kb.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dontexist.kb.service.converter.PalladioIT2UnicodeConverterServiceImpl;
import org.dontexist.kb.service.converter.Sanskrit99ToUnicodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class ParentEpubReaderServiceImpl implements EpubReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentEpubReaderServiceImpl.class);

    private static final String SPAN_END_TAG = "</span>";

    private static final String SPAN_START_TAG = "<span";

    @Autowired
    private Sanskrit99ToUnicodeService san99Converter;

    @Autowired
    private PalladioIT2UnicodeConverterServiceImpl palladioITConverter;

    @Value("${sanskrit99.span.class}")
    private String sanskrit99SpanClassCsv;

    private Set<String> sanskrit99SpanClasses = new HashSet<String>();

    @Override
    public Collection<File> drillDownFolderForExtension(final File folder, final boolean isRecursive, final String... extensions) {
        @SuppressWarnings("unchecked")
        Collection<File> filesToConvert = FileUtils.listFiles(folder, extensions, isRecursive);
        LOGGER.debug("Found in folder [{}] files [{}]", folder, filesToConvert);
        return filesToConvert;
    }

    @Override
    public StringBuilder convertFileAsOneStringToUnicode(final String entireFileAsOneString) throws IOException {
        // do not escape html characters yet, because we need to split
        // based on tags
        StringBuilder outputStringy = new StringBuilder();
        int fromIndex = 0;
        while (true) {
            int spanStart = entireFileAsOneString.indexOf(SPAN_START_TAG, fromIndex);
            if (spanStart == -1) {
                // flush out rest of file and quit
                String restOfFile = entireFileAsOneString.substring(fromIndex);
                String convertedRestOfFile = palladioITConverter.convertHtmlBlock(restOfFile);
                outputStringy.append(convertedRestOfFile);
                break;
            }
            String partBeforeSpan = entireFileAsOneString.substring(fromIndex, spanStart);
            String convertedPartBeforeSpan = palladioITConverter.convertHtmlBlock(partBeforeSpan);
            outputStringy.append(convertedPartBeforeSpan);
            int spanEnd = entireFileAsOneString.indexOf(SPAN_END_TAG, spanStart);

            int beginIndex = spanStart;
            int endIndex = spanEnd + SPAN_END_TAG.length();
            String spanString = entireFileAsOneString.substring(beginIndex, endIndex);

            // perform Unicode replacement logic
            String convertedSpanString = performUnicodeReplacementOnSpanBlock(spanString);

            // prepare for next iteration
            fromIndex = endIndex;

            // flush work
            outputStringy.append(convertedSpanString);
        }
        // add final newline at end to make files identical
        outputStringy.append("\n");
        return outputStringy;
    }

    private String performUnicodeReplacementOnSpanBlock(final String spanString) {
        String convertedSpanString;

        // allow both santext, santext1, etc. to be replaced
        boolean isSpanStringSanskrit = false; // default
        for (String validSanskritSpanClass : sanskrit99SpanClasses) {
            if (StringUtils.contains(spanString, "class=\"" + validSanskritSpanClass + "\"")) {
                isSpanStringSanskrit = true;
                break;
            }
        }

        if (isSpanStringSanskrit) {
            convertedSpanString = san99Converter.convertHtmlBlock(spanString);
        } else {
            convertedSpanString = palladioITConverter.convertHtmlBlock(spanString);
        }
        return convertedSpanString;
    }

    @Override
    public void afterPropertiesSet() {
        addSanskritSpanClasses(sanskrit99SpanClassCsv);
    }

    void addSanskritSpanClasses(final String csvString) {
        String[] spanClasses = StringUtils.split(csvString, ",");
        if (spanClasses == null) {
            return;
        }
        for (String ithSpanClass : spanClasses) {
            sanskrit99SpanClasses.add(ithSpanClass);
        }
    }
}