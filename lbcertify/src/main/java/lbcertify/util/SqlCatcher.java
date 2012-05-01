package lbcertify.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Captures liquibase code into a buffer and offers access and assertions to that buffer
 * 
 * @author PaulM
 * 
 */
public class SqlCatcher {
	private StringWriter buff = new StringWriter();
	private List<String> sqlLines;

	public Writer getWriter() {
		return buff;
	}

	@Test
	public List<String> getSqlLines(final String tableName) {
		List<String> r = new ArrayList<String>();
		r.addAll(Collections2.filter(getSqlLines(), new Predicate<String>() {
			public boolean apply(String input) {
				String tst = input.toLowerCase();
				final int slot = input.indexOf(" " + tableName.toLowerCase());
				System.err.println(slot + ": " + tst);
				return slot > 0;
			}
		}));
		return r;
	}

	public List<String> getSqlLines() {
		List<String> r = new ArrayList<String>();
		r.addAll(Collections2.filter(getLines(), new Predicate<String>() {
			public boolean apply(String input) {
				return !input.startsWith("--");
			}
		}));
		return r;
	}

	public List<String> getLines() {
		if (sqlLines == null) {
			parseBuffer();
		}
		return sqlLines;
	}

	private void parseBuffer() {
		String[] lines = buff.toString().split("\n");
		sqlLines = new ArrayList<String>();
		for (String line : lines) {
			if (line.trim().length() > 0)
				sqlLines.add(line.trim());
		}

	}
}
