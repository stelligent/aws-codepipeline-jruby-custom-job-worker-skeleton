#! /bin/sh
chkconfig aws-codepipeline-jobworker on
/etc/init.d/aws-codepipeline-jobworker start SampleCustomJRubyActionJobWorkerConfiguration
