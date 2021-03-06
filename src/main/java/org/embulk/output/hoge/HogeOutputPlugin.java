package org.embulk.output.hoge;

import java.util.List;

import com.google.common.base.Optional;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskReport;
import org.embulk.config.TaskSource;
import org.embulk.spi.Exec;
import org.embulk.spi.OutputPlugin;
import org.embulk.spi.Page;
import org.embulk.spi.PageOutput;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.TransactionalPageOutput;
import org.embulk.spi.util.PagePrinter;

public class HogeOutputPlugin
        implements OutputPlugin
{
    public interface PluginTask
            extends Task
    {
        // configuration option 1 (required integer)
        @Config("option1")
        public int getOption1();

        // configuration option 2 (optional string, null is not allowed)
        @Config("option2")
        @ConfigDefault("\"myvalue\"")
        public String getOption2();

        // configuration option 3 (optional string, null is allowed)
        @Config("option3")
        @ConfigDefault("null")
        public Optional<String> getOption3();
    }

    @Override
    public ConfigDiff transaction(ConfigSource config,
            Schema schema, int taskCount,
            OutputPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);

        // retryable (idempotent) output:
        // return resume(task.dump(), schema, taskCount, control);

        // non-retryable (non-idempotent) output:
        control.run(task.dump());
        return Exec.newConfigDiff();
    }

    @Override
    public ConfigDiff resume(TaskSource taskSource,
            Schema schema, int taskCount,
            OutputPlugin.Control control)
    {
        throw new UnsupportedOperationException("hoge output plugin does not support resuming");
    }

    @Override
    public void cleanup(TaskSource taskSource,
            Schema schema, int taskCount,
            List<TaskReport> successTaskReports)
    {
    }

    public static class HogePageOutput implements TransactionalPageOutput {
        private TaskSource taskSource;
        private Schema schema;
        private int taskIndex;
        private PageReader reader;

        public HogePageOutput(TaskSource taskSource, Schema schema, int taskIndex){
            this.reader = new PageReader(schema);
            this.taskSource = taskSource;
            this.schema = schema;
            this.taskIndex = taskIndex;
        }

        @Override
        public void add(final Page page)
        {
            reader.setPage(page);
            while( reader.nextRecord() ) {
                System.out.println("test");
            }
        }

        @Override
        public void finish()
        {

        }

        @Override
        public void close()
        {

        }

        @Override
        public void abort()
        {

        }

        @Override
        public TaskReport commit()
        {
            return null;
        }
    }
    @Override
    public TransactionalPageOutput open(TaskSource taskSource, Schema schema, int taskIndex)
    {
        PluginTask task = taskSource.loadTask(PluginTask.class);

        // Write your code here :)
        return new HogePageOutput(taskSource, schema, taskIndex);
    }


}
