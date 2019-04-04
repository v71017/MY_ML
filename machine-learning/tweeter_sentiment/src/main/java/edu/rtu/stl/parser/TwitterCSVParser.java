package edu.rtu.stl.parser;

import static edu.rtu.stl.domain.Sentiment.fromValue;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.rtu.stl.domain.Document;

public class TwitterCSVParser implements Parser {

	private static final Logger LOG = LoggerFactory.getLogger(TwitterCSVParser.class);

	@Override
	public List<Document> parse(List<String> lines) {
		List<Document> result = new ArrayList<>(lines.size());
		int count = 0;
		for (String line : lines) {

			if (line.equalsIgnoreCase("sentiment,text,user"))
				continue;

			count++;
			List<String> lineItems = new ArrayList<>();
			for (String s : line.split("\"")) {
				if (!",".equals(s) && !s.trim().isEmpty()) {
					lineItems.add(s);
				}
			}
			String[] parsedLine = lineItems.toArray(new String[lineItems.size()]);
			if (parsedLine.length < 3) {
				LOG.error("Invalid line detected at {}. Line: {}", count, line);
				continue;
			}

			result.add(new Document(fromValue(parsedLine[0]), parsedLine[2]));
			if (count % 10000 == 0) {
				LOG.debug("Count {}", count);
			}
		}
		LOG.debug("Count {}", count);
		return result;
	}
}
