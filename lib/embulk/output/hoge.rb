Embulk::JavaPlugin.register_output(
  "hoge", "org.embulk.output.hoge.HogeOutputPlugin",
  File.expand_path('../../../../classpath', __FILE__))
