package com.amazonaws.codepipeline.jobworker;

import org.jruby.Ruby;
import java.io.InputStreamReader;
import java.io.InputStream;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import com.amazonaws.codepipeline.jobworker.configuration.JobWorkerConfiguration;

public class JRubyJobWorkerConfigurationFactory {

  static {
    Ruby.newInstance();
  }

  /**
   * Evaluate the JRuby code which should define the JobWorkerConfiguration class
   * and most importantly, the processor to do the heavy lifting for the custom action
   *
   * @param className this is set in application-start.sh  it needs to agree with the class name
   *                  in jruby_action_job_worker_configuration.rb
   */
  public JobWorkerConfiguration constructJobWorkerConfiguration(String className) throws Exception {

    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("jruby");

    String[] jrubyResourcesToEvaluate = new String[] {
      "/jruby_code_pipeline_job_processor.rb",
      "/jruby_action_job_worker_configuration.rb"
    };

    for(String jrubyResourceToEvaluate : jrubyResourcesToEvaluate) {
      try(InputStream jrubyResourceInputStream = JRubyJobWorkerConfigurationFactory.class.getResourceAsStream(jrubyResourceToEvaluate)) {
        engine.eval(new InputStreamReader(jrubyResourceInputStream));
      }
    }

    String rubyCodeToConstructJobWorkerConfiguration = className + ".new";
    return (JobWorkerConfiguration)engine.eval(rubyCodeToConstructJobWorkerConfiguration);
  }
}