package com.kaviddiss.storm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class IsTweetNegativeBolt extends BaseRichBolt {

	private static final long serialVersionUID = 5151173513759399636L;

	private OutputCollector collector;
	private ArrayList<String> stopwords = new ArrayList<String>();
	private Map<String, String> wordRatingMap = new HashMap<String, String>();

	public IsTweetNegativeBolt() {

		BufferedReader stop;
		try {
			stop = new BufferedReader(new FileReader("Data/stopwords.txt"));
			String line = "";
			while ((line = stop.readLine()) != null) {
				this.stopwords.add(line);
				// Stopwords are meaningless words with no score e.g. [the, and, me]
			}

			BufferedReader in = new BufferedReader(new FileReader("Data/AFINN"));
			line = "";
			while ((line = in.readLine()) != null) {
				String parts[] = line.split("\t");
				wordRatingMap.put(parts[0], parts[1]); // HashMap of words with a rating
			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String fullTweet = (String) input.getValueByField("tweet");

		float tweetScore = 0;
		String[] tweetArr = fullTweet.split(" ");

		for (String word : tweetArr) {
			word = word.toLowerCase();

			// loop through words in tweet, get the score of the tweet
			if (!this.stopwords.contains(word) && this.wordRatingMap.get(word) != null) {
				String wordScore = wordRatingMap.get(word);
				tweetScore += Integer.parseInt(wordScore); // Update score
			}
		}
		if (tweetScore < 0) // if tweet is neg, we emit this
			collector.emit(new Values(tweetScore, fullTweet));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("score", "tweet"));
	}
}
