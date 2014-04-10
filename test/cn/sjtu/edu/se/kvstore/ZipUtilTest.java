package cn.sjtu.edu.se.kvstore;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import cn.edu.sjtu.se.kvstore.common.ZipUtil;

public class ZipUtilTest {

	@Test
	public void test() {
		try {
			String  str = "n March 1999, the company moved its o"
					+ "ffices to Palo Alto, California, which is home to several "
					+ "prominent Silicon Valley technology startups.[68] The next"
					+ " year, against Page and Brin's initial opposition toward an "
					+ "advertising-funded search engine,[69] Google began selling"
					+ " advertisements associated with search keywords.[25] In order"
					+ " to maintain an uncluttered page design and increase speed, "
					+ "advertisements were solely text-based. Keywords were sold based"
					+ " on a combination of price bids and click-throughs, with bidding "
					+ "starting at five cents per click.[25]Google announced the launch "
					+ "of a new company called Calico on September 19, 2013, which will "
					+ "be led by Apple chairman Arthur Levinson. In the official public "
					+ "statement, Page explained that the health and wellbeing company"
					+ " will focus on the challenge of ageing and associated diseases"
					+ "As of September 2013, Google operates 70 offices in more than 40 "
					+ "countries.[79] Google celebrated its 15-year anniversary on September "
					+ "27, 2013, although it has used other dates for its official birthday."
					+ "[80] The reason for the choice of September 27 remains unclear, and "
					+ "a dispute with rival search engine Yahoo! Search in 2005 has been "
					+ "suggested as the cause";


			System.out.println("before compress: " + str.length());
			String shortStr = ZipUtil.compress(str);
			System.out.println("after compress: " + shortStr.length());
			System.out.println("after decompress: " + ZipUtil.decompress(shortStr).length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
