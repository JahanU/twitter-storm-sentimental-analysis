package com.kaviddiss.storm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class ScoreCountBolt extends BaseRichBolt {

	private static final long serialVersionUID = 5151173513759399636L;
	private static int posCount = 0;
	private static int negCount = 0;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		float tweetScore = (Float) input.getValueByField("score");
		String fullTweet = (String) input.getValueByField("tweet");

		if (tweetScore < 0)
			negCount++;
		else
			posCount++;

		if (negCount % 10 == 0 || posCount % 10 == 0) {
			System.out.println("Negative Score: " + negCount + " " + "Positive Score: " + posCount);
			System.out.println("Rating: " + tweetScore + " - " + fullTweet);

		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
