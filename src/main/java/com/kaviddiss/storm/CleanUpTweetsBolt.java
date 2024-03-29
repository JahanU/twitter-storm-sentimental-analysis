package com.kaviddiss.storm;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import twitter4j.Status;

public class CleanUpTweetsBolt extends BaseRichBolt {

	private static final long serialVersionUID = 5151173513759399636L;
	private OutputCollector collector;

	public CleanUpTweetsBolt() {
	}

	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		Status tweet = (Status) input.getValueByField("tweet");

		// Regex to filter irrelevant words. e.g [@, 'RT', any HTTP text, and new lines]
		String pattern = "\bhtt[ps]?[a-zA-Z].*|[0-9]|@[a-zA-Z]+|RT|\n";
		Pattern regex = Pattern.compile(pattern);
		StringBuilder sb = new StringBuilder();

		String[] textArr = tweet.getText().split(" ");
		for (String word : textArr) {
			Matcher m = regex.matcher(word);
			if (!m.find()) // If current word does not match the regex, then append to stringBuilder!
				sb.append(word + " ");
		}

		// Final filtered string/tweet!
		collector.emit(new Values(sb.toString()));

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet"));
	}
}
