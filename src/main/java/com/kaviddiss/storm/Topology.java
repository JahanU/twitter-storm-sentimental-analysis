package com.kaviddiss.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class Topology {

	static final String TOPOLOGY_NAME = "storm-twitter-word-count";

	public static void main(String[] args) {
		Config config = new Config();
		config.setMessageTimeoutSecs(120);

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("TwitterSampleSpout", new TwitterSampleSpout());
		builder.setBolt("CleanUpTweetsBolt", new CleanUpTweets()).shuffleGrouping("TwitterSampleSpout");

		builder.setBolt("isTweetPositiveBolt", new IsTweetPostiveBolt()).shuffleGrouping("CleanUpTweetsBolt");
		builder.setBolt("isTweetNegativeBolt", new IsTweetNegativeBolt()).shuffleGrouping("CleanUpTweetsBolt");

//		builder.setBolt("WordSplitterBolt", new WordSplitterBolt(5)).shuffleGrouping("TwitterSampleSpout");
//		builder.setBolt("IgnoreWordsBolt", new IgnoreWordsBolt()).shuffleGrouping("WordSplitterBolt");
//		builder.setBolt("WordCounterBolt", new WordCounterBolt(10, 5 * 60, 50)).shuffleGrouping("IgnoreWordsBolt");

		final LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(TOPOLOGY_NAME, config, builder.createTopology());

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				cluster.killTopology(TOPOLOGY_NAME);
				cluster.shutdown();
			}
		});

	}

}
