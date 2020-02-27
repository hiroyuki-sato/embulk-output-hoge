package org.embulk.output.hoge;

import com.google.common.io.Resources;
import org.embulk.config.ConfigException;
import org.embulk.config.ConfigSource;
import org.embulk.exec.PartialExecutionException;
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
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class TestHogeOutputPlugin
{
    @Rule
    public TestingEmbulk embulk = TestingEmbulk.builder().registerPlugin(OutputPlugin.class,"hoge",HogeOutputPlugin.class).build();

    @Test
    public void testNormalRun() throws Exception {
        ConfigSource configSource = embulk.loadYamlResource("org/embulk/output/hoge/out.yml");
        Path in =  Paths.get(Resources.getResource("org/embulk/output/hoge/in.csv").getPath());
        //System.out.println(configSource);
        //System.out.println(in);

        embulk.runOutput(configSource, in);
    }
    //@Test(expected = ConfigException.class)
    @Test(expected = PartialExecutionException.class)
    public void testNoOption() throws Exception {
        ConfigSource configSource = embulk.loadYamlResource("org/embulk/output/hoge/out_err.yml");
        Path in =  Paths.get(Resources.getResource("org/embulk/output/hoge/in.csv").getPath());

        embulk.runOutput(configSource, in);
    }
    private Path toPath(String fileName) throws URISyntaxException
    {
        URL url = Resources.getResource("org/embulk/output/hoge/" + fileName);
        return FileSystems.getDefault().getPath(new File(url.toURI()).getAbsolutePath());
    }
}
