package com.kaviddiss.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class Topology {

	static final String TOPOLOGY_NAME = "storm-twitter-covid-vaccine-analysis";

	public static void main(String[] args) {
		Config config = new Config();
		config.setMessageTimeoutSecs(120);

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("TwitterSampleSpout", new TwitterSampleSpout());
		builder.setBolt("CleanUpTweetsBolt", new CleanUpTweetsBolt()).shuffleGrouping("TwitterSampleSpout");
		builder.setBolt("isTweetPositiveBolt", new IsTweetPostiveBolt()).shuffleGrouping("CleanUpTweetsBolt");
		builder.setBolt("isTweetNegativeBolt", new IsTweetNegativeBolt()).shuffleGrouping("CleanUpTweetsBolt");

		ScoreCountBolt scoreCountObj = new ScoreCountBolt();
		builder.setBolt("ScoreCountBoltPos", scoreCountObj).shuffleGrouping("isTweetPositiveBolt");
		builder.setBolt("ScoreCountBoltNeg", scoreCountObj).shuffleGrouping("isTweetNegativeBolt");

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
