package com.kaviddiss.storm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

/**
 * Receives tweets and emits its words over a certain length.
 */
public class IsTweetPostive extends BaseRichBolt {

	private static final long serialVersionUID = 5151173513759399636L;

	private OutputCollector collector;

	public IsTweetPostive() {
	}

	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String lang = (String) input.getValueByField("lang");
		String fullTweet = (String) input.getValueByField("word");

		System.out.println("Tweet is positive");

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("lang", "word"));
	}
}
