package org.embulk.output.hoge;

import com.google.common.io.Resources;
import org.embulk.config.ConfigSource;
import org.embulk.spi.OutputPlugin;
import org.junit.Rule;
import org.junit.Test;
import org.embulk.test.EmbulkTests;
import org.embulk.test.TestingEmbulk;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class TestHogeOutputPlugin
{
    @Rule
    public TestingEmbulk embulk = TestingEmbulk.builder().registerPlugin(OutputPlugin.class,"hoge",HogeOutputPlugin.class).build();

    @Test
    public void test() throws Exception {
        Path in = null;
        ConfigSource config = embulk.newConfig().set("type","hoge");
        try {
            in = toPath("in.csv");
        } catch (Exception ex ){
            fail("Resource failed");
        }
        System.out.println(config);
        System.out.println(in);

        TestingEmbulk.RunResult result = embulk.runOutput(config,in);

        /*
        try {
            TestingEmbulk.RunResult result = embulk.runOutput(config,in);
            System.out.println(result);
            assertEquals(true,true);
        } catch (Exception ex) {
            fail("Run failed");
        }
*/
    }
    private Path toPath(String fileName) throws URISyntaxException
    {
        URL url = Resources.getResource("org/embulk/output/hoge/" + fileName);
        return FileSystems.getDefault().getPath(new File(url.toURI()).getAbsolutePath());
    }
}
