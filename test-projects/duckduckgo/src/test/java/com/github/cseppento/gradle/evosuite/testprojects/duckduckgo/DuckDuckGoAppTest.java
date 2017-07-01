package com.github.cseppento.gradle.evosuite.testprojects.duckduckgo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DuckDuckGoAppTest {
    private List<String> input;
    private List<String> expectedOutput;

    @Mock
    private DuckDuckGoClient client;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        input = new ArrayList<>();
        expectedOutput = new ArrayList<>();
    }

    @Test
    public void testNoInput() throws Exception {
        expectedOutput.add("I will search Duck Duck Go for you");
        expectedOutput.add("Search: Thanks for using me!");

        executeTest();
    }

    @Test
    public void testEmptyAndBlankInput() throws Exception {
        input.add("");
        input.add(" ");

        expectedOutput.add("I will search Duck Duck Go for you");
        expectedOutput.add("Search: Search: Search: Thanks for using me!");

        executeTest();
    }

    @Test
    public void testNormalInput() throws Exception {
        Mockito.doReturn(new DuckDuckGoAnswer("normal", "http://example.com/normal"))
                .when(client)
                .search("normal");

        input.add("normal");

        expectedOutput.add("I will search Duck Duck Go for you");
        expectedOutput.add("Search: DuckDuckGoAnswer{heading=normal, abstractUrl=http://example.com/normal}");
        expectedOutput.add("Search: Thanks for using me!");

        executeTest();
    }

    @Test
    public void testJackpotInput() throws Exception {
        Mockito.doReturn(new DuckDuckGoAnswer("Equals Sign", "http://example.com/Equals+Sign"))
                .when(client)
                .search("=");

        input.add("=");

        expectedOutput.add("I will search Duck Duck Go for you");
        expectedOutput.add("Search: DuckDuckGoAnswer{heading=Equals Sign, abstractUrl=http://example.com/Equals+Sign}");
        expectedOutput.add("JACKPOT!");
        expectedOutput.add("Search: Thanks for using me!");

        executeTest();
    }

    @Test(expected = DuckDuckGoAppException.class)
    public void testAnswerHeadingNullFails_knownBug() throws Exception {
        Mockito.doReturn(new DuckDuckGoAnswer(null, null))
                .when(client)
                .search("null");

        input.add("null");

        executeTest();
    }

    private void executeTest() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(String.join("\n", input).getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DuckDuckGoApp sut = new DuckDuckGoApp(in, new PrintStream(out), client);
        sut.run();

        String actualOutput = out.toString(StandardCharsets.UTF_8.name()).replace("\r\n", "\n").trim();

        assertEquals(String.join("\n", expectedOutput), actualOutput);
    }
}
