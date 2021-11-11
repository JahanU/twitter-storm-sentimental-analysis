
The code subscribes to Tweets containing the filter query of "Vaccine", "Pfizer", "BioNTech". Keeping stats on the sentiment around tweets related to covid-19 vaccinations.

This project contains a simple storm topology (**Apache Storm**) that connects to the sample stream of the Twitter Streaming API and analysis the sentiment of each tweet, determining if it is positive/negative. All tweets collected are then tallied up.

**Results:** Negative tweets outweighed positive tweets.

This was for a university assignment, so some design aspects were limited, namely the need to have both a positive and negative class instead of merging the two together. A working AI solution was found and implemented found using CoreNLP (https://stanfordnlp.github.io/CoreNLP/) but was not permitted for the assignment.

**To get started:**

Clone this repo
Import as existing Maven project in Eclipse
Run Topology.java with your twitter credentials as VM args (see http://twitter4j.org/en/configuration.html#systempropertyconfiguration)
You'll need to have valid Twitter OAuth credentials to get the sample working. For the exact steps on how to do that, visit https://dev.twitter.com/discussions/631.
