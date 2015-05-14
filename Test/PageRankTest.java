package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import expertNet.PageRank;

public class PageRankTest
{
	public static void main(String[] args)
	{
		PageRank pagerank = new PageRank();
		
		pagerank.compute();
	}
}
