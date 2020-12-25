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

/**
 * Receives tweets and emits its words over a certain length.
 */
public class IsTweetPostive extends BaseRichBolt {

	private static final long serialVersionUID = 5151173513759399636L;

	private OutputCollector collector;
	private ArrayList<String> stopwords = new ArrayList<String>();
	private Map<String, String> map = new HashMap<String, String>();

	public IsTweetPostive() {

		BufferedReader stop;
		try {
			stop = new BufferedReader(new FileReader("Data/stopwords.txt"));
			String line = "";
			while ((line = stop.readLine()) != null) {
				this.stopwords.add(line);
			}

			System.out.println();
			BufferedReader in = new BufferedReader(new FileReader("Data/AFINN"));
			line = "";
			while ((line = in.readLine()) != null) {
				String parts[] = line.split("\t");
				map.put(parts[0], parts[1]);
			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(this.stopwords.size() + " " + this.map.size());

	}

	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String lang = (String) input.getValueByField("lang");
		String fullTweet = (String) input.getValueByField("word");

		float tweetScore = 0;
		String[] tweetArr = fullTweet.split(" ");

		for (String word : tweetArr) {
			word = word.toLowerCase();

			if (!this.stopwords.contains(word) && this.map.get(word) != null) {
				String wordScore = map.get(word);
				tweetScore = tweetScore + Integer.parseInt(wordScore);
			}
		}
		System.out.println(tweetScore + " --- " + fullTweet);

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("lang", "word"));
	}
}
